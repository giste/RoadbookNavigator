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
import org.giste.roadbooknavigator.features.roadbook.data.dto.rn2.Rn2Waypoint
import org.giste.roadbooknavigator.features.roadbook.domain.Point
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Utility class for geometric calculations required by the roadbook parser.
 */
class RoadbookGeometryCalculator @Inject constructor() {

    /**
     * Calculates the Haversine distance between two waypoints in meters.
     */
    fun calculateDistance(waypoint1: Rn2Waypoint, waypoint2: Rn2Waypoint): Double {
        val earthRadius = 6371000.0
        val lat1 = Math.toRadians(waypoint1.lat)
        val lon1 = Math.toRadians(waypoint1.lon)
        val lat2 = Math.toRadians(waypoint2.lat)
        val lon2 = Math.toRadians(waypoint2.lon)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val chordLengthSquared = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
        val angularDistance = 2 * atan2(sqrt(chordLengthSquared), sqrt(1 - chordLengthSquared))

        return earthRadius * angularDistance
    }

    /**
     * Calculates the relative exit point for a tulip diagram based on the bearing difference.
     */
    fun calculateExitPoint(
        prev: Rn2Waypoint?,
        current: Rn2Waypoint,
        next: Rn2Waypoint
    ): Point {
        val bearingOut = calculateBearing(current, next)
        val bearingIn = if (prev != null) calculateBearing(prev, current) else bearingOut

        // Relative bearing: shortest angular difference
        val relativeBearing = atan2(sin(bearingOut - bearingIn), cos(bearingOut - bearingIn))

        // RN2 coordinates: Y increases downwards, X increases rightwards.
        // "Up" in RN2 corresponds to the direction of arrival (relative angle 0).
        // North (0 rad) -> RN2 angle -PI/2 (up)
        val rn2Angle = relativeBearing - Math.PI / 2.0

        // Tulip boundaries relative to center (100, 85)
        val margin = 25.0
        val left = -100.0 + margin
        val right = 100.0 - margin
        val top = -85.0 + margin
        val bottom = 50.0 - margin

        val dx = cos(rn2Angle)
        val dy = sin(rn2Angle)

        var t = Double.MAX_VALUE

        if (dx > 0) t = minOf(t, right / dx)
        else if (dx < 0) t = minOf(t, left / dx)

        if (dy > 0) t = minOf(t, bottom / dy)
        else if (dy < 0) t = minOf(t, top / dy)

        val exitPoint = Point(dx * t, dy * t)
        Logger.v("Calculated relative exit point for wp ${current.waypointId}: in=${Math.toDegrees(bearingIn)}°, out=${Math.toDegrees(bearingOut)}°")
        return exitPoint
    }

    private fun calculateBearing(from: Rn2Waypoint, to: Rn2Waypoint): Double {
        val lat1 = Math.toRadians(from.lat)
        val lon1 = Math.toRadians(from.lon)
        val lat2 = Math.toRadians(to.lat)
        val lon2 = Math.toRadians(to.lon)

        val dLon = lon2 - lon1
        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
        return atan2(y, x)
    }
}
