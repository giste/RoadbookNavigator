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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.buffer
import okio.sink
import org.giste.roadbooknavigator.core.di.IoDispatcher
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.data.datasource.RemoteMapDataSource
import org.giste.roadbooknavigator.features.map.domain.model.DownloadStatus
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import org.giste.roadbooknavigator.features.map.domain.repository.RemoteMapRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HttpRemoteMapRepository @Inject constructor(
    private val dataSource: RemoteMapDataSource,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val logger: Logger
) : RemoteMapRepository {

    private val rootUrl = "https://ftp-stud.hs-esslingen.de/pub/Mirrors/download.mapsforge.org/maps/v5/"

    override fun getRemoteMaps(): Flow<List<RemoteMapFolder>> = flow {
        try {
            val root = dataSource.getRemoteMaps(rootUrl)
            val fullTree = fetchFolderRecursive(root)
            emit(fullTree.subFolders)
        } catch (e: Exception) {
            logger.e(e, "Error fetching remote maps")
            throw e
        }
    }.flowOn(ioDispatcher)

    private suspend fun fetchFolderRecursive(folder: RemoteMapFolder): RemoteMapFolder = coroutineScope {
        val enrichedSubfolders = folder.subFolders.map { subFolder ->
            async {
                try {
                    val fetched = dataSource.getRemoteMaps(subFolder.path)
                    fetchFolderRecursive(fetched)
                } catch (e: Exception) {
                    logger.w("Error fetching subfolder %s: %s", subFolder.path, e.message)
                    subFolder
                }
            }
        }.awaitAll()
        folder.copy(subFolders = enrichedSubfolders)
    }

    override fun downloadMap(remoteMapFile: RemoteMapFile, destinationPath: String): Flow<DownloadStatus> = flow {
        emit(DownloadStatus.Idle)
        try {
            val responseBody = dataSource.downloadFile(remoteMapFile.url)
            val file = File(destinationPath)
            file.parentFile?.mkdirs()
            
            val totalSize = responseBody.contentLength()
            var bytesRead = 0L
            
            responseBody.source().use { source ->
                file.sink().buffer().use { sink ->
                    val buffer = ByteArray(8192)
                    var read: Int
                    while (source.read(buffer).also { read = it } != -1) {
                        sink.write(buffer, 0, read)
                        bytesRead += read
                        if (totalSize > 0) {
                            emit(DownloadStatus.Progress(bytesRead.toFloat() / totalSize))
                        }
                    }
                }
            }
            // Set the last modified date to match remote for update tracking
            if (remoteMapFile.lastModified > 0) {
                file.setLastModified(remoteMapFile.lastModified)
            }
            emit(DownloadStatus.Success)
        } catch (e: Exception) {
            logger.e(e, "Error downloading map %s", remoteMapFile.name)
            emit(DownloadStatus.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(ioDispatcher)
}
