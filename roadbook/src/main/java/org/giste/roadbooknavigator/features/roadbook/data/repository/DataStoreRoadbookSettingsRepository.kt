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

package org.giste.roadbooknavigator.features.roadbook.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookSettings
import org.giste.roadbooknavigator.features.roadbook.domain.model.ShortDistanceThreshold
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookSettingsRepository
import java.io.IOException
import javax.inject.Inject

/**
 * DataStore implementation of [RoadbookSettingsRepository].
 */
class DataStoreRoadbookSettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : RoadbookSettingsRepository {

    private object PreferencesKeys {
        val SHORT_DISTANCE_THRESHOLD = longPreferencesKey("roadbook_short_distance_threshold")
    }

    override fun getSettings(): Flow<RoadbookSettings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            RoadbookSettings(
                shortDistanceThreshold = ShortDistanceThreshold(
                    preferences[PreferencesKeys.SHORT_DISTANCE_THRESHOLD]
                        ?: ShortDistanceThreshold.DEFAULT
                )
            )
        }

    override suspend fun saveShortDistanceThreshold(threshold: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SHORT_DISTANCE_THRESHOLD] = threshold
        }
    }
}
