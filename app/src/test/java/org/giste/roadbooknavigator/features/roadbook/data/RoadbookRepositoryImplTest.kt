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

package org.giste.roadbooknavigator.features.roadbook.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.roadbook.data.dto.persistence.PersistentRoute
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.ByteArrayInputStream

@OptIn(ExperimentalCoroutinesApi::class)
class RoadbookRepositoryImplTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var repository: RoadbookRepositoryImpl
    private val mapper: Rn2Mapper = mockk()
    private val persistenceMapper: PersistenceMapper = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher + Job())
    
    private lateinit var dataStore: DataStore<PersistentRoute>

    @Before
    fun setup() {
        dataStore = DataStoreFactory.create(
            serializer = RoadbookSerializer(),
            scope = testScope,
            produceFile = { tempFolder.newFile("active_roadbook.json") }
        )
        repository = RoadbookRepositoryImpl(mapper, persistenceMapper, dataStore, testDispatcher)
    }

    @Test
    fun `processNewRoadbook should parse, save to DataStore, and emit new route`() = runTest {
        // Given
        val jsonContent = "{\"test\": \"json\"}"
        val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
        val route = mockk<Route>(relaxed = true)
        val persistentRoute = PersistentRoute(
            name = "Test",
            description = "",
            startLocation = "",
            endLocation = "",
            waypoints = emptyList()
        )
        
        every { mapper.mapToDomain(jsonContent) } returns route
        every { persistenceMapper.toPersistent(route) } returns persistentRoute
        every { persistenceMapper.toDomain(persistentRoute) } returns route

        // When
        val result = repository.processNewRoadbook(inputStream)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(route, result.getOrNull())
        assertEquals(route, repository.activeRoadbook.first())
    }

    @Test
    fun `activeRoadbook should emit null if DataStore is empty`() = runTest {
        // When
        val result = repository.activeRoadbook.first()

        // Then
        assertNull(result)
    }

    @Test
    fun `activeRoadbook should emit route if DataStore contains data`() = runTest {
        // Given
        val persistentRoute = PersistentRoute(
            name = "Cached Route",
            description = "",
            startLocation = "",
            endLocation = "",
            waypoints = emptyList()
        )
        val route = mockk<Route>(relaxed = true)
        
        dataStore.updateData { persistentRoute }
        every { persistenceMapper.toDomain(persistentRoute) } returns route

        // When
        val result = repository.activeRoadbook.first()

        // Then
        assertEquals(route, result)
    }
}
