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

package org.giste.roadbooknavigator.features.roadbook.ui

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.longClick
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.platform.app.InstrumentationRegistry
import org.giste.roadbooknavigator.features.roadbook.R
import org.giste.roadbooknavigator.features.roadbook.domain.model.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.model.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.model.ShortDistanceThreshold
import org.giste.roadbooknavigator.features.roadbook.domain.model.Waypoint
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RoadbookUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun loadingState_displaysLoadingIndicator() {
        composeTestRule.setContent {
            RoadbookSection(
                state = RoadbookUiState.Loading,
                listState = rememberLazyListState(),
                onFileSelected = {},
                onSetPartialClick = {},
                onWaypointVisible = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithTag("LoadingIndicator").assertIsDisplayed()
    }

    @Test
    fun emptyState_displaysImportButton() {
        composeTestRule.setContent {
            RoadbookSection(
                state = RoadbookUiState.Empty,
                listState = rememberLazyListState(),
                onFileSelected = {},
                onSetPartialClick = {},
                onWaypointVisible = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithText(context.getString(R.string.main_no_route)).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.action_import)).assertIsDisplayed()
    }

    @Test
    fun errorState_displaysErrorMessageAndImportButton() {
        val errorMessage = "Test Error"
        composeTestRule.setContent {
            RoadbookSection(
                state = RoadbookUiState.Error(errorMessage),
                listState = rememberLazyListState(),
                onFileSelected = {},
                onSetPartialClick = {},
                onWaypointVisible = { _, _ -> }
            )
        }

        val expectedMessage = context.getString(R.string.main_error_prefix, errorMessage)
        composeTestRule.onNodeWithText(expectedMessage).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.action_import)).assertIsDisplayed()
    }

    @Test
    fun successState_displaysImportFab() {
        val route = Route(name = "Test Route", waypoints = emptyList())
        composeTestRule.setContent {
            RoadbookSection(
                state = RoadbookUiState.Success(route),
                listState = rememberLazyListState(),
                onFileSelected = {},
                onSetPartialClick = {},
                onWaypointVisible = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.action_import)).assertIsDisplayed()
    }

    @Test
    fun clickingImportButton_launchesGetContentIntent() {
        // Stub the intent so it doesn't actually open the system picker
        val resultIntent = Intent()
        val result = Instrumentation.ActivityResult(Activity.RESULT_CANCELED, resultIntent)
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result)

        composeTestRule.setContent {
            RoadbookSection(
                state = RoadbookUiState.Empty,
                listState = rememberLazyListState(),
                onFileSelected = {},
                onSetPartialClick = {},
                onWaypointVisible = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.action_import)).performClick()

        // Verify that the intent with ACTION_GET_CONTENT was indeed launched
        intended(hasAction(Intent.ACTION_GET_CONTENT))
    }

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
        var capturedDistance = -1.0
        val route = Route(name = "Test", waypoints = listOf(waypoint))

        composeTestRule.setContent {
            RoadbookSection(
                state = RoadbookUiState.Success(route),
                listState = rememberLazyListState(),
                onFileSelected = {},
                onSetPartialClick = { capturedDistance = it },
                onWaypointVisible = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithTag("WaypointDistanceInfo_1").performTouchInput {
            longClick()
        }

        assert(capturedDistance == distance.toDouble())
    }

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
        var capturedDistance = -1.0
        val route = Route(name = "Test", waypoints = listOf(waypoint))

        composeTestRule.setContent {
            RoadbookSection(
                state = RoadbookUiState.Success(route),
                listState = rememberLazyListState(),
                onFileSelected = {},
                onSetPartialClick = { capturedDistance = it },
                onWaypointVisible = { _, _ -> }
            )
        }

        composeTestRule.onNodeWithTag("WaypointDistanceInfo_1").performTouchInput {
            longClick()
        }

        assert(capturedDistance == 0.0)
    }

    @Test
    fun shortDistanceThreshold_appliesHighlighting() {
        val shortThreshold = 300L
        val shortDistance = 250L
        val normalDistance = 400L
        
        val waypointShort = Waypoint(
            number = 1,
            coordinates = Coordinates(0.0, 0.0),
            distance = Distance(shortDistance),
            distanceFromPrevious = Distance(shortDistance)
        )
        val waypointNormal = Waypoint(
            number = 2,
            coordinates = Coordinates(0.0, 0.01),
            distance = Distance(shortDistance + normalDistance),
            distanceFromPrevious = Distance(normalDistance)
        )
        
        val route = Route(name = "Test", waypoints = listOf(waypointShort, waypointNormal))

        composeTestRule.setContent {
            RoadbookSection(
                state = RoadbookUiState.Success(
                    route = route,
                    shortDistanceThreshold = ShortDistanceThreshold(shortThreshold)
                ),
                listState = rememberLazyListState(),
                onFileSelected = {},
                onSetPartialClick = {},
                onWaypointVisible = { _, _ -> }
            )
        }

        // Verify that the first waypoint (short) has the highlight tag
        composeTestRule.onNodeWithTag("ShortDistanceHighlight").assertIsDisplayed()
        
        // Verify that there is only one node with that highlight tag
        composeTestRule.onAllNodesWithTag("ShortDistanceHighlight").assertCountEquals(1)
    }
}
