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
import kotlinx.coroutines.flow.StateFlow
import org.giste.roadbook.ui.RoadbookViewModel

/**
 * State object for the Roadbook module, following the State Hoisting pattern.
 * This class acts as a public proxy for the internal navigation logic.
 *
 * Use [rememberRoadbookState] to create an instance in Compose.
 */
@Stable
public class RoadbookState internal constructor(
    internal val viewModel: RoadbookViewModel
) {
    /**
     * Reactive stream of [RoadbookEvent]s emitted by the module.
     */
    public val events: Flow<RoadbookEvent> get() = viewModel.events

    /**
     * The name of the currently active route, or null if no route is loaded.
     */
    public val routeName: StateFlow<String?> get() = viewModel.routeName

    /**
     * Commands the roadbook to move forward to the next waypoint.
     */
    public fun scrollUp() {
        viewModel.scrollUp()
    }

    /**
     * Commands the roadbook to move backward to the previous waypoint.
     */
    public fun scrollDown() {
        viewModel.scrollDown()
    }
}
