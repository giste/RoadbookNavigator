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

package org.giste.odometer.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.scan
import org.giste.odometer.domain.DistanceUtils
import org.giste.odometer.domain.Odometer
import org.giste.odometer.domain.OdometerLocation
import org.giste.odometer.domain.OdometerLogger
import org.giste.odometer.domain.OdometerRepository
import org.giste.odometer.domain.OdometerSettings
import javax.inject.Inject

/**
 * Use case to observe and update the odometer.
 *
 * It acts as the business engine:
 * 1. Observes the GPS stream and settings.
 * 2. Calculates distance deltas between high-accuracy fixes using reactive state.
 * 3. Updates the persistent storage via the repository.
 * 4. Exposes the reactive odometer state.
 */
public class GetOdometerUseCase @Inject internal constructor(
    private val odometerRepository: OdometerRepository,
    private val distanceUtils: DistanceUtils,
    private val logger: OdometerLogger
) {
    public operator fun invoke(
        settingsFlow: Flow<OdometerSettings>,
        locationFlow: Flow<OdometerLocation>
    ): Flow<Odometer> {
        logger.d("GetOdometerUseCase: Invoked")

        val processedLocations = combine(
            settingsFlow,
            locationFlow.onStart { emit(OdometerLocation(0.0, 0.0, 0.0, 999f, null, 0f, 0L)) }
        ) { settings, location ->
            settings to location
        }
            .distinctUntilChanged { old, new -> old.second === new.second }
            .scan(null as OdometerLocation?) { lastValid, (settings, current) ->
                processLocation(lastValid, current, settings)
            }

        return combine(
            odometerRepository.odometer,
            processedLocations
        ) { odometer, _ -> odometer }
            .onEach {
                logger.v(
                    "GetOdometerUseCase: New odometer state: total=%f, partial=%f",
                    it.total,
                    it.partial
                )
            }
    }

    private suspend fun processLocation(
        lastValid: OdometerLocation?,
        current: OdometerLocation,
        settings: OdometerSettings
    ): OdometerLocation? {
        if (current.accuracy > settings.minAccuracy) {
            logger.v(
                "GetOdometerUseCase: Location ignored (poor accuracy: %f > %f)",
                current.accuracy,
                settings.minAccuracy
            )
            return lastValid
        }

        if (lastValid == null) {
            logger.d(
                "GetOdometerUseCase: First valid location received: %f, %f",
                current.latitude,
                current.longitude
            )
            return current
        }

        // Ignore updates if the user is effectively stopped to avoid GPS jitter "drifting" the odometer
        if (current.speed < settings.speedThreshold) {
            logger.v(
                "GetOdometerUseCase: Location ignored (speed %f < threshold %f)",
                current.speed,
                settings.speedThreshold
            )
            return lastValid
        }

        val delta = distanceUtils.calculateDistance(
            start = lastValid,
            end = current,
            verticalAccuracyThreshold = settings.minVerticalAccuracy
        )

        if (delta > 0) {
            logger.d("GetOdometerUseCase: Updating distance with delta: %f", delta)
            odometerRepository.updateDistance(delta)
        }
        return current
    }
}
