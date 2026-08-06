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

package org.giste.roadbooknavigator.features.roadbook

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookContent
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookUiState
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookViewModel

/**
 * The main UI entry point for the Roadbook module.
 *
 * @param onControllerReady Callback providing a [RoadbookController] to the consumer for external control.
 * @param modifier Modifier for the root container.
 */
@Composable
public fun Roadbook(
    onControllerReady: (RoadbookController) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Phase 2 will make RoadbookViewModel internal, for now we just use it.
    val viewModel: RoadbookViewModel = hiltViewModel()
    val state by viewModel.roadbookState.collectAsStateWithLifecycle()
    val initialPosition by viewModel.initialScrollPosition.collectAsStateWithLifecycle()

    // Pass the controller back to the consumer
    LaunchedEffect(viewModel) {
        onControllerReady(viewModel)
    }

    // LazyListState management (moved from RoadbookSection)
    val routeKey = (state as? RoadbookUiState.Success)?.let {
        "route_${it.route.name}_${it.route.waypoints.size}"
    }

    val listState = androidx.compose.runtime.saveable.rememberSaveable(routeKey, saver = LazyListState.Saver) {
        LazyListState(
            firstVisibleItemIndex = initialPosition.index,
            firstVisibleItemScrollOffset = initialPosition.offset
        )
    }

    LaunchedEffect(initialPosition) {
        if (listState.firstVisibleItemIndex != initialPosition.index ||
            listState.firstVisibleItemScrollOffset != initialPosition.offset
        ) {
            listState.animateScrollToItem(initialPosition.index, initialPosition.offset)
        }
    }

    RoadbookContent(
        state = state,
        listState = listState,
        modifier = modifier,
        onFileSelected = viewModel::importRoute,
        onSetPartialClick = viewModel::onDistanceSectionLongPressed,
        onWaypointVisible = viewModel::onWaypointVisible
    )
}
