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

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.giste.odometer.data.OdometerApplicationScope
import org.giste.odometer.domain.Odometer
import org.giste.odometer.domain.usecase.DecrementPartialDistanceUseCase
import org.giste.odometer.domain.usecase.GetOdometerUseCase
import org.giste.odometer.domain.usecase.IncrementPartialDistanceUseCase
import org.giste.odometer.domain.usecase.ResetAllDistancesUseCase
import org.giste.odometer.domain.usecase.ResetPartialDistanceUseCase
import org.giste.odometer.domain.usecase.SetPartialDistanceUseCase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Headless business logic controller for the Odometer module.
 *
 * This class coordinates the calculation engine, persistence, and external providers
 * to expose an atomic reactive state of the odometer.
 *
 * It is infrastructure-agnostic and does not depend on any UI framework.
 */
@Singleton
public class OdometerController internal constructor(
    /**
     * Atomic reactive state of the odometer.
     */
    public val odometer: StateFlow<Odometer>,
    internal val internals: InternalData
) {
    internal class InternalData(
        val onResetPartial: () -> Unit,
        val onResetAll: () -> Unit,
        val onSetPartial: (Double) -> Unit,
        val onIncrement: () -> Unit,
        val onDecrement: () -> Unit
    )

    @Inject internal constructor(
        getOdometerUseCase: GetOdometerUseCase,
        resetPartialDistanceUseCase: ResetPartialDistanceUseCase,
        resetAllDistancesUseCase: ResetAllDistancesUseCase,
        incrementPartialDistanceUseCase: IncrementPartialDistanceUseCase,
        decrementPartialDistanceUseCase: DecrementPartialDistanceUseCase,
        setPartialDistanceUseCase: SetPartialDistanceUseCase,
        locationProvider: OdometerLocationProvider,
        settingsProvider: OdometerSettingsProvider,
        @OdometerApplicationScope scope: CoroutineScope
    ) : this(
        odometer = getOdometerUseCase(
            settingsFlow = settingsProvider.getSettings(),
            locationFlow = locationProvider.observeLocation()
        ).stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = Odometer()
        ),
        internals = InternalData(
            onResetPartial = { scope.launch { resetPartialDistanceUseCase() } },
            onResetAll = { scope.launch { resetAllDistancesUseCase() } },
            onSetPartial = { distance -> scope.launch { setPartialDistanceUseCase(distance) } },
            onIncrement = { scope.launch { incrementPartialDistanceUseCase() } },
            onDecrement = { scope.launch { decrementPartialDistanceUseCase() } }
        )
    )

    /**
     * Resets the partial distance to zero.
     */
    public fun resetPartial() {
        internals.onResetPartial()
    }

    /**
     * Resets both total and partial distances to zero.
     */
    public fun resetAll() {
        internals.onResetAll()
    }

    /**
     * Sets the partial distance to a specific value (in meters).
     */
    public fun setPartial(distance: Double) {
        internals.onSetPartial(distance)
    }

    /**
     * Manually increments the partial distance by a small step.
     */
    public fun increment() {
        internals.onIncrement()
    }

    /**
     * Manually decrements the partial distance by a small step.
     */
    public fun decrement() {
        internals.onDecrement()
    }
}

/**
 * Factory function to create an [OdometerController] for previews or testing.
 */
public fun OdometerController(
    totalDistance: Double = 0.0,
    partialDistance: Double = 0.0,
    onResetPartial: () -> Unit = {},
    onResetAll: () -> Unit = {},
    onSetPartial: (Double) -> Unit = {},
    onIncrement: () -> Unit = {},
    onDecrement: () -> Unit = {}
): OdometerController = OdometerController(
    odometer = MutableStateFlow(Odometer(totalDistance, partialDistance)),
    internals = OdometerController.InternalData(
        onResetPartial = onResetPartial,
        onResetAll = onResetAll,
        onSetPartial = onSetPartial,
        onIncrement = onIncrement,
        onDecrement = onDecrement
    )
)
