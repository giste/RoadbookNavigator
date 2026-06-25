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

package org.giste.roadbooknavigator.features.settings.domain

import kotlinx.coroutines.flow.Flow

/**
 * Domain interface defining the contract for accessing and modifying application settings.
 */
interface SettingsRepository {
    /**
     * Provides a reactive stream of the current [AppSettings].
     */
    fun getSettings(): Flow<AppSettings>

    /** Updates the application theme. */
    suspend fun setTheme(theme: AppTheme)

    /** Updates the preferred screen orientation. */
    suspend fun setOrientation(orientation: AppOrientation)

    /** Sets the threshold for short distance warnings. */
    suspend fun setShortDistanceThreshold(threshold: Long)

    /** Sets the speed threshold for the GPS odometer. */
    suspend fun setOdometerSpeedThreshold(threshold: Float)

    /** Sets the minimum horizontal accuracy required for odometer updates. */
    suspend fun setOdometerMinAccuracy(accuracy: Float)

    /** Sets the minimum vertical accuracy required for altitude-related odometer logic. */
    suspend fun setOdometerMinVerticalAccuracy(accuracy: Float)

    /** Sets the polling interval for GPS updates in milliseconds. */
    suspend fun setOdometerPollingInterval(interval: Long)

    /** Sets the minimum distance for GPS updates in meters. */
    suspend fun setOdometerMinDistance(distance: Float)

    /** Resets all odometer-related parameters to their default values. */
    suspend fun restoreOdometerDefaults()
}
