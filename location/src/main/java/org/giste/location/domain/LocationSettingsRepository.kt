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

package org.giste.location.domain

import kotlinx.coroutines.flow.Flow
import org.giste.location.LocationSettings

/**
 * Interface to manage location settings persistence.
 */
internal interface LocationSettingsRepository {
    /**
     * Emits the current location settings.
     */
    fun getLocationSettings(): Flow<LocationSettings>

    /**
     * Updates the location settings.
     */
    suspend fun saveLocationSettings(settings: LocationSettings)
}
