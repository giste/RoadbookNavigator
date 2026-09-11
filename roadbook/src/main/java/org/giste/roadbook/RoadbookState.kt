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

package org.giste.roadbook

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import org.giste.roadbook.domain.model.RoadbookPosition
import org.giste.roadbook.ui.RoadbookUiState
import org.giste.roadbook.ui.RoadbookViewModel
import java.io.InputStream

/**
 * State object for the Roadbook module, following the State Hoisting pattern.
 * This class acts as a public proxy for the internal navigation logic.
 */
@Stable
public class RoadbookState internal constructor(
    public val routeName: StateFlow<String?>,
    public val events: Flow<RoadbookEvent>,
    private val onScrollUp: () -> Unit,
    private val onScrollDown: () -> Unit,
    // Grouped internal implementation details to keep the constructor manageable.
    internal val internals: InternalData
) {
    internal class InternalData(
        val roadbookUiState: StateFlow<RoadbookUiState>,
        val initialScrollPosition: StateFlow<RoadbookPosition>,
        val onImportRoute: (InputStream) -> Unit,
        val onDistanceSectionLongPressed: (Double) -> Unit,
        val onWaypointVisible: (Int, Int) -> Unit,
        val viewModel: RoadbookViewModel? = null // For backward compatibility
    )

    internal constructor(viewModel: RoadbookViewModel) : this(
        routeName = viewModel.routeName,
        events = viewModel.events,
        onScrollUp = viewModel::scrollUp,
        onScrollDown = viewModel::scrollDown,
        internals = InternalData(
            roadbookUiState = viewModel.roadbookState,
            initialScrollPosition = viewModel.initialScrollPosition,
            onImportRoute = viewModel::importRoute,
            onDistanceSectionLongPressed = viewModel::onDistanceSectionLongPressed,
            onWaypointVisible = viewModel::onWaypointVisible,
            viewModel = viewModel
        )
    )

    /**
     * Commands the roadbook to move forward to the next waypoint.
     */
    public fun scrollUp() {
        onScrollUp()
    }

    /**
     * Commands the roadbook to move backward to the previous waypoint.
     */
    public fun scrollDown() {
        onScrollDown()
    }
}

/**
 * Factory function to create a [RoadbookState] for previews or testing.
 */
public fun RoadbookState(
    routeName: StateFlow<String?> = MutableStateFlow(null),
    events: Flow<RoadbookEvent> = emptyFlow(),
    onScrollUp: () -> Unit = {},
    onScrollDown: () -> Unit = {}
): RoadbookState = RoadbookState(
    routeName = routeName,
    events = events,
    onScrollUp = onScrollUp,
    onScrollDown = onScrollDown,
    internals = RoadbookState.InternalData(
        roadbookUiState = MutableStateFlow(RoadbookUiState.Empty),
        initialScrollPosition = MutableStateFlow(RoadbookPosition()),
        onImportRoute = {},
        onDistanceSectionLongPressed = {},
        onWaypointVisible = { _, _ -> }
    )
)
