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

package org.giste.roadbooknavigator.core.ui.components

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.junit.Rule
import org.junit.Test

class RoadbookTextTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun roadbookAutoSizeText_rendersCorrectText() {
        val testText = "123.45"
        val testTag = "AutoSizeTextTag"

        composeTestRule.setContent {
            RoadbookNavigatorTheme(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
            ) {
                RoadbookAutoSizeText(
                    text = testText,
                    modifier = Modifier.testTag(testTag)
                )
            }
        }

        composeTestRule.onNodeWithTag(testTag)
            .assertIsDisplayed()
            .assertTextEquals(testText)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @Test
    fun roadbookAutoSizeText_appliesCustomStyling() {
        val testText = "999.99"
        val testTag = "StyledTextTag"

        composeTestRule.setContent {
            RoadbookNavigatorTheme(
                windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
            ) {
                RoadbookAutoSizeText(
                    text = testText,
                    modifier = Modifier.testTag(testTag),
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    maxFontSize = 50.sp
                )
            }
        }

        composeTestRule.onNodeWithTag(testTag)
            .assertIsDisplayed()
            .assertTextEquals(testText)
    }
}
