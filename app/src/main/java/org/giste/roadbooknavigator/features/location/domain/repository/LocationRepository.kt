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

package org.giste.roadbooknavigator.features.location.domain.repository

import kotlinx.coroutines.flow.Flow
import org.giste.roadbooknavigator.features.location.domain.model.UserLocation

/**
 * Interface to provide raw location updates.
 */
interface LocationRepository {
    /**
     * Emits the current location as it changes.
     *
     * @param pollingInterval Minimum time interval between updates in ms.
     * @param minDistance Minimum distance between updates in meters.
     */
    fun getLocations(
        pollingInterval: Long = 1000L,
        minDistance: Float = 0f
    ): Flow<UserLocation>
}
