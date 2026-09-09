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

package org.giste.map.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.giste.map.di.MapSettingsDataStore
import org.giste.map.MapLogger
import org.giste.map.MapSettings
import org.giste.map.domain.repository.MapSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DataStoreMapSettingsRepository @Inject constructor(
    @param:MapSettingsDataStore private val dataStore: DataStore<Preferences>,
    private val logger: MapLogger
) : MapSettingsRepository {

    private object Keys {
        val INITIAL_ZOOM = intPreferencesKey("initial_zoom")
        val INITIAL_TILT = floatPreferencesKey("initial_tilt")
    }

    override fun getMapSettings(): Flow<MapSettings> = dataStore.data.map { preferences ->
        MapSettings(
            initialZoom = preferences[Keys.INITIAL_ZOOM] ?: MapSettings.DEFAULT_ZOOM,
            initialTilt = preferences[Keys.INITIAL_TILT] ?: MapSettings.DEFAULT_TILT
        )
    }.also { logger.d("DataStoreMapSettingsRepository: New map settings emitted") }

    override suspend fun saveMapSettings(settings: MapSettings) {
        dataStore.edit { preferences ->
            preferences[Keys.INITIAL_ZOOM] = settings.initialZoom
            preferences[Keys.INITIAL_TILT] = settings.initialTilt
        }
        logger.d("DataStoreMapSettingsRepository: Saved map settings: %s", settings)
    }
}
