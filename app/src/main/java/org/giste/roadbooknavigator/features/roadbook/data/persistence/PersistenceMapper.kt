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

package org.giste.roadbooknavigator.features.roadbook.data.persistence

import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentCoordinates
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentElement
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentIcon
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentPoint
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentRoad
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentRoute
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentText
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentTrack
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentWaypoint
import org.giste.roadbooknavigator.features.roadbook.domain.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.Element
import org.giste.roadbooknavigator.features.roadbook.domain.Icon
import org.giste.roadbooknavigator.features.roadbook.domain.Point
import org.giste.roadbooknavigator.features.roadbook.domain.Road
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import org.giste.roadbooknavigator.features.roadbook.domain.Text
import org.giste.roadbooknavigator.features.roadbook.domain.Track
import org.giste.roadbooknavigator.features.roadbook.domain.Waypoint
import org.giste.roadbooknavigator.features.roadbook.domain.Waypoint.DangerLevel
import javax.inject.Inject

/**
 * Mapper responsible for converting between Domain models and Persistence DTOs.
 */
class PersistenceMapper @Inject constructor() {

    fun toPersistent(route: Route): PersistentRoute {
        return PersistentRoute(
            name = route.name,
            description = route.description,
            startLocation = route.startLocation,
            endLocation = route.endLocation,
            waypoints = route.waypoints.map { toPersistent(it) }
        )
    }

    fun toDomain(persistentRoute: PersistentRoute): Route {
        return Route(
            name = persistentRoute.name,
            description = persistentRoute.description,
            startLocation = persistentRoute.startLocation,
            endLocation = persistentRoute.endLocation,
            waypoints = persistentRoute.waypoints.map { toDomain(it) }
        )
    }

    private fun toPersistent(waypoint: Waypoint): PersistentWaypoint {
        return PersistentWaypoint(
            number = waypoint.number,
            coordinates = PersistentCoordinates(
                waypoint.coordinates.latitude,
                waypoint.coordinates.longitude,
                waypoint.coordinates.elevation
            ),
            distanceMeters = waypoint.distance.meters,
            distFromPrevMeters = waypoint.distanceFromPrevious.meters,
            shortDistance = waypoint.shortDistance,
            reset = waypoint.reset,
            dangerLevel = waypoint.dangerLevel.name,
            tulipElements = waypoint.tulipElements.map { toPersistent(it) },
            notesElements = waypoint.notesElements.map { toPersistent(it) }
        )
    }

    private fun toDomain(pw: PersistentWaypoint): Waypoint {
        return Waypoint(
            number = pw.number,
            coordinates = Coordinates(
                pw.coordinates.latitude,
                pw.coordinates.longitude,
                pw.coordinates.elevation
            ),
            distance = Distance(pw.distanceMeters),
            distanceFromPrevious = Distance(pw.distFromPrevMeters),
            shortDistance = pw.shortDistance,
            reset = pw.reset,
            dangerLevel = DangerLevel.valueOf(pw.dangerLevel),
            tulipElements = pw.tulipElements.map { toDomain(it) },
            notesElements = pw.notesElements.map { toDomain(it) }
        )
    }

    private fun toPersistent(element: Element): PersistentElement {
        return when (element) {
            is Road -> PersistentRoad(
                start = element.start?.let { PersistentPoint(it.x, it.y) },
                end = element.end?.let { PersistentPoint(it.x, it.y) },
                roadType = element.roadType.name,
                handles = element.handles.map { PersistentPoint(it.x, it.y) }
            )
            is Track -> PersistentTrack(
                roadIn = toPersistent(element.roadIn) as PersistentRoad,
                roadOut = toPersistent(element.roadOut) as PersistentRoad
            )
            is Icon -> PersistentIcon(
                iconType = element.type.name,
                width = element.width,
                height = element.height,
                center = PersistentPoint(element.center.x, element.center.y),
                angle = element.angle,
                scaleX = element.scaleX,
                scaleY = element.scaleY,
                originalId = element.originalId
            )
            is Text -> PersistentText(
                text = element.text,
                fontSize = element.fontSize,
                lineHeight = element.lineHeight,
                width = element.width,
                height = element.height,
                maxWidth = element.maxWidth,
                maxHeight = element.maxHeight,
                center = PersistentPoint(element.center.x, element.center.y)
            )
        }
    }

    private fun toDomain(pe: PersistentElement): Element {
        return when (pe) {
            is PersistentRoad -> Road(
                start = pe.start?.let { Point(it.x, it.y) },
                end = pe.end?.let { Point(it.x, it.y) },
                roadType = Road.RoadType.valueOf(pe.roadType),
                handles = pe.handles.map { Point(it.x, it.y) }
            )
            is PersistentTrack -> Track(
                roadIn = toDomain(pe.roadIn) as Road,
                roadOut = toDomain(pe.roadOut) as Road
            )
            is PersistentIcon -> Icon(
                type = Icon.IconType.valueOf(pe.iconType),
                width = pe.width,
                height = pe.height,
                center = Point(pe.center.x, pe.center.y),
                angle = pe.angle,
                scaleX = pe.scaleX,
                scaleY = pe.scaleY,
                originalId = pe.originalId
            )
            is PersistentText -> Text(
                text = pe.text,
                fontSize = pe.fontSize,
                lineHeight = pe.lineHeight,
                width = pe.width,
                height = pe.height,
                maxWidth = pe.maxWidth,
                maxHeight = pe.maxHeight,
                center = Point(pe.center.x, pe.center.y)
            )
        }
    }
}
