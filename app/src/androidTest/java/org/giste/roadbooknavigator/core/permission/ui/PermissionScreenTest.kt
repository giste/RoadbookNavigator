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

import android.content.Intent
import android.provider.Settings
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.giste.roadbooknavigator.core.permission.domain.AppPermission
import org.giste.roadbooknavigator.core.permission.domain.PermissionState
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PermissionScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenPermissionsAreDenied_thenGrantButtonIsShown() {
        // Given
        val state = PermissionUiState(
            permissions = mapOf(AppPermission.FINE_LOCATION to PermissionState.Denied)
        )

        // When
        composeTestRule.setContent {
            PermissionScreen(uiState = state)
        }

        // Then
        composeTestRule.onNodeWithText("Grant Permissions").assertIsDisplayed()
    }

    @Test
    fun whenPermissionsArePermanentlyDenied_thenSettingsButtonIsShown() {
        // Given
        val state = PermissionUiState(
            permissions = mapOf(AppPermission.FINE_LOCATION to PermissionState.PermanentlyDenied)
        )

        // When
        composeTestRule.setContent {
            PermissionScreen(uiState = state)
        }

        // Then
        composeTestRule.onNodeWithText("Open App Settings").assertIsDisplayed()
    }

    @Test
    fun whenSettingsButtonClick_thenIntentIsLaunched() {
        Intents.init()
        try {
            // Given
            val state = PermissionUiState(
                permissions = mapOf(AppPermission.FINE_LOCATION to PermissionState.PermanentlyDenied)
            )

            composeTestRule.setContent {
                PermissionScreen(uiState = state)
            }

            // When
            composeTestRule.onNodeWithText("Open App Settings").performClick()

            // Then
            intended(
                allOf(
                    hasAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS),
                    hasData("package:org.giste.roadbooknavigator")
                )
            )
        } finally {
            Intents.release()
        }
    }
}
