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
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class OdometerControllerTest {

    private val locationProvider: OdometerLocationProvider = mockk()
    private val settingsProvider: OdometerSettingsProvider = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Test
    fun `controller should instantiate cleanly and implement OdometerProvider`() = runTest(testDispatcher) {
        val context: Context = RuntimeEnvironment.getApplication()
        every { settingsProvider.getSettings() } returns emptyFlow()
        every { locationProvider.observeLocation() } returns emptyFlow()

        val controller: OdometerProvider = OdometerController(
            context = context,
            locationProvider = locationProvider,
            settingsProvider = settingsProvider,
            ioDispatcher = testDispatcher,
            scope = testScope
        )

        assertEquals(Odometer(0.0, 0.0), controller.odometer.first())
    }

    @Test
    fun `OdometerProvider factory function should create valid mock instance`() = runTest {
        var incrementCalled = false
        val provider: OdometerProvider = OdometerProvider(
            odometer = Odometer(1000.0, 500.0),
            onIncrement = { incrementCalled = true }
        )

        assertEquals(1000.0, provider.odometer.value.total, 0.0)
        assertEquals(500.0, provider.odometer.value.partial, 0.0)

        provider.increment()
        assertEquals(true, incrementCalled)
    }
}
