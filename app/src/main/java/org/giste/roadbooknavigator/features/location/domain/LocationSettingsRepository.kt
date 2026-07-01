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

package org.giste.roadbooknavigator.features.location.domain

import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing location-related settings.
 */
interface LocationSettingsRepository {
    /**
     * Emits the current location settings.
     */
    fun getLocationSettings(): Flow<LocationSettings>

    /**
     * Updates the polling interval.
     */
    suspend fun updatePollingInterval(interval: Long)

    /**
     * Updates the minimum distance.
     */
    suspend fun updateMinDistance(distance: Float)

    /**
     * Updates the minimum horizontal accuracy.
     */
    suspend fun updateMinAccuracy(accuracy: Float)

    /**
     * Updates the minimum vertical accuracy.
     */
    suspend fun updateMinVerticalAccuracy(accuracy: Float)

    /**
     * Updates the speed threshold.
     */
    suspend fun updateSpeedThreshold(threshold: Float)

    /**
     * Restores default values for all location settings.
     */
    suspend fun restoreDefaults()
}
