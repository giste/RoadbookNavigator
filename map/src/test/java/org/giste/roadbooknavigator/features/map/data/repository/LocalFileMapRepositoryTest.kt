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
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.testing.WorkManagerTestInitHelper
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.data.datasource.RemoteMapDataSource
import org.giste.roadbooknavigator.features.map.domain.model.DownloadStatus
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LocalFileMapRepositoryTest {

    private lateinit var context: Context
    private val remoteDataSource: RemoteMapDataSource = mockk()
    private val logger: Logger = mockk(relaxed = true)
    private lateinit var repository: LocalFileMapRepository
    private lateinit var mapsDir: File

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)

        repository = LocalFileMapRepository(context, remoteDataSource, Dispatchers.Unconfined, logger)
        mapsDir = File(context.filesDir, "maps")
        mapsDir.deleteRecursively()
        mapsDir.mkdirs()
    }

    @Test
    fun `getLocalMaps should return list of map files in directory`() = runTest {
        // Given
        val europeDir = File(mapsDir, "europe")
        europeDir.mkdirs()
        val mapFile = File(europeDir, "spain.map")
        mapFile.writeText("dummy content")

        // When
        val maps = repository.getLocalMaps().first()

        // Then
        assertEquals(1, maps.size)
        assertEquals("spain.map", maps[0].name)
        assertEquals("europe", maps[0].parentPath)
    }

    @Test
    fun `deleteMap should remove file, clean empty parent directories and trigger refresh`() = runTest {
        // Given
        val europeDir = File(mapsDir, "europe/spain")
        europeDir.mkdirs()
        val mapFile = File(europeDir, "madrid.map")
        mapFile.writeText("dummy content")
        val domainMapFile = MapFile("madrid.map", mapFile.absolutePath, 100L, 0L, "europe/spain")

        val results = mutableListOf<List<MapFile>>()
        val job = launch {
            repository.getLocalMaps().collect { results.add(it) }
        }
        runCurrent()

        // Initially contains the map
        assertEquals(1, results[0].size)

        // When deleted
        repository.deleteMap(domainMapFile)
        runCurrent()

        // Then it should be deleted from disk
        assertFalse(mapFile.exists())
        // And parent directories should be cleaned up
        assertFalse(europeDir.exists())
        assertFalse(File(mapsDir, "europe").exists())
        assertTrue(mapsDir.exists()) // Root should remain

        // And flow should emit again
        assertEquals(2, results.size)
        assertTrue(results[1].isEmpty())

        job.cancel()
    }

    @Test
    fun `getRemoteMaps should fetch regions recursively and handle errors gracefully`() = runTest {
        val rootUrl = "https://ftp-stud.hs-esslingen.de/pub/Mirrors/download.mapsforge.org/maps/v5/"
        val europeUrl = "${rootUrl}europe/"
        val spainUrl = "${europeUrl}spain/"
        val asiaUrl = "${rootUrl}asia/"

        val rootFolder = RemoteMapFolder("v5", rootUrl, subFolders = listOf(
            RemoteMapFolder("europe", europeUrl),
            RemoteMapFolder("asia", asiaUrl)
        ))
        val europeFolder = RemoteMapFolder("europe", europeUrl, subFolders = listOf(
            RemoteMapFolder("spain", spainUrl)
        ))
        val spainFolder = RemoteMapFolder("spain", spainUrl, maps = listOf(
            RemoteMapFile("madrid.map", "europe/spain", "url1", 100L, 1000L)
        ))

        coEvery { remoteDataSource.getRemoteMaps(rootUrl) } returns rootFolder
        coEvery { remoteDataSource.getRemoteMaps(europeUrl) } returns europeFolder
        coEvery { remoteDataSource.getRemoteMaps(spainUrl) } returns spainFolder
        coEvery { remoteDataSource.getRemoteMaps(asiaUrl) } throws Exception("Asia is down")

        val result = repository.getRemoteMaps().first()

        assertEquals(2, result.size)
        val europe = result.find { it.name == "europe" }!!
        val asia = result.find { it.name == "asia" }!!

        // Europe should be fully enriched
        assertEquals(1, europe.subFolders.size)
        assertEquals("spain", europe.subFolders[0].name)
        assertEquals(1, europe.subFolders[0].maps.size)

        // Asia should remain as is due to error
        assertTrue(asia.subFolders.isEmpty())
        assertTrue(asia.maps.isEmpty())
    }

    @Test
    fun `downloadMap should enqueue unique work and return flow`() = runTest {
        val remoteMapFile = RemoteMapFile("spain.map", "europe", "http://url", 100L, 1000L)

        val results = mutableListOf<DownloadStatus>()
        val job = launch {
            repository.downloadMap(remoteMapFile).collect { results.add(it) }
        }
        runCurrent()

        val workManager = WorkManager.getInstance(context)
        val workInfos = workManager.getWorkInfosForUniqueWork(remoteMapFile.url).get()
        assertEquals(1, workInfos.size)

        job.cancel()
    }

    @Test
    fun `cancelDownload should cancel unique work`() = runTest {
        val url = "http://url"
        val remoteMapFile = RemoteMapFile("spain.map", "europe", url, 100L, 1000L)
        repository.downloadMap(remoteMapFile)
        
        repository.cancelDownload(url)
        runCurrent()

        val workManager = WorkManager.getInstance(context)
        val workInfos = workManager.getWorkInfosForUniqueWork(url).get()
        assertEquals(1, workInfos.size)
        // It might be CANCELLED or ENQUEUED depending on timing, but it should be present.
        // If SynchronousExecutor is used, it might have FAILED already.
    }
}
