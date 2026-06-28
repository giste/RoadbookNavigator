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

package org.giste.roadbooknavigator.core.permission.ui

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.roadbooknavigator.core.permission.domain.AppPermission
import org.giste.roadbooknavigator.core.permission.domain.ObserveAllPermissionsUseCase
import org.giste.roadbooknavigator.core.permission.domain.PermissionState
import org.giste.roadbooknavigator.core.permission.domain.RefreshPermissionStatesUseCase
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PermissionViewModelTest {

    private val observeAllPermissionsUseCase: ObserveAllPermissionsUseCase = mockk()
    private val refreshPermissionStatesUseCase: RefreshPermissionStatesUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `when all permissions are granted then allGranted is true`() = runTest {
        // Given
        val states = mapOf(
            AppPermission.FINE_LOCATION to PermissionState.Granted,
            AppPermission.COARSE_LOCATION to PermissionState.Granted
        )
        every { observeAllPermissionsUseCase() } returns flowOf(states)

        // When
        val viewModel = PermissionViewModel(observeAllPermissionsUseCase, refreshPermissionStatesUseCase)
        
        // Then
        viewModel.uiState.first { it.allGranted }
        assertTrue(viewModel.uiState.value.allGranted)
    }

    @Test
    fun `when any permission is denied then allGranted is false`() = runTest {
        // Given
        val states = mapOf(
            AppPermission.FINE_LOCATION to PermissionState.Granted,
            AppPermission.COARSE_LOCATION to PermissionState.Denied
        )
        every { observeAllPermissionsUseCase() } returns flowOf(states)

        // When
        val viewModel = PermissionViewModel(observeAllPermissionsUseCase, refreshPermissionStatesUseCase)
        
        // Then
        viewModel.uiState.first { it.permissions.isNotEmpty() }
        assertFalse(viewModel.uiState.value.allGranted)
    }

    @Test
    fun `when permissions map is empty then allGranted is false`() = runTest {
        // Given
        val states = emptyMap<AppPermission, PermissionState>()
        every { observeAllPermissionsUseCase() } returns flowOf(states)

        // When
        val viewModel = PermissionViewModel(observeAllPermissionsUseCase, refreshPermissionStatesUseCase)

        // Then
        assertFalse(viewModel.uiState.value.allGranted)
    }

    @Test
    fun `when refresh is called then use case is executed`() = runTest {
        // Given
        every { observeAllPermissionsUseCase() } returns flowOf(emptyMap())
        every { refreshPermissionStatesUseCase() } just runs
        val viewModel = PermissionViewModel(observeAllPermissionsUseCase, refreshPermissionStatesUseCase)

        // When
        viewModel.refresh()

        // Then
        verify { refreshPermissionStatesUseCase() }
    }
}
