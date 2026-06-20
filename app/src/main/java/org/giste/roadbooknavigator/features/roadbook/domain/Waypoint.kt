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
