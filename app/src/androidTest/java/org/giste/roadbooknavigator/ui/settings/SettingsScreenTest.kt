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

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import org.giste.roadbooknavigator.R
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.AppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

    @Test
    fun tabSwitching_updatesContent() {
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
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
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings(theme = AppTheme.LIGHT)),
                    onBackClick = {},
                    onThemeSelected = { selectedTheme = it },
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        // Click Dark theme card
        composeTestRule.onNodeWithText(context.getString(R.string.settings_theme_dark))
            .performScrollTo()
            .performClick()
        assertTrue(selectedTheme == AppTheme.DARK)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun backButtonClick_triggersCallback() {
        var backClicked = false
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings()),
                    onBackClick = { backClicked = true },
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("SettingsBackButton").performClick()
        assertTrue(backClicked)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun restoreDefaultsButton_triggersCallback() {
        var restoreClicked = false
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = { restoreClicked = true }
                )
            }
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
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = { selectedOrientation = it },
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        // Click Horizontal orientation button
        composeTestRule.onNodeWithTag("OrientationButton_${AppOrientation.HORIZONTAL.name}")
            .performScrollTo()
            .performClick()
        assertTrue(selectedOrientation == AppOrientation.HORIZONTAL)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun fullScreenToggle_triggersCallback() {
        var fullScreenEnabled: Boolean? = null
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings(fullScreen = false)),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = { fullScreenEnabled = it },
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("FullScreenSwitch")
            .performScrollTo()
            .performClick()
        assertTrue(fullScreenEnabled == true)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun shortDistanceSlider_triggersCallback() {
        var newValue: Long? = null
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings(shortDistanceThreshold = 100L)),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = { newValue = it },
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        // Click on the slider (should click the center by default)
        composeTestRule.onNodeWithTag("ShortDistanceSlider")
            .performScrollTo()
            .performClick()
        composeTestRule.waitForIdle()

        assertTrue("Expected newValue to be > 100L but was $newValue", newValue != null && newValue > 100L)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun pollingIntervalSlider_triggersCallback() {
        var newValue: Long? = null
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings(odometerPollingInterval = 500L)),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = { newValue = it },
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_1").performClick()
        composeTestRule.waitForIdle()

        // Click on the slider
        composeTestRule.onNodeWithTag("PollingIntervalSlider")
            .performScrollTo()
            .performClick()
        composeTestRule.waitForIdle()

        assertTrue("Expected newValue to be > 500L but was $newValue", newValue != null && newValue > 500L)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun speedThresholdSlider_triggersCallback() {
        var newValue: Float? = null
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings(odometerSpeedThreshold = 0.5f)),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = { newValue = it },
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_1").performClick()
        composeTestRule.waitForIdle()

        // Click on the slider
        composeTestRule.onNodeWithTag("SpeedThresholdSlider")
            .performScrollTo()
            .performClick()
        composeTestRule.waitForIdle()

        assertTrue("Expected newValue to be > 0.5f but was $newValue", newValue != null && newValue > 0.5f)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun minAccuracySlider_triggersCallback() {
        var newValue: Float? = null
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings(odometerMinAccuracy = 10.0f)),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = { newValue = it },
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_1").performClick()
        composeTestRule.waitForIdle()

        // Click on the slider
        composeTestRule.onNodeWithTag("MinAccuracySlider")
            .performScrollTo()
            .performClick()
        composeTestRule.waitForIdle()

        assertTrue("Expected newValue to be > 10.0f but was $newValue", newValue != null && newValue > 10.0f)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun minVerticalAccuracySlider_triggersCallback() {
        var newValue: Float? = null
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings(odometerMinVerticalAccuracy = 5.0f)),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = { newValue = it },
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_1").performClick()
        composeTestRule.waitForIdle()

        // Click on the slider
        composeTestRule.onNodeWithTag("MinVerticalAccuracySlider")
            .performScrollTo()
            .performClick()
        composeTestRule.waitForIdle()

        assertTrue("Expected newValue to be > 5.0f but was $newValue", newValue != null && newValue > 5.0f)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun minDistanceSlider_triggersCallback() {
        var newValue: Float? = null
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings(odometerMinDistance = 1.0f)),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = { newValue = it },
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_1").performClick()
        composeTestRule.waitForIdle()

        // Click on the slider
        composeTestRule.onNodeWithTag("MinDistanceSlider")
            .performScrollTo()
            .performClick()
        composeTestRule.waitForIdle()

        assertTrue("Expected newValue to be > 1.0f but was $newValue", newValue != null && newValue > 1.0f)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun activeTab_survivesConfigurationChange() {
        val restorationTester = StateRestorationTester(composeTestRule)

        restorationTester.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onOdometerPollingIntervalChange = {},
                    onOdometerMinDistanceChange = {},
                    onRestoreOdometerDefaults = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_1").performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_advanced_warning)).assertIsDisplayed()

        // Simulate configuration change
        restorationTester.emulateSavedInstanceStateRestore()

        // Verify Advanced tab is still active
        composeTestRule.onNodeWithText(context.getString(R.string.settings_advanced_warning)).assertIsDisplayed()
    }
}
