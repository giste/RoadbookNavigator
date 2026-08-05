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
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.settings.domain.input.InputKeySettings
import org.giste.roadbooknavigator.features.settings.domain.input.RemoteModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreInputKeySettingsRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var logger: Logger
    private lateinit var repository: DataStoreInputKeySettingsRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(temporaryFolder.newFolder(), "test_input_settings.preferences_pb") }
        )
        logger = mockk(relaxed = true)
        repository = DataStoreInputKeySettingsRepository(dataStore, logger)
    }

    @Test
    fun `initial settings should be default when no data exists`() = runTest {
        val settings = repository.getInputKeySettings().first()
        assertEquals(RemoteModel.DND2, settings.model)
        assertEquals(InputKeySettings.DEFAULT_UP_KEYS, settings.upKeys)
    }

    @Test
    fun `setRemoteModel should persist remote model value`() = runTest {
        repository.setRemoteModel(RemoteModel.TERRA_PIRATA)
        
        val settings = repository.getInputKeySettings().first()
        assertEquals(RemoteModel.TERRA_PIRATA, settings.model)

        // Verify with new instance
        val newRepo = DataStoreInputKeySettingsRepository(dataStore, logger)
        val persisted = newRepo.getInputKeySettings().first()
        assertEquals(RemoteModel.TERRA_PIRATA, persisted.model)
    }

    @Test
    fun `setRoadbookRemoteKeys should persist roadbook key values`() = runTest {
        val up = listOf(1, 2)
        val down = listOf(3, 4)
        repository.setRoadbookRemoteKeys(up, down)

        val settings = repository.getInputKeySettings().first()
        assertEquals(up, settings.upKeys)
        assertEquals(down, settings.downKeys)

        // Verify with new instance
        val newRepo = DataStoreInputKeySettingsRepository(dataStore, logger)
        val persisted = newRepo.getInputKeySettings().first()
        assertEquals(up, persisted.upKeys)
        assertEquals(down, persisted.downKeys)
    }

    @Test
    fun `setOdometerRemoteKeys should persist odometer key values`() = runTest {
        val inc = listOf(1, 2)
        val dec = listOf(3, 4)
        val res = listOf(5, 6)
        repository.setOdometerRemoteKeys(inc, dec, res)

        val settings = repository.getInputKeySettings().first()
        assertEquals(inc, settings.increasePartialKeys)
        assertEquals(dec, settings.decreasePartialKeys)
        assertEquals(res, settings.resetPartialKeys)

        // Verify with new instance
        val newRepo = DataStoreInputKeySettingsRepository(dataStore, logger)
        val persisted = newRepo.getInputKeySettings().first()
        assertEquals(inc, persisted.increasePartialKeys)
        assertEquals(dec, persisted.decreasePartialKeys)
        assertEquals(res, persisted.resetPartialKeys)
    }
}
