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

import org.giste.roadbooknavigator.features.roadbook.domain.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.Icon
import org.giste.roadbooknavigator.features.roadbook.domain.Point
import org.giste.roadbooknavigator.features.roadbook.domain.Road
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import org.giste.roadbooknavigator.features.roadbook.domain.Text
import org.giste.roadbooknavigator.features.roadbook.domain.Track
import org.giste.roadbooknavigator.features.roadbook.domain.Waypoint
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PersistenceMapperTest {

    private lateinit var mapper: PersistenceMapper

    @Before
    fun setup() {
        mapper = PersistenceMapper()
    }

    @Test
    fun `route round trip conversion should preserve all data`() {
        // Given
        val route = Route(
            name = "Desert Challenge",
            description = "A long route through the dunes",
            startLocation = "Checkpoint Alpha",
            endLocation = "Checkpoint Omega",
            waypoints = listOf(
                Waypoint(
                    number = 1,
                    coordinates = Coordinates(25.123, 45.456, 100.0),
                    distance = Distance(10000),
                    distanceFromPrevious = Distance(10000),
                    shortDistance = false,
                    reset = false,
                    dangerLevel = Waypoint.DangerLevel.LOW,
                    tulipElements = listOf(
                        Track(
                            roadIn = Road(
                                Point(0.0, 50.0),
                                Point(0.0, 0.0),
                                Road.RoadType.TarmacRoad
                            ),
                            roadOut = Road(Point(0.0, 0.0), Point(50.0, 0.0), Road.RoadType.Track)
                        )
                    ),
                    notesElements = listOf(
                        Icon(Icon.IconType.FuelZone, 50, 50, Point(10.0, 10.0)),
                        Text("GAS STATION", 18, 1.2, 100.0, 20.0, 100.0, 20.0, Point(10.0, 30.0))
                    )
                ),
                Waypoint(
                    number = 2,
                    coordinates = Coordinates(25.130, 45.460, 110.0),
                    distance = Distance(12000),
                    distanceFromPrevious = Distance(2000),
                    shortDistance = true,
                    reset = true,
                    dangerLevel = Waypoint.DangerLevel.HIGH,
                    tulipElements = emptyList(),
                    notesElements = listOf(
                        Icon(
                            Icon.IconType.Danger3,
                            60,
                            60,
                            Point(0.0, 0.0),
                            45,
                            1.5,
                            1.5,
                            "danger_id"
                        )
                    )
                )
            )
        )

        // When
        val persistent = mapper.toPersistent(route)
        val result = mapper.toDomain(persistent)

        // Then
        Assert.assertEquals(route.name, result.name)
        Assert.assertEquals(route.description, result.description)
        Assert.assertEquals(route.startLocation, result.startLocation)
        Assert.assertEquals(route.endLocation, result.endLocation)
        Assert.assertEquals(route.waypoints.size, result.waypoints.size)

        for (i in route.waypoints.indices) {
            val expectedWp = route.waypoints[i]
            val actualWp = result.waypoints[i]

            Assert.assertEquals(expectedWp.number, actualWp.number)
            Assert.assertEquals(expectedWp.coordinates, actualWp.coordinates)
            Assert.assertEquals(expectedWp.distance, actualWp.distance)
            Assert.assertEquals(expectedWp.distanceFromPrevious, actualWp.distanceFromPrevious)
            Assert.assertEquals(expectedWp.shortDistance, actualWp.shortDistance)
            Assert.assertEquals(expectedWp.reset, actualWp.reset)
            Assert.assertEquals(expectedWp.dangerLevel, actualWp.dangerLevel)

            Assert.assertEquals(expectedWp.tulipElements, actualWp.tulipElements)
            Assert.assertEquals(expectedWp.notesElements, actualWp.notesElements)
        }

        // Full object equality check (requires all domain models to be data classes)
        Assert.assertEquals(route, result)
    }
}