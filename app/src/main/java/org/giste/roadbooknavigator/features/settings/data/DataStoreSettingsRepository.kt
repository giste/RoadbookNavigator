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
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.giste.roadbooknavigator.core.util.logger
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.AppTheme
import org.giste.roadbooknavigator.features.settings.domain.SettingsRepository
import javax.inject.Inject

/**
 * Jetpack DataStore implementation of [SettingsRepository].
 *
 * This class handles the persistence of user preferences in a reactive way
 * using a key-value pair storage system.
 */
internal class DataStoreSettingsRepository @Inject constructor(
    @param:SettingsDataStore private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object Keys {
        val THEME = stringPreferencesKey("app_theme")
        val ORIENTATION = stringPreferencesKey("app_orientation")
        val FULL_SCREEN = booleanPreferencesKey("full_screen")
        val SHORT_DISTANCE_THRESHOLD = longPreferencesKey("short_distance_threshold")
    }

    override fun getSettings(): Flow<AppSettings> = dataStore.data.map { preferences ->
        AppSettings(
            theme = preferences[Keys.THEME]?.let { safeThemeOf(it) } ?: AppTheme.FOLLOW_SYSTEM,
            orientation = preferences[Keys.ORIENTATION]?.let { safeOrientationOf(it) }
                ?: AppOrientation.FOLLOW_SYSTEM,
            fullScreen = preferences[Keys.FULL_SCREEN] ?: true,
            shortDistanceThreshold = preferences[Keys.SHORT_DISTANCE_THRESHOLD]
                ?: AppSettings.DEFAULT_SHORT_DISTANCE_THRESHOLD,
        )
    }.onEach {
        logger.v("DataStoreSettingsRepository: Settings updated: %s", it)
    }

    override suspend fun setTheme(theme: AppTheme) {
        logger.i("DataStoreSettingsRepository: Setting theme to %s", theme)
        dataStore.edit { preferences ->
            preferences[Keys.THEME] = theme.name
        }
    }

    override suspend fun setOrientation(orientation: AppOrientation) {
        logger.i("DataStoreSettingsRepository: Setting orientation to %s", orientation)
        dataStore.edit { preferences ->
            preferences[Keys.ORIENTATION] = orientation.name
        }
    }

    override suspend fun setFullScreen(enabled: Boolean) {
        logger.i("DataStoreSettingsRepository: Setting full screen to %s", enabled)
        dataStore.edit { preferences ->
            preferences[Keys.FULL_SCREEN] = enabled
        }
    }

    override suspend fun setShortDistanceThreshold(threshold: Long) {
        logger.i("DataStoreSettingsRepository: Setting short distance threshold to %d", threshold)
        dataStore.edit { preferences ->
            preferences[Keys.SHORT_DISTANCE_THRESHOLD] = threshold
        }
    }

    private fun safeThemeOf(name: String): AppTheme = try {
        AppTheme.valueOf(name)
    } catch (_: IllegalArgumentException) {
        AppTheme.FOLLOW_SYSTEM
    }

    private fun safeOrientationOf(name: String): AppOrientation = try {
        AppOrientation.valueOf(name)
    } catch (_: IllegalArgumentException) {
        AppOrientation.FOLLOW_SYSTEM
    }
}
