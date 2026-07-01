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
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.giste.roadbooknavigator.features.location.domain.LocationSettings
import org.giste.roadbooknavigator.features.location.domain.LocationSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Jetpack DataStore implementation of [LocationSettingsRepository].
 */
@Singleton
internal class DataStoreLocationSettingsRepository @Inject constructor(
    @param:LocationSettingsDataStore private val dataStore: DataStore<Preferences>
) : LocationSettingsRepository {

    private object Keys {
        val POLLING_INTERVAL = longPreferencesKey("polling_interval")
        val MIN_DISTANCE = floatPreferencesKey("min_distance")
        val MIN_ACCURACY = floatPreferencesKey("min_accuracy")
        val MIN_VERTICAL_ACCURACY = floatPreferencesKey("min_vertical_accuracy")
        val SPEED_THRESHOLD = floatPreferencesKey("speed_threshold")
    }

    override fun getLocationSettings(): Flow<LocationSettings> = dataStore.data.map { preferences ->
        LocationSettings(
            pollingInterval = preferences[Keys.POLLING_INTERVAL]
                ?: LocationSettings.DEFAULT_POLLING_INTERVAL,
            minDistance = preferences[Keys.MIN_DISTANCE]
                ?: LocationSettings.DEFAULT_MIN_DISTANCE,
            minAccuracy = preferences[Keys.MIN_ACCURACY]
                ?: LocationSettings.DEFAULT_MIN_ACCURACY,
            minVerticalAccuracy = preferences[Keys.MIN_VERTICAL_ACCURACY]
                ?: LocationSettings.DEFAULT_MIN_VERTICAL_ACCURACY,
            speedThreshold = preferences[Keys.SPEED_THRESHOLD]
                ?: LocationSettings.DEFAULT_SPEED_THRESHOLD
        )
    }

    override suspend fun updatePollingInterval(interval: Long) {
        dataStore.edit { preferences ->
            preferences[Keys.POLLING_INTERVAL] = interval
        }
    }

    override suspend fun updateMinDistance(distance: Float) {
        dataStore.edit { preferences ->
            preferences[Keys.MIN_DISTANCE] = distance
        }
    }

    override suspend fun updateMinAccuracy(accuracy: Float) {
        dataStore.edit { preferences ->
            preferences[Keys.MIN_ACCURACY] = accuracy
        }
    }

    override suspend fun updateMinVerticalAccuracy(accuracy: Float) {
        dataStore.edit { preferences ->
            preferences[Keys.MIN_VERTICAL_ACCURACY] = accuracy
        }
    }

    override suspend fun updateSpeedThreshold(threshold: Float) {
        dataStore.edit { preferences ->
            preferences[Keys.SPEED_THRESHOLD] = threshold
        }
    }

    override suspend fun restoreDefaults() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
