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

package org.giste.android.location.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.giste.android.location.domain.LocationEvent
import org.giste.android.location.domain.LocationLogger
import org.giste.android.location.domain.LocationRepository
import org.giste.android.location.domain.UserLocation

/**
 * Implementation of [LocationRepository] using the Android Framework [LocationManager].
 */
internal class GpsLocationRepository(
    context: Context,
    private val logger: LocationLogger
) : LocationRepository {

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
    override fun getLocations(
        pollingInterval: Long,
        minDistance: Float
    ): Flow<LocationEvent> = callbackFlow {
        logger.d(
            "GpsLocationRepository: Requesting location events (interval: %d, distance: %f)",
            pollingInterval,
            minDistance
        )

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                logger.v(
                    "GpsLocationRepository: Location changed: %f, %f (acc: %f)",
                    location.latitude,
                    location.longitude,
                    location.accuracy
                )
                trySend(LocationEvent.LocationUpdated(location.toUserLocation()))
            }

            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                logger.d("GpsLocationRepository: Status changed for %s: %d", provider, status)
                // Note: TEMPORARY - simple mapping for demonstration. 
                // status 0 = OUT_OF_SERVICE, 1 = TEMPORARILY_UNAVAILABLE, 2 = AVAILABLE
                if (status < 2) {
                    trySend(LocationEvent.SignalLost)
                } else {
                    trySend(LocationEvent.SignalRestored)
                }
            }

            override fun onProviderEnabled(provider: String) {
                logger.i("GpsLocationRepository: Provider enabled: %s", provider)
                trySend(LocationEvent.SignalRestored)
            }

            override fun onProviderDisabled(provider: String) {
                logger.w("GpsLocationRepository: Provider disabled: %s", provider)
                trySend(LocationEvent.ProviderDisabled)
            }
        }

        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                pollingInterval,
                minDistance,
                listener
            )
        } catch (e: Exception) {
            logger.e(e, "GpsLocationRepository: Error requesting location updates: %s", e.message)
            trySend(LocationEvent.Error(e.message ?: "Unknown error"))
            close(e)
        }

        awaitClose {
            logger.d("GpsLocationRepository: Removing location updates")
            locationManager.removeUpdates(listener)
        }
    }

    private fun Location.toUserLocation(): UserLocation = UserLocation(
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        accuracy = accuracy,
        verticalAccuracy = if (hasVerticalAccuracy()) {
            verticalAccuracyMeters
        } else {
            null
        },
        speed = speed,
        bearing = bearing,
        time = time
    )
}