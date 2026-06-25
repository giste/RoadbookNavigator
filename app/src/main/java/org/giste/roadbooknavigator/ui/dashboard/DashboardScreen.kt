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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookSection
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookUiState
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookViewModel

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    roadbookViewModel: RoadbookViewModel = hiltViewModel(),
) {
    val roadbookState by roadbookViewModel.uiState.collectAsState()

    // We use the route name as a key to reset the scroll state when a new roadbook is loaded
    val routeKey = (roadbookState as? RoadbookUiState.Success)?.route?.name ?: ""

    val roadbookListState = key(routeKey) {
        val successState = roadbookState as? RoadbookUiState.Success
        rememberLazyListState(
            initialFirstVisibleItemIndex = successState?.initialIndex ?: 0,
            initialFirstVisibleItemScrollOffset = successState?.initialOffset ?: 0
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Roadbook Section
            RoadbookSection(
                state = roadbookState,
                listState = roadbookListState,
                onFileSelected = { inputStream ->
                    roadbookViewModel.onFileSelected(inputStream)
                },
                onSetPartialClick = { /* TODO: Implement odometer logic */ },
                onWaypointVisible = { index, offset ->
                    roadbookViewModel.onWaypointVisible(index, offset)
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
