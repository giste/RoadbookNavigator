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

package org.giste.roadbooknavigator.features.map.data.receiver

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import org.giste.roadbooknavigator.features.map.data.worker.DownloadMapWorker
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class DownloadCancelReceiverTest {

    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        val config = Configuration.Builder()
            .setExecutor(SynchronousExecutor())
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }

    @Test
    fun `onReceive should cancel unique work when URL is present`() {
        val url = "http://example.com/map.map"
        val workManager = WorkManager.getInstance(context)
        
        // Use constraints to keep it enqueued
        val request = OneTimeWorkRequestBuilder<DownloadMapWorker>()
            .setConstraints(androidx.work.Constraints.Builder().setRequiresCharging(true).build())
            .build()
        workManager.enqueueUniqueWork(url, androidx.work.ExistingWorkPolicy.KEEP, request)

        val receiver = DownloadCancelReceiver()
        val intent = Intent(context, DownloadCancelReceiver::class.java).apply {
            putExtra(DownloadCancelReceiver.KEY_URL, url)
        }
        
        receiver.onReceive(context, intent)

        val workInfos = workManager.getWorkInfosForUniqueWork(url).get()
        assertEquals(WorkInfo.State.CANCELLED, workInfos[0].state)
    }
}
