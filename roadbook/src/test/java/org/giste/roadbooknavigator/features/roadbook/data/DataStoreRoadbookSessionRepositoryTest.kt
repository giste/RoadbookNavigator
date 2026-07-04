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
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreRoadbookSessionRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: DataStoreRoadbookSessionRepository
    private val logger: Logger = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(testDispatcher + SupervisorJob()),
            produceFile = { File(temporaryFolder.newFolder(), "test_session.preferences_pb") }
        )
        repository = DataStoreRoadbookSessionRepository(dataStore, logger)
    }

    @Test
    fun `scrollPosition should emit default RoadbookPosition initially`() = runTest {
        val position = repository.scrollPosition.first()
        assertEquals(RoadbookPosition(0, 0), position)
    }

    @Test
    fun `saveScrollPosition should persist values`() = runTest {
        val expectedPosition = RoadbookPosition(index = 42, offset = 150)

        repository.saveScrollPosition(expectedPosition)
        
        val position = repository.scrollPosition.first()
        assertEquals(expectedPosition, position)

        // Verify persistence with a new instance
        val newRepo = DataStoreRoadbookSessionRepository(dataStore, logger)
        val persistedPosition = newRepo.scrollPosition.first()
        assertEquals(expectedPosition, persistedPosition)
    }

    @Test
    fun `scrollPosition should emit updates in real time`() = runTest {
        val positions = mutableListOf<RoadbookPosition>()
        // Use backgroundScope so it's automatically cancelled, 
        // but we still want to verify the items.
        val job = backgroundScope.launch(testDispatcher) {
            repository.scrollPosition.toList(positions)
        }

        // Small delays to avoid DataStore coalescing emissions in some environments
        repository.saveScrollPosition(RoadbookPosition(1, 10))
        repository.saveScrollPosition(RoadbookPosition(2, 20))

        // Give it a moment to process the emissions
        testScheduler.runCurrent()

        assertEquals(
            listOf(
                RoadbookPosition(0, 0),
                RoadbookPosition(1, 10),
                RoadbookPosition(2, 20)
            ),
            positions
        )
        job.cancel()
    }

    @Test
    fun `should handle partial data in DataStore by falling back to defaults`() = runTest {
        // Manually inject only the index into DataStore
        val indexKey = intPreferencesKey("roadbook_scroll_index")
        dataStore.edit { it[indexKey] = 100 }

        val position = repository.scrollPosition.first()
        
        // Offset should be 0 (default)
        assertEquals(RoadbookPosition(100, 0), position)
    }

    @Test
    fun `should handle negative data by falling back to 0`() = runTest {
        val indexKey = intPreferencesKey("roadbook_scroll_index")
        val offsetKey = intPreferencesKey("roadbook_scroll_offset")
        dataStore.edit { 
            it[indexKey] = -5 
            it[offsetKey] = -10
        }

        val position = repository.scrollPosition.first()
        
        assertEquals(RoadbookPosition(0, 0), position)
    }

    @Test
    fun `concurrent updates should result in the last write winning`() = runTest {
        val finalPosition = RoadbookPosition(99, 999)
        
        // Launch multiple concurrent saves and wait for them
        val job1 = launch { repository.saveScrollPosition(RoadbookPosition(1, 10)) }
        val job2 = launch { repository.saveScrollPosition(RoadbookPosition(2, 20)) }
        val job3 = launch { repository.saveScrollPosition(finalPosition) }

        joinAll(job1, job2, job3)

        val position = repository.scrollPosition.first()
        assertEquals(finalPosition, position)
    }
}
