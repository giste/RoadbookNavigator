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

package org.giste.roadbooknavigator.features.roadbook.data.persistence.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class PersistentRoute(
    val name: String = "",
    val description: String = "",
    val startLocation: String = "",
    val endLocation: String = "",
    val waypoints: List<PersistentWaypoint> = emptyList(),
) {
    companion object {
        val empty = PersistentRoute()
    }
}

@Serializable
internal data class PersistentWaypoint(
    val number: Int,
    val coordinates: PersistentCoordinates,
    val distanceMeters: Long,
    val distFromPrevMeters: Long,
    val reset: Boolean,
    val dangerLevel: String,
    val tulipElements: List<PersistentElement>,
    val notesElements: List<PersistentElement>,
)

/**
 * Persistent DTO for geographic coordinates.
 */
@Serializable
internal data class PersistentCoordinates(
    val latitude: Double,
    val longitude: Double,
    val elevation: Double = 0.0,
)

/**
 * Persistent DTO for a 2D point in the drawing canvas.
 */
@Serializable
internal data class PersistentPoint(
    val x: Double,
    val y: Double,
)
