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
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.core.settings.domain.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreAppSettingsRepositoryTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var logger: Logger
    private lateinit var repository: DataStoreAppSettingsRepository
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(temporaryFolder.newFolder(), "test_settings.preferences_pb") }
        )
        logger = mockk(relaxed = true)
        repository = DataStoreAppSettingsRepository(dataStore, logger)
    }

    @Test
    fun `initial settings should be default when no data exists`() = runTest {
        val settings = repository.getSettings().first()
        assertEquals(AppTheme.FOLLOW_SYSTEM, settings.theme)
        assertEquals(AppOrientation.FOLLOW_SYSTEM, settings.orientation)
    }

    @Test
    fun `setTheme should persist theme value`() = runTest {
        repository.setTheme(AppTheme.DARK)
        
        val settings = repository.getSettings().first()
        assertEquals(AppTheme.DARK, settings.theme)

        // Verify with new instance
        val newRepo = DataStoreAppSettingsRepository(dataStore, logger)
        val persisted = newRepo.getSettings().first()
        assertEquals(AppTheme.DARK, persisted.theme)
    }

    @Test
    fun `setOrientation should persist orientation value`() = runTest {
        repository.setOrientation(AppOrientation.HORIZONTAL)
        
        val settings = repository.getSettings().first()
        assertEquals(AppOrientation.HORIZONTAL, settings.orientation)

        // Verify with new instance
        val newRepo = DataStoreAppSettingsRepository(dataStore, logger)
        val persisted = newRepo.getSettings().first()
        assertEquals(AppOrientation.HORIZONTAL, persisted.orientation)
    }

    @Test
    fun `setLandscapeDistanceSectionWeight should persist weight value`() = runTest {
        val testWeight = 0.35f
        repository.setLandscapeDistanceSectionWeight(testWeight)

        val settings = repository.getSettings().first()
        assertEquals(testWeight, settings.landscapeDistanceSectionWeight)

        // Verify with new instance
        val newRepo = DataStoreAppSettingsRepository(dataStore, logger)
        val persisted = newRepo.getSettings().first()
        assertEquals(testWeight, persisted.landscapeDistanceSectionWeight)
    }

    @Test
    fun `safe parsing should fallback to default for unknown theme string`() = runTest {
        val settings = repository.getSettings().first()
        assertEquals(AppTheme.FOLLOW_SYSTEM, settings.theme)
    }
}
