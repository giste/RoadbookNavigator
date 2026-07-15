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

package org.giste.roadbooknavigator.features.roadbook.domain.model

/**
 * Domain model for roadbook-specific settings.
 *
 * @property shortDistanceThreshold Threshold in meters to highlight "short distance" instructions.
 * @property roadbookUp Keys to navigate up in the roadbook.
 * @property roadbookDown Keys to navigate down in the roadbook.
 */
data class RoadbookSettings(
    val shortDistanceThreshold: ShortDistanceThreshold = ShortDistanceThreshold(ShortDistanceThreshold.DEFAULT),
    val roadbookUp: List<Int> = DEFAULT_UP_KEYS,
    val roadbookDown: List<Int> = DEFAULT_DOWN_KEYS,
) {
    companion object {
        /** Default keys for roadbook up (DPAD_UP). */
        val DEFAULT_UP_KEYS = listOf(19)

        /** Default keys for roadbook down (DPAD_DOWN). */
        val DEFAULT_DOWN_KEYS = listOf(20)
    }
}
