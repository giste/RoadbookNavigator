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

import io.mockk.mockk
import org.giste.roadbooknavigator.core.util.AppLogger
import org.giste.roadbooknavigator.features.location.domain.UserLocation
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.math.sqrt

class DistanceUtilsTest {

    private val logger: AppLogger = mockk(relaxed = true)
    private lateinit var distanceUtils: DistanceUtils

    @Before
    fun setup() {
        distanceUtils = DistanceUtils(logger)
    }

    @Test
    fun `calculateDistance2D returns zero for same point`() {
        val loc = createLocation(40.0, -3.0)
        val distance = distanceUtils.calculateDistance2D(loc, loc)
        assertEquals(0.0, distance, 0.001)
    }

    @Test
    fun `calculateDistance2D returns correct distance for known points`() {
        // Points approx 111km apart (1 degree of latitude)
        val loc1 = createLocation(40.0, -3.0)
        val loc2 = createLocation(41.0, -3.0)
        
        val distance = distanceUtils.calculateDistance2D(loc1, loc2)
        
        // 1 degree of latitude is roughly 111.1 km
        assertEquals(111194.0, distance, 10.0)
    }

    @Test
    fun `calculateDistance uses 3D distance when accuracy is good`() {
        // Horizontal distance ~3m, Vertical distance 4m -> Result 5m
        val loc1 = createLocation(40.0, -3.0, altitude = 0.0, verticalAccuracy = 1f)
        // 0.000027 degrees is approx 3 meters at equator
        val loc2 = createLocation(40.0, -3.000027, altitude = 4.0, verticalAccuracy = 1f)
        
        val horizontal = distanceUtils.calculateDistance2D(loc1, loc2)
        val expected3D = sqrt(horizontal * horizontal + 16.0)
        
        val distance = distanceUtils.calculateDistance(loc1, loc2, verticalAccuracyThreshold = 5f)
        
        assertEquals(expected3D, distance, 0.001)
    }

    @Test
    fun `calculateDistance falls back to 2D when vertical accuracy is poor`() {
        val loc1 = createLocation(40.0, -3.0, altitude = 0.0, verticalAccuracy = 10f)
        val loc2 = createLocation(40.1, -3.1, altitude = 100.0, verticalAccuracy = 10f)
        
        // Threshold is 5m, accuracy is 10m -> Should use 2D
        val expected2D = distanceUtils.calculateDistance2D(loc1, loc2)
        val distance = distanceUtils.calculateDistance(loc1, loc2, verticalAccuracyThreshold = 5f)
        
        assertEquals(expected2D, distance, 0.001)
    }

    @Test
    fun `calculateDistance falls back to 2D when vertical accuracy is null`() {
        val loc1 = createLocation(40.0, -3.0, altitude = 0.0, verticalAccuracy = null)
        val loc2 = createLocation(40.1, -3.1, altitude = 100.0, verticalAccuracy = null)
        
        val expected2D = distanceUtils.calculateDistance2D(loc1, loc2)
        val distance = distanceUtils.calculateDistance(loc1, loc2)
        
        assertEquals(expected2D, distance, 0.001)
    }

    private fun createLocation(
        lat: Double,
        lon: Double,
        altitude: Double = 0.0,
        verticalAccuracy: Float? = null
    ) = UserLocation(
        latitude = lat,
        longitude = lon,
        altitude = altitude,
        accuracy = 5f,
        verticalAccuracy = verticalAccuracy,
        speed = 10f,
        bearing = 0f,
        time = 1000L
    )
}
