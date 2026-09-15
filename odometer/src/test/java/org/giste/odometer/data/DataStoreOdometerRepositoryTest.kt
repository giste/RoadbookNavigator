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
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.odometer.OdometerLogger
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.io.IOException
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreOdometerRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var logger: OdometerLogger
    private lateinit var timeProvider: FakeTimeProvider
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
        timeProvider = FakeTimeProvider()
        odometerRepository = DataStoreOdometerRepository(
            dataStore = dataStore,
            logger = logger,
            ioDispatcher = testDispatcher,
            scope = testScope,
            timeProvider = timeProvider
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private class FakeTimeProvider(var time: Long = 0L) : OdometerTimeProvider {
        override fun currentTimeMillis(): Long = time
    }

    @Test
    fun `initial distances should be zero when no data exists`() = runTest {
        val initial = odometerRepository.odometer.first()
        Assert.assertEquals(0.0, initial.total, 0.0)
        Assert.assertEquals(0.0, initial.partial, 0.0)
    }

    @Test
    fun `updateDistance should persist new values if threshold reached`() = runTest {
        // Default threshold is 500m (0.5km). 10.5km is well above it.
        odometerRepository.updateDistance(10.5)

        val updated = odometerRepository.odometer.first()
        Assert.assertEquals(10.5, updated.total, 0.0)
        Assert.assertEquals(10.5, updated.partial, 0.0)

        // Persistence check with new instance
        val newRepo = DataStoreOdometerRepository(dataStore, logger, testDispatcher, testScope, timeProvider)
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
        // Default threshold is 500m (0.5km). Add 100m (0.1km).
        odometerRepository.updateDistance(0.1)

        // Live state should be updated
        val live = odometerRepository.odometer.first()
        Assert.assertEquals(0.1, live.total, 0.0)

        // DataStore should NOT be updated yet (still 0)
        val newRepo = DataStoreOdometerRepository(dataStore, logger, testDispatcher, testScope, timeProvider)
        val persisted = newRepo.odometer.first()
        Assert.assertEquals(0.0, persisted.total, 0.0)

        // Add another 450m (total 550m > 500m threshold)
        odometerRepository.updateDistance(0.45)

        // Now persistence should have the value
        val newRepo2 = DataStoreOdometerRepository(dataStore, logger, testDispatcher, testScope, timeProvider)
        val persisted2 = newRepo2.odometer.first()
        Assert.assertEquals(0.55, persisted2.total, 0.0001)
    }

    @Test
    fun `should NOT lose meters if a GPS update arrives during disk persistence`() = runTest(testDispatcher) {
        // 1. We use a custom DataStore wrapper to simulate a slow write
        val slowDataStore = object : DataStore<Preferences> {
            override val data: Flow<Preferences> = dataStore.data
            override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
                delay(1000.milliseconds) // Simulate slow disk I/O
                return dataStore.updateData(transform)
            }
        }

        val safeRepo = DataStoreOdometerRepository(slowDataStore, logger, testDispatcher, testScope, timeProvider)

        // Start the first update (triggers persistence because > 500m)
        val firstUpdate = async { safeRepo.updateDistance(0.600) } // 600m
        
        // Wait a bit to ensure it's "writing" but not finished
        advanceTimeBy(500.milliseconds)

        // 2. WHILE writing, a second GPS update arrives for another 50m
        safeRepo.updateDistance(0.050) // 50m

        // Complete the first update
        firstUpdate.await()

        // 3. Verify total distance in memory is correct (650m)
        val finalState = safeRepo.odometer.first()
        Assert.assertEquals(0.650, finalState.total, 0.0001)

        // 4. Verify that persistence eventually has the correct final values
        // Trigger another save to flush the buffer (now it will be fast)
        safeRepo.updateDistance(0.500) 
        
        val persisted = DataStoreOdometerRepository(dataStore, logger, testDispatcher, testScope, timeProvider).odometer.first()
        Assert.assertEquals(1.150, persisted.total, 0.0001)
    }

    @Test
    fun `should persist small distances after time threshold is reached`() = runTest(testDispatcher) {
        // 1. User moves only 1 meter (0.001 km). Below 500m threshold.
        odometerRepository.updateDistance(0.001)

        // Verify it's NOT on disk yet
        val newInstance1 = DataStoreOdometerRepository(dataStore, logger, testDispatcher, testScope, timeProvider)
        Assert.assertEquals(0.0, newInstance1.odometer.first().total, 0.0)

        // 2. Advance time by 61 seconds (threshold is 60s)
        timeProvider.time += 61000

        // 3. Another small update arrives
        odometerRepository.updateDistance(0.001)

        // 4. Verify it's now on disk (Total 2m)
        val newInstance2 = DataStoreOdometerRepository(dataStore, logger, testDispatcher, testScope, timeProvider)
        Assert.assertEquals(0.002, newInstance2.odometer.first().total, 0.0001)
    }

    @Test
    fun `should recover from DataStore failures without losing in-memory distance`() = runTest(testDispatcher) {
        val failingDataStore = mockk<DataStore<Preferences>>()
        coEvery { failingDataStore.data } returns dataStore.data
        coEvery { failingDataStore.updateData(any()) } throws IOException("Disk full")

        val repository = DataStoreOdometerRepository(failingDataStore, logger, testDispatcher, testScope, timeProvider)

        // 1. User moves 600m. Persistence fails (above 500m threshold).
        repository.updateDistance(0.600)

        // 2. Verify UI state is still 600m (Memory SSOT works)
        Assert.assertEquals(0.600, repository.odometer.value.total, 0.0001)

        // 3. Fix the DataStore (stop throwing)
        val transformSlot = slot<suspend (Preferences) -> Preferences>()
        coEvery { failingDataStore.updateData(capture(transformSlot)) } coAnswers {
            dataStore.updateData(transformSlot.captured)
        }

        // 4. Next update arrives
        repository.updateDistance(0.100)

        // 5. Verify total is correct (700m) and finally persisted
        Assert.assertEquals(0.700, repository.odometer.value.total, 0.0001)
        
        val persisted = DataStoreOdometerRepository(dataStore, logger, testDispatcher, testScope, timeProvider).odometer.first()
        Assert.assertEquals(0.700, persisted.total, 0.0001)
    }
}
