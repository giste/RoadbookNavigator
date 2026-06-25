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

package org.giste.roadbooknavigator.features.settings.domain

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
        const val MIN = 0.0
        const val MAX = 500.0
    }
}

/**
 * Value Object for speed threshold. Odometer will ignore updates if speed is lower than this.
 */
@JvmInline
value class SpeedThreshold(val metersPerSecond: Float) {
    init {
        require(metersPerSecond in MIN..MAX) {
            "Speed threshold must be between $MIN and $MAX m/s"
        }
    }

    companion object {
        const val MIN = 0.0f
        const val MAX = 5.0f
    }
}

/**
 * Value Object for GPS accuracy threshold. Odometer will ignore updates with accuracy worse than
 * this.
 */
@JvmInline
value class AccuracyThreshold(val meters: Float) {
    init {
        require(meters in MIN..MAX) {
            "Accuracy threshold must be between $MIN and $MAX meters"
        }
    }

    companion object {
        const val MIN = 1.0f
        const val MAX = 100.0f
    }
}

/**
 * Value Object for GPS vertical accuracy threshold. Odometer will ignore updates with vertical
 * accuracy worse than this.
 */
@JvmInline
value class VerticalAccuracyThreshold(val meters: Float) {
    init {
        require(meters in MIN..MAX) {
            "Vertical accuracy threshold must be between $MIN and $MAX meters"
        }
    }

    companion object {
        const val MIN = 1.0f
        const val MAX = 100.0f
    }
}

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
