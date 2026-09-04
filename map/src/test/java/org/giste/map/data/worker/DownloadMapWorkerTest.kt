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

package org.giste.map.data.worker

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.giste.map.R
import org.giste.map.data.datasource.RemoteMapDataSource
import org.giste.roadbooknavigator.core.util.Logger
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class DownloadMapWorkerTest {

    private lateinit var context: Context
    private val remoteDataSource = mockk<RemoteMapDataSource>()
    private val logger = mockk<Logger>(relaxed = true)

    @Before
    fun setup() {
        val realContext = ApplicationProvider.getApplicationContext<Context>()
        context = spyk(realContext)
        // Mock strings to avoid Resources$NotFoundException in some environments
        every { context.getString(R.string.map_download_notification_channel_id) } returns "map_downloads_channel"
        every { context.getString(R.string.map_download_notification_channel_name) } returns "Map Downloads"
        every { context.getString(R.string.map_download_notification_title) } returns "Downloading map"
        every { context.getString(R.string.map_download_notification_cancel) } returns "Cancel"
        every { context.getString(R.string.map_download_notification_content, any()) } returns "Downloading..."
        every { context.getString(R.string.map_download_notification_channel_description) } returns "Description"
    }

    @Test
    fun `doWork returns success when download is successful`() = runTest {
        val url = "https://example.com/map.map"
        val name = "test.map"
        val content = "map content"
        val responseBody = content.toResponseBody()

        coEvery { remoteDataSource.downloadFile(url) } returns responseBody

        val worker = TestListenableWorkerBuilder<DownloadMapWorker>(context)
            .setWorkerFactory(object : WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker {
                    return DownloadMapWorker(
                        appContext,
                        workerParameters,
                        remoteDataSource,
                        logger
                    )
                }
            })
            .setInputData(
                workDataOf(
                    DownloadMapWorker.KEY_URL to url,
                    DownloadMapWorker.KEY_NAME to name
                )
            )
            .build()

        val result = worker.doWork()

        Assert.assertEquals(ListenableWorker.Result.success(), result)
        val file = File(context.filesDir, "maps/$name")
        Assert.assertEquals(true, file.exists())
        Assert.assertEquals(content, file.readText())
    }

    @Test
    fun `doWork returns failure when download fails`() = runTest {
        val url = "https://example.com/map.map"
        val name = "test.map"

        coEvery { remoteDataSource.downloadFile(url) } throws Exception("Download failed")

        val worker = TestListenableWorkerBuilder<DownloadMapWorker>(context)
            .setWorkerFactory(object : WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker {
                    return DownloadMapWorker(
                        appContext,
                        workerParameters,
                        remoteDataSource,
                        logger
                    )
                }
            })
            .setInputData(
                workDataOf(
                    DownloadMapWorker.KEY_URL to url,
                    DownloadMapWorker.KEY_NAME to name
                )
            )
            .build()

        val result = worker.doWork()

        Assert.assertEquals(ListenableWorker.Result.failure(), result)
    }

    @Test
    fun `doWork updates notification progress during download`() = runTest {
        val url = "https://example.com/map.map"
        val name = "test.map"
        // 16KB content to ensure multiple 8KB reads
        val content = "A".repeat(16384)
        val responseBody = content.toResponseBody()

        coEvery { remoteDataSource.downloadFile(url) } returns responseBody

        val worker = TestListenableWorkerBuilder<DownloadMapWorker>(context)
            .setWorkerFactory(object : WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters
                ): ListenableWorker {
                    return DownloadMapWorker(
                        appContext,
                        workerParameters,
                        remoteDataSource,
                        logger
                    )
                }
            })
            .setInputData(
                workDataOf(
                    DownloadMapWorker.KEY_URL to url,
                    DownloadMapWorker.KEY_NAME to name
                )
            )
            .build()

        worker.doWork()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val shadowNotificationManager = Shadows.shadowOf(notificationManager)

        // Verify notifications were posted
        val notifications = shadowNotificationManager.allNotifications
        assert(notifications.isNotEmpty())

        // Check the notification has progress and flags
        val notification = notifications.first()
        // Progress should be finished (100/100)
        Assert.assertEquals(100, notification.extras.getInt(Notification.EXTRA_PROGRESS))
        Assert.assertEquals(100, notification.extras.getInt(Notification.EXTRA_PROGRESS_MAX))
        // Verify FLAG_ONLY_ALERT_ONCE is set
        Assert.assertEquals(
            true,
            (notification.flags and Notification.FLAG_ONLY_ALERT_ONCE) != 0
        )
    }
}