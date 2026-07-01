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

package org.giste.roadbooknavigator.features.location.domain

/**
 * Domain representation of location-specific settings.
 *
 * @property pollingInterval Minimum time interval between location updates, in milliseconds.
 * @property minDistance Minimum distance between location updates, in meters.
 * @property minAccuracy Maximum allowed horizontal GPS accuracy in meters.
 * @property minVerticalAccuracy Maximum allowed vertical GPS accuracy in meters.
 * @property speedThreshold Minimum speed in m/s below which updates might be ignored to prevent "jitter".
 */
data class LocationSettings(
    val pollingInterval: Long = DEFAULT_POLLING_INTERVAL,
    val minDistance: Float = DEFAULT_MIN_DISTANCE,
    val minAccuracy: Float = DEFAULT_MIN_ACCURACY,
    val minVerticalAccuracy: Float = DEFAULT_MIN_VERTICAL_ACCURACY,
    val speedThreshold: Float = DEFAULT_SPEED_THRESHOLD
) {
    companion object {
        const val DEFAULT_POLLING_INTERVAL = 500L // ms
        const val DEFAULT_MIN_DISTANCE = 1.0f // m
        const val DEFAULT_MIN_ACCURACY = 20.0f // m
        const val DEFAULT_MIN_VERTICAL_ACCURACY = 10.0f // m
        const val DEFAULT_SPEED_THRESHOLD = 0.5f // m/s
    }
}
