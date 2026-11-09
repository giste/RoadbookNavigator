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

package dev.giste.roadbooknavigator.features.odometer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import dev.giste.location.LocationEvent
import dev.giste.location.LocationProvider
import dev.giste.odometer.OdometerLocationProvider
import dev.giste.odometer.OdometerLocation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OdometerLocationProviderBridge @Inject constructor(
    private val locationProvider: LocationProvider
) : OdometerLocationProvider {
    override fun observeLocation(): Flow<OdometerLocation> {
        return locationProvider.observeLocation()
            .filterIsInstance<LocationEvent.LocationUpdated>()
            .map { it.location.toOdometerLocation() }
    }
}
