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
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class FileLocalMapRepositoryTest {

    private lateinit var context: Context
    private val logger: Logger = mockk(relaxed = true)
    private lateinit var repository: FileLocalMapRepository
    private lateinit var mapsDir: File

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        repository = FileLocalMapRepository(context, Dispatchers.Unconfined, logger)
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
    fun `deleteMap should remove file and empty parent directories`() = runTest {
        // Given
        val europeDir = File(mapsDir, "europe")
        europeDir.mkdirs()
        val mapFile = File(europeDir, "spain.map")
        mapFile.writeText("dummy content")
        val domainMapFile = MapFile(
            name = "spain.map",
            path = mapFile.absolutePath,
            size = mapFile.length(),
            lastModified = mapFile.lastModified(),
            parentPath = "europe"
        )

        // When
        repository.deleteMap(domainMapFile)

        // Then
        assertFalse(mapFile.exists())
        assertFalse(europeDir.exists()) // Should be deleted if empty
        assertTrue(mapsDir.exists())   // Root maps dir should remain
    }

    @Test
    fun `getLocalMaps should be reactive to deletions`() = runTest {
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

        // Then it should emit again with empty list
        assertEquals(2, results.size)
        assertTrue(results[1].isEmpty())

        job.cancel()
    }

    @Test
    fun `getLocalMaps should be reactive to refresh`() = runTest {
        val results = mutableListOf<List<MapFile>>()
        val job = launch {
            repository.getLocalMaps().collect { results.add(it) }
        }
        runCurrent()

        assertEquals(1, results.size)
        assertTrue(results[0].isEmpty())

        // Create file manually (simulating background download)
        val mapFile = File(mapsDir, "spain.map")
        mapFile.writeText("dummy content")

        // Trigger refresh
        repository.refresh()
        runCurrent()

        assertEquals(2, results.size)
        assertEquals(1, results[1].size)

        job.cancel()
    }

    @Test
    fun `getMapInternalStorageDir should return correct path`() {
        val path = repository.getMapInternalStorageDir()
        assertEquals(mapsDir.absolutePath, path)
    }
}
