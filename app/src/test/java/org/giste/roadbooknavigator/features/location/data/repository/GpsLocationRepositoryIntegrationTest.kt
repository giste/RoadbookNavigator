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

package org.giste.roadbooknavigator.features.location.data.repository

import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.location.domain.model.UserLocation
import org.junit.Assert.assertEquals
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
class GpsLocationRepositoryIntegrationTest {

    private lateinit var context: Context
    private lateinit var locationManager: LocationManager
    private lateinit var shadowLocationManager: ShadowLocationManager
    private lateinit var repository: GpsLocationRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        
        // Grant permissions in Robolectric
        shadowOf(context as android.app.Application).grantPermissions(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        shadowLocationManager = shadowOf(locationManager)
        
        // Ensure GPS provider is enabled in the shadow
        shadowLocationManager.setProviderEnabled(LocationManager.GPS_PROVIDER, true)
        
        repository = GpsLocationRepository(context)
    }

    @Test
    fun `should emit location when system sends update`() = runTest {
        val collectedLocations = mutableListOf<UserLocation>()
        
        val job = launch(UnconfinedTestDispatcher()) {
            repository.getLocations(pollingInterval = 1000L, minDistance = 0f)
                .collect { collectedLocations.add(it) }
        }

        // Simulate a location update
        val location = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 42.0
            longitude = 2.0
            accuracy = 10f
            time = System.currentTimeMillis()
            elapsedRealtimeNanos = System.nanoTime()
        }
        
        shadowLocationManager.simulateLocation(location)
        ShadowLooper.idleMainLooper()

        assertEquals(1, collectedLocations.size)
        assertEquals(42.0, collectedLocations[0].latitude, 0.0)
        assertEquals(2.0, collectedLocations[0].longitude, 0.0)

        job.cancel()
    }

    @Test
    fun `should stop requesting updates when flow is cancelled`() = runTest {
        val job = launch(UnconfinedTestDispatcher()) {
            repository.getLocations(pollingInterval = 1000L, minDistance = 0f).collect {}
        }

        // Verify there is an active request
        assertEquals(1, shadowLocationManager.getLocationRequests(LocationManager.GPS_PROVIDER).size)

        job.cancel()
        
        // Verify that the request has been removed after cancellation
        assertEquals(0, shadowLocationManager.getLocationRequests(LocationManager.GPS_PROVIDER).size)
    }
}
