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

package org.giste.android.location

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import org.giste.android.location.data.LocationApplicationScope
import org.giste.android.location.domain.usecase.ObserveLocationUseCase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Headless controller that manages the reactive GPS stream for the location module.
 *
 * This class coordinates the stateless observation logic with application-specific
 * settings to provide a single, shared source of location truth.
 */
@Singleton
public class LocationController @Inject internal constructor(
    observeLocationUseCase: ObserveLocationUseCase,
    settingsProvider: LocationSettingsProvider,
    @LocationApplicationScope private val scope: CoroutineScope,
    private val logger: LocationLogger
) : LocationProvider {

    /**
     * Shared stream of location events, automatically updated when polling settings change.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private val locationEvents: Flow<LocationEvent> = combine(
        settingsProvider.pollingInterval,
        settingsProvider.minDistance
    ) { interval, distance ->
        interval to distance
    }.flatMapLatest { (interval, distance) ->
        observeLocationUseCase(interval, distance)
    }.onEach { event ->
        logger.v("LocationController: New event: %s", event)
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000),
        replay = 1
    )

    /**
     * Provides access to the location stream.
     * Implementation of [LocationProvider] to maintain compatibility while hoisting logic.
     */
    override fun observeLocation(): Flow<LocationEvent> = locationEvents
}
