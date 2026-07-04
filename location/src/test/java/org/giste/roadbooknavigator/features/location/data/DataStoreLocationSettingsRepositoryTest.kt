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

package org.giste.roadbooknavigator.features.location.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.location.domain.LocationSettings
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
    private lateinit var repository: DataStoreLocationSettingsRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val logger: Logger = mockk(relaxed = true)

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(temporaryFolder.newFolder(), "test_location_settings.preferences_pb") }
        )
        repository = DataStoreLocationSettingsRepository(dataStore, logger)
    }

    @Test
    fun `initial settings should be default when no data exists`() = runTest {
        val settings = repository.getLocationSettings().first()
        assertEquals(LocationSettings.DEFAULT_POLLING_INTERVAL, settings.pollingInterval)
        assertEquals(LocationSettings.DEFAULT_MIN_DISTANCE, settings.minDistance)
    }

    @Test
    fun `updatePollingInterval should persist value`() = runTest {
        val newValue = 2000L
        repository.updatePollingInterval(newValue)
        
        val settings = repository.getLocationSettings().first()
        assertEquals(newValue, settings.pollingInterval)
    }

    @Test
    fun `updateMinDistance should persist value`() = runTest {
        val newValue = 10.0f
        repository.updateMinDistance(newValue)
        
        val settings = repository.getLocationSettings().first()
        assertEquals(newValue, settings.minDistance)
    }

    @Test
    fun `restoreDefaults should reset values`() = runTest {
        // Given
        repository.updatePollingInterval(3000L)
        
        // When
        repository.restoreDefaults()
        
        // Then
        val settings = repository.getLocationSettings().first()
        assertEquals(LocationSettings.DEFAULT_POLLING_INTERVAL, settings.pollingInterval)
    }
}
