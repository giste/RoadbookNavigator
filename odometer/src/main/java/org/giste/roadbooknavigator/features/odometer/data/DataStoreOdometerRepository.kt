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
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.odometer.domain.OdometerRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Pure SSOT (Single Source of Truth) implementation of [OdometerRepository].
 * It only handles persistent storage using DataStore.
 */
@Singleton
internal class DataStoreOdometerRepository @Inject constructor(
    @param:OdometerDataStoreQualifier private val dataStore: DataStore<Preferences>,
    private val logger: Logger
) : OdometerRepository {

    companion object {
        private val TOTAL_DISTANCE_KEY = doublePreferencesKey("total_distance")
        private val PARTIAL_DISTANCE_KEY = doublePreferencesKey("partial_distance")
    }

    override val odometer: Flow<Odometer> = dataStore.data
        .map { prefs ->
            Odometer(
                total = prefs[TOTAL_DISTANCE_KEY] ?: 0.0,
                partial = prefs[PARTIAL_DISTANCE_KEY] ?: 0.0
            )
        }
        .onStart { logger.d("DataStoreOdometerRepository: Odometer flow started") }

    override suspend fun updateDistance(delta: Double) {
        logger.v("DataStoreOdometerRepository: Updating total and partial with delta: %f", delta)
        dataStore.edit { prefs ->
            val currentTotal = prefs[TOTAL_DISTANCE_KEY] ?: 0.0
            val currentPartial = prefs[PARTIAL_DISTANCE_KEY] ?: 0.0
            prefs[TOTAL_DISTANCE_KEY] = currentTotal + delta
            prefs[PARTIAL_DISTANCE_KEY] = currentPartial + delta
        }
    }

    override suspend fun updatePartialDistance(delta: Double) {
        logger.v("DataStoreOdometerRepository: Updating partial with delta: %f", delta)
        dataStore.edit { prefs ->
            val currentPartial = prefs[PARTIAL_DISTANCE_KEY] ?: 0.0
            prefs[PARTIAL_DISTANCE_KEY] = currentPartial + delta
        }
    }

    override suspend fun resetPartialDistance() {
        logger.i("DataStoreOdometerRepository: Resetting partial distance")
        dataStore.edit { it[PARTIAL_DISTANCE_KEY] = 0.0 }
    }

    override suspend fun resetAllDistances() {
        logger.i("DataStoreOdometerRepository: Resetting all distances")
        dataStore.edit {
            it[TOTAL_DISTANCE_KEY] = 0.0
            it[PARTIAL_DISTANCE_KEY] = 0.0
        }
    }

    override suspend fun setPartialDistance(distance: Double) {
        logger.i("DataStoreOdometerRepository: Setting partial distance to: %f", distance)
        dataStore.edit { it[PARTIAL_DISTANCE_KEY] = distance }
    }
}
