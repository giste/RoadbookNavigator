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
import org.junit.Assert.assertTrue
import org.junit.Test

class DistanceTest {

    @Test
    fun `distance in meters should convert correctly to kilometers`() {
        val distance = Distance(1500)
        assertEquals(1.5, distance.kilometers, 0.0)
    }

    @Test
    fun `fromKilometers should create correct distance in meters`() {
        val distance = Distance.fromKilometers(2.5)
        assertEquals(2500L, distance.meters)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `negative distance should throw exception`() {
        Distance(-1)
    }

    @Test
    fun `plus operator should add distances correctly`() {
        val d1 = Distance(1000)
        val d2 = Distance(500)
        val result = d1 + d2
        assertEquals(1500L, result.meters)
    }

    @Test
    fun `minus operator should subtract distances correctly`() {
        val d1 = Distance(1000)
        val d2 = Distance(300)
        val result = d1 - d2
        assertEquals(700L, result.meters)
    }

    @Test
    fun `compareTo should work correctly`() {
        val d1 = Distance(1000)
        val d2 = Distance(2000)
        assertTrue(d1 < d2)
        assertTrue(d2 > d1)
        assertEquals(0, d1.compareTo(Distance(1000)))
    }
}
