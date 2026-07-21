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
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.data.datasource.RemoteMapDataSource
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class DownloadMapWorkerTest {

    private lateinit var context: Context
    private val remoteDataSource = mockk<RemoteMapDataSource>()
    private val logger = mockk<Logger>(relaxed = true)

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `doWork returns success when download is successful`() = runTest {
        val url = "https://example.com/map.map"
        val name = "test.map"
        val content = "map content"
        val responseBody = content.toResponseBody()

        coEvery { remoteDataSource.downloadFile(url) } returns responseBody

        val worker = TestListenableWorkerBuilder<DownloadMapWorker>(context)
            .setWorkerFactory(object : androidx.work.WorkerFactory() {
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

        assertEquals(ListenableWorker.Result.success(), result)
        val file = File(context.filesDir, "maps/$name")
        assertEquals(true, file.exists())
        assertEquals(content, file.readText())
    }

    @Test
    fun `doWork returns failure when download fails`() = runTest {
        val url = "https://example.com/map.map"
        val name = "test.map"

        coEvery { remoteDataSource.downloadFile(url) } throws Exception("Download failed")

        val worker = TestListenableWorkerBuilder<DownloadMapWorker>(context)
            .setWorkerFactory(object : androidx.work.WorkerFactory() {
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

        assertEquals(ListenableWorker.Result.failure(), result)
    }
}
