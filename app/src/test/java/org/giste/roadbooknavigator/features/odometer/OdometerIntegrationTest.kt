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

package org.giste.roadbooknavigator.features.odometer

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.location.domain.UserLocation
import org.giste.roadbooknavigator.features.location.domain.ObserveLocationUseCase
import org.giste.roadbooknavigator.features.odometer.data.DataStoreOdometerRepository
import org.giste.roadbooknavigator.features.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.odometer.domain.usecase.GetOdometerUseCase
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class OdometerIntegrationTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var odometerRepository: DataStoreOdometerRepository
    private val observeLocationUseCase: ObserveLocationUseCase = mockk()
    private val getSettingsUseCase: GetSettingsUseCase = mockk()
    
    private val gpsFlow = MutableSharedFlow<UserLocation>()
    private val settingsFlow = MutableSharedFlow<AppSettings>()
    
    private lateinit var getOdometerUseCase: GetOdometerUseCase

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(temporaryFolder.newFolder(), "test_odometer.preferences_pb") }
        )
        odometerRepository = DataStoreOdometerRepository(dataStore)
        
        every { observeLocationUseCase() } returns gpsFlow
        every { getSettingsUseCase() } returns settingsFlow
        
        getOdometerUseCase = GetOdometerUseCase(odometerRepository, observeLocationUseCase, getSettingsUseCase)
    }

    @Test
    fun `full flow should update persistence and emit new state when movement occurs`() = runTest(testDispatcher) {
        // Start observing odometer
        val results = mutableListOf<Odometer>()
        val job = backgroundScope.launch {
            getOdometerUseCase().collect { results.add(it) }
        }

        // 1. Setup settings
        settingsFlow.emit(AppSettings(odometerMinAccuracy = 10f, odometerSpeedThreshold = 0.5f))

        // 2. Initial state should be 0,0 (emitted when odometerRepository.odometer is combined)
        assertTrue(results.any { it.total == 0.0 && it.partial == 0.0 })

        // 3. Emit first location (seed)
        gpsFlow.emit(createLocation(40.0, -3.0))
        
        // 4. Emit second location (~111m north)
        // 0.001 degrees latitude is approx 111 meters
        gpsFlow.emit(createLocation(40.001, -3.0))

        // 5. Verify the odometer has updated
        val lastOdometer = results.last()
        assertEquals(111.194, lastOdometer.total, 0.01)
        assertEquals(lastOdometer.total, lastOdometer.partial, 0.001)

        // 6. Verify persistence with a fresh repository instance
        val freshRepo = DataStoreOdometerRepository(dataStore)
        val persistedState = freshRepo.odometer.first()
        assertEquals(lastOdometer.total, persistedState.total, 0.001)

        job.cancel()
    }

    @Test
    fun `odometer should survive settings changes and keep tracking from last valid point`() = runTest(testDispatcher) {
        val results = mutableListOf<Odometer>()
        val job = backgroundScope.launch {
            getOdometerUseCase().collect { results.add(it) }
        }

        // Valid fix 1
        settingsFlow.emit(AppSettings(odometerSpeedThreshold = 0.5f))
        val loc1 = createLocation(40.0, -3.0)
        gpsFlow.emit(loc1)

        // Valid fix 2 -> Distance update
        val loc2 = createLocation(40.001, -3.0)
        gpsFlow.emit(loc2)
        val firstDistance = results.last().total
        assertTrue(firstDistance > 0)

        // Change settings (e.g. higher speed threshold)
        settingsFlow.emit(AppSettings(odometerSpeedThreshold = 50.0f))
        
        // Valid fix 3 but ignored due to speed threshold
        val loc3 = createLocation(40.002, -3.0, speed = 10f) // 10 < 50
        gpsFlow.emit(loc3)
        
        // Distance should not have changed
        assertEquals(firstDistance, results.last().total, 0.001)

        // Restore settings
        settingsFlow.emit(AppSettings(odometerSpeedThreshold = 0.5f))

        // Valid fix 4 -> Should calculate distance from loc2 (last valid point) to loc4
        val loc4 = createLocation(40.003, -3.0)
        gpsFlow.emit(loc4)

        // Total distance = distance(loc1, loc2) + distance(loc2, loc4)
        // 111.194 + 222.388 = 333.582
        assertEquals(333.582, results.last().total, 0.01)

        job.cancel()
    }

    private fun createLocation(
        lat: Double,
        lon: Double,
        speed: Float = 10f
    ) = UserLocation(
        latitude = lat,
        longitude = lon,
        altitude = 0.0,
        accuracy = 5f,
        verticalAccuracy = 1f,
        speed = speed,
        bearing = 0f,
        time = System.currentTimeMillis()
    )
}
