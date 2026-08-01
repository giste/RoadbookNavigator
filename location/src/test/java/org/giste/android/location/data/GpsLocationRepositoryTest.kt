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

package org.giste.android.location.data

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
import org.giste.android.location.domain.LocationEvent
import org.giste.android.location.domain.LocationLogger
import org.giste.android.location.domain.UserLocation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GpsLocationRepositoryTest {

    private val context: Context = mockk()
    private val locationManager: LocationManager = mockk()
    private val logger: LocationLogger = mockk(relaxed = true)
    private lateinit var gpsLocationRepository: GpsLocationRepository

    @Before
    fun setup() {
        every { context.getSystemService(Context.LOCATION_SERVICE) } returns locationManager
        every { locationManager.removeUpdates(any<LocationListener>()) } returns Unit
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
    fun `should correctly map android location to user location event`() = runTest {
        val listenerSlot = slot<LocationListener>()
        every { 
            locationManager.requestLocationUpdates(
                any<String>(), 
                any<Long>(), 
                any<Float>(), 
                capture(listenerSlot),
            )
        } returns Unit

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

        val collectedEvents = mutableListOf<LocationEvent>()
        val job = launch(UnconfinedTestDispatcher()) {
            gpsLocationRepository.getLocations(500L, 1f).collect { collectedEvents.add(it) }
        }

        listenerSlot.captured.onLocationChanged(androidLocation)

        val result = collectedEvents.first()
        assertTrue(result is LocationEvent.LocationUpdated)
        val location = (result as LocationEvent.LocationUpdated).location
        assertEquals(40.0, location.latitude, 0.0)
        assertEquals(-3.0, location.longitude, 0.0)
        assertEquals(100.0, location.altitude, 0.0)
        assertEquals(5f, location.accuracy)
        assertEquals(2f, location.verticalAccuracy)
        assertEquals(10f, location.speed)
        assertEquals(90f, location.bearing)
        assertEquals(123456789L, location.time)

        job.cancel()
    }

    @Test
    fun `should emit SignalLost when provider status is unavailable`() = runTest {
        val listenerSlot = slot<LocationListener>()
        every { 
            locationManager.requestLocationUpdates(any<String>(), any<Long>(), any<Float>(), capture(listenerSlot))
        } returns Unit

        val collectedEvents = mutableListOf<LocationEvent>()
        val job = launch(UnconfinedTestDispatcher()) {
            gpsLocationRepository.getLocations(1000L, 0f).collect { collectedEvents.add(it) }
        }

        // status 1 = TEMPORARILY_UNAVAILABLE
        @Suppress("DEPRECATION")
        listenerSlot.captured.onStatusChanged(LocationManager.GPS_PROVIDER, 1, null)

        assertTrue(collectedEvents.last() is LocationEvent.SignalLost)
        job.cancel()
    }

    @Test
    fun `should emit SignalRestored when provider status is available`() = runTest {
        val listenerSlot = slot<LocationListener>()
        every { 
            locationManager.requestLocationUpdates(any<String>(), any<Long>(), any<Float>(), capture(listenerSlot))
        } returns Unit

        val collectedEvents = mutableListOf<LocationEvent>()
        val job = launch(UnconfinedTestDispatcher()) {
            gpsLocationRepository.getLocations(1000L, 0f).collect { collectedEvents.add(it) }
        }

        // status 2 = AVAILABLE
        @Suppress("DEPRECATION")
        listenerSlot.captured.onStatusChanged(LocationManager.GPS_PROVIDER, 2, null)

        assertTrue(collectedEvents.last() is LocationEvent.SignalRestored)
        job.cancel()
    }

    @Test
    fun `should emit ProviderDisabled when provider is disabled`() = runTest {
        val listenerSlot = slot<LocationListener>()
        every { 
            locationManager.requestLocationUpdates(any<String>(), any<Long>(), any<Float>(), capture(listenerSlot))
        } returns Unit

        val collectedEvents = mutableListOf<LocationEvent>()
        val job = launch(UnconfinedTestDispatcher()) {
            gpsLocationRepository.getLocations(1000L, 0f).collect { collectedEvents.add(it) }
        }

        listenerSlot.captured.onProviderDisabled(LocationManager.GPS_PROVIDER)

        assertTrue(collectedEvents.last() is LocationEvent.ProviderDisabled)
        job.cancel()
    }

    @Test
    fun `should emit SignalRestored when provider is enabled`() = runTest {
        val listenerSlot = slot<LocationListener>()
        every { 
            locationManager.requestLocationUpdates(any<String>(), any<Long>(), any<Float>(), capture(listenerSlot))
        } returns Unit

        val collectedEvents = mutableListOf<LocationEvent>()
        val job = launch(UnconfinedTestDispatcher()) {
            gpsLocationRepository.getLocations(1000L, 0f).collect { collectedEvents.add(it) }
        }

        listenerSlot.captured.onProviderEnabled(LocationManager.GPS_PROVIDER)

        assertTrue(collectedEvents.last() is LocationEvent.SignalRestored)
        job.cancel()
    }

    @Test
    fun `should emit Error when requestLocationUpdates throws`() = runTest {
        every { 
            locationManager.requestLocationUpdates(any<String>(), any<Long>(), any<Float>(), any<LocationListener>())
        } throws SecurityException("No permission")

        val collectedEvents = mutableListOf<LocationEvent>()
        
        // We expect the flow to throw the exception after emitting the Error event
        try {
            gpsLocationRepository.getLocations(1000L, 0f).collect { 
                collectedEvents.add(it) 
            }
        } catch (e: SecurityException) {
            // Expected exception from close(e)
        }

        assertTrue(collectedEvents.isNotEmpty())
        assertTrue(collectedEvents.last() is LocationEvent.Error)
        assertEquals("No permission", (collectedEvents.last() as LocationEvent.Error).message)
    }
}
