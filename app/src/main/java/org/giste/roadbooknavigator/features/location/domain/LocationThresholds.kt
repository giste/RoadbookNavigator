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

package org.giste.roadbooknavigator.features.location.domain

/**
 * Value Object for GPS polling interval.
 */
@JvmInline
value class PollingIntervalThreshold(val milliseconds: Long) {
    init {
        require(milliseconds.toDouble() in MIN..MAX) {
            "Polling interval must be between $MIN and $MAX ms"
        }
    }

    companion object {
        const val MIN = 100.0
        const val MAX = 5000.0
    }
}

/**
 * Value Object for GPS minimum distance.
 */
@JvmInline
value class MinDistanceThreshold(val meters: Float) {
    init {
        require(meters in MIN..MAX) {
            "Min distance must be between $MIN and $MAX meters"
        }
    }

    companion object {
        const val MIN = 0.0f
        const val MAX = 50.0f
    }
}
