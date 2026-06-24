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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.roadbook.domain.RoadbookPosition
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
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(temporaryFolder.newFolder(), "test_session.preferences_pb") }
        )
        repository = DataStoreRoadbookSessionRepository(dataStore)
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
        val newRepo = DataStoreRoadbookSessionRepository(dataStore)
        val persistedPosition = newRepo.scrollPosition.first()
        assertEquals(expectedPosition, persistedPosition)
    }
}
