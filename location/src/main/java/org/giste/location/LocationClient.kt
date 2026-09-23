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

package org.giste.location

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import org.giste.location.data.AndroidLocationLogger
import org.giste.location.data.GpsLocationRepository
import org.giste.location.domain.LocationRepository
import org.giste.location.domain.usecase.ObserveLocationUseCase

/**
 * Public client for observing location events from the device.
 *
 * This class serves as the primary entry point for the location library and is completely
 * framework-agnostic (does not mandate any dependency injection framework).
 *
 * @param context Application context used to register location listeners.
 * @param settings Reactive stream emitting the current [LocationSettings].
 * @param logger Optional logger implementation for location events.
 * @param scope Coroutine scope used to share the location event flow.
 */
public class LocationClient public constructor(
    context: Context,
    settings: Flow<LocationSettings> = flowOf(LocationSettings()),
    logger: LocationLogger = AndroidLocationLogger("LocationClient"),
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : LocationProvider {

    private val repository: LocationRepository = GpsLocationRepository(
        context = context.applicationContext,
        logger = logger
    )

    private val observeLocationUseCase: ObserveLocationUseCase = ObserveLocationUseCase(
        locationRepository = repository,
        logger = logger
    )

    /**
     * Shared stream of location events, automatically reconfigured when location settings change.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private val locationEvents: Flow<LocationEvent> = settings
        .flatMapLatest { config ->
            observeLocationUseCase(config.pollingInterval, config.minDistance)
        }
        .onEach { event ->
            logger.v("LocationClient: New event: %s", event)
        }
        .shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    /**
     * Provides access to the reactive location stream.
     */
    override fun observeLocation(): Flow<LocationEvent> = locationEvents
}
