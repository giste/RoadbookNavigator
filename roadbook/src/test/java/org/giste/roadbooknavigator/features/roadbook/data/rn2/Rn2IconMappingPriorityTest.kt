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
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Icon
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Waypoint
import org.giste.roadbooknavigator.features.roadbook.domain.model.Icon
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class Rn2IconMappingPriorityTest {

    private lateinit var mapper: Rn2ElementMapper
    private val logger: Logger = mockk(relaxed = true)

    @Before
    fun setup() {
        val geometryCalculator = RoadbookGeometryCalculator(logger)
        mapper = Rn2ElementMapper(geometryCalculator, logger)
    }

    @Test
    fun `should use width and height multiplied by scale when scales are present`() {
        // Given
        val iconDto = Rn2Icon.Danger1(
            id = "id",
            name = "name",
            w = 50.0, // Should be ignored if scales are present
            width = 100.0,
            height = 100.0,
            scaleX = 0.5,
            scaleY = 0.5
        )
        val currentWaypoint = mockk<Rn2Waypoint>(relaxed = true)

        // When
        val elements = mapper.mapElements(listOf(iconDto), currentWaypoint = currentWaypoint)
        val domainIcon = elements.first() as Icon

        // Then
        assertEquals(50, domainIcon.width)
        assertEquals(50, domainIcon.height)
    }

    @Test
    fun `should use w for width and height when scales are missing`() {
        // Given
        val iconDto = Rn2Icon.Danger1(
            id = "id",
            name = "name",
            w = 75.0, // Should be used
            width = 100.0, // Should be ignored
            height = 100.0, // Should be ignored
            scaleX = null,
            scaleY = null
        )
        val currentWaypoint = mockk<Rn2Waypoint>(relaxed = true)

        // When
        val elements = mapper.mapElements(listOf(iconDto), currentWaypoint = currentWaypoint)
        val domainIcon = elements.first() as Icon

        // Then
        assertEquals(75, domainIcon.width)
        assertEquals(75, domainIcon.height)
    }
}
