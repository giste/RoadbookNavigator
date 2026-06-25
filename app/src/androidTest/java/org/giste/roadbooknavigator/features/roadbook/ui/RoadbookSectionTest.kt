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

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import org.giste.roadbooknavigator.R
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.junit.Rule
import org.junit.Test

class RoadbookSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

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
}
