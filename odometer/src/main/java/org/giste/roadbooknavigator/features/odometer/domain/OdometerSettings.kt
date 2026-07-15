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

package org.giste.roadbooknavigator.features.odometer.domain

/**
 * Value Object representing odometer-specific settings.
 *
 * @property speedThreshold Minimum speed in m/s below which the odometer might ignore updates to prevent "jitter".
 * @property minAccuracy Maximum allowed horizontal GPS accuracy in meters.
 * @property minVerticalAccuracy Maximum allowed vertical GPS accuracy in meters.
 * @property increasePartial Keys to increase the partial distance.
 * @property decreasePartial Keys to decrease the partial distance.
 * @property resetPartial Keys to reset the partial distance.
 */
data class OdometerSettings(
    val speedThreshold: Float = DEFAULT_SPEED_THRESHOLD,
    val minAccuracy: Float = DEFAULT_MIN_ACCURACY,
    val minVerticalAccuracy: Float = DEFAULT_MIN_VERTICAL_ACCURACY,
    val increasePartial: List<Int> = DEFAULT_INCREASE_KEYS,
    val decreasePartial: List<Int> = DEFAULT_DECREASE_KEYS,
    val resetPartial: List<Int> = DEFAULT_RESET_KEYS,
) {
    companion object {
        const val DEFAULT_SPEED_THRESHOLD = 0.5f // m/s
        const val DEFAULT_MIN_ACCURACY = 20.0f // m
        const val DEFAULT_MIN_VERTICAL_ACCURACY = 10.0f // m

        /** Default keys for increase partial (DPAD_RIGHT). */
        val DEFAULT_INCREASE_KEYS = listOf(22)

        /** Default keys for decrease partial (DPAD_LEFT). */
        val DEFAULT_DECREASE_KEYS = listOf(21)

        /** Default keys for reset partial (F6). */
        val DEFAULT_RESET_KEYS = listOf(136)
    }
}
