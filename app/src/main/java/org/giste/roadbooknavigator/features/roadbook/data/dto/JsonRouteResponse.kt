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

package org.giste.roadbooknavigator.features.roadbook.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class JsonRouteResponse(
    val route: JsonRouteData
) {
    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        /**
         * Factory method to create a JsonRouteResponse from a JSON string
         */
        fun fromJson(jsonString: String): JsonRouteResponse {
            return json.decodeFromString<JsonRouteResponse>(jsonString)
        }
    }
}

@Serializable
data class JsonRouteData(
    val version: Int,
    val name: String = "",
    val description: String = "",
    @SerialName("startlocation") val startLocation: String = "",
    @SerialName("endlocation") val endLocation: String = "",
    @SerialName("current_style") val currentStyle: String = "",
    val waypoints: List<JsonWaypoint> = emptyList(),
    val settings: JsonRouteSettings? = null
)
