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

package org.giste.roadbooknavigator.features.roadbook.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CoordinatesTest {

    @Test
    fun `valid coordinates should be created successfully`() {
        val lat = 40.0
        val lon = -3.0
        val elev = 600.0
        val coordinates = Coordinates(lat, lon, elev)

        assertEquals(lat, coordinates.latitude, 0.0)
        assertEquals(lon, coordinates.longitude, 0.0)
        assertEquals(elev, coordinates.elevation, 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `latitude above 90 should throw exception`() {
        Coordinates(90.1, 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `latitude below -90 should throw exception`() {
        Coordinates(-90.1, 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `longitude above 180 should throw exception`() {
        Coordinates(0.0, 180.1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `longitude below -180 should throw exception`() {
        Coordinates(0.0, -180.1)
    }
}
