/*
 * Copyright (C) 2026  Giste
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.giste.roadbooknavigator.features.map.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import okio.buffer
import okio.sink
import org.giste.roadbooknavigator.core.di.IoDispatcher
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.data.datasource.RemoteMapDataSource
import org.giste.roadbooknavigator.features.map.data.worker.DownloadMapWorker
import org.giste.roadbooknavigator.features.map.domain.model.DownloadStatus
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import org.giste.roadbooknavigator.features.map.domain.repository.MapRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LocalFileMapRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val remoteDataSource: RemoteMapDataSource,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val logger: Logger
) : MapRepository {

    private val workManager = WorkManager.getInstance(context)
    private val repositoryScope = CoroutineScope(ioDispatcher + SupervisorJob())

    private val rootUrl =
        "https://ftp-stud.hs-esslingen.de/pub/Mirrors/download.mapsforge.org/maps/v5/"

    init {
        workManager.getWorkInfosByTagFlow("map_download")
            .onEach { workInfos ->
                if (workInfos.any { it.state == WorkInfo.State.SUCCEEDED }) {
                    refresh()
                }
            }
            .launchIn(repositoryScope)
    }

    private val mapsDir: File
        get() = File(context.filesDir, "maps").apply {
            if (!exists()) mkdirs()
        }

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply {
        tryEmit(Unit)
    }

    override fun getLocalMaps(): Flow<List<MapFile>> = refreshTrigger
        .map { scanLocalMaps() }
        .flowOn(ioDispatcher)

    private fun scanLocalMaps(): List<MapFile> {
        return mapsDir.walkTopDown()
            .filter { it.isFile && it.extension == "map" }
            .map { file ->
                MapFile(
                    name = file.name,
                    path = file.absolutePath,
                    size = file.length(),
                    lastModified = file.lastModified(),
                    parentPath = file.parentFile?.relativeTo(mapsDir)?.path ?: ""
                )
            }.toList()
    }

    override suspend fun deleteMap(mapFile: MapFile) {
        val file = File(mapFile.path)
        if (file.exists()) {
            val deleted = file.delete()
            if (deleted) {
                logger.d("MapRepositoryImpl: Deleted map %s", mapFile.name)
                // Clean up empty parent directories
                var parent = file.parentFile
                while (parent != null && parent != mapsDir && parent.list()?.isEmpty() == true) {
                    parent.delete()
                    parent = parent.parentFile
                }
                refresh()
            } else {
                logger.e("MapRepositoryImpl: Failed to delete map %s", mapFile.name)
            }
        }
    }

    private suspend fun refresh() {
        refreshTrigger.emit(Unit)
    }

    override fun getRemoteMaps(): Flow<List<RemoteMapFolder>> = flow {
        try {
            val root = remoteDataSource.getRemoteMaps(rootUrl)
            val fullTree = fetchFolderRecursive(root)
            emit(fullTree.subFolders)
        } catch (e: Exception) {
            logger.e(e, "Error fetching remote maps")
            throw e
        }
    }.flowOn(ioDispatcher)

    private suspend fun fetchFolderRecursive(folder: RemoteMapFolder): RemoteMapFolder =
        coroutineScope {
            val enrichedSubfolders = folder.subFolders.map { subFolder ->
                async {
                    try {
                        val fetched = remoteDataSource.getRemoteMaps(subFolder.path)
                        fetchFolderRecursive(fetched)
                    } catch (e: Exception) {
                        logger.w("Error fetching subfolder %s: %s", subFolder.path, e.message)
                        subFolder
                    }
                }
            }.awaitAll()
            folder.copy(subFolders = enrichedSubfolders)
        }

    override fun downloadMap(remoteMapFile: RemoteMapFile): Flow<DownloadStatus> {
        val workRequest = OneTimeWorkRequestBuilder<DownloadMapWorker>()
            .setInputData(
                workDataOf(
                    DownloadMapWorker.KEY_URL to remoteMapFile.url,
                    DownloadMapWorker.KEY_NAME to remoteMapFile.name,
                    DownloadMapWorker.KEY_PARENT_PATH to remoteMapFile.parentPath,
                    DownloadMapWorker.KEY_LAST_MODIFIED to remoteMapFile.lastModified
                )
            )
            .addTag("map_download")
            .addTag("url:${remoteMapFile.url}")
            .build()

        workManager.enqueueUniqueWork(
            remoteMapFile.url,
            ExistingWorkPolicy.KEEP,
            workRequest
        )

        return workManager.getWorkInfosForUniqueWorkFlow(remoteMapFile.url)
            .map { workInfos ->
                val workInfo = workInfos.firstOrNull() ?: return@map DownloadStatus.Idle
                mapWorkInfoToDownloadStatus(workInfo)
            }
    }

    override fun cancelDownload(url: String) {
        workManager.cancelUniqueWork(url)
    }

    override fun getDownloadingMaps(): Flow<Map<String, DownloadStatus>> {
        return workManager.getWorkInfosByTagFlow("map_download")
            .map { workInfos ->
                workInfos
                    .filter { !it.state.isFinished }
                    .associate { info ->
                        val url = info.tags.find { it.startsWith("url:") }?.removePrefix("url:") ?: ""
                        url to mapWorkInfoToDownloadStatus(info)
                    }
                    .filterKeys { it.isNotEmpty() }
            }
    }

    private fun mapWorkInfoToDownloadStatus(workInfo: WorkInfo): DownloadStatus {
        return when (workInfo.state) {
            WorkInfo.State.ENQUEUED, WorkInfo.State.BLOCKED -> DownloadStatus.Idle
            WorkInfo.State.RUNNING -> {
                val progress = workInfo.progress.getFloat(DownloadMapWorker.PROGRESS_KEY, 0f)
                DownloadStatus.Progress(progress)
            }

            WorkInfo.State.SUCCEEDED -> DownloadStatus.Success
            WorkInfo.State.FAILED -> DownloadStatus.Error("Download failed")
            WorkInfo.State.CANCELLED -> DownloadStatus.Idle
        }
    }
}
