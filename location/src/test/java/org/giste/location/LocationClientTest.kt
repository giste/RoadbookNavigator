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

package org.giste.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.test.core.app.ApplicationProvider
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLocationManager
import org.robolectric.shadows.ShadowLooper

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LocationClientTest {

    private lateinit var context: Context
    private lateinit var locationManager: LocationManager
    private lateinit var shadowLocationManager: ShadowLocationManager
    private val logger: LocationLogger = mockk(relaxed = true)
    private val settingsFlow = MutableStateFlow(LocationSettings(pollingInterval = 1000L, minDistance = 2f))

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        shadowOf(context as Application).grantPermissions(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        shadowLocationManager = shadowOf(locationManager)
        shadowLocationManager.setProviderEnabled(LocationManager.GPS_PROVIDER, true)
    }

    @Test
    fun `observeLocation should emit location events when GPS provider sends updates`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val client = LocationClient(
            context = context,
            settings = settingsFlow,
            logger = logger,
            scope = CoroutineScope(testDispatcher)
        )

        val collectedEvents = mutableListOf<LocationEvent>()
        backgroundScope.launch(testDispatcher) {
            client.observeLocation().collect { collectedEvents.add(it) }
        }

        val testLocation = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 40.4168
            longitude = -3.7038
            accuracy = 5.0f
            time = System.currentTimeMillis()
            elapsedRealtimeNanos = System.nanoTime()
        }

        shadowLocationManager.simulateLocation(testLocation)
        ShadowLooper.idleMainLooper()

        assertTrue(collectedEvents.isNotEmpty())
        val firstEvent = collectedEvents.first()
        assertTrue(firstEvent is LocationEvent.LocationUpdated)
        val location = (firstEvent as LocationEvent.LocationUpdated).location
        assertEquals(40.4168, location.latitude, 0.0001)
        assertEquals(-3.7038, location.longitude, 0.0001)
    }

    @Test
    fun `observeLocation should reconfigure location requests when settings flow updates`() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        val client = LocationClient(
            context = context,
            settings = settingsFlow,
            logger = logger,
            scope = CoroutineScope(testDispatcher)
        )

        backgroundScope.launch(testDispatcher) {
            client.observeLocation().collect {}
        }
        ShadowLooper.idleMainLooper()

        val initialRequests = shadowLocationManager.getLocationRequests(LocationManager.GPS_PROVIDER)
        assertNotNull(initialRequests)

        // Update settings to new interval and min distance
        settingsFlow.value = LocationSettings(pollingInterval = 500L, minDistance = 10f)
        ShadowLooper.idleMainLooper()

        val updatedRequests = shadowLocationManager.getLocationRequests(LocationManager.GPS_PROVIDER)
        assertNotNull(updatedRequests)
    }

    @Test
    fun `LocationClient can be instantiated with default parameters`() {
        val defaultClient = LocationClient(context)
        assertNotNull(defaultClient)
    }
}
