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

package org.giste.odometer

/**
 * Value Object for speed threshold. Odometer will ignore updates if speed is lower than this.
 */
@JvmInline
public value class SpeedThreshold(public val metersPerSecond: Float) {
    init {
        require(metersPerSecond in MIN..MAX) {
            "Speed threshold must be between $MIN and $MAX m/s"
        }
    }

    public companion object {
        public const val MIN: Float = 0.0f
        public const val MAX: Float = 2.0f
    }
}

/**
 * Value Object for GPS accuracy threshold. Odometer will ignore updates with accuracy worse than
 * this.
 */
@JvmInline
public value class AccuracyThreshold(public val meters: Float) {
    init {
        require(meters in MIN..MAX) {
            "Accuracy threshold must be between $MIN and $MAX meters"
        }
    }

    public companion object {
        public const val MIN: Float = 1.0f
        public const val MAX: Float = 100.0f
    }
}

/**
 * Value Object for GPS vertical accuracy threshold. Odometer will ignore updates with vertical
 * accuracy worse than this.
 */
@JvmInline
public value class VerticalAccuracyThreshold(public val meters: Float) {
    init {
        require(meters in MIN..MAX) {
            "Vertical accuracy threshold must be between $MIN and $MAX meters"
        }
    }

    public companion object {
        public const val MIN: Float = 1.0f
        public const val MAX: Float = 100.0f
    }
}

/**
 * Value Object representing odometer-specific settings.
 *
 * @property speedThreshold Minimum speed below which the odometer might ignore updates to prevent "jitter".
 * @property minAccuracy Maximum allowed horizontal GPS accuracy in meters.
 * @property minVerticalAccuracy Maximum allowed vertical GPS accuracy in meters.
 */
public data class OdometerSettings(
    val speedThreshold: SpeedThreshold = SpeedThreshold(DEFAULT_SPEED_THRESHOLD),
    val minAccuracy: AccuracyThreshold = AccuracyThreshold(DEFAULT_MIN_ACCURACY),
    val minVerticalAccuracy: VerticalAccuracyThreshold = VerticalAccuracyThreshold(DEFAULT_MIN_VERTICAL_ACCURACY),
) {
    public companion object {
        public const val DEFAULT_SPEED_THRESHOLD: Float = 0.5f // m/s
        public const val DEFAULT_MIN_ACCURACY: Float = 20.0f // m
        public const val DEFAULT_MIN_VERTICAL_ACCURACY: Float = 10.0f // m
    }
}
