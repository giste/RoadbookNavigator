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

package org.giste.roadbooknavigator.features.roadbook.domain.model

/**
 * Value object representing the scroll position in a roadbook list.
 *
 * @property index The index of the first visible waypoint.
 * @property offset The pixel offset of the first visible waypoint.
 */
data class RoadbookPosition(
    val index: Int = 0,
    val offset: Int = 0
) {
    init {
        require(index >= 0) { "Index must be non-negative" }
        require(offset >= 0) { "Offset must be non-negative" }
    }
}
