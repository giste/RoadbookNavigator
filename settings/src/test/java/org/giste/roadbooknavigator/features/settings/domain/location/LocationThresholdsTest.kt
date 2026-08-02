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

package org.giste.roadbooknavigator.features.settings.domain.location

import org.junit.Assert
import org.junit.Test

class LocationThresholdsTest {

    @Test
    fun `PollingIntervalThreshold should accept valid values`() {
        val min = PollingIntervalThreshold(100L)
        val max = PollingIntervalThreshold(2000L)
        val mid = PollingIntervalThreshold(500L)

        Assert.assertEquals(100L, min.milliseconds)
        Assert.assertEquals(2000L, max.milliseconds)
        Assert.assertEquals(500L, mid.milliseconds)
    }

    @Test
    fun `PollingIntervalThreshold should reject invalid values`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            PollingIntervalThreshold(99L)
        }
        Assert.assertThrows(IllegalArgumentException::class.java) {
            PollingIntervalThreshold(2001L)
        }
    }

    @Test
    fun `MinDistanceThreshold should accept valid values`() {
        val min = MinDistanceThreshold(0.0f)
        val max = MinDistanceThreshold(10.0f)
        val mid = MinDistanceThreshold(2.0f)

        Assert.assertEquals(0.0f, min.meters)
        Assert.assertEquals(10.0f, max.meters)
        Assert.assertEquals(2.0f, mid.meters)
    }

    @Test
    fun `MinDistanceThreshold should reject invalid values`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            MinDistanceThreshold(-0.1f)
        }
        Assert.assertThrows(IllegalArgumentException::class.java) {
            MinDistanceThreshold(10.1f)
        }
    }
}