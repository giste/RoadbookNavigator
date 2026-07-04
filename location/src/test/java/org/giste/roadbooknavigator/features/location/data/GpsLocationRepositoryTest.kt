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

package org.giste.roadbooknavigator.features.location.data

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.location.domain.UserLocation
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GpsLocationRepositoryTest {

    private val context: Context = mockk()
    private val locationManager: LocationManager = mockk()
    private val logger: Logger = mockk(relaxed = true)
    private lateinit var gpsLocationRepository: GpsLocationRepository

    @Before
    fun setup() {
        every { context.getSystemService(Context.LOCATION_SERVICE) } returns locationManager
        gpsLocationRepository = GpsLocationRepository(context, logger)
    }

    @Test
    fun `getLocations should request updates when collected and remove them when cancelled`() = runTest {
        val listenerSlot = slot<LocationListener>()
        every { 
            locationManager.requestLocationUpdates(
                any<String>(), 
                any<Long>(), 
                any<Float>(), 
                capture(listenerSlot),
            ) 
        } returns Unit
        every { locationManager.removeUpdates(any<LocationListener>()) } returns Unit

        val job = launch(UnconfinedTestDispatcher()) {
            gpsLocationRepository.getLocations(1000L, 2f).collect {}
        }

        verify { 
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                1000L, 
                2f,
                any<LocationListener>(),
            ) 
        }

        job.cancel()

        verify { locationManager.removeUpdates(listenerSlot.captured) }
    }

    @Test
    fun `should correctly map android location to user location`() = runTest {
        val listenerSlot = slot<LocationListener>()
        every { 
            locationManager.requestLocationUpdates(
                any<String>(), 
                any<Long>(), 
                any<Float>(), 
                capture(listenerSlot),
            )
        } returns Unit
        every { locationManager.removeUpdates(any<LocationListener>()) } returns Unit

        val androidLocation = mockk<Location>()
        every { androidLocation.latitude } returns 40.0
        every { androidLocation.longitude } returns -3.0
        every { androidLocation.altitude } returns 100.0
        every { androidLocation.accuracy } returns 5f
        every { androidLocation.hasVerticalAccuracy() } returns true
        every { androidLocation.verticalAccuracyMeters } returns 2f
        every { androidLocation.speed } returns 10f
        every { androidLocation.bearing } returns 90f
        every { androidLocation.time } returns 123456789L

        val collectedLocations = mutableListOf<UserLocation>()
        val job = launch(UnconfinedTestDispatcher()) {
            gpsLocationRepository.getLocations(500L, 1f).collect { collectedLocations.add(it) }
        }

        listenerSlot.captured.onLocationChanged(androidLocation)

        val result = collectedLocations.first()
        assertEquals(40.0, result.latitude, 0.0)
        assertEquals(-3.0, result.longitude, 0.0)
        assertEquals(100.0, result.altitude, 0.0)
        assertEquals(5f, result.accuracy)
        assertEquals(2f, result.verticalAccuracy)
        assertEquals(10f, result.speed)
        assertEquals(90f, result.bearing)
        assertEquals(123456789L, result.time)

        job.cancel()
    }
}
