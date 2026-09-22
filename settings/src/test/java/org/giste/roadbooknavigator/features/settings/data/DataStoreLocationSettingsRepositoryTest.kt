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

package org.giste.roadbooknavigator.features.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.location.LocationSettings
import org.giste.roadbooknavigator.core.util.Logger
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreLocationSettingsRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var logger: Logger
    private lateinit var repository: DataStoreLocationSettingsRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(temporaryFolder.newFolder(), "test_location_settings.preferences_pb") }
        )
        logger = mockk(relaxed = true)
        repository = DataStoreLocationSettingsRepository(dataStore, logger)
    }

    @Test
    fun `initial settings should be default when no data exists`() = runTest {
        val settings = repository.settings.first()
        assertEquals(LocationSettings.DEFAULT_POLLING_INTERVAL, settings.pollingInterval)
        assertEquals(LocationSettings.DEFAULT_MIN_DISTANCE, settings.minDistance)
    }

    @Test
    fun `updateSettings should persist new values`() = runTest {
        val updated = LocationSettings(pollingInterval = 1000L, minDistance = 5.0f)
        repository.updateSettings(updated)

        val settings = repository.settings.first()
        assertEquals(1000L, settings.pollingInterval)
        assertEquals(5.0f, settings.minDistance)

        // Verify with new repository instance reading from same DataStore
        val newRepo = DataStoreLocationSettingsRepository(dataStore, logger)
        val persisted = newRepo.settings.first()
        assertEquals(1000L, persisted.pollingInterval)
        assertEquals(5.0f, persisted.minDistance)
    }

    @Test
    fun `restoreDefaults should reset values`() = runTest {
        // Given
        val updated = LocationSettings(pollingInterval = 2000L, minDistance = 8.0f)
        repository.updateSettings(updated)

        // When
        repository.restoreDefaults()

        // Then
        val settings = repository.settings.first()
        assertEquals(LocationSettings.DEFAULT_POLLING_INTERVAL, settings.pollingInterval)
        assertEquals(LocationSettings.DEFAULT_MIN_DISTANCE, settings.minDistance)
    }
}
