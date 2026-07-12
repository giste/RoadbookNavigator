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

package org.giste.roadbooknavigator.features.map.ui.management

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.giste.roadbooknavigator.features.map.domain.model.DownloadedMapInfo
import org.giste.roadbooknavigator.features.map.domain.model.DownloadedMapStatus
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapManagementScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenStateIsLoading_thenProgressIndicatorIsShown() {
        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementContent(
                        uiState = MapManagementUiState.Loading,
                        onDownloadClick = {},
                        onDeleteClick = {},
                        onCancelDownloadClick = {}
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("MapManagementLoading").assertIsDisplayed()
    }

    @Test
    fun whenStateIsError_thenErrorMessageIsShown() {
        val errorMessage = "Fatal error"
        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementContent(
                        uiState = MapManagementUiState.Error(errorMessage),
                        onDownloadClick = {},
                        onDeleteClick = {},
                        onCancelDownloadClick = {}
                    )
                }
            }
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun whenStateIsSuccess_thenMapListsAreShown() {
        val downloadedMap = MapFile("Spain", "/path/spain.map", 100L, 0L, "Europe")
        val remoteMap = RemoteMapFile("France", "/", "http://france.map", 200L, 0L)
        
        val state = MapManagementUiState.Success(
            downloadedMaps = listOf(DownloadedMapInfo(downloadedMap, DownloadedMapStatus.UpToDate)),
            remoteFolders = RemoteMapFolder("Main Folder", "/", maps = listOf(remoteMap))
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementContent(
                        uiState = state,
                        onDownloadClick = {},
                        onDeleteClick = {},
                        onCancelDownloadClick = {}
                    )
                }
            }
        }

        // Expand sections to see maps
        composeTestRule.onNodeWithTag("SectionHeader_DownloadedMaps").performClick()
        composeTestRule.onNodeWithTag("SectionHeader_Main Folder").performClick()

        composeTestRule.onNodeWithText("Spain").assertIsDisplayed()
        composeTestRule.onNodeWithText("France").assertIsDisplayed()
        composeTestRule.onNodeWithTag("SectionHeader_DownloadedMaps").assertIsDisplayed()
        composeTestRule.onNodeWithTag("SectionHeader_Main Folder").assertIsDisplayed()
    }

    @Test
    fun whenDownloadClick_thenCallbackIsInvoked() {
        var clickedMap: RemoteMapFile? = null
        val remoteMap = RemoteMapFile("France", "/", "http://france.map", 200L, 0L)
        val state = MapManagementUiState.Success(
            downloadedMaps = emptyList(),
            remoteFolders = RemoteMapFolder("Main Folder", "/", maps = listOf(remoteMap))
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementContent(
                        uiState = state,
                        onDownloadClick = { clickedMap = it },
                        onDeleteClick = {},
                        onCancelDownloadClick = {}
                    )
                }
            }
        }

        // Expand section to see the button
        composeTestRule.onNodeWithTag("SectionHeader_Main Folder").performClick()

        composeTestRule.onNodeWithTag("DownloadMapButton").performClick()

        assert(clickedMap == remoteMap)
    }

    @Test
    fun whenDeleteClick_thenCallbackIsInvoked() {
        var clickedMap: MapFile? = null
        val downloadedMap = MapFile("Spain", "/path/spain.map", 100L, 0L, "Europe")
        val state = MapManagementUiState.Success(
            downloadedMaps = listOf(DownloadedMapInfo(downloadedMap, DownloadedMapStatus.UpToDate)),
            remoteFolders = RemoteMapFolder("Main Folder", "/")
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementContent(
                        uiState = state,
                        onDownloadClick = {},
                        onDeleteClick = { clickedMap = it },
                        onCancelDownloadClick = {}
                    )
                }
            }
        }

        // Expand section to see the button
        composeTestRule.onNodeWithTag("SectionHeader_DownloadedMaps").performClick()

        composeTestRule.onNodeWithTag("DeleteMapButton").performClick()

        assert(clickedMap == downloadedMap)
    }

    @Test
    fun whenSectionTapped_thenItCollapsesAndExpands() {
        val downloadedMap = MapFile("Spain", "/path/spain.map", 100L, 0L, "Europe")
        val state = MapManagementUiState.Success(
            downloadedMaps = listOf(DownloadedMapInfo(downloadedMap, DownloadedMapStatus.UpToDate)),
            remoteFolders = RemoteMapFolder("Main Folder", "/")
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementContent(
                        uiState = state,
                        onDownloadClick = {},
                        onDeleteClick = {},
                        onCancelDownloadClick = {}
                    )
                }
            }
        }

        // Initially collapsed (due to new behavior)
        composeTestRule.onNodeWithText("Spain").assertDoesNotExist()

        // Tap to expand
        composeTestRule.onNodeWithTag("SectionHeader_DownloadedMaps").performClick()
        composeTestRule.onNodeWithText("Spain").assertIsDisplayed()

        // Tap to collapse
        composeTestRule.onNodeWithTag("SectionHeader_DownloadedMaps").performClick()
        composeTestRule.onNodeWithText("Spain").assertDoesNotExist()
    }
}
