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
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.giste.location.LocationSettings
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.settings.domain.location.LocationSettingsRepository
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Jetpack DataStore implementation of [LocationSettingsRepository] for the app.
 */
@Singleton
internal class DataStoreLocationSettingsRepository @Inject constructor(
    @param:LocationSettingsDataStore private val dataStore: DataStore<Preferences>,
    private val logger: Logger
) : LocationSettingsRepository {

    private object Keys {
        val POLLING_INTERVAL = longPreferencesKey("polling_interval")
        val MIN_DISTANCE = floatPreferencesKey("min_distance")
    }

    override val settings: Flow<LocationSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            LocationSettings(
                pollingInterval = preferences[Keys.POLLING_INTERVAL]
                    ?: LocationSettings.DEFAULT_POLLING_INTERVAL,
                minDistance = preferences[Keys.MIN_DISTANCE]
                    ?: LocationSettings.DEFAULT_MIN_DISTANCE,
            )
        }

    override suspend fun updateSettings(settings: LocationSettings) {
        logger.i("DataStoreLocationSettingsRepository: Saving settings %s", settings)
        dataStore.edit { preferences ->
            preferences[Keys.POLLING_INTERVAL] = settings.pollingInterval
            preferences[Keys.MIN_DISTANCE] = settings.minDistance
        }
    }

    override suspend fun restoreDefaults() {
        logger.i("DataStoreLocationSettingsRepository: Restoring default location settings")
        dataStore.edit { preferences ->
            preferences.remove(Keys.POLLING_INTERVAL)
            preferences.remove(Keys.MIN_DISTANCE)
        }
    }
}
