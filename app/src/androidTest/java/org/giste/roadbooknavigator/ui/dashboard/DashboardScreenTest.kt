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

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.giste.roadbooknavigator.features.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.roadbook.domain.model.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.model.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.model.Waypoint
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookUiState
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
    fun roadbookEmpty_displaysNoRouteMessage() {
        val uiState = DashboardUiState(
            roadbook = RoadbookUiState.Empty,
            odometer = Odometer(0.0, 0.0),
        )

        composeTestRule.setContent {
            MainContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                uiState = uiState,
                listState = rememberLazyListState(),
                onSetPartialClick = {},
                onLongClickPartial = {},
                onSettingsClick = {},
                onWaypointVisible = { _, _ -> },
                onFileSelected = {},
            )
        }

        val expectedMessage = context.getString(RoadbookR.string.main_no_route)
        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun displaysOdometerValues() {
        val uiState = DashboardUiState(
            roadbook = RoadbookUiState.Success(
                route = Route(name = "Test Route", waypoints = emptyList())
            ),
            odometer = Odometer(1200.0, 500.0),
        )

        composeTestRule.setContent {
            MainContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                uiState = uiState,
                listState = rememberLazyListState(),
                onSetPartialClick = {},
                onLongClickPartial = {},
                onSettingsClick = {},
                onWaypointVisible = { _, _ -> },
                onFileSelected = {},
            )
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
            roadbook = RoadbookUiState.Empty,
            odometer = Odometer(0.0, 500.0),
        )
        val expectedPartial = String.format(Locale.getDefault(), "%.2f", 500.0 / 1000.0)

        composeTestRule.setContent {
            MainContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                uiState = uiState,
                listState = rememberLazyListState(),
                onSetPartialClick = {},
                onLongClickPartial = { longClickTriggered = true },
                onSettingsClick = {},
                onWaypointVisible = { _, _ -> },
                onFileSelected = {},
            )
        }

        composeTestRule.onNodeWithText(expectedPartial).performTouchInput {
            longClick()
        }

        assert(longClickTriggered)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun roadbookLoading_displaysLoadingIndicator() {
        val uiState = DashboardUiState(
            roadbook = RoadbookUiState.Loading,
            odometer = Odometer(0.0, 0.0),
        )

        composeTestRule.setContent {
            MainContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                uiState = uiState,
                listState = rememberLazyListState(),
                onSetPartialClick = {},
                onLongClickPartial = {},
                onSettingsClick = {},
                onWaypointVisible = { _, _ -> },
                onFileSelected = {},
            )
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun roadbookError_displaysErrorMessage() {
        val errorMessage = "Failed to load route"
        val uiState = DashboardUiState(
            roadbook = RoadbookUiState.Error(errorMessage),
            odometer = Odometer(0.0, 0.0),
        )

        composeTestRule.setContent {
            MainContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                uiState = uiState,
                listState = rememberLazyListState(),
                onSetPartialClick = {},
                onLongClickPartial = {},
                onSettingsClick = {},
                onWaypointVisible = { _, _ -> },
                onFileSelected = {},
            )
        }

        val expectedMessage = context.getString(RoadbookR.string.main_error_prefix, errorMessage)
        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun tabletPortrait_displaysMapSection() {
        val uiState = DashboardUiState(
            roadbook = RoadbookUiState.Empty,
            odometer = Odometer(0.0, 0.0),
        )

        composeTestRule.setContent {
            MainContent(
                // Tablet Portrait: Medium Width (686dp), Expanded Height (1097dp)
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(686.dp, 1097.dp)),
                uiState = uiState,
                listState = rememberLazyListState(),
                onSetPartialClick = {},
                onLongClickPartial = {},
                onSettingsClick = {},
                onWaypointVisible = { _, _ -> },
                onFileSelected = {},
            )
        }

        val mapDescription = context.getString(RoadbookR.string.content_description_map)
        composeTestRule.onNodeWithContentDescription(mapDescription).assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun phonePortrait_doesNotDisplayMapSection() {
        val uiState = DashboardUiState(
            roadbook = RoadbookUiState.Empty,
            odometer = Odometer(0.0, 0.0),
        )

        composeTestRule.setContent {
            MainContent(
                // Phone Portrait: Compact Width (411dp), Expanded Height (891dp)
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                uiState = uiState,
                listState = rememberLazyListState(),
                onSetPartialClick = {},
                onLongClickPartial = {},
                onSettingsClick = {},
                onWaypointVisible = { _, _ -> },
                onFileSelected = {},
            )
        }

        val mapDescription = context.getString(RoadbookR.string.content_description_map)
        composeTestRule.onAllNodesWithContentDescription(mapDescription).assertCountEquals(0)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun volumeUpKey_triggersIncrementPartialDistance() {
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        every { viewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            DashboardContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                onSettingsClick = {},
                viewModel = viewModel
            )
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

        composeTestRule.setContent {
            DashboardContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                onSettingsClick = {},
                viewModel = viewModel
            )
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

        composeTestRule.setContent {
            DashboardContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                onSettingsClick = {},
                viewModel = viewModel
            )
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
    fun dpadUpKey_triggersScrollDown() {
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(DashboardUiState())
        every { viewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            DashboardContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                onSettingsClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("MainScreen").performClick()
        
        composeTestRule.onNodeWithTag("MainScreen").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_DPAD_UP
                )
            )
        )
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun dashboardContent_displaysMainContent() {
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(
            DashboardUiState(
                roadbook = RoadbookUiState.Empty
            )
        )
        every { viewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            DashboardContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                onSettingsClick = {},
                viewModel = viewModel
            )
        }

        val expectedMessage = context.getString(RoadbookR.string.main_no_route)
        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun directionUpKey_scrollsToNextWaypoint() {
        val waypoints = List(10) { i ->
            Waypoint(
                number = i + 1,
                coordinates = Coordinates(0.0, 0.0),
                distance = Distance(i * 1000L),
                distanceFromPrevious = Distance(1000L)
            )
        }
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(
            DashboardUiState(
                roadbook = RoadbookUiState.Success(Route(name = "Test", waypoints = waypoints))
            )
        )
        every { viewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            DashboardContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                onSettingsClick = {},
                viewModel = viewModel
            )
        }

        // Initially waypoint 1 is displayed
        composeTestRule.onNodeWithText("1").assertIsDisplayed()

        // Press Direction Up (Forward)
        composeTestRule.onNodeWithTag("MainScreen").performClick()
        composeTestRule.onNodeWithTag("MainScreen").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_DPAD_UP
                )
            )
        )

        composeTestRule.waitForIdle()

        // Waypoint 2 should now be visible
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun directionDownKey_scrollsToPreviousWaypoint() {
        val waypoints = List(10) { i ->
            Waypoint(
                number = i + 1,
                coordinates = Coordinates(0.0, 0.0),
                distance = Distance(i * 1000L),
                distanceFromPrevious = Distance(1000L)
            )
        }
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        // Start at waypoint 3
        val uiStateFlow = MutableStateFlow(
            DashboardUiState(
                roadbook = RoadbookUiState.Success(Route(name = "Test", waypoints = waypoints)),
                initialScrollPosition = RoadbookPosition(2, 0)
            )
        )
        every { viewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            DashboardContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                onSettingsClick = {},
                viewModel = viewModel
            )
        }

        // Initially waypoint 3 is displayed
        composeTestRule.onNodeWithText("3").assertIsDisplayed()

        // Press Direction Down (Backward)
        composeTestRule.onNodeWithTag("MainScreen").performClick()
        composeTestRule.onNodeWithTag("MainScreen").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_DPAD_DOWN
                )
            )
        )

        composeTestRule.waitForIdle()

        // Waypoint 2 should now be visible
        composeTestRule.onNodeWithText("2").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun directionDownKey_whenPartiallyScrolled_snapsToCurrentWaypoint() {
        val waypoints = List(10) { i ->
            Waypoint(
                number = i + 1,
                coordinates = Coordinates(0.0, 0.0),
                distance = Distance(i * 1000L),
                distanceFromPrevious = Distance(1000L)
            )
        }
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(
            DashboardUiState(
                roadbook = RoadbookUiState.Success(Route(name = "Test", waypoints = waypoints))
            )
        )
        every { viewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            DashboardContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                onSettingsClick = {},
                viewModel = viewModel
            )
        }

        // Manually scroll partially
        composeTestRule.onNodeWithTag("RoadbookList").performTouchInput {
            swipeUp(startY = 500f, endY = 400f) // Small swipe up to leave waypoint 1 partially visible
        }

        composeTestRule.waitForIdle()

        // Press Direction Down (Backward)
        composeTestRule.onNodeWithTag("MainScreen").performClick()
        composeTestRule.onNodeWithTag("MainScreen").performKeyPress(
            KeyEvent(
                nativeKeyEvent = android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_DPAD_DOWN
                )
            )
        )

        composeTestRule.waitForIdle()

        // Should still show waypoint 1 (snapped to top)
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun longClickOnWaypointDistance_setsPartialDistance() {
        val distance = 1500L
        val waypoint = Waypoint(
            number = 1,
            coordinates = Coordinates(0.0, 0.0),
            distance = Distance(distance),
            distanceFromPrevious = Distance(distance),
            reset = false
        )
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(
            DashboardUiState(
                roadbook = RoadbookUiState.Success(Route(name = "Test", waypoints = listOf(waypoint)))
            )
        )
        every { viewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            DashboardContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                onSettingsClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("WaypointDistanceInfo_1").performTouchInput {
            longClick()
        }

        verify { viewModel.setPartialDistance(distance.toDouble()) }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun longClickOnResetWaypointDistance_resetsPartialDistance() {
        val distance = 1500L
        val waypoint = Waypoint(
            number = 1,
            coordinates = Coordinates(0.0, 0.0),
            distance = Distance(distance),
            distanceFromPrevious = Distance(distance),
            reset = true
        )
        val viewModel: DashboardViewModel = mockk(relaxed = true)
        val uiStateFlow = MutableStateFlow(
            DashboardUiState(
                roadbook = RoadbookUiState.Success(Route(name = "Test", waypoints = listOf(waypoint)))
            )
        )
        every { viewModel.uiState } returns uiStateFlow

        composeTestRule.setContent {
            DashboardContent(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
                onSettingsClick = {},
                viewModel = viewModel
            )
        }

        composeTestRule.onNodeWithTag("WaypointDistanceInfo_1").performTouchInput {
            longClick()
        }

        verify { viewModel.setPartialDistance(0.0) }
    }
}
