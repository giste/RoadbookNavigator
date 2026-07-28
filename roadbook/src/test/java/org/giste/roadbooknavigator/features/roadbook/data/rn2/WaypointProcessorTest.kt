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
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Element
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Icon
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Notes
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Road
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Track
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Tulip
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Waypoint
import org.giste.roadbooknavigator.features.roadbook.domain.model.Road
import org.giste.roadbooknavigator.features.roadbook.domain.model.Track
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.math.roundToLong

class WaypointProcessorTest {

    private lateinit var processor: WaypointProcessor
    private val logger: Logger = mockk(relaxed = true)
    private val geometryCalculator = RoadbookGeometryCalculator(logger)
    private val rn2ElementMapper = Rn2ElementMapper(geometryCalculator, logger)

    @Before
    fun setup() {
        processor = WaypointProcessor(geometryCalculator, rn2ElementMapper, logger)
    }

    @Test
    fun `processWaypoints with single visible waypoint should return one waypoint with zero distance`() {
        // Given
        val waypoints = listOf(
            createMockWaypoint(id = 0, lat = 40.0, lon = -3.0, show = true)
        )

        // When
        val result = processor.processWaypoints(waypoints)

        // Then
        assertEquals(1, result.size)
        assertEquals(0L, result[0].distance.meters)
        assertEquals(0L, result[0].distanceFromPrevious.meters)
    }

