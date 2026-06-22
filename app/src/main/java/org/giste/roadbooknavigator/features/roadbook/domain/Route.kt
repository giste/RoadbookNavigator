/*
 * Copyright (C) 2024  Giste
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

import kotlin.math.roundToLong

data class Route(
    val name: String = "",
    val description: String = "",
    val startLocation: String = "",
    val endLocation: String = "",
    val waypoints: List<Waypoint>,
)

/**
 * Represents a specific point of interest or instruction in a rally roadbook.
 * This is the primary entity for the navigation logic.
 *
 * @property number The sequential index of the waypoint in the roadbook.
 * @property coordinates The GPS position and altitude of the waypoint.
 * @property distance The total distance from the start of the route.
 * @property distanceFromPrevious The partial distance since the last waypoint.
 * @property reset If true, the odometer should be reset to zero at this point.
 * @property tulipElements Graphical components used to draw the "tulip" direction diagram.
 * @property notesElements Graphical or text components for the "notes" column.
 */
data class Waypoint (
    val number: Int,
    val coordinates: Coordinates,
    val distance: Distance,
    val distanceFromPrevious: Distance,
    val shortDistance: Boolean = false,
    val reset: Boolean = false,
    val dangerLevel: DangerLevel = DangerLevel.NONE,
    val tulipElements: List<Element> = emptyList(),
    val notesElements: List<Element> = emptyList(),
) {
    init {
        require(number >= 0) { "Waypoint number must be non-negative" }
    }
    enum class DangerLevel {
        NONE,
        LOW,
        MEDIUM,
        HIGH
    }
}

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

/**
 * Value object representing a physical distance.
 *
 * Internally stored as [meters] in a [Long] to prevent floating-point arithmetic errors
 * during cumulative odometer calculations.
 *
 * @property meters The absolute distance in meters. Must be non-negative.
 */
@JvmInline
value class Distance(val meters: Long) : Comparable<Distance> {

    val kilometers: Double get() = meters / 1000.0

    operator fun plus(other: Distance) = Distance(this.meters + other.meters)
    operator fun minus(other: Distance) = Distance(this.meters - other.meters)

    override fun compareTo(other: Distance): Int = this.meters.compareTo(other.meters)

    init {
        require(meters >= 0) { "Distance cannot be negative" }
    }

    companion object {
        fun fromKilometers(km: Double) = Distance((km * 1000).roundToLong())
        val ZERO = Distance(0)
    }
}