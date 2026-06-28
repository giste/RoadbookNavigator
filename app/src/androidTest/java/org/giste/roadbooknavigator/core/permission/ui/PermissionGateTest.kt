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

import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.giste.roadbooknavigator.core.permission.domain.AppPermission
import org.giste.roadbooknavigator.core.permission.domain.PermissionState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PermissionGateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel: PermissionViewModel = mockk(relaxed = true)
    private val uiState = MutableStateFlow(PermissionUiState())

    @Before
    fun setup() {
        every { viewModel.uiState } returns uiState
    }

    @Test
    fun whenPermissionsAreGranted_thenContentIsShown() {
        // Given
        uiState.value = PermissionUiState(allGranted = true)

        // When
        composeTestRule.setContent {
            PermissionGate(
                viewModel = viewModel,
                content = { Text("Content Shown") }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Content Shown").assertIsDisplayed()
        composeTestRule.onNodeWithText("Permissions Required").assertDoesNotExist()
    }

    @Test
    fun whenPermissionsAreDenied_thenPermissionScreenIsShown() {
        // Given
        uiState.value = PermissionUiState(
            allGranted = false,
            permissions = mapOf(AppPermission.FINE_LOCATION to PermissionState.Denied)
        )

        // When
        composeTestRule.setContent {
            PermissionGate(
                viewModel = viewModel,
                content = { Text("Content Shown") }
            )
        }

        // Then
        composeTestRule.onNodeWithText("Permissions Required").assertIsDisplayed()
        composeTestRule.onNodeWithText("Content Shown").assertDoesNotExist()
    }

    @Test
    fun whenPermissionsChange_thenUIUpdatesReactively() {
        // Given starting denied
        uiState.value = PermissionUiState(
            allGranted = false,
            permissions = mapOf(AppPermission.FINE_LOCATION to PermissionState.Denied)
        )

        composeTestRule.setContent {
            PermissionGate(
                viewModel = viewModel,
                content = { Text("Content Shown") }
            )
        }
        composeTestRule.onNodeWithText("Permissions Required").assertIsDisplayed()

        // When granting
        uiState.value = PermissionUiState(allGranted = true)

        // Then
        composeTestRule.onNodeWithText("Content Shown").assertIsDisplayed()
        composeTestRule.onNodeWithText("Permissions Required").assertDoesNotExist()
    }

    @Test
    fun whenAppResumes_thenViewModelRefreshIsCalled() {
        // Given
        val lifecycleOwner = TestLifecycleOwner(Lifecycle.State.INITIALIZED)
        
        composeTestRule.setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                PermissionGate(
                    viewModel = viewModel,
                    content = { Text("Content Shown") }
                )
            }
        }

        // When
        composeTestRule.runOnUiThread {
            lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }

        // Then
        verify { viewModel.refresh() }
    }
}
