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

package org.giste.roadbooknavigator.features.odometer.domain

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
