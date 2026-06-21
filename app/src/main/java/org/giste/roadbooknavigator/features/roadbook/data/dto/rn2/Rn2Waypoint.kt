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

package org.giste.roadbooknavigator.features.roadbook.data.dto.rn2

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Rn2Waypoint(
    @SerialName("t_uuid") val tUuid: String,
    @SerialName("waypointid") val waypointId: Int,
    val lat: Double,
    val lon: Double,
    val ele: Double = 0.0,
    val show: Boolean,
    val tulip: Rn2Tulip,
    val notes: Rn2Notes,
)

@Serializable
data class Rn2Tulip(
    val elements: List<Rn2Element> = emptyList()
)

@Serializable
data class Rn2Notes(
    val elements: List<Rn2Element> = emptyList()
)
