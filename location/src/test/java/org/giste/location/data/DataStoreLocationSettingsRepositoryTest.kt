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

package org.giste.location.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.giste.location.LocationLogger
import org.giste.location.LocationSettings
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class DataStoreLocationSettingsRepositoryTest {

    private val logger: LocationLogger = mockk(relaxed = true)
    private lateinit var repository: DataStoreLocationSettingsRepository
    private lateinit var testDataStore: DataStore<Preferences>

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        testDataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(Dispatchers.Unconfined + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile("test_settings") }
        )
        repository = DataStoreLocationSettingsRepository(
            dataStore = testDataStore,
            logger = logger
        )
    }

    @After
    fun tearDown() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        File(context.filesDir, "datastore").deleteRecursively()
    }

    @Test
    fun `default values are returned initially`() = runTest {
        val settings = repository.getLocationSettings().first()
        assertEquals(LocationSettings.DEFAULT_POLLING_INTERVAL, settings.pollingInterval)
        assertEquals(LocationSettings.DEFAULT_MIN_DISTANCE, settings.minDistance, 0.01f)
    }

    @Test
    fun `updated values are persisted`() = runTest {
        val newSettings = LocationSettings(pollingInterval = 1000L, minDistance = 5.0f)
        repository.saveLocationSettings(newSettings)

        val persistedSettings = repository.getLocationSettings().first()
        assertEquals(1000L, persistedSettings.pollingInterval)
        assertEquals(5.0f, persistedSettings.minDistance, 0.01f)
    }

    @Test
    fun `restoreDefaults resets to default values`() = runTest {
        val newSettings = LocationSettings(pollingInterval = 1000L, minDistance = 5.0f)
        repository.saveLocationSettings(newSettings)

        repository.restoreDefaults()

        val settings = repository.getLocationSettings().first()
        assertEquals(LocationSettings.DEFAULT_POLLING_INTERVAL, settings.pollingInterval)
        assertEquals(LocationSettings.DEFAULT_MIN_DISTANCE, settings.minDistance, 0.01f)
    }
}
