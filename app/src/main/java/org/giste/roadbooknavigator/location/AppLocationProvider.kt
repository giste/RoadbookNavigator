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

package org.giste.roadbooknavigator.location

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import org.giste.android.location.domain.LocationEvent
import org.giste.android.location.domain.LocationProvider
import org.giste.android.location.domain.usecase.ObserveLocationUseCase
import org.giste.roadbooknavigator.features.settings.domain.usecase.ObserveLocationSettingsUseCase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * App-level implementation of [LocationProvider] that combines the stateless
 * location engine with the app's persistent settings.
 */
@Singleton
internal class AppLocationProvider @Inject constructor(
    private val observeLocationUseCase: ObserveLocationUseCase,
    private val observeLocationSettingsUseCase: ObserveLocationSettingsUseCase
) : LocationProvider {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeLocation(): Flow<LocationEvent> {
        return observeLocationSettingsUseCase().flatMapLatest { settings ->
            observeLocationUseCase(
                pollingInterval = settings.pollingInterval,
                minDistance = settings.minDistance
            )
        }
    }
}
