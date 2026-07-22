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

package org.giste.roadbooknavigator.features.odometer.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class OdometerThresholdsTest {

    @Test
    fun `SpeedThreshold should accept values within range`() {
        val value = 1.5f
        val threshold = SpeedThreshold(value)
        assertEquals(value, threshold.metersPerSecond)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `SpeedThreshold should throw when value is below minimum`() {
        SpeedThreshold(SpeedThreshold.MIN - 0.1f)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `SpeedThreshold should throw when value is above maximum`() {
        SpeedThreshold(SpeedThreshold.MAX + 0.1f)
    }

    @Test
    fun `AccuracyThreshold should accept values within range`() {
        val value = 50.0f
        val threshold = AccuracyThreshold(value)
        assertEquals(value, threshold.meters)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `AccuracyThreshold should throw when value is below minimum`() {
        AccuracyThreshold(AccuracyThreshold.MIN - 0.1f)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `AccuracyThreshold should throw when value is above maximum`() {
        AccuracyThreshold(AccuracyThreshold.MAX + 0.1f)
    }

    @Test
    fun `VerticalAccuracyThreshold should accept values within range`() {
        val value = 50.0f
        val threshold = VerticalAccuracyThreshold(value)
        assertEquals(value, threshold.meters)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `VerticalAccuracyThreshold should throw when value is below minimum`() {
        VerticalAccuracyThreshold(VerticalAccuracyThreshold.MIN - 0.1f)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `VerticalAccuracyThreshold should throw when value is above maximum`() {
        VerticalAccuracyThreshold(VerticalAccuracyThreshold.MAX + 0.1f)
    }
}
