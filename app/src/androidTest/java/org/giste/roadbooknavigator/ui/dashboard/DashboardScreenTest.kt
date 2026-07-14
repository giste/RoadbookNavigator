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

package org.giste.roadbooknavigator.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.odometer.ui.PartialDistance
import org.giste.roadbooknavigator.features.odometer.ui.TotalDistance
import org.giste.roadbooknavigator.features.roadbook.domain.model.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.model.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.model.Waypoint
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookUiState
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookViewModel
import org.junit.Rule
import org.junit.Test
import java.util.Locale
import org.giste.roadbooknavigator.features.roadbook.R as RoadbookR

class DashboardScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun displaysOdometerValues() {
        val uiState = DashboardUiState(
            odometer = Odometer(1200.0, 500.0),
        )
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                MainContent(
                    windowSizeClass = windowSizeClass,
                    uiState = uiState,
                    onSettingsClick = {},
                    primaryOdometerSlot = { modifier ->
                        val configuration = LocalConfiguration.current
                        val locale = if (configuration.locales.size() > 0) configuration.locales[0] else LocalLocale.current.platformLocale
                        val partialDistanceStr = String.format(locale, "%.2f", uiState.odometer.partial / 1000.0)
                        PartialDistance(distance = partialDistanceStr, modifier = modifier, onLongClick = {})
                    },
                    secondaryOdometerSlot = { modifier ->
                        val configuration = LocalConfiguration.current
                        val locale = if (configuration.locales.size() > 0) configuration.locales[0] else LocalLocale.current.platformLocale
                        val totalDistanceStr = String.format(locale, "%.1f", uiState.odometer.total / 1000.0)
                        TotalDistance(distance = totalDistanceStr, modifier = modifier)
                    },
                    roadbookSlot = { modifier -> Box(modifier) },
                    mapSlot = { modifier -> Box(modifier) }
                )
            }
        }

        // Format expectations using default locale to match production behavior
        val expectedTotal = String.format(Locale.getDefault(), "%.1f", 1200.0 / 1000.0)
        val expectedPartial = String.format(Locale.getDefault(), "%.2f", 500.0 / 1000.0)

        composeTestRule.onNodeWithText(expectedTotal).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedPartial).assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun longClickOnPartialDistance_triggersOnLongClickPartial() {
        var longClickTriggered = false
        val uiState = DashboardUiState(
            odometer = Odometer(0.0, 500.0),
        )
        val expectedPartial = String.format(Locale.getDefault(), "%.2f", 500.0 / 1000.0)
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                MainContent(
                    windowSizeClass = windowSizeClass,
                    uiState = uiState,
                    onSettingsClick = {},
                    primaryOdometerSlot = { modifier ->
                        PartialDistance(distance = expectedPartial, modifier = modifier, onLongClick = { longClickTriggered = true })
                    },
                    secondaryOdometerSlot = { modifier ->
                        TotalDistance(distance = "0.0", modifier = modifier)
                    },
                    roadbookSlot = { modifier -> Box(modifier) },
                    mapSlot = { modifier -> Box(modifier) }
                )
            }
        }

        composeTestRule.onNodeWithText(expectedPartial).performTouchInput {
            longClick()
        }

        assert(longClickTriggered)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun tabletPortrait_displaysMapSection() {
        val uiState = DashboardUiState(
            odometer = Odometer(0.0, 0.0),
        )

        val mapTag = "MapSectionTag"
        val mapDummyText = "MAP_CONTENT_DUMMY"
        // Medium width and Tall height
        val size = DpSize(700.dp, 1000.dp)
        val windowSizeClass = WindowSizeClass.calculateFromSize(size)

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                MainContent(
                    windowSizeClass = windowSizeClass,
                    uiState = uiState,
                    onSettingsClick = {},
                    primaryOdometerSlot = { modifier -> Box(modifier) },
                    secondaryOdometerSlot = { modifier -> Box(modifier) },
                    roadbookSlot = { modifier -> Box(modifier) },
                    mapSlot = { modifier ->
                        Text(text = mapDummyText, modifier = modifier.testTag(mapTag))
                    }
                )
            }
        }

        // Verify map is present by searching for dummy text
        composeTestRule.onNodeWithText(mapDummyText).assertExists()
        // verify we are in PortraitLayout
        composeTestRule.onNodeWithTag("PortraitLayout").assertExists()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun phonePortrait_doesNotDisplayMapSection() {
        val uiState = DashboardUiState(
            odometer = Odometer(0.0, 0.0),
        )

        val mapTag = "MapSectionTag"
        val mapDummyText = "MAP_CONTENT_DUMMY"
        // Phone Portrait size
        val size = DpSize(411.dp, 891.dp)
        val windowSizeClass = WindowSizeClass.calculateFromSize(size)

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                MainContent(
                    windowSizeClass = windowSizeClass,
                    uiState = uiState,
                    onSettingsClick = {},
                    primaryOdometerSlot = { modifier -> Box(modifier) },
                    secondaryOdometerSlot = { modifier -> Box(modifier) },
                    roadbookSlot = { modifier -> Box(modifier) },
                    mapSlot = { modifier ->
                        Text(text = mapDummyText, modifier = modifier.testTag(mapTag))
                    }
                )
            }
        }

        // Verify map is NOT present
        composeTestRule.onAllNodesWithText(mapDummyText).assertCountEquals(0)
        // Verify we are in CompactPortraitLayout
        composeTestRule.onNodeWithTag("CompactPortraitLayout").assertExists()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun volumeUpKey_triggersIncrementPartialDistance() {
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val roadbookViewModel: RoadbookViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        val roadbookStateFlow = MutableStateFlow<RoadbookUiState>(RoadbookUiState.Empty)
        val scrollPositionFlow = MutableStateFlow(RoadbookPosition(0, 0))
        every { viewModel.uiState } returns uiStateFlow
        every { roadbookViewModel.roadbookState } returns roadbookStateFlow
        every { roadbookViewModel.initialScrollPosition } returns scrollPositionFlow
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookViewModel = roadbookViewModel,
                )
            }
        }

        // Focus the main screen to receive key events
        composeTestRule.onNodeWithTag("MainScreen").performClick()
        
        composeTestRule.onNodeWithTag("MainScreen").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_VOLUME_UP
                )
            )
        )

        verify { viewModel.incrementPartialDistance() }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun f6Key_triggersResetPartialDistance() {
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val roadbookViewModel: RoadbookViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        val roadbookStateFlow = MutableStateFlow<RoadbookUiState>(RoadbookUiState.Empty)
        val scrollPositionFlow = MutableStateFlow(RoadbookPosition(0, 0))
        every { viewModel.uiState } returns uiStateFlow
        every { roadbookViewModel.roadbookState } returns roadbookStateFlow
        every { roadbookViewModel.initialScrollPosition } returns scrollPositionFlow
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookViewModel = roadbookViewModel,
                )
            }
        }

        composeTestRule.onNodeWithTag("MainScreen").performClick()
        
        composeTestRule.onNodeWithTag("MainScreen").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_F6
                )
            )
        )

        verify { viewModel.resetPartialDistance() }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun volumeDownKey_triggersDecrementPartialDistance() {
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val roadbookViewModel: RoadbookViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        val roadbookStateFlow = MutableStateFlow<RoadbookUiState>(RoadbookUiState.Empty)
        val scrollPositionFlow = MutableStateFlow(RoadbookPosition(0, 0))
        every { viewModel.uiState } returns uiStateFlow
        every { roadbookViewModel.roadbookState } returns roadbookStateFlow
        every { roadbookViewModel.initialScrollPosition } returns scrollPositionFlow
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookViewModel = roadbookViewModel,
                )
            }
        }

        composeTestRule.onNodeWithTag("MainScreen").performClick()

        composeTestRule.onNodeWithTag("MainScreen").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_VOLUME_DOWN
                )
            )
        )

        verify { viewModel.decrementPartialDistance() }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun dashboardContent_displaysMainContent() {
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val roadbookViewModel: RoadbookViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        val roadbookStateFlow = MutableStateFlow<RoadbookUiState>(RoadbookUiState.Empty)
        val scrollPositionFlow = MutableStateFlow(RoadbookPosition(0, 0))
        every { viewModel.uiState } returns uiStateFlow
        every { roadbookViewModel.roadbookState } returns roadbookStateFlow
        every { roadbookViewModel.initialScrollPosition } returns scrollPositionFlow
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookViewModel = roadbookViewModel,
                )
            }
        }

        val expectedMessage = context.getString(RoadbookR.string.main_no_route)
        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()
    }

}
