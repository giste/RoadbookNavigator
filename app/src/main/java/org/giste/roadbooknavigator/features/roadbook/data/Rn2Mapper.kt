/*
 * Rn2 Viewer
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

package org.giste.roadbooknavigator.features.roadbook.data

import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.data.dto.JsonRouteData
import org.giste.roadbooknavigator.features.roadbook.data.dto.JsonRouteResponse
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import javax.inject.Inject

/**
 * High-level Orchestrator to convert RN2 JSON strings to Domain Route models.
 * Delegates specific processing and mapping to specialized components.
 */
class Rn2Mapper @Inject constructor(
    private val waypointProcessor: WaypointProcessor
) {

    /**
     * Maps a JSON string representing an .rn2 file into a Domain [Route].
     *
     * @param jsonString The raw JSON content.
     * @param threshold Optional distance threshold in meters for "short distance" warnings.
     * @return The fully parsed and processed [Route].
     */
    fun mapToDomain(jsonString: String, threshold: Double? = null): Route {
        Logger.d("Starting mapping from JSON string, length: ${jsonString.length}")
        val jsonResponse = JsonRouteResponse.fromJson(jsonString)
        return mapToDomain(jsonResponse.route, threshold)
    }

    private fun mapToDomain(jsonRouteData: JsonRouteData, threshold: Double? = null): Route {
        Logger.i("Mapping route: ${jsonRouteData.name} with ${jsonRouteData.waypoints.size} waypoints")
        return Route(
            name = jsonRouteData.name,
            description = jsonRouteData.description,
            startLocation = jsonRouteData.startLocation,
            endLocation = jsonRouteData.endLocation,
            waypoints = waypointProcessor.processWaypoints(jsonRouteData.waypoints, threshold),
        )
    }
}
