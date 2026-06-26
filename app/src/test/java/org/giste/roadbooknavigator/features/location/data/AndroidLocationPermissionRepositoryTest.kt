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

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.location.domain.PermissionStatus
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AndroidLocationPermissionRepositoryTest {

    private val context: Context = mockk()
    private lateinit var repository: AndroidLocationPermissionRepository

    @Before
    fun setUp() {
        mockkStatic(ContextCompat::class)
        // Provide a default return value for the static mock before instantiating the repository
        every { ContextCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_DENIED
        repository = AndroidLocationPermissionRepository(context)
    }

    @After
    fun tearDown() {
        unmockkStatic(ContextCompat::class)
    }

    @Test
    fun `getPermissionStatus should return GRANTED when FINE_LOCATION is granted`() = runTest {
        // Given
        every {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        } returns PackageManager.PERMISSION_GRANTED
        every {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        } returns PackageManager.PERMISSION_DENIED

        // When
        val status = repository.getPermissionStatus().first()

        // Then
        assertEquals(PermissionStatus.GRANTED, status)
    }

    @Test
    fun `getPermissionStatus should return GRANTED when COARSE_LOCATION is granted`() = runTest {
        // Given
        every {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        } returns PackageManager.PERMISSION_DENIED
        every {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        } returns PackageManager.PERMISSION_GRANTED

        // When
        val status = repository.getPermissionStatus().first()

        // Then
        assertEquals(PermissionStatus.GRANTED, status)
    }

    @Test
    fun `getPermissionStatus should return DENIED when neither is granted`() = runTest {
        // Given
        every {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        } returns PackageManager.PERMISSION_DENIED
        every {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        } returns PackageManager.PERMISSION_DENIED

        // When
        val status = repository.getPermissionStatus().first()

        // Then
        assertEquals(PermissionStatus.DENIED, status)
    }
}
