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

package org.giste.roadbooknavigator.features.odometer.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettings
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Jetpack DataStore implementation of [OdometerSettingsRepository].
 */
@Singleton
internal class DataStoreOdometerSettingsRepository @Inject constructor(
    @param:OdometerDataStoreQualifier private val dataStore: DataStore<Preferences>,
    private val logger: Logger
) : OdometerSettingsRepository {

    private object Keys {
        val SPEED_THRESHOLD = floatPreferencesKey("odometer_speed_threshold")
        val MIN_ACCURACY = floatPreferencesKey("odometer_min_accuracy")
        val MIN_VERTICAL_ACCURACY = floatPreferencesKey("odometer_min_vertical_accuracy")
        val INCREASE_KEYS = stringPreferencesKey("odometer_keys_increase")
        val DECREASE_KEYS = stringPreferencesKey("odometer_keys_decrease")
        val RESET_KEYS = stringPreferencesKey("odometer_keys_reset")
    }

    override fun getSettings(): Flow<OdometerSettings> = dataStore.data.map { preferences ->
        OdometerSettings(
            speedThreshold = preferences[Keys.SPEED_THRESHOLD]
                ?: OdometerSettings.DEFAULT_SPEED_THRESHOLD,
            minAccuracy = preferences[Keys.MIN_ACCURACY]
                ?: OdometerSettings.DEFAULT_MIN_ACCURACY,
            minVerticalAccuracy = preferences[Keys.MIN_VERTICAL_ACCURACY]
                ?: OdometerSettings.DEFAULT_MIN_VERTICAL_ACCURACY,
            increasePartial = preferences[Keys.INCREASE_KEYS]?.toIntList()
                ?: OdometerSettings.DEFAULT_INCREASE_KEYS,
            decreasePartial = preferences[Keys.DECREASE_KEYS]?.toIntList()
                ?: OdometerSettings.DEFAULT_DECREASE_KEYS,
            resetPartial = preferences[Keys.RESET_KEYS]?.toIntList()
                ?: OdometerSettings.DEFAULT_RESET_KEYS,
        )
    }

    override suspend fun setSpeedThreshold(threshold: Float) {
        logger.i("DataStoreOdometerSettingsRepository: Setting speed threshold to %f", threshold)
        dataStore.edit { preferences ->
            preferences[Keys.SPEED_THRESHOLD] = threshold
        }
    }

    override suspend fun setMinAccuracy(accuracy: Float) {
        logger.i("DataStoreOdometerSettingsRepository: Setting min accuracy to %f", accuracy)
        dataStore.edit { preferences ->
            preferences[Keys.MIN_ACCURACY] = accuracy
        }
    }

    override suspend fun setMinVerticalAccuracy(accuracy: Float) {
        logger.i(
            "DataStoreOdometerSettingsRepository: Setting min vertical accuracy to %f",
            accuracy
        )
        dataStore.edit { preferences ->
            preferences[Keys.MIN_VERTICAL_ACCURACY] = accuracy
        }
    }

    override suspend fun setRemoteKeys(
        increase: List<Int>,
        decrease: List<Int>,
        reset: List<Int>
    ) {
        logger.i("DataStoreOdometerSettingsRepository: Setting remote keys")
        dataStore.edit { preferences ->
            preferences[Keys.INCREASE_KEYS] = increase.toPreferenceString()
            preferences[Keys.DECREASE_KEYS] = decrease.toPreferenceString()
            preferences[Keys.RESET_KEYS] = reset.toPreferenceString()
        }
    }

    override suspend fun restoreSettingsDefaults() {
        logger.i("DataStoreOdometerSettingsRepository: Restoring settings defaults")
        dataStore.edit { preferences ->
            preferences.remove(Keys.SPEED_THRESHOLD)
            preferences.remove(Keys.MIN_ACCURACY)
            preferences.remove(Keys.MIN_VERTICAL_ACCURACY)
            preferences.remove(Keys.INCREASE_KEYS)
            preferences.remove(Keys.DECREASE_KEYS)
            preferences.remove(Keys.RESET_KEYS)
        }
    }

    private fun List<Int>.toPreferenceString(): String = joinToString(",")

    private fun String.toIntList(): List<Int> =
        if (isEmpty()) emptyList() else split(",").mapNotNull { it.toIntOrNull() }
}
