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
 * along with this program.  See <https://www.gnu.org/licenses/>.
 */

package org.giste.roadbooknavigator.features.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.giste.roadbooknavigator.features.settings.data.di.SettingsDataStore
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
class DataStoreSettingsRepository @Inject constructor(
    @param:SettingsDataStore private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object Keys {
        val THEME = stringPreferencesKey("app_theme")
        val ORIENTATION = stringPreferencesKey("app_orientation")
        val SHORT_DISTANCE_THRESHOLD = longPreferencesKey("short_distance_threshold")
        val ODOMETER_SPEED_THRESHOLD = floatPreferencesKey("odometer_speed_threshold")
        val ODOMETER_MIN_ACCURACY = floatPreferencesKey("odometer_min_accuracy")
        val ODOMETER_MIN_VERTICAL_ACCURACY = floatPreferencesKey("odometer_min_vertical_accuracy")
        val ODOMETER_POLLING_INTERVAL = longPreferencesKey("odometer_polling_interval")
        val ODOMETER_MIN_DISTANCE = floatPreferencesKey("odometer_min_distance")
    }

    override fun getSettings(): Flow<AppSettings> = dataStore.data.map { preferences ->
        AppSettings(
            theme = preferences[Keys.THEME]?.let { safeThemeOf(it) } ?: AppTheme.FOLLOW_SYSTEM,
            orientation = preferences[Keys.ORIENTATION]?.let { safeOrientationOf(it) }
                ?: AppOrientation.FOLLOW_SYSTEM,
            shortDistanceThreshold = preferences[Keys.SHORT_DISTANCE_THRESHOLD]
                ?: AppSettings.DEFAULT_SHORT_DISTANCE_THRESHOLD,
            odometerSpeedThreshold = preferences[Keys.ODOMETER_SPEED_THRESHOLD]
                ?: AppSettings.DEFAULT_ODOMETER_SPEED_THRESHOLD,
            odometerMinAccuracy = preferences[Keys.ODOMETER_MIN_ACCURACY]
                ?: AppSettings.DEFAULT_ODOMETER_MIN_ACCURACY,
            odometerMinVerticalAccuracy = preferences[Keys.ODOMETER_MIN_VERTICAL_ACCURACY]
                ?: AppSettings.DEFAULT_ODOMETER_MIN_VERTICAL_ACCURACY,
            odometerPollingInterval = preferences[Keys.ODOMETER_POLLING_INTERVAL]
                ?: AppSettings.DEFAULT_ODOMETER_POLLING_INTERVAL,
            odometerMinDistance = preferences[Keys.ODOMETER_MIN_DISTANCE]
                ?: AppSettings.DEFAULT_ODOMETER_MIN_DISTANCE
        )
    }

    override suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { preferences ->
            preferences[Keys.THEME] = theme.name
        }
    }

    override suspend fun setOrientation(orientation: AppOrientation) {
        dataStore.edit { preferences ->
            preferences[Keys.ORIENTATION] = orientation.name
        }
    }

    override suspend fun setShortDistanceThreshold(threshold: Long) {
        dataStore.edit { preferences ->
            preferences[Keys.SHORT_DISTANCE_THRESHOLD] = threshold
        }
    }

    override suspend fun setOdometerSpeedThreshold(threshold: Float) {
        dataStore.edit { preferences ->
            preferences[Keys.ODOMETER_SPEED_THRESHOLD] = threshold
        }
    }

    override suspend fun setOdometerMinAccuracy(accuracy: Float) {
        dataStore.edit { preferences ->
            preferences[Keys.ODOMETER_MIN_ACCURACY] = accuracy
        }
    }

    override suspend fun setOdometerMinVerticalAccuracy(accuracy: Float) {
        dataStore.edit { preferences ->
            preferences[Keys.ODOMETER_MIN_VERTICAL_ACCURACY] = accuracy
        }
    }

    override suspend fun setOdometerPollingInterval(interval: Long) {
        dataStore.edit { preferences ->
            preferences[Keys.ODOMETER_POLLING_INTERVAL] = interval
        }
    }

    override suspend fun setOdometerMinDistance(distance: Float) {
        dataStore.edit { preferences ->
            preferences[Keys.ODOMETER_MIN_DISTANCE] = distance
        }
    }

    override suspend fun restoreOdometerDefaults() {
        dataStore.edit { preferences ->
            preferences.remove(Keys.ODOMETER_SPEED_THRESHOLD)
            preferences.remove(Keys.ODOMETER_MIN_ACCURACY)
            preferences.remove(Keys.ODOMETER_MIN_VERTICAL_ACCURACY)
            preferences.remove(Keys.ODOMETER_POLLING_INTERVAL)
            preferences.remove(Keys.ODOMETER_MIN_DISTANCE)
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