    @Test
    fun `processWaypoints with all waypoints hidden should return empty list`() {
        // Given
        val waypoints = listOf(
            createMockWaypoint(id = 0, lat = 40.0, lon = -3.0, show = false),
            createMockWaypoint(id = 1, lat = 40.001, lon = -3.0, show = false)
        )

        // When
        val result = processor.processWaypoints(waypoints)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `processWaypoints with consecutive resets should zero accumulated distance repeatedly`() {
        // WaypointProcessor defines reset based on Rn2Icon.ResetDistance in notes
        // Our mock helper doesn't add icons yet, so we need to craft them.
        
        val resetIcon = Rn2Icon.ResetDistance(
            id = "308c7365-bc3f-451b-9e98-531e9015024f",
            name = "Reset Distance",
            w = 50.0
        )
        
        val wp1 = createMockWaypoint(0, 40.0, -3.0, true)
        val wp2 = createMockWaypoint(1, 40.01, -3.0, true, notesElements = listOf(resetIcon)) // Reset here
        val wp3 = createMockWaypoint(2, 40.02, -3.0, true, notesElements = listOf(resetIcon)) // Reset again here

        // When
        val result = processor.processWaypoints(listOf(wp1, wp2, wp3))

        // Then
        assertEquals(3, result.size)
        
        // WP1: Start
        assertEquals(0L, result[0].distance.meters)
        
        // WP2: Distance from WP1 is ~1111m. Since WP2 has a reset icon, it is marked as reset.
        // The implementation in WaypointProcessor says:
        // currentAccDist = if (previousWasReset) distance else currentAccDist + distance
        // So WP2 accumulated distance should be 1111m.
        val dist1to2 = geometryCalculator.calculateDistance(wp1, wp2).roundToLong()
        assertEquals(dist1to2, result[1].distance.meters)
        assertTrue(result[1].reset)

        // WP3: Distance from WP2 is ~1111m. Since WP2 was a reset, WP3 accumulated distance
        // starts from 0 + distance(WP2, WP3).
        val dist2to3 = geometryCalculator.calculateDistance(wp2, wp3).roundToLong()
        assertEquals(dist2to3, result[2].distance.meters)
        assertTrue(result[2].reset)
    }

    @Test
    fun `track type should be inherited from previous visible waypoint roadOut`() {
        // Given:
        // WP1 (visible): Track(roadIn=OffTrack(16), roadOut=TarmacRoad(18))
        // WP2 (visible): Track(roadIn=null, roadOut=null) -> Should inherit TarmacRoad for both
        
        val wp1 = createMockWaypoint(
            id = 1,
            lat = 40.0,
            lon = -3.0,
            show = true,
            tulipElements = listOf(
                Rn2Track(
                    roadIn = Rn2Road(typeId = 16),
                    roadOut = Rn2Road(typeId = 18)
                )
            )
        )
        val wp2 = createMockWaypoint(
            id = 2,
            lat = 40.01,
            lon = -3.0,
            show = true,
            tulipElements = listOf(
                Rn2Track(
                    roadIn = Rn2Road(typeId = null),
                    roadOut = Rn2Road(typeId = null)
                )
            )
        )

        // When
        val result = processor.processWaypoints(listOf(wp1, wp2))

        // Then
        assertEquals(2, result.size)
        
        val track1 = result[0].tulipElements.filterIsInstance<Track>().first()
        assertEquals(Road.RoadType.OffTrack, track1.roadIn.roadType)
        assertEquals(Road.RoadType.TarmacRoad, track1.roadOut.roadType)

        val track2 = result[1].tulipElements.filterIsInstance<Track>().first()
        assertEquals(Road.RoadType.TarmacRoad, track2.roadIn.roadType)
        assertEquals(Road.RoadType.TarmacRoad, track2.roadOut.roadType)
    }

    @Test
    fun `roadOut should inherit from roadIn if roadOut type is not specified`() {
        // Given:
        // WP1 (visible): Track(roadIn=OffTrack(16), roadOut=null) -> roadOut should be OffTrack
        
        val wp1 = createMockWaypoint(
            id = 1,
            lat = 40.0,
            lon = -3.0,
            show = true,
            tulipElements = listOf(
                Rn2Track(
                    roadIn = Rn2Road(typeId = 16),
                    roadOut = Rn2Road(typeId = null)
                )
            )
        )

        // When
        val result = processor.processWaypoints(listOf(wp1))

        // Then
        val track1 = result[0].tulipElements.filterIsInstance<Track>().first()
        assertEquals(Road.RoadType.OffTrack, track1.roadIn.roadType)
        assertEquals(Road.RoadType.OffTrack, track1.roadOut.roadType)
    }

    @Test
    fun `inheritance should skip non-visible waypoints`() {
        // Given:
        // WP1 (visible): Track(roadIn=16, roadOut=18)
        // WP2 (hidden):  Track(roadIn=12, roadOut=12) -> Should be ignored for inheritance
        // WP3 (visible): Track(roadIn=null, roadOut=null) -> Should inherit 18 from WP1
        
        val wp1 = createMockWaypoint(1, 40.01, -3.0, true, listOf(Rn2Track(Rn2Road(typeId = 16), Rn2Road(typeId = 18))))
        val wp2 = createMockWaypoint(2, 40.02, -3.0, false, listOf(Rn2Track(Rn2Road(typeId = 12), Rn2Road(typeId = 12))))
        val wp3 = createMockWaypoint(3, 40.03, -3.0, true, listOf(Rn2Track(Rn2Road(typeId = null), Rn2Road(typeId = null))))

        // When
        val result = processor.processWaypoints(listOf(wp1, wp2, wp3))

        // Then
        assertEquals(2, result.size) // Only WP1 and WP3 are visible
        
        val track3 = result[1].tulipElements.filterIsInstance<Track>().first()
        assertEquals(Road.RoadType.TarmacRoad, track3.roadIn.roadType)
        assertEquals(Road.RoadType.TarmacRoad, track3.roadOut.roadType)
    }

    @Test
    fun `standalone Road elements should not affect or consume track inheritance`() {
        // Given:
        // WP1 (visible): Track(roadIn=16, roadOut=18)
        // WP2 (visible): Road(typeId=null), Track(roadIn=null, roadOut=null)
        
        val wp1 = createMockWaypoint(1, 40.0, -3.0, true, listOf(Rn2Track(Rn2Road(typeId = 16), Rn2Road(typeId = 18))))
        val wp2 = createMockWaypoint(2, 40.01, -3.0, true, listOf(
            Rn2Road(typeId = null), // Should be default (Track/17), NOT inherited (Tarmac/18)
            Rn2Track(roadIn = Rn2Road(typeId = null), roadOut = Rn2Road(typeId = null)) // Should inherit 18
        ))

        // When
        val result = processor.processWaypoints(listOf(wp1, wp2))

        // Then
        val wp2Elements = result[1].tulipElements
        val standaloneRoad = wp2Elements.filterIsInstance<Road>().first()
        val track = wp2Elements.filterIsInstance<Track>().first()

        assertEquals(Road.RoadType.Track, standaloneRoad.roadType) // Default 17, not inherited 18
        assertEquals(Road.RoadType.TarmacRoad, track.roadIn.roadType) // Inherited 18 from WP1 Track
    }

    private fun createMockWaypoint(
        id: Int, 
        lat: Double, 
        lon: Double, 
        show: Boolean,
        tulipElements: List<Rn2Element> = emptyList(),
        notesElements: List<Rn2Element> = emptyList()
    ): Rn2Waypoint {
        return Rn2Waypoint(
            tUuid = "uuid-$id",
            waypointId = id,
            lat = lat,
            lon = lon,
            ele = 0.0,
            show = show,
            tulip = Rn2Tulip(tulipElements),
            notes = Rn2Notes(notesElements)
        )
    }
}
