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

package org.giste.roadbooknavigator.features.settings.domain

/**
 * Aggregate Value Object representing all user-configurable settings.
 *
 * @property theme Selected visual theme.
 * @property orientation Preferred screen orientation.
 * @property fullScreen Whether the app should be in immersive full-screen mode.
 * @property shortDistanceThreshold Threshold in meters to highlight "short distance" instructions.
 * @property odometerSpeedThreshold Minimum speed in m/s below which the odometer might ignore updates to prevent "jitter".
 * @property odometerMinAccuracy Maximum allowed horizontal GPS accuracy in meters.
 * @property odometerMinVerticalAccuracy Maximum allowed vertical GPS accuracy in meters.
 * @property odometerPollingInterval Minimum time interval between location updates, in milliseconds.
 * @property odometerMinDistance Minimum distance between location updates, in meters.
 */
data class AppSettings(
    val theme: AppTheme = AppTheme.FOLLOW_SYSTEM,
    val orientation: AppOrientation = AppOrientation.FOLLOW_SYSTEM,
    val fullScreen: Boolean = true,
    val shortDistanceThreshold: Long = DEFAULT_SHORT_DISTANCE_THRESHOLD,
    val odometerSpeedThreshold: Float = DEFAULT_ODOMETER_SPEED_THRESHOLD,
    val odometerMinAccuracy: Float = DEFAULT_ODOMETER_MIN_ACCURACY,
    val odometerMinVerticalAccuracy: Float = DEFAULT_ODOMETER_MIN_VERTICAL_ACCURACY,
    val odometerPollingInterval: Long = DEFAULT_ODOMETER_POLLING_INTERVAL,
    val odometerMinDistance: Float = DEFAULT_ODOMETER_MIN_DISTANCE
) {
    companion object {
        const val DEFAULT_SHORT_DISTANCE_THRESHOLD = 300L
        const val DEFAULT_ODOMETER_SPEED_THRESHOLD = 0.5f // m/s
        const val DEFAULT_ODOMETER_MIN_ACCURACY = 20.0f // m
        const val DEFAULT_ODOMETER_MIN_VERTICAL_ACCURACY = 10.0f // m
        const val DEFAULT_ODOMETER_POLLING_INTERVAL = 500L // ms
        const val DEFAULT_ODOMETER_MIN_DISTANCE = 1.0f // m
    }
}
