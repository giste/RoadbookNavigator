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

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
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
import org.giste.roadbooknavigator.features.roadbook.domain.model.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.model.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.model.Waypoint
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookSection
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

    @Composable
    private fun FocusedRoadbookStub(modifier: Modifier = Modifier) {
        val focusRequester = remember { FocusRequester() }
        Box(
            modifier = modifier
                .testTag("RoadbookStub")
                .focusRequester(focusRequester)
                .focusable()
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun displaysOdometerValues() {
        val uiState = DashboardUiState(
            odometer = Odometer(1200.0, 500.0),
        )
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(uiState)
        
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookSlot = { modifier -> FocusedRoadbookStub(modifier) },
                    mapSlot = { Box(it) }
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
        val uiState = DashboardUiState(
            odometer = Odometer(0.0, 500.0),
        )
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(uiState)
        
        val expectedPartial = String.format(Locale.getDefault(), "%.2f", 500.0 / 1000.0)
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookSlot = { modifier -> FocusedRoadbookStub(modifier) },
                    mapSlot = { Box(it) }
                )
            }
        }

        composeTestRule.onNodeWithText(expectedPartial).performTouchInput {
            longClick()
        }

        verify { viewModel.showSetPartialDialog() }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun tabletPortrait_displaysMapSection() {
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(DashboardUiState())
        
        val mapTag = "MapSectionTag"
        val mapDummyText = "MAP_CONTENT_DUMMY"
        // Medium width and Tall height
        val size = DpSize(700.dp, 1000.dp)
        val windowSizeClass = WindowSizeClass.calculateFromSize(size)

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookSlot = { modifier -> FocusedRoadbookStub(modifier) },
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
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(DashboardUiState())
        
        val mapTag = "MapSectionTag"
        val mapDummyText = "MAP_CONTENT_DUMMY"
        // Phone Portrait size
        val size = DpSize(411.dp, 891.dp)
        val windowSizeClass = WindowSizeClass.calculateFromSize(size)

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookSlot = { modifier -> FocusedRoadbookStub(modifier) },
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
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        every { viewModel.uiState } returns uiStateFlow
        
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookSlot = { modifier -> FocusedRoadbookStub(modifier) },
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
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        every { viewModel.uiState } returns uiStateFlow
        
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookSlot = { modifier -> FocusedRoadbookStub(modifier) },
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
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        every { viewModel.uiState } returns uiStateFlow
        
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookSlot = { modifier -> FocusedRoadbookStub(modifier) },
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
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        every { viewModel.uiState } returns uiStateFlow
        
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookSlot = { modifier ->
                        Text(text = context.getString(RoadbookR.string.main_no_route), modifier = modifier)
                    }
                )
            }
        }

        val expectedMessage = context.getString(RoadbookR.string.main_no_route)
        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun roadbookAndOdometerKeys_areCorrectlyRouted() {
        val waypoints = List(10) { i ->
            Waypoint(
                number = i + 1,
                coordinates = Coordinates(0.0, 0.0),
                distance = Distance(i * 1000L),
                distanceFromPrevious = Distance(1000L)
            )
        }
        val route = Route(name = "Integration Test", waypoints = waypoints)
        
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val roadbookViewModel: RoadbookViewModel = mockk(relaxed = true)
        
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        val roadbookStateFlow = MutableStateFlow<RoadbookUiState>(RoadbookUiState.Success(route))
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
                    roadbookSlot = { modifier ->
                        RoadbookSection(
                            viewModel = roadbookViewModel,
                            modifier = modifier,
                            onSetPartialClick = {}
                        )
                    }
                )
            }
        }

        // --- Step 1 & 2: Roadbook Navigation (DPAD_UP) ---
        // Initially waypoint 1 is displayed
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
        
        // Press DPAD_UP. The roadbook component (focused by default) should handle it.
        composeTestRule.onNodeWithTag("RoadbookList").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_DPAD_UP
                )
            )
        )
        composeTestRule.waitForIdle()

        // Waypoint 2 should now be visible (scrolled to)
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
        // Waypoint 1 should be scrolled out and not exist in the semantics tree
        composeTestRule.onNodeWithText("1").assertDoesNotExist()
        
        // --- Step 3: Odometer Integration (VOLUME_UP) ---
        // The roadbook doesn't handle VOLUME_UP, so it should bubble up to the dashboard.
        composeTestRule.onNodeWithTag("RoadbookList").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_VOLUME_UP
                )
            )
        )
        
        // Verify dashboard logic was triggered exactly once (bubbled up)
        verify(exactly = 1) { viewModel.incrementPartialDistance() }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun directionalKeys_triggerOdometerActions() {
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        every { viewModel.uiState } returns uiStateFlow
        
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookSlot = { modifier -> FocusedRoadbookStub(modifier) },
                )
            }
        }

        composeTestRule.onNodeWithTag("MainScreen").performClick()

        // Test DPAD_RIGHT
        composeTestRule.onNodeWithTag("MainScreen").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_DPAD_RIGHT
                )
            )
        )
        verify { viewModel.incrementPartialDistance() }

        // Test DPAD_LEFT
        composeTestRule.onNodeWithTag("MainScreen").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_DPAD_LEFT
                )
            )
        )
        verify { viewModel.decrementPartialDistance() }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun mediaKeys_triggerOdometerActions() {
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        every { viewModel.uiState } returns uiStateFlow
        
        val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))

        composeTestRule.setContent {
            RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
                DashboardScreen(
                    windowSizeClass = windowSizeClass,
                    onSettingsClick = {},
                    viewModel = viewModel,
                    roadbookSlot = { modifier -> FocusedRoadbookStub(modifier) },
                )
            }
        }

        composeTestRule.onNodeWithTag("MainScreen").performClick()

        val mediaKeys = listOf(
            android.view.KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
            android.view.KeyEvent.KEYCODE_MEDIA_PLAY,
            android.view.KeyEvent.KEYCODE_MEDIA_PAUSE
        )

        mediaKeys.forEach { keyCode ->
            composeTestRule.onNodeWithTag("MainScreen").performKeyPress(
                KeyEvent(
                    nativeKeyEvent = android.view.KeyEvent(
                        android.view.KeyEvent.ACTION_DOWN,
                        keyCode
                    )
                )
            )
        }

        verify(exactly = 3) { viewModel.resetPartialDistance() }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun focus_verifiesRoadbookRetainsPriorityForDirectionalKeys() {
        val waypoints = List(5) { i ->
            Waypoint(
                number = i + 1,
                coordinates = Coordinates(0.0, 0.0),
                distance = Distance(i * 1000L),
                distanceFromPrevious = Distance(1000L)
            )
        }
        val route = Route(name = "Focus Test", waypoints = waypoints)
        
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val roadbookViewModel: RoadbookViewModel = mockk(relaxed = true)
        
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        val roadbookStateFlow = MutableStateFlow<RoadbookUiState>(RoadbookUiState.Success(route))
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
                    roadbookSlot = { modifier ->
                        RoadbookSection(
                            viewModel = roadbookViewModel,
                            modifier = modifier,
                            onSetPartialClick = {}
                        )
                    }
                )
            }
        }

        // Send DPAD_UP to RoadbookList
        composeTestRule.onNodeWithTag("RoadbookList").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_DPAD_UP
                )
            )
        )
        composeTestRule.waitForIdle()

        // Verify Waypoint 2 is displayed (handled by Roadbook)
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
        
        // Verify it did NOT reach the dashboard's Odometer logic (even if it were mapped there)
        verify(exactly = 0) { viewModel.incrementPartialDistance() }
        verify(exactly = 0) { viewModel.decrementPartialDistance() }
        verify(exactly = 0) { viewModel.resetPartialDistance() }
    }
}
