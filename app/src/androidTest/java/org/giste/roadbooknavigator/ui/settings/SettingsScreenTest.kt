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

package org.giste.roadbooknavigator.ui.settings

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.platform.app.InstrumentationRegistry
import org.giste.roadbooknavigator.R
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.AppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun tabSwitching_updatesContent() {
        composeTestRule.setContent {
            SettingsContent(
                uiState = SettingsUiState.Success(AppSettings()),
                onBackClick = {},
                onThemeSelected = {},
                onOrientationSelected = {},
                onShortDistanceThresholdChange = {},
                onOdometerSpeedThresholdChange = {},
                onOdometerMinAccuracyChange = {},
                onOdometerMinVerticalAccuracyChange = {},
                onOdometerPollingIntervalChange = {},
                onOdometerMinDistanceChange = {},
                onRestoreOdometerDefaults = {}
            )
        }

        // Initially on User tab
        composeTestRule.onNodeWithText(context.getString(R.string.settings_theme_title)).assertIsDisplayed()

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_1").performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_advanced_warning)).assertIsDisplayed()

        // Switch to Maps tab
        composeTestRule.onNodeWithTag("SettingsTab_2").performClick()
        composeTestRule.onNodeWithText("Map Management - Coming Soon").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun themeSelection_triggersCallback() {
        var selectedTheme: AppTheme? = null
        composeTestRule.setContent {
            SettingsContent(
                uiState = SettingsUiState.Success(AppSettings(theme = AppTheme.LIGHT)),
                onBackClick = {},
                onThemeSelected = { selectedTheme = it },
                onOrientationSelected = {},
                onShortDistanceThresholdChange = {},
                onOdometerSpeedThresholdChange = {},
                onOdometerMinAccuracyChange = {},
                onOdometerMinVerticalAccuracyChange = {},
                onOdometerPollingIntervalChange = {},
                onOdometerMinDistanceChange = {},
                onRestoreOdometerDefaults = {}
            )
        }

        // Click Dark theme card
        composeTestRule.onNodeWithText(context.getString(R.string.settings_theme_dark)).performClick()
        assertTrue(selectedTheme == AppTheme.DARK)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun backButtonClick_triggersCallback() {
        var backClicked = false
        composeTestRule.setContent {
            SettingsContent(
                uiState = SettingsUiState.Success(AppSettings()),
                onBackClick = { backClicked = true },
                onThemeSelected = {},
                onOrientationSelected = {},
                onShortDistanceThresholdChange = {},
                onOdometerSpeedThresholdChange = {},
                onOdometerMinAccuracyChange = {},
                onOdometerMinVerticalAccuracyChange = {},
                onOdometerPollingIntervalChange = {},
                onOdometerMinDistanceChange = {},
                onRestoreOdometerDefaults = {}
            )
        }

        composeTestRule.onNodeWithTag("SettingsBackButton").performClick()
        assertTrue(backClicked)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun restoreDefaultsButton_triggersCallback() {
        var restoreClicked = false
        composeTestRule.setContent {
            SettingsContent(
                uiState = SettingsUiState.Success(AppSettings()),
                onBackClick = {},
                onThemeSelected = {},
                onOrientationSelected = {},
                onShortDistanceThresholdChange = {},
                onOdometerSpeedThresholdChange = {},
                onOdometerMinAccuracyChange = {},
                onOdometerMinVerticalAccuracyChange = {},
                onOdometerPollingIntervalChange = {},
                onOdometerMinDistanceChange = {},
                onRestoreOdometerDefaults = { restoreClicked = true }
            )
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_1").performClick()

        // Click restore defaults button
        composeTestRule.onNodeWithTag("RestoreDefaultsButton")
            .performScrollTo()
            .performClick()
        assertTrue(restoreClicked)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun orientationSelection_triggersCallback() {
        var selectedOrientation: AppOrientation? = null
        composeTestRule.setContent {
            SettingsContent(
                uiState = SettingsUiState.Success(AppSettings()),
                onBackClick = {},
                onThemeSelected = {},
                onOrientationSelected = { selectedOrientation = it },
                onShortDistanceThresholdChange = {},
                onOdometerSpeedThresholdChange = {},
                onOdometerMinAccuracyChange = {},
                onOdometerMinVerticalAccuracyChange = {},
                onOdometerPollingIntervalChange = {},
                onOdometerMinDistanceChange = {},
                onRestoreOdometerDefaults = {}
            )
        }

        // Click Horizontal orientation button
        composeTestRule.onNodeWithTag("OrientationButton_${AppOrientation.HORIZONTAL.name}").performClick()
        assertTrue(selectedOrientation == AppOrientation.HORIZONTAL)
    }
}
