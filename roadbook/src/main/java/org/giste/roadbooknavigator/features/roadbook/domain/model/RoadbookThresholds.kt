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
 * Value Object for short distance threshold. Waypoint with distance from previous lower than this
 * will be marked as "short distance".
 */
@JvmInline
value class ShortDistanceThreshold(val meters: Long) {
    init {
        require(meters.toDouble() in MIN..MAX) {
            "Short distance threshold must be between $MIN and $MAX meters"
        }
    }

    companion object {
        const val MIN = 100.0
        const val MAX = 500.0
        const val DEFAULT = 300L
    }
}
