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
    fun `deleteMap should remove file and trigger refresh`() = runTest {
        // Given
        val mapFile = File(mapsDir, "spain.map")
        mapFile.writeText("dummy content")
        val domainMapFile = MapFile("spain.map", mapFile.absolutePath, 100L, 0L, "")

        val results = mutableListOf<List<MapFile>>()
        val job = launch {
            repository.getLocalMaps().collect { results.add(it) }
        }
        runCurrent()

        // Initially contains the map
        assertEquals(1, results.size)
        assertEquals(1, results[0].size)

        // When deleted
        repository.deleteMap(domainMapFile)
        runCurrent()

        // Then it should be deleted from disk and flow should emit again
        assertFalse(mapFile.exists())
        assertEquals(2, results.size)
        assertTrue(results[1].isEmpty())

        job.cancel()
    }

    @Test
    fun `getRemoteMaps should fetch regions omitting root folder`() = runTest {
        val rootUrl = "https://ftp-stud.hs-esslingen.de/pub/Mirrors/download.mapsforge.org/maps/v5/"
        val europeUrl = "${rootUrl}europe/"
        val rootFolder = RemoteMapFolder("v5", rootUrl, subFolders = listOf(RemoteMapFolder("europe", europeUrl)))
        val europeFolder = RemoteMapFolder("europe", europeUrl, maps = listOf(RemoteMapFile("spain.map", "europe", "url", 100L, 1000L)))

        coEvery { remoteDataSource.getRemoteMaps(rootUrl) } returns rootFolder
        coEvery { remoteDataSource.getRemoteMaps(europeUrl) } returns europeFolder

        val result = repository.getRemoteMaps().first()

        assertEquals(1, result.size)
        assertEquals("europe", result[0].name)
        assertEquals(1, result[0].maps.size)
    }

    @Test
    fun `downloadMap should emit progress, success and trigger refresh`() = runTest {
        val remoteMapFile = RemoteMapFile("spain.map", "europe", "http://url", 100L, 1000L)
        val responseBody = mockk<okhttp3.ResponseBody>(relaxed = true)

        coEvery { remoteDataSource.downloadFile(remoteMapFile.url) } returns responseBody
        coEvery { responseBody.contentLength() } returns 10L
        coEvery { responseBody.source().read(any<ByteArray>()) } returns 5 andThen 5 andThen -1

        val localResults = mutableListOf<List<MapFile>>()
        val localJob = launch {
            repository.getLocalMaps().collect { localResults.add(it) }
        }
        runCurrent()
        assertEquals(1, localResults.size) // Initially empty

        val downloadResults = repository.downloadMap(remoteMapFile).toList()

        assertTrue(downloadResults.contains(DownloadStatus.Success))
        
        runCurrent()
        // Should have emitted again after success
        assertEquals(2, localResults.size)
        assertEquals(1, localResults[1].size)

        localJob.cancel()
    }
}
