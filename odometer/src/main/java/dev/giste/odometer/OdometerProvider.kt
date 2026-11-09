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

package dev.giste.odometer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Gateway interface providing reactive odometer state and control operations.
 */
public interface OdometerProvider {
    /**
     * Atomic reactive state of the odometer.
     */
    public val odometer: StateFlow<Odometer>

    /**
     * Resets the partial distance to zero.
     */
    public fun resetPartial()

    /**
     * Resets both total and partial distances to zero.
     */
    public fun resetAll()

    /**
     * Sets the partial distance to a specific value (in meters).
     */
    public fun setPartial(distance: Double)

    /**
     * Manually increments the partial distance by a small step.
     */
    public fun increment()

    /**
     * Manually decrements the partial distance by a small step.
     */
    public fun decrement()
}

/**
 * Factory function to create a fake [OdometerProvider] for Compose Previews or testing.
 */
public fun OdometerProvider(
    odometer: Odometer = Odometer(),
    onResetPartial: () -> Unit = {},
    onResetAll: () -> Unit = {},
    onSetPartial: (Double) -> Unit = {},
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {}
): OdometerProvider = object : OdometerProvider {
    override val odometer: StateFlow<Odometer> = MutableStateFlow(odometer)
    override fun resetPartial() = onResetPartial()
    override fun resetAll() = onResetAll()
    override fun setPartial(distance: Double) = onSetPartial(distance)
    override fun increment() = onIncrement()
    override fun decrement() = onDecrement()
}
