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

import io.mockk.mockk
import org.giste.roadbooknavigator.core.util.AppLogger
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Waypoint
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Notes
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Tulip
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RoadbookGeometryCalculatorTest {

    private lateinit var calculator: RoadbookGeometryCalculator
    private val logger: AppLogger = mockk(relaxed = true)

    @Before
    fun setup() {
        calculator = RoadbookGeometryCalculator(logger)
    }

    @Test
    fun `calculateDistance should return correct Haversine distance between two points`() {
        // 1 degree latitude is approx 111.1km (111100m)
        val p1 = createMockWaypoint(0.0, 0.0)
        val p2 = createMockWaypoint(1.0, 0.0)

        val distance = calculator.calculateDistance(p1, p2)

        assertEquals(111195.0, distance, 10.0)
    }

    @Test
    fun `calculateDistance between identical points should be zero`() {
        val p1 = createMockWaypoint(40.0, -3.0)
        val distance = calculator.calculateDistance(p1, p1)
        assertEquals(0.0, distance, 0.001)
    }

    @Test
    fun `calculateExitPoint should return a point straight up for zero relative bearing`() {
        // Path is North -> North
        val prev = createMockWaypoint(40.0, -3.0)
        val current = createMockWaypoint(40.001, -3.0)
        val next = createMockWaypoint(40.002, -3.0)

        val exitPoint = calculator.calculateExitPoint(prev, current, next)

        // Up in RN2 is -Y. 
        // dx should be 0, dy should be negative.
        assertEquals(0.0, exitPoint.x, 0.1)
        assertEquals(-60.0, exitPoint.y, 0.1) // Top boundary is -85 + 25 = -60
    }

    @Test
    fun `calculateExitPoint should return a point to the right for a 90 degree right turn`() {
        // Path is North (arrival) -> East (departure)
        val prev = createMockWaypoint(40.0, -3.0)
        val current = createMockWaypoint(40.01, -3.0)
        val next = createMockWaypoint(40.01, -2.99)

        val exitPoint = calculator.calculateExitPoint(prev, current, next)

        // Right turn (90 deg) relative to arrival (Up)
        // dx should be positive, dy should be 0.
        // Right boundary is 100 - 25 = 75.0
        assertEquals(75.0, exitPoint.x, 0.1)
        assertEquals(0.0, exitPoint.y, 0.1)
    }

    @Test
    fun `calculateExitPoint should return a point to the left for a 90 degree left turn`() {
        // Path is North (arrival) -> West (departure)
        val prev = createMockWaypoint(40.0, -3.0)
        val current = createMockWaypoint(40.01, -3.0)
        val next = createMockWaypoint(40.01, -3.01)

        val exitPoint = calculator.calculateExitPoint(prev, current, next)

        // Left turn (-90 deg) relative to arrival (Up)
        // dx should be negative, dy should be 0.
        // Left boundary is -100 + 25 = -75.0
        assertEquals(-75.0, exitPoint.x, 0.1)
        assertEquals(0.0, exitPoint.y, 0.1)
    }

    private fun createMockWaypoint(lat: Double, lon: Double): Rn2Waypoint {
        return Rn2Waypoint(
            tUuid = "",
            waypointId = 0,
            lat = lat,
            lon = lon,
            ele = 0.0,
            show = true,
            tulip = Rn2Tulip(emptyList()),
            notes = Rn2Notes(emptyList())
        )
    }
}
