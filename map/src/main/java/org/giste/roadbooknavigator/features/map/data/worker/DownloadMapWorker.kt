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

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okio.buffer
import okio.sink
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.R
import org.giste.roadbooknavigator.features.map.data.datasource.RemoteMapDataSource
import org.giste.roadbooknavigator.features.map.data.receiver.DownloadCancelReceiver
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

        setProgress(workDataOf(KEY_URL to url, PROGRESS_KEY to 0f))
        setForeground(createForegroundInfo(name, url))

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
                            setProgress(workDataOf(KEY_URL to url, PROGRESS_KEY to progress))
                            updateNotification(name, url, progress)
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

    private suspend fun updateNotification(name: String, url: String, progress: Float) {
        val foregroundInfo = createForegroundInfo(name, url, progress)
        setForeground(foregroundInfo)
    }

    private fun createForegroundInfo(name: String, url: String, progress: Float = 0f): ForegroundInfo {
        val id = applicationContext.getString(R.string.map_download_notification_channel_id)
        val channelName = applicationContext.getString(R.string.map_download_notification_channel_name)
        val title = applicationContext.getString(R.string.map_download_notification_title)
        val cancel = applicationContext.getString(R.string.map_download_notification_cancel)

        val channel = NotificationChannel(id, channelName, NotificationManager.IMPORTANCE_LOW).apply {
            description = applicationContext.getString(R.string.map_download_notification_channel_description)
        }
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(applicationContext, DownloadCancelReceiver::class.java).apply {
            putExtra(DownloadCancelReceiver.KEY_URL, url)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            url.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(applicationContext.getString(R.string.map_download_notification_content, name))
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setOngoing(true)
            .setProgress(100, (progress * 100).toInt(), false)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, cancel, pendingIntent)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
    }

    companion object {
        private const val NOTIFICATION_ID = 100
        const val KEY_URL = "url"
        const val KEY_NAME = "name"
        const val KEY_PARENT_PATH = "parent_path"
        const val KEY_LAST_MODIFIED = "last_modified"
        const val PROGRESS_KEY = "progress"
    }
}
