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

package org.giste.roadbooknavigator.ui.roadbook

import org.giste.roadbooknavigator.features.roadbook.domain.Route

/**
 * Represent the different states of the Roadbook screen.
 */
sealed class RoadbookUiState {
    /**
     * The screen is waiting for the initial load from disk.
     */
    data object Loading : RoadbookUiState()

    /**
     * A roadbook has been successfully loaded (or is null if none exists).
     */
    data class Success(val route: Route?) : RoadbookUiState()

    /**
     * An error occurred while loading or processing the roadbook.
     */
    data class Error(val message: String) : RoadbookUiState()
}
