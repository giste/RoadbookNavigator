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

package org.giste.roadbooknavigator.core.permissions.ui

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.giste.roadbooknavigator.R
import org.giste.roadbooknavigator.core.permissions.domain.PermissionStatus
import org.junit.Rule
import org.junit.Test

class LocationPermissionGateTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun whenPermissionGranted_showsContent() {
        val viewModel: PermissionViewModel = mockk(relaxed = true)
        val statusFlow = MutableStateFlow(PermissionStatus.GRANTED)
        every { viewModel.locationPermissionStatus } returns statusFlow

        composeTestRule.setContent {
            LocationPermissionGate(viewModel = viewModel) {
                Text("Protected Content")
            }
        }

        composeTestRule.onNodeWithText("Protected Content").assertIsDisplayed()
    }

    @Test
    fun whenPermissionDenied_showsPermissionRequest() {
        val viewModel: PermissionViewModel = mockk(relaxed = true)
        val statusFlow = MutableStateFlow(PermissionStatus.DENIED)
        every { viewModel.locationPermissionStatus } returns statusFlow

        composeTestRule.setContent {
            LocationPermissionGate(viewModel = viewModel) {
                Text("Protected Content")
            }
        }

        val expectedMessage = context.getString(R.string.permission_location_request)
        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText("Protected Content").assertDoesNotExist()
    }

    @Test
    fun whenRationaleRequired_showsRationaleMessage() {
        val viewModel: PermissionViewModel = mockk(relaxed = true)
        val statusFlow = MutableStateFlow(PermissionStatus.RATIONALE_REQUIRED)
        every { viewModel.locationPermissionStatus } returns statusFlow

        composeTestRule.setContent {
            LocationPermissionGate(viewModel = viewModel) {
                Text("Protected Content")
            }
        }

        val expectedMessage = context.getString(R.string.permission_location_rationale)
        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()
    }
}
