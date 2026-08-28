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

package org.giste.odometer.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.odometer.domain.OdometerLogger
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreOdometerRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var logger: OdometerLogger
    private lateinit var odometerRepository: DataStoreOdometerRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(temporaryFolder.newFolder(), "test.preferences_pb") }
        )
        logger = mockk(relaxed = true)
        odometerRepository = DataStoreOdometerRepository(
            dataStore = dataStore,
            logger = logger,
            ioDispatcher = testDispatcher,
            scope = testScope
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial distances should be zero when no data exists`() = runTest {
        val initial = odometerRepository.odometer.first()
        Assert.assertEquals(0.0, initial.total, 0.0)
        Assert.assertEquals(0.0, initial.partial, 0.0)
    }

    @Test
    fun `updateDistance should persist new values if threshold reached`() = runTest {
        // Default threshold is 50m (0.05km). 10.5km is well above it.
        odometerRepository.updateDistance(10.5)

        val updated = odometerRepository.odometer.first()
        Assert.assertEquals(10.5, updated.total, 0.0)
        Assert.assertEquals(10.5, updated.partial, 0.0)

        // Persistence check with new instance
        val newRepo = DataStoreOdometerRepository(dataStore, logger, testDispatcher, testScope)
        val persisted = newRepo.odometer.first()
        Assert.assertEquals(10.5, persisted.total, 0.0)
    }

    @Test
    fun `resetPartialDistance should only reset partial distance`() = runTest {
        odometerRepository.updateDistance(100.0)
        odometerRepository.resetPartialDistance()

        val state = odometerRepository.odometer.first()
        Assert.assertEquals(100.0, state.total, 0.0)
        Assert.assertEquals(0.0, state.partial, 0.0)
    }

    @Test
    fun `resetAllDistances should reset everything`() = runTest {
        odometerRepository.updateDistance(100.0)
        odometerRepository.resetAllDistances()

        val state = odometerRepository.odometer.first()
        Assert.assertEquals(0.0, state.total, 0.0)
        Assert.assertEquals(0.0, state.partial, 0.0)
    }

    @Test
    fun `updateDistance should buffer updates until default distance threshold is reached`() = runTest {
        // Default threshold is 50m (0.05km). Add 10m (0.01km).
        odometerRepository.updateDistance(0.01)

        // Live state should be updated
        val live = odometerRepository.odometer.first()
        Assert.assertEquals(0.01, live.total, 0.0)

        // DataStore should NOT be updated yet (still 0)
        val newRepo = DataStoreOdometerRepository(dataStore, logger, testDispatcher, testScope)
        val persisted = newRepo.odometer.first()
        Assert.assertEquals(0.0, persisted.total, 0.0)

        // Add another 50m (total 60m > 50m threshold)
        odometerRepository.updateDistance(0.05)

        // Now persistence should have the value
        val newRepo2 = DataStoreOdometerRepository(dataStore, logger, testDispatcher, testScope)
        val persisted2 = newRepo2.odometer.first()
        Assert.assertEquals(0.06, persisted2.total, 0.0001)
    }
}
