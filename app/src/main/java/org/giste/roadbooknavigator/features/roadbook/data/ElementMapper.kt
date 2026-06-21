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
import org.giste.roadbooknavigator.features.roadbook.data.dto.rn2.Rn2Element
import org.giste.roadbooknavigator.features.roadbook.data.dto.rn2.Rn2Icon
import org.giste.roadbooknavigator.features.roadbook.data.dto.rn2.Rn2Road
import org.giste.roadbooknavigator.features.roadbook.data.dto.rn2.Rn2Text
import org.giste.roadbooknavigator.features.roadbook.data.dto.rn2.Rn2Track
import org.giste.roadbooknavigator.features.roadbook.data.dto.rn2.Rn2Waypoint
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
        rn2Elements: List<Rn2Element>,
        prevWaypoint: Rn2Waypoint? = null,
        currentWaypoint: Rn2Waypoint,
        nextWaypoint: Rn2Waypoint? = null
    ): List<Element> {
        return rn2Elements.map { mapJsonElementToDomain(it, prevWaypoint, currentWaypoint, nextWaypoint) }
    }

    private fun mapJsonElementToDomain(
        rn2Element: Rn2Element,
        prevWaypoint: Rn2Waypoint?,
        currentWaypoint: Rn2Waypoint,
        nextWaypoint: Rn2Waypoint?
    ): Element {
        return when (rn2Element) {
            is Rn2Icon -> mapJsonIconToDomain(rn2Element)
            is Rn2Road -> {
                Road(
                    start = rn2Element.start?.let { Point(it.x, it.y) },
                    end = rn2Element.end?.let { Point(it.x, it.y) },
                    handles = rn2Element.handles.map { Point(it.x, it.y) },
                    roadType = mapToRoadType(rn2Element.typeId)
                )
            }

            is Rn2Track -> {
                val roadOutEnd = if (rn2Element.roadOut.end != null) {
                    Point(rn2Element.roadOut.end.x, rn2Element.roadOut.end.y)
                } else if (nextWaypoint != null) {
                    geometryCalculator.calculateExitPoint(prevWaypoint, currentWaypoint, nextWaypoint)
                } else {
                    Logger.v("No roadOut end and no next waypoint for waypoint ${currentWaypoint.waypointId}, using default")
                    Point(0.0, -55.0) // Default if no next waypoint
                }

                Track(
                    roadIn = Road(
                        start = rn2Element.roadIn.start?.let { Point(it.x, it.y) },
                        end = rn2Element.roadIn.end?.let { Point(it.x, it.y) } ?: Point(0.0, 35.0),
                        roadType = mapToRoadType(rn2Element.roadIn.typeId),
                        handles = rn2Element.roadIn.handles.map { Point(it.x, it.y) },
                    ),
                    roadOut = Road(
                        start = rn2Element.roadOut.start?.let { Point(it.x, it.y) } ?: Point(0.0, 0.0),
                        end = roadOutEnd,
                        roadType = mapToRoadType(rn2Element.roadOut.typeId),
                        handles = rn2Element.roadOut.handles.map { Point(it.x, it.y) },
                    ),
                )
            }

            is Rn2Text -> {
                Text(
                    text = rn2Element.text,
                    fontSize = rn2Element.fontSize,
                    lineHeight = rn2Element.lineHeight ?: 1.0,
                    width = rn2Element.width,
                    height = rn2Element.height,
                    maxWidth = rn2Element.maxWidth ?: 180.0,
                    maxHeight = rn2Element.maxHeight ?: 100.0,
                    center = Point(rn2Element.x, rn2Element.y),
                )
            }
        }
    }

    private fun mapJsonIconToDomain(jsonIcon: Rn2Icon): Icon {
        val baseWidth = jsonIcon.width ?: 50.0
        val baseHeight = jsonIcon.height ?: 50.0
        val scaleX = jsonIcon.scaleX ?: 1.0
        val scaleY = jsonIcon.scaleY ?: 1.0
        val width = (baseWidth * scaleX).toInt()
        val height = (baseHeight * scaleY).toInt()
        val center = Point(jsonIcon.x ?: 0.0, jsonIcon.y ?: 0.0)
        val angle = jsonIcon.angle?.toInt() ?: 0

        val type = when (jsonIcon) {
            is Rn2Icon.Danger1 -> Icon.IconType.Danger1
            is Rn2Icon.Danger2 -> Icon.IconType.Danger2
            is Rn2Icon.Danger3 -> Icon.IconType.Danger3
            is Rn2Icon.FuelZone -> Icon.IconType.FuelZone
            is Rn2Icon.ResetDistance -> Icon.IconType.ResetDistance
            is Rn2Icon.AboveBridge -> Icon.IconType.AboveBridge
            is Rn2Icon.FortCastle -> Icon.IconType.FortCastle
            is Rn2Icon.House -> Icon.IconType.House
            is Rn2Icon.TrafficLight -> Icon.IconType.TrafficLight
            is Rn2Icon.Tunnel -> Icon.IconType.Tunnel
            is Rn2Icon.UnderBridge -> Icon.IconType.UnderBridge
            is Rn2Icon.Alert -> Icon.IconType.Alert
            is Rn2Icon.Roundabout -> Icon.IconType.Roundabout
            is Rn2Icon.Stop -> Icon.IconType.Stop
            is Rn2Icon.RiverWater -> Icon.IconType.RiverWater
            is Rn2Icon.Unknown -> Icon.IconType.Unknown
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
