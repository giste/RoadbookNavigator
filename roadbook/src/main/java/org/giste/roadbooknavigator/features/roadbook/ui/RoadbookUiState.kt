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

import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookSettings
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.model.ShortDistanceThreshold

sealed interface RoadbookUiState {
    data object Loading : RoadbookUiState
    data object Empty : RoadbookUiState
    data class Success(
        val route: Route,
        val shortDistanceThreshold: ShortDistanceThreshold = ShortDistanceThreshold(ShortDistanceThreshold.DEFAULT),
        val initialIndex: Int = 0,
        val initialOffset: Int = 0,
        val roadbookUp: List<Int> = RoadbookSettings.DEFAULT_UP_KEYS,
        val roadbookDown: List<Int> = RoadbookSettings.DEFAULT_DOWN_KEYS,
    ) : RoadbookUiState

    data class Error(val message: String) : RoadbookUiState
}
