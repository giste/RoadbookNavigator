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

package org.giste.roadbooknavigator.features.odometer.data

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
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettings
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreOdometerSettingsRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var logger: Logger
    private lateinit var repository: DataStoreOdometerSettingsRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(temporaryFolder.newFolder(), "test_odometer_settings.preferences_pb") }
        )
        logger = mockk(relaxed = true)
        repository = DataStoreOdometerSettingsRepository(dataStore, logger)
    }

    @Test
    fun `initial settings should be default when no data exists`() = runTest {
        val settings = repository.getSettings().first()
        assertEquals(OdometerSettings.DEFAULT_SPEED_THRESHOLD, settings.speedThreshold)
        assertEquals(OdometerSettings.DEFAULT_MIN_ACCURACY, settings.minAccuracy)
        assertEquals(OdometerSettings.DEFAULT_MIN_VERTICAL_ACCURACY, settings.minVerticalAccuracy)
    }

    @Test
    fun `setSpeedThreshold should persist value`() = runTest {
        val newValue = 1.5f
        repository.setSpeedThreshold(newValue)
        
        val settings = repository.getSettings().first()
        assertEquals(newValue, settings.speedThreshold)

        // Verify with new instance
        val newRepo = DataStoreOdometerSettingsRepository(dataStore, logger)
        val persisted = newRepo.getSettings().first()
        assertEquals(newValue, persisted.speedThreshold)
    }

    @Test
    fun `setMinAccuracy should persist value`() = runTest {
        val newValue = 30.0f
        repository.setMinAccuracy(newValue)
        
        val settings = repository.getSettings().first()
        assertEquals(newValue, settings.minAccuracy)
    }

    @Test
    fun `setMinVerticalAccuracy should persist value`() = runTest {
        val newValue = 15.0f
        repository.setMinVerticalAccuracy(newValue)
        
        val settings = repository.getSettings().first()
        assertEquals(newValue, settings.minVerticalAccuracy)
    }

    @Test
    fun `setRemoteKeys should persist values`() = runTest {
        val increase = listOf(1, 2)
        val decrease = listOf(3, 4)
        val reset = listOf(5, 6)
        
        repository.setRemoteKeys(increase, decrease, reset)
        
        val settings = repository.getSettings().first()
        assertEquals(increase, settings.increasePartial)
        assertEquals(decrease, settings.decreasePartial)
        assertEquals(reset, settings.resetPartial)
    }

    @Test
    fun `restoreSettingsDefaults should reset values`() = runTest {
        // Given
        repository.setSpeedThreshold(2.0f)
        repository.setMinAccuracy(50f)
        repository.setMinVerticalAccuracy(30f)
        repository.setRemoteKeys(listOf(1), listOf(2), listOf(3))
        
        // When
        repository.restoreSettingsDefaults()
        
        // Then
        val settings = repository.getSettings().first()
        assertEquals(OdometerSettings.DEFAULT_SPEED_THRESHOLD, settings.speedThreshold)
        assertEquals(OdometerSettings.DEFAULT_MIN_ACCURACY, settings.minAccuracy)
        assertEquals(OdometerSettings.DEFAULT_MIN_VERTICAL_ACCURACY, settings.minVerticalAccuracy)
        assertEquals(OdometerSettings.DEFAULT_INCREASE_KEYS, settings.increasePartial)
        assertEquals(OdometerSettings.DEFAULT_DECREASE_KEYS, settings.decreasePartial)
        assertEquals(OdometerSettings.DEFAULT_RESET_KEYS, settings.resetPartial)
    }
}
