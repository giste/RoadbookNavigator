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
 * along with this program.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.giste.roadbooknavigator.features.roadbook.data

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.ByteArrayInputStream
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class RoadbookRepositoryImplTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var repository: RoadbookRepositoryImpl
    private val mapper: Rn2Mapper = mockk()
    private val persistenceMapper: PersistenceMapper = mockk()
    private val context: Context = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        every { context.filesDir } returns tempFolder.root
        repository = RoadbookRepositoryImpl(mapper, persistenceMapper, testDispatcher, context)
    }

    @Test
    fun `processNewRoadbook should parse, save to disk, and emit new route`() = runTest {
        // Given
        val jsonContent = "{\"test\": \"json\"}"
        val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
        val route = mockk<Route>(relaxed = true)
        val persistentRoute = org.giste.roadbooknavigator.features.roadbook.data.dto.persistence.PersistentRoute(
            name = "Test",
            description = "",
            startLocation = "",
            endLocation = "",
            waypoints = emptyList()
        )
        
        every { mapper.mapToDomain(jsonContent) } returns route
        every { persistenceMapper.toPersistent(route) } returns persistentRoute

        // When
        val result = repository.processNewRoadbook(inputStream)

        // Then
        if (result.isFailure) {
            println("Test failed with: ${result.exceptionOrNull()}")
        }
        assertTrue(result.isSuccess)
        assertEquals(route, result.getOrNull())
        assertEquals(route, repository.activeRoadbook.first())
        
        // Verify file was created
        val cacheFile = File(tempFolder.root, "active_roadbook.json")
        assertTrue(cacheFile.exists())
    }

    @Test
    fun `loadActiveRoadbook should return null if no cache exists`() = runTest {
        // When
        val result = repository.loadActiveRoadbook()

        // Then
        assertTrue(result.isSuccess)
        assertNull(result.getOrNull())
    }

    @Test
    fun `loadActiveRoadbook should load from disk and emit if cache exists`() = runTest {
        // Given
        val cacheFile = File(tempFolder.root, "active_roadbook.json")
        val validPersistentJson = """
            {
                "name": "Cached Route",
                "description": "",
                "startLocation": "",
                "endLocation": "",
                "waypoints": []
            }
        """.trimIndent()
        cacheFile.writeText(validPersistentJson)
        val route = mockk<Route>(relaxed = true)
        
        every { persistenceMapper.toDomain(any()) } returns route

        // When
        val result = repository.loadActiveRoadbook()

        // Then
        if (result.isFailure) {
            println("Test failed with: ${result.exceptionOrNull()}")
        }
        assertTrue(result.isSuccess)
        assertEquals(route, result.getOrNull())
        assertEquals(route, repository.activeRoadbook.first())
    }
}
