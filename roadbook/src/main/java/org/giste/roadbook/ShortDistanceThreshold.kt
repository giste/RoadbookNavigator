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

package org.giste.roadbook

/**
 * Value object representing a threshold for "short" distances in the roadbook.
 * Distances below this value are typically highlighted to alert the user of upcoming fast waypoints.
 */
@JvmInline
public value class ShortDistanceThreshold(public val meters: Long) {
    public companion object {
        /** Default threshold in meters (250m). */
        public const val DEFAULT: Long = 250L
        /** Minimum threshold in meters (50m). */
        public const val MIN: Long = 50L
        /** Maximum threshold in meters (1000m). */
        public const val MAX: Long = 1000L
    }
}
