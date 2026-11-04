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

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.giste.odometer.data.AndroidOdometerLogger
import org.giste.odometer.data.DataStoreOdometerRepository
import org.giste.odometer.data.RealOdometerTimeProvider
import org.giste.odometer.data.odometerDataStore
import org.giste.odometer.domain.DistanceUtils
import org.giste.odometer.domain.OdometerRepository
import org.giste.odometer.domain.usecase.DecrementPartialDistanceUseCase
import org.giste.odometer.domain.usecase.ObserveOdometerUseCase
import org.giste.odometer.domain.usecase.IncrementPartialDistanceUseCase
import org.giste.odometer.domain.usecase.ResetAllDistancesUseCase
import org.giste.odometer.domain.usecase.ResetPartialDistanceUseCase
import org.giste.odometer.domain.usecase.SetPartialDistanceUseCase

/**
 * Headless business logic controller for the Odometer module.
 *
 * This class coordinates the calculation engine, persistence, and external providers
 * to expose an atomic reactive state of the odometer.
 *
 * It is completely framework-agnostic and does not depend on any UI or DI framework.
 *
 * @param context Application context used for DataStore persistence.
 * @param locationProvider Provider stream for location updates.
 * @param settingsProvider Provider stream for odometer settings.
 * @param logger Optional logger implementation for odometer events.
 * @param ioDispatcher Dispatcher for asynchronous persistence and calculations.
 * @param scope Coroutine scope for running async operations.
 */
public class OdometerController public constructor(
    context: Context,
    locationProvider: OdometerLocationProvider,
    settingsProvider: OdometerSettingsProvider,
    logger: OdometerLogger = AndroidOdometerLogger("OdometerController"),
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)
) : OdometerProvider {

    private val repository: OdometerRepository = DataStoreOdometerRepository(
        dataStore = context.applicationContext.odometerDataStore,
        logger = logger,
        ioDispatcher = ioDispatcher,
        scope = scope,
        timeProvider = RealOdometerTimeProvider()
    )

    private val observeOdometerUseCase: ObserveOdometerUseCase = ObserveOdometerUseCase(
        odometerRepository = repository,
        distanceUtils = DistanceUtils(logger),
        logger = logger
    )

    private val resetPartialDistanceUseCase: ResetPartialDistanceUseCase = ResetPartialDistanceUseCase(
        repository = repository,
        logger = logger
    )

    private val resetAllDistancesUseCase: ResetAllDistancesUseCase = ResetAllDistancesUseCase(
        repository = repository,
        logger = logger
    )

    private val incrementPartialDistanceUseCase: IncrementPartialDistanceUseCase = IncrementPartialDistanceUseCase(
        repository = repository,
        logger = logger
    )

    private val decrementPartialDistanceUseCase: DecrementPartialDistanceUseCase = DecrementPartialDistanceUseCase(
        repository = repository,
        logger = logger
    )

    private val setPartialDistanceUseCase: SetPartialDistanceUseCase = SetPartialDistanceUseCase(
        repository = repository,
        logger = logger
    )

    /**
     * Atomic reactive state of the odometer.
     */
    override val odometer: StateFlow<Odometer> = observeOdometerUseCase(
        settingsFlow = settingsProvider.getSettings(),
        locationFlow = locationProvider.observeLocation()
    ).stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = Odometer()
    )

    /**
     * Resets the partial distance to zero.
     */
    override fun resetPartial() {
        scope.launch { resetPartialDistanceUseCase() }
    }

    /**
     * Resets both total and partial distances to zero.
     */
    override fun resetAll() {
        scope.launch { resetAllDistancesUseCase() }
    }

    /**
     * Sets the partial distance to a specific value (in meters).
     */
    override fun setPartial(distance: Double) {
        scope.launch { setPartialDistanceUseCase(distance) }
    }

    /**
     * Manually increments the partial distance by a small step.
     */
    override fun increment() {
        scope.launch { incrementPartialDistanceUseCase() }
    }

    /**
     * Manually decrements the partial distance by a small step.
     */
    override fun decrement() {
        scope.launch { decrementPartialDistanceUseCase() }
    }
}
