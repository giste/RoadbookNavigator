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

package org.giste.roadbooknavigator.features.map.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okio.buffer
import okio.sink
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.data.datasource.RemoteMapDataSource
import java.io.File

@HiltWorker
internal class DownloadMapWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val remoteDataSource: RemoteMapDataSource,
    private val logger: Logger
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_URL) ?: return Result.failure()
        val name = inputData.getString(KEY_NAME) ?: return Result.failure()
        val parentPath = inputData.getString(KEY_PARENT_PATH) ?: ""
        val lastModified = inputData.getLong(KEY_LAST_MODIFIED, 0L)

        val mapsDir = File(applicationContext.filesDir, "maps")
        val destinationPath = "${mapsDir.absolutePath}/$parentPath/$name"
        val file = File(destinationPath)

        return try {
            file.parentFile?.mkdirs()
            val responseBody = remoteDataSource.downloadFile(url)
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
                            val progress = bytesRead.toFloat() / totalSize
                            setProgress(workDataOf(PROGRESS_KEY to progress))
                        }
                    }
                }
            }

            if (lastModified > 0) {
                file.setLastModified(lastModified)
            }

            Result.success()
        } catch (e: Exception) {
            logger.e(e, "Error downloading map %s", name)
            Result.failure()
        }
    }

    companion object {
        const val KEY_URL = "url"
        const val KEY_NAME = "name"
        const val KEY_PARENT_PATH = "parent_path"
        const val KEY_LAST_MODIFIED = "last_modified"
        const val PROGRESS_KEY = "progress"
    }
}
