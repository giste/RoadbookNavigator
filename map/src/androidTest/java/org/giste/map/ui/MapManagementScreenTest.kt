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

package org.giste.map.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import org.giste.map.MapManagementScreen
import org.giste.map.MapManagementState
import org.giste.map.compactMapDimensions
import org.giste.map.domain.model.DownloadedMapInfo
import org.giste.map.domain.model.DownloadedMapStatus
import org.giste.map.domain.model.MapFile
import org.giste.map.domain.model.RemoteMapFile
import org.giste.map.domain.model.RemoteMapFolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapManagementScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenStateIsLoading_thenProgressIndicatorIsShown() {
        val state = MapManagementState(
            internals = MapManagementState.InternalData(
                uiState = MutableStateFlow(MapManagementUiState.Loading),
                onDownloadClick = {},
                onDeleteClick = {},
                onCancelDownloadClick = {}
            )
        )
        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementScreen(
                        state = state,
                        dimensions = compactMapDimensions
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("MapManagementLoading").assertIsDisplayed()
    }

    @Test
    fun whenStateIsError_thenErrorMessageIsShown() {
        val errorMessage = "Fatal error"
        val state = MapManagementState(
            internals = MapManagementState.InternalData(
                uiState = MutableStateFlow(MapManagementUiState.Error(errorMessage)),
                onDownloadClick = {},
                onDeleteClick = {},
                onCancelDownloadClick = {}
            )
        )
        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementScreen(
                        state = state,
                        dimensions = compactMapDimensions
                    )
                }
            }
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun whenStateIsSuccess_thenMapListsAreShown() {
        val downloadedMap = MapFile("Spain", "/path/spain.map", 100L, 0L, "Europe")
        val remoteMapDownloaded = RemoteMapFile("Spain", "Europe", "http://spain.map", 100L, 0L)
        val remoteMap = RemoteMapFile("France", "/", "http://france.map", 200L, 0L)

        val uiState = MapManagementUiState.Success(
            downloadedMaps = listOf(
                DownloadedMapInfo(
                    downloadedMap,
                    DownloadedMapStatus.UpToDate(remoteMapDownloaded)
                )
            ),
            remoteFolders = listOf(RemoteMapFolder("Europe", "/", maps = listOf(remoteMap)))
        )

        val state = MapManagementState(
            internals = MapManagementState.InternalData(
                uiState = MutableStateFlow(uiState),
                onDownloadClick = {},
                onDeleteClick = {},
                onCancelDownloadClick = {}
            )
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementScreen(
                        state = state,
                        dimensions = compactMapDimensions
                    )
                }
            }
        }

        // Expand sections to see maps
        composeTestRule.onNodeWithTag("SectionHeader_DownloadedMaps").performClick()
        composeTestRule.onNodeWithTag("SectionHeader_Europe").performClick()

        composeTestRule.onNodeWithText("Spain").assertIsDisplayed()
        composeTestRule.onNodeWithText("France").assertIsDisplayed()
        composeTestRule.onNodeWithTag("SectionHeader_DownloadedMaps").assertIsDisplayed()
        composeTestRule.onNodeWithTag("SectionHeader_Europe").assertIsDisplayed()
    }

    @Test
    fun whenDownloadClick_thenCallbackIsInvoked() {
        var clickedMap: RemoteMapFile? = null
        val remoteMap = RemoteMapFile("France", "/", "http://france.map", 200L, 0L)
        val uiState = MapManagementUiState.Success(
            downloadedMaps = emptyList(),
            remoteFolders = listOf(RemoteMapFolder("Europe", "/", maps = listOf(remoteMap)))
        )

        val state = MapManagementState(
            internals = MapManagementState.InternalData(
                uiState = MutableStateFlow(uiState),
                onDownloadClick = { clickedMap = it },
                onDeleteClick = {},
                onCancelDownloadClick = {}
            )
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementScreen(
                        state = state,
                        dimensions = compactMapDimensions
                    )
                }
            }
        }

        // Expand section to see the button
        composeTestRule.onNodeWithTag("SectionHeader_Europe").performClick()

        composeTestRule.onNodeWithTag("DownloadMapButton").performClick()

        assert(clickedMap == remoteMap)
    }

    @Test
    fun whenDeleteClick_thenCallbackIsInvoked() {
        var clickedMap: MapFile? = null
        val downloadedMap = MapFile("Spain", "/path/spain.map", 100L, 0L, "Europe")
        val remoteMapDownloaded = RemoteMapFile("Spain", "Europe", "http://spain.map", 100L, 0L)
        val uiState = MapManagementUiState.Success(
            downloadedMaps = listOf(
                DownloadedMapInfo(
                    downloadedMap,
                    DownloadedMapStatus.UpToDate(remoteMapDownloaded)
                )
            ),
            remoteFolders = emptyList()
        )

        val state = MapManagementState(
            internals = MapManagementState.InternalData(
                uiState = MutableStateFlow(uiState),
                onDownloadClick = {},
                onDeleteClick = { clickedMap = it },
                onCancelDownloadClick = {}
            )
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementScreen(
                        state = state,
                        dimensions = compactMapDimensions
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
        val remoteMapDownloaded = RemoteMapFile("Spain", "Europe", "http://spain.map", 100L, 0L)
        val uiState = MapManagementUiState.Success(
            downloadedMaps = listOf(
                DownloadedMapInfo(
                    downloadedMap,
                    DownloadedMapStatus.UpToDate(remoteMapDownloaded)
                )
            ),
            remoteFolders = emptyList()
        )

        val state = MapManagementState(
            internals = MapManagementState.InternalData(
                uiState = MutableStateFlow(uiState),
                onDownloadClick = {},
                onDeleteClick = {},
                onCancelDownloadClick = {}
            )
        )

        composeTestRule.setContent {
            MaterialTheme {
                Surface {
                    MapManagementScreen(
                        state = state,
                        dimensions = compactMapDimensions
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