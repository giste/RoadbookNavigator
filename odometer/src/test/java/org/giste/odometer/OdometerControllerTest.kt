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

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.odometer.domain.usecase.DecrementPartialDistanceUseCase
import org.giste.odometer.domain.usecase.GetOdometerUseCase
import org.giste.odometer.domain.usecase.IncrementPartialDistanceUseCase
import org.giste.odometer.domain.usecase.ResetAllDistancesUseCase
import org.giste.odometer.domain.usecase.ResetPartialDistanceUseCase
import org.giste.odometer.domain.usecase.SetPartialDistanceUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OdometerControllerTest {

    private val getOdometerUseCase: GetOdometerUseCase = mockk()
    private val resetPartialDistanceUseCase: ResetPartialDistanceUseCase = mockk(relaxed = true)
    private val resetAllDistancesUseCase: ResetAllDistancesUseCase = mockk(relaxed = true)
    private val incrementPartialDistanceUseCase: IncrementPartialDistanceUseCase = mockk(relaxed = true)
    private val decrementPartialDistanceUseCase: DecrementPartialDistanceUseCase = mockk(relaxed = true)
    private val setPartialDistanceUseCase: SetPartialDistanceUseCase = mockk(relaxed = true)
    private val locationProvider: OdometerLocationProvider = mockk()
    private val settingsProvider: OdometerSettingsProvider = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Test
    fun `odometer flow should observe engine through use case`() = runTest(testDispatcher) {
        val expectedOdometer = Odometer(100.0, 50.0)
        every { settingsProvider.getSettings() } returns emptyFlow()
        every { locationProvider.observeLocation() } returns emptyFlow()
        every { getOdometerUseCase(any(), any()) } returns MutableStateFlow(expectedOdometer)

        val controller = OdometerController(
            getOdometerUseCase,
            resetPartialDistanceUseCase,
            resetAllDistancesUseCase,
            incrementPartialDistanceUseCase,
            decrementPartialDistanceUseCase,
            setPartialDistanceUseCase,
            locationProvider,
            settingsProvider,
            testScope
        )

        assertEquals(expectedOdometer, controller.odometer.first())
    }

    @Test
    fun `actions should delegate to use cases`() = runTest(testDispatcher) {
        every { settingsProvider.getSettings() } returns emptyFlow()
        every { locationProvider.observeLocation() } returns emptyFlow()
        every { getOdometerUseCase(any(), any()) } returns emptyFlow()

        val controller = OdometerController(
            getOdometerUseCase,
            resetPartialDistanceUseCase,
            resetAllDistancesUseCase,
            incrementPartialDistanceUseCase,
            decrementPartialDistanceUseCase,
            setPartialDistanceUseCase,
            locationProvider,
            settingsProvider,
            testScope
        )

        controller.resetPartial()
        coVerify { resetPartialDistanceUseCase() }

        controller.resetAll()
        coVerify { resetAllDistancesUseCase() }

        controller.setPartial(10.0)
        coVerify { setPartialDistanceUseCase(10.0) }

        controller.increment()
        coVerify { incrementPartialDistanceUseCase() }

        controller.decrement()
        coVerify { decrementPartialDistanceUseCase() }
    }

    @Test
    fun `factory function should create a valid controller for testing`() = runTest {
        var incrementCalled = false
        val controller = OdometerController(
            totalDistance = 1000.0,
            partialDistance = 500.0,
            onIncrement = { incrementCalled = true }
        )

        assertEquals(1000.0, controller.odometer.value.total, 0.0)
        assertEquals(500.0, controller.odometer.value.partial, 0.0)

        controller.increment()
        assertEquals(true, incrementCalled)
    }
}
