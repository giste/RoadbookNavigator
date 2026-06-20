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

package org.giste.roadbooknavigator.features.roadbook.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class ElementTest {

    @Test
    fun `track element should have correct type`() {
        val road = Road(Point(0.0, 0.0), Point(1.0, 1.0))
        val track = Track(road, road)
        assertEquals(Element.ElementType.Track, track.elementType)
    }

    @Test
    fun `road element should have correct type`() {
        val road = Road(Point(0.0, 0.0), Point(1.0, 1.0))
        assertEquals(Element.ElementType.Road, road.elementType)
    }

    @Test
    fun `icon element should have correct type and properties`() {
        val icon = Icon(
            type = Icon.IconType.Danger1,
            width = 10,
            height = 10,
            center = Point(5.0, 5.0)
        )
        assertEquals(Element.ElementType.Icon, icon.elementType)
        assertEquals(Icon.IconType.Danger1, icon.type)
    }

    @Test
    fun `text element should have correct type`() {
        val text = Text(
            text = "Careful!",
            lineHeight = 1.0,
            width = 100.0,
            height = 20.0,
            maxWidth = 100.0,
            maxHeight = 20.0,
            center = Point(0.0, 0.0)
        )
        assertEquals(Element.ElementType.Text, text.elementType)
    }
}
