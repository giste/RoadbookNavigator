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

import androidx.datastore.core.DataStoreFactory
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.core.util.AppLogger
import org.giste.roadbooknavigator.features.roadbook.data.persistence.PersistenceMapper
import org.giste.roadbooknavigator.features.roadbook.data.persistence.PersistenceRoadbookSerializer
import org.giste.roadbooknavigator.features.roadbook.data.rn2.Rn2ElementMapper
import org.giste.roadbooknavigator.features.roadbook.data.rn2.Rn2Mapper
import org.giste.roadbooknavigator.features.roadbook.data.rn2.RoadbookGeometryCalculator
import org.giste.roadbooknavigator.features.roadbook.data.rn2.WaypointProcessor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class RoadbookDataIntegrationTest {

    @get:Rule
    val tempFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val logger: AppLogger = mockk(relaxed = true)
    
    private lateinit var persistenceMapper: PersistenceMapper
    private lateinit var rn2Mapper: Rn2Mapper

    @Before
    fun setup() {
        val geometryCalculator = RoadbookGeometryCalculator(logger)
        val rn2ElementMapper = Rn2ElementMapper(geometryCalculator, logger)
        val waypointProcessor = WaypointProcessor(geometryCalculator, rn2ElementMapper, logger)
        rn2Mapper = Rn2Mapper(waypointProcessor, logger)
        persistenceMapper = PersistenceMapper()
    }

    @Test
    fun `should import rn2 file, persist it, and reload accurately`() = runTest {
        val scope1 = CoroutineScope(testDispatcher + Job())
        val dataStoreFile = File(tempFolder.root, "test_roadbook.json")
        
        val dataStore1 = DataStoreFactory.create(
            serializer = PersistenceRoadbookSerializer(logger),
            scope = scope1,
            produceFile = { dataStoreFile }
        )
        
        val repository1 = DataStoreRoadbookRepository(
            mapper = rn2Mapper,
            persistenceMapper = persistenceMapper,
            dataStore = dataStore1,
            ioDispatcher = testDispatcher,
            logger = logger
        )

        // 1. Load the real .rn2 sample
        val inputStream = this.javaClass.classLoader?.getResourceAsStream("route_example.rn2")
            ?: throw IllegalStateException("Resource route_example.rn2 not found")

        // 2. Process it through the repository
        val importResult = repository1.processNewRoadbook(inputStream)
        val importedRoute = importResult.getOrThrow()

        // 3. Verify immediate state
        val activeRoute = repository1.activeRoadbook.first()
        assertNotNull(activeRoute)
        assertEquals(importedRoute, activeRoute)
        
        // 4. Simulate app restart
        scope1.cancel() // Close the first DataStore

        val scope2 = CoroutineScope(testDispatcher + Job())
        val dataStore2 = DataStoreFactory.create(
            serializer = PersistenceRoadbookSerializer(logger),
            scope = scope2,
            produceFile = { dataStoreFile }
        )
        val repository2 = DataStoreRoadbookRepository(
            mapper = rn2Mapper,
            persistenceMapper = persistenceMapper,
            dataStore = dataStore2,
            ioDispatcher = testDispatcher,
            logger = logger
        )

        // 5. Verify data is preserved after "restart"
        val reloadedRoute = repository2.activeRoadbook.first()
        assertNotNull(reloadedRoute)
        assertEquals(importedRoute.name, reloadedRoute!!.name)
        assertEquals(importedRoute.waypoints.size, reloadedRoute.waypoints.size)
        
        // Deep check of the first waypoint elements (testing sealed class serialization)
        val firstWp = importedRoute.waypoints[0]
        val reloadedWp = reloadedRoute.waypoints[0]
        assertEquals(firstWp.tulipElements, reloadedWp.tulipElements)
        assertEquals(firstWp.notesElements, reloadedWp.notesElements)
        
        // Full object equality
        assertEquals(importedRoute, reloadedRoute)
        
        scope2.cancel()
    }
}
