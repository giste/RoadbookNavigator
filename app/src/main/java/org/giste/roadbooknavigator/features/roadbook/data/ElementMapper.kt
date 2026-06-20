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

package org.giste.roadbooknavigator.features.roadbook.data

import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.data.dto.*
import org.giste.roadbooknavigator.features.roadbook.domain.*
import javax.inject.Inject

/**
 * Mapper class responsible for converting individual JSON elements (Icons, Roads, etc.)
 * into their corresponding Domain models.
 */
class ElementMapper @Inject constructor(
    private val geometryCalculator: RoadbookGeometryCalculator
) {

    /**
     * Maps a list of JSON elements into Domain elements.
     */
    fun mapElements(
        jsonElements: List<JsonElement>,
        prevWaypoint: JsonWaypoint? = null,
        currentWaypoint: JsonWaypoint,
        nextWaypoint: JsonWaypoint? = null
    ): List<Element> {
        return jsonElements.map { mapJsonElementToDomain(it, prevWaypoint, currentWaypoint, nextWaypoint) }
    }

    private fun mapJsonElementToDomain(
        jsonElement: JsonElement,
        prevWaypoint: JsonWaypoint?,
        currentWaypoint: JsonWaypoint,
        nextWaypoint: JsonWaypoint?
    ): Element {
        return when (jsonElement) {
            is JsonIcon -> mapJsonIconToDomain(jsonElement)
            is JsonRoad -> {
                Road(
                    start = jsonElement.start?.let { Point(it.x, it.y) },
                    end = jsonElement.end?.let { Point(it.x, it.y) },
                    handles = jsonElement.handles.map { Point(it.x, it.y) },
                    roadType = mapToRoadType(jsonElement.typeId)
                )
            }

            is JsonTrack -> {
                val roadOutEnd = if (jsonElement.roadOut.end != null) {
                    Point(jsonElement.roadOut.end.x, jsonElement.roadOut.end.y)
                } else if (nextWaypoint != null) {
                    geometryCalculator.calculateExitPoint(prevWaypoint, currentWaypoint, nextWaypoint)
                } else {
                    Logger.v("No roadOut end and no next waypoint for waypoint ${currentWaypoint.waypointId}, using default")
                    Point(0.0, -55.0) // Default if no next waypoint
                }

                Track(
                    roadIn = Road(
                        start = jsonElement.roadIn.start?.let { Point(it.x, it.y) },
                        end = jsonElement.roadIn.end?.let { Point(it.x, it.y) } ?: Point(0.0, 35.0),
                        roadType = mapToRoadType(jsonElement.roadIn.typeId),
                        handles = jsonElement.roadIn.handles.map { Point(it.x, it.y) },
                    ),
                    roadOut = Road(
                        start = jsonElement.roadOut.start?.let { Point(it.x, it.y) } ?: Point(0.0, 0.0),
                        end = roadOutEnd,
                        roadType = mapToRoadType(jsonElement.roadOut.typeId),
                        handles = jsonElement.roadOut.handles.map { Point(it.x, it.y) },
                    ),
                )
            }

            is JsonText -> {
                Text(
                    text = jsonElement.text,
                    fontSize = jsonElement.fontSize,
                    lineHeight = jsonElement.lineHeight ?: 1.0,
                    width = jsonElement.width,
                    height = jsonElement.height,
                    maxWidth = jsonElement.maxWidth ?: 180.0,
                    maxHeight = jsonElement.maxHeight ?: 100.0,
                    center = Point(jsonElement.x, jsonElement.y),
                )
            }
        }
    }

    private fun mapJsonIconToDomain(jsonIcon: JsonIcon): Icon {
        val baseWidth = jsonIcon.width ?: 50.0
        val baseHeight = jsonIcon.height ?: 50.0
        val scaleX = jsonIcon.scaleX ?: 1.0
        val scaleY = jsonIcon.scaleY ?: 1.0
        val width = (baseWidth * scaleX).toInt()
        val height = (baseHeight * scaleY).toInt()
        val center = Point(jsonIcon.x ?: 0.0, jsonIcon.y ?: 0.0)
        val angle = jsonIcon.angle?.toInt() ?: 0

        val type = when (jsonIcon) {
            is JsonIcon.Danger1 -> Icon.IconType.Danger1
            is JsonIcon.Danger2 -> Icon.IconType.Danger2
            is JsonIcon.Danger3 -> Icon.IconType.Danger3
            is JsonIcon.FuelZone -> Icon.IconType.FuelZone
            is JsonIcon.ResetDistance -> Icon.IconType.ResetDistance
            is JsonIcon.AboveBridge -> Icon.IconType.AboveBridge
            is JsonIcon.FortCastle -> Icon.IconType.FortCastle
            is JsonIcon.House -> Icon.IconType.House
            is JsonIcon.TrafficLight -> Icon.IconType.TrafficLight
            is JsonIcon.Tunnel -> Icon.IconType.Tunnel
            is JsonIcon.UnderBridge -> Icon.IconType.UnderBridge
            is JsonIcon.Alert -> Icon.IconType.Alert
            is JsonIcon.Roundabout -> Icon.IconType.Roundabout
            is JsonIcon.Stop -> Icon.IconType.Stop
            is JsonIcon.RiverWater -> Icon.IconType.RiverWater
            is JsonIcon.Unknown -> Icon.IconType.Unknown
        }

        return Icon(
            type = type,
            width = width,
            height = height,
            center = center,
            angle = angle,
            scaleX = scaleX,
            scaleY = scaleY,
            originalId = jsonIcon.id
        )
    }

    private fun mapToRoadType(typeId: Int?): Road.RoadType {
        return Road.RoadType.entries.find { it.value == typeId } ?: Road.RoadType.Track
    }
}
