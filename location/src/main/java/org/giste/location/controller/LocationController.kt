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

package org.giste.location.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import org.giste.location.LocationEvent
import org.giste.location.LocationLogger
import org.giste.location.LocationProvider
import org.giste.location.LocationSettings
import org.giste.location.LocationSettingsProvider
import org.giste.location.di.LocationApplicationScope
import org.giste.location.domain.usecase.ObserveLocationSettingsUseCase
import org.giste.location.domain.usecase.ObserveLocationUseCase
import org.giste.location.domain.usecase.RestoreLocationSettingsUseCase
import org.giste.location.domain.usecase.UpdateLocationSettingsUseCase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Headless controller that manages the reactive GPS stream for the location module.
 *
 * This class coordinates the stateless observation logic with application-specific
 * settings to provide a single, shared source of location truth.
 */
@Singleton
internal class LocationController @Inject constructor(
    observeLocationUseCase: ObserveLocationUseCase,
    observeLocationSettingsUseCase: ObserveLocationSettingsUseCase,
    private val updateLocationSettingsUseCase: UpdateLocationSettingsUseCase,
    private val restoreLocationSettingsUseCase: RestoreLocationSettingsUseCase,
    @LocationApplicationScope private val scope: CoroutineScope,
    private val logger: LocationLogger
) : LocationProvider, LocationSettingsProvider {

    /**
     * Shared stream of location events, automatically updated when polling settings change.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private val locationEvents: Flow<LocationEvent> = observeLocationSettingsUseCase()
        .flatMapLatest { settings ->
            observeLocationUseCase(settings.pollingInterval, settings.minDistance)
        }.onEach { event ->
            logger.v("LocationController: New event: %s", event)
        }.shareIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )

    /**
     * Provides access to the location stream.
     */
    override fun observeLocation(): Flow<LocationEvent> = locationEvents

    /**
     * Emits the current location settings.
     */
    override val settings: Flow<LocationSettings> = observeLocationSettingsUseCase()

    /**
     * Updates the location settings.
     */
    override suspend fun updateSettings(settings: LocationSettings) {
        updateLocationSettingsUseCase(settings)
    }

    /**
     * Restores the default location settings.
     */
    override suspend fun restoreDefaults() {
        restoreLocationSettingsUseCase()
    }

    @Deprecated("Use settings flow instead")
    override val pollingInterval: Flow<Long> = settings.map { it.pollingInterval }

    @Deprecated("Use settings flow instead")
    override val minDistance: Flow<Float> = settings.map { it.minDistance }
}