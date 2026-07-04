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

package org.giste.roadbooknavigator.core.permission.data

import android.Manifest
import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.core.permission.domain.AppPermission
import org.giste.roadbooknavigator.core.permission.domain.PermissionState
import org.giste.roadbooknavigator.core.util.Logger
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class AndroidPermissionRepositoryTest {

    private lateinit var context: Application
    private lateinit var logger: Logger
    private lateinit var repository: AndroidPermissionRepository

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        logger = mockk(relaxed = true)
        repository = AndroidPermissionRepository(context, logger)
    }

    @Test
    fun `when permission is granted then state is Granted`() = runTest {
        // Given
        shadowOf(context).grantPermissions(Manifest.permission.ACCESS_FINE_LOCATION)

        // When
        repository.refreshPermissionStates()
        val result = repository.observeAllPermissions().first()

        // Then
        assertEquals(PermissionState.Granted, result[AppPermission.FINE_LOCATION])
    }

    @Test
    fun `when permission is denied then state is Denied`() = runTest {
        // Given
        shadowOf(context).denyPermissions(Manifest.permission.ACCESS_FINE_LOCATION)

        // When
        repository.refreshPermissionStates()
        val result = repository.observeAllPermissions().first()

        // Then
        assertEquals(PermissionState.Denied, result[AppPermission.FINE_LOCATION])
    }
}
