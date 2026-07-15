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

package org.giste.roadbooknavigator.features.settings.ui

import androidx.compose.material3.Text
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
import org.giste.roadbooknavigator.features.settings.R
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.location.domain.LocationSettings
import org.giste.roadbooknavigator.features.map.domain.model.MapSettings
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettings
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.core.settings.domain.AppTheme
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookSettings
import org.giste.roadbooknavigator.features.roadbook.domain.model.ShortDistanceThreshold
import org.giste.roadbooknavigator.features.settings.domain.RemoteModel
import org.junit.Assert.assertEquals
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
                    uiState = SettingsUiState.Success(AppSettings(), LocationSettings(), OdometerSettings(), MapSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = { Text("Map Management Content") }
                )
            }
        }

        // Initially on User tab
        composeTestRule.onNodeWithText(context.getString(R.string.settings_theme_title)).assertIsDisplayed()

        // Switch to Remote tab
        composeTestRule.onNodeWithTag("SettingsTab_1").performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_remote_model_title)).assertIsDisplayed()

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_2").performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_advanced_warning)).assertIsDisplayed()

        // Switch to Maps tab
        composeTestRule.onNodeWithTag("SettingsTab_3").performClick()
        composeTestRule.onNodeWithText("Map Management Content").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun themeSelection_triggersCallback() {
        var selectedTheme: AppTheme? = null
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings(theme = AppTheme.LIGHT), LocationSettings(), OdometerSettings(), MapSettings()),
                    onBackClick = {},
                    onThemeSelected = { selectedTheme = it },
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
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
                    uiState = SettingsUiState.Success(AppSettings(), LocationSettings(), OdometerSettings(), MapSettings()),
                    onBackClick = { backClicked = true },
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
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
                    uiState = SettingsUiState.Success(
                        AppSettings(),
                        LocationSettings(),
                        OdometerSettings(),
                        MapSettings()
                    ),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = { restoreClicked = true },
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_2").performClick()

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
                    uiState = SettingsUiState.Success(AppSettings(), LocationSettings(), OdometerSettings(), MapSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = { selectedOrientation = it },
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
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
                    uiState = SettingsUiState.Success(AppSettings(fullScreen = false), LocationSettings(), OdometerSettings(), MapSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = { fullScreenEnabled = it },
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
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
                    uiState = SettingsUiState.Success(AppSettings(), LocationSettings(), OdometerSettings(), MapSettings(),
                        RoadbookSettings(shortDistanceThreshold = ShortDistanceThreshold(100L))),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = { newValue = it },
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
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
                    uiState = SettingsUiState.Success(locationSettings = LocationSettings(pollingInterval = 500L), odometerSettings = OdometerSettings(), mapSettings = MapSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = { newValue = it },
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_2").performClick()
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
                    uiState = SettingsUiState.Success(appSettings = AppSettings(), locationSettings = LocationSettings(), odometerSettings = OdometerSettings(speedThreshold = 0.5f), mapSettings = MapSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = { newValue = it },
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_2").performClick()
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
                    uiState = SettingsUiState.Success(appSettings = AppSettings(), locationSettings = LocationSettings(), odometerSettings = OdometerSettings(minAccuracy = 10.0f), mapSettings = MapSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = { newValue = it },
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_2").performClick()
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
                    uiState = SettingsUiState.Success(appSettings = AppSettings(), locationSettings = LocationSettings(), odometerSettings = OdometerSettings(minVerticalAccuracy = 5.0f), mapSettings = MapSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = { newValue = it },
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_2").performClick()
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
                    uiState = SettingsUiState.Success(
                        locationSettings = LocationSettings(
                            minDistance = 1.0f
                        ), odometerSettings = OdometerSettings(),
                        mapSettings = MapSettings()
                    ),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = { newValue = it },
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_2").performClick()
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
                    uiState = SettingsUiState.Success(AppSettings(), LocationSettings(), OdometerSettings(), MapSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = {},
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
                )
            }
        }

        // Switch to Advanced tab
        composeTestRule.onNodeWithTag("SettingsTab_2").performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.settings_advanced_warning)).assertIsDisplayed()

        // Simulate configuration change
        restorationTester.emulateSavedInstanceStateRestore()

        // Verify Advanced tab is still active
        composeTestRule.onNodeWithText(context.getString(R.string.settings_advanced_warning)).assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun remoteModelSelection_triggersCallback() {
        var selectedModel: RemoteModel? = null
        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                SettingsContent(
                    uiState = SettingsUiState.Success(AppSettings(), LocationSettings(), OdometerSettings(), MapSettings()),
                    onBackClick = {},
                    onThemeSelected = {},
                    onOrientationSelected = {},
                    onFullScreenChange = {},
                    onShortDistanceThresholdChange = {},
                    onOdometerSpeedThresholdChange = {},
                    onOdometerMinAccuracyChange = {},
                    onOdometerMinVerticalAccuracyChange = {},
                    onLocationPollingIntervalChange = {},
                    onLocationMinDistanceChange = {},
                    onRestoreOdometerDefaults = {},
                    onRemoteModelSelected = { selectedModel = it },
                    onOdometerKeysChanged = { _, _, _ -> },
                    onRoadbookKeysChanged = { _, _ -> },
                    onMapInitialZoomChange = {},
                    onMapInitialTiltChange = {},
                    mapManagementContent = {}
                )
            }
        }

        // Switch to Remote tab
        composeTestRule.onNodeWithTag("SettingsTab_1").performClick()

        // Click Terra Pirata button
        composeTestRule.onNodeWithTag("RemoteModelButton_${RemoteModel.TERRA_PIRATA.name}")
            .performScrollTo()
            .performClick()
        assertEquals(RemoteModel.TERRA_PIRATA, selectedModel)
    }
}
