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

package org.giste.android.location.domain

import androidx.annotation.FloatRange
import androidx.annotation.IntRange

/**
 * Value Object for GPS polling interval.
 */
@JvmInline
public value class PollingIntervalThreshold(
    @IntRange(from = MIN, to = MAX) public val milliseconds: Long
) {
    init {
        require(milliseconds in MIN..MAX) {
            "Polling interval must be between $MIN and $MAX ms"
        }
    }

    public companion object {
        public const val MIN: Long = 100L
        public const val MAX: Long = 2000L
    }
}

/**
 * Value Object for GPS minimum distance.
 */
@JvmInline
public value class MinDistanceThreshold(
    @FloatRange(from = MIN.toDouble(), to = MAX.toDouble()) public val meters: Float
) {
    init {
        require(meters in MIN..MAX) {
            "Min distance must be between $MIN and $MAX meters"
        }
    }

    public companion object {
        public const val MIN: Float = 0.0f
        public const val MAX: Float = 10.0f
    }
}
