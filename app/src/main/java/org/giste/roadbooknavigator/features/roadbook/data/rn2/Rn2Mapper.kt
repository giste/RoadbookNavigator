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

package org.giste.roadbooknavigator.features.roadbook.data.rn2

import org.giste.roadbooknavigator.core.util.AppLogger
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2RouteData
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2RouteResponse
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import javax.inject.Inject

/**
 * High-level Orchestrator to convert RN2 JSON strings to Domain Route models.
 * Delegates specific processing and mapping to specialized components.
 */
internal class Rn2Mapper @Inject constructor(
    private val waypointProcessor: WaypointProcessor,
    private val logger: AppLogger
) {

    /**
     * Maps a JSON string representing an .rn2 file into a Domain [Route].
     *
     * @param jsonString The raw JSON content.
     * @return The fully parsed and processed [Route].
     */
    fun mapToDomain(jsonString: String): Route {
        logger.d("Starting mapping from JSON string, length: %d", jsonString.length)
        val jsonResponse = Rn2RouteResponse.fromJson(jsonString)
        return mapToDomain(jsonResponse.route)
    }

    private fun mapToDomain(rn2RouteData: Rn2RouteData): Route {
        logger.i("Mapping route: %s with %d waypoints", rn2RouteData.name, rn2RouteData.waypoints.size)
        return Route(
            name = rn2RouteData.name,
            description = rn2RouteData.description,
            startLocation = rn2RouteData.startLocation,
            endLocation = rn2RouteData.endLocation,
            waypoints = waypointProcessor.processWaypoints(rn2RouteData.waypoints),
        )
    }
}
