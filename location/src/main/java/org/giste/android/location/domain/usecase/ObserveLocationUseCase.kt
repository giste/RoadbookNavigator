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

package org.giste.android.location.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import org.giste.android.location.domain.LocationEvent
import org.giste.android.location.domain.LocationLogger
import org.giste.android.location.domain.LocationRepository
import org.giste.android.location.domain.UserLocation
import javax.inject.Inject

/**
 * Use case to observe location events from the device.
 */
public class ObserveLocationUseCase @Inject internal constructor(
    private val locationRepository: LocationRepository,
    private val logger: LocationLogger
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    public operator fun invoke(
        pollingInterval: Long,
        minDistance: Float
    ): Flow<LocationEvent> {
        logger.i(
            "ObserveLocationUseCase: Requesting location events with interval: %d ms, minDistance: %f m",
            pollingInterval,
            minDistance
        )
        return locationRepository.getLocations(
            pollingInterval = pollingInterval,
            minDistance = minDistance
        )
    }
}
