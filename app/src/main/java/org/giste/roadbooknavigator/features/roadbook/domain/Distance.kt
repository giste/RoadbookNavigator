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

package org.giste.roadbooknavigator.features.roadbook.domain

/**
 * Value object representing a physical distance.
 *
 * Internally stored as [meters] in a [Long] to prevent floating-point arithmetic errors
 * during cumulative odometer calculations.
 *
 * @property meters The absolute distance in meters. Must be non-negative.
 */
@JvmInline
value class Distance(val meters: Long) : Comparable<Distance> {

    val kilometers: Double get() = meters / 1000.0

    operator fun plus(other: Distance) = Distance(this.meters + other.meters)
    operator fun minus(other: Distance) = Distance(this.meters - other.meters)

    override fun compareTo(other: Distance): Int = this.meters.compareTo(other.meters)

    init {
        require(meters >= 0) { "Distance cannot be negative" }
    }

    companion object {
        fun fromKilometers(km: Double) = Distance((km * 1000).toLong())
        val ZERO = Distance(0)
    }
}
