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

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.giste.location.LocationLogger
import org.giste.location.LocationSettings
import org.giste.location.di.LocationSettingsDataStore
import org.giste.location.domain.LocationSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataStoreLocationSettingsRepository @Inject constructor(
    @LocationSettingsDataStore private val dataStore: DataStore<Preferences>,
    private val logger: LocationLogger
) : LocationSettingsRepository {

    private object Keys {
        val POLLING_INTERVAL = longPreferencesKey("polling_interval")
        val MIN_DISTANCE = floatPreferencesKey("min_distance")
    }

    override fun getLocationSettings(): Flow<LocationSettings> = dataStore.data.map { preferences ->
        LocationSettings(
            pollingInterval = preferences[Keys.POLLING_INTERVAL]
                ?: LocationSettings.DEFAULT_POLLING_INTERVAL,
            minDistance = preferences[Keys.MIN_DISTANCE]
                ?: LocationSettings.DEFAULT_MIN_DISTANCE,
        )
    }

    override suspend fun saveLocationSettings(settings: LocationSettings) {
        dataStore.edit { preferences ->
            preferences[Keys.POLLING_INTERVAL] = settings.pollingInterval
            preferences[Keys.MIN_DISTANCE] = settings.minDistance
        }
        logger.d("DataStoreLocationSettingsRepository: Saved settings %s", settings)
    }

    override suspend fun restoreDefaults() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
        logger.d("DataStoreLocationSettingsRepository: Restored default settings")
    }
}
