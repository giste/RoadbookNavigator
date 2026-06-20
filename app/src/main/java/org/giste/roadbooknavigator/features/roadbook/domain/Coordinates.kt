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

package org.giste.roadbooknavigator.features.roadbook.domain

/**
 * Represents geographic coordinates on Earth using the WGS84 system.
 * This is a Value Object in DDD.
 *
 * @property latitude Degrees north or south of the equator (-90 to 90).
 * @property longitude Degrees east or west of the prime meridian (-180 to 180).
 * @property elevation Meters above sea level.
 */
data class Coordinates(
    val latitude: Double,
    val longitude: Double,
    val elevation: Double = 0.0,
) {
    init {
        require(latitude in -90.0..90.0) { "Latitude must be between -90 and 90 degrees" }
        require(longitude in -180.0..180.0) { "Longitude must be between -180 and 180 degrees" }
    }
}
