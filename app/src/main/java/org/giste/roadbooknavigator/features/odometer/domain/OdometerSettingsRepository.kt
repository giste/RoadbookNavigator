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

package org.giste.roadbooknavigator.features.odometer.domain

import kotlinx.coroutines.flow.Flow

/**
 * Interface to provide access to odometer-specific settings.
 */
interface OdometerSettingsRepository {
    /**
     * Provides a reactive stream of the current [OdometerSettings].
     */
    fun getSettings(): Flow<OdometerSettings>

    /** Sets the speed threshold for the GPS odometer. */
    suspend fun setSpeedThreshold(threshold: Float)

    /** Sets the minimum horizontal accuracy required for odometer updates. */
    suspend fun setMinAccuracy(accuracy: Float)

    /** Sets the minimum vertical accuracy required for altitude-related odometer logic. */
    suspend fun setMinVerticalAccuracy(accuracy: Float)

    /** Resets all odometer-related parameters to their default values. */
    suspend fun restoreSettingsDefaults()
}
