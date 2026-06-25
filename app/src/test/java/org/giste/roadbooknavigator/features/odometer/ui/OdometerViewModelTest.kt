/*
 * Rn2 Viewer
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

package org.giste.roadbooknavigator.features.odometer.ui

import android.net.Uri
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.roadbooknavigator.features.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.odometer.domain.usecase.DecrementPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.GetOdometerUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.IncrementPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.ResetAllDistancesUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.ResetPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.SetPartialDistanceUseCase
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OdometerViewModelTest {

    private val getOdometerUseCase: GetOdometerUseCase = mockk()
    private val resetPartialDistanceUseCase: ResetPartialDistanceUseCase = mockk()
    private val resetAllDistancesUseCase: ResetAllDistancesUseCase = mockk()
    private val incrementPartialDistanceUseCase: IncrementPartialDistanceUseCase = mockk()
    private val decrementPartialDistanceUseCase: DecrementPartialDistanceUseCase = mockk()
    private val setPartialDistanceUseCase: SetPartialDistanceUseCase = mockk()

    private lateinit var viewModel: OdometerViewModel

    private val testDispatcher = UnconfinedTestDispatcher()
    private val odometerFlow = MutableStateFlow(Odometer())

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getOdometerUseCase() } returns odometerFlow

        viewModel = OdometerViewModel(
            getOdometerUseCase = getOdometerUseCase,
            resetPartialDistanceUseCase = resetPartialDistanceUseCase,
            resetAllDistancesUseCase = resetAllDistancesUseCase,
            incrementPartialDistanceUseCase = incrementPartialDistanceUseCase,
            decrementPartialDistanceUseCase = decrementPartialDistanceUseCase,
            setPartialDistanceUseCase = setPartialDistanceUseCase,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Uri::class)
    }

    @Test
    fun `setPartialDistance should call the use case`() = runTest(testDispatcher) {
        coEvery { setPartialDistanceUseCase(any()) } returns Unit

        viewModel.setPartialDistance(1234.0)

        coVerify { setPartialDistanceUseCase(1234.0) }
    }

    @Test
    fun `showSetPartialDialog should update uiState`() = runTest(testDispatcher) {
        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.showSetPartialDialog()

        assertTrue(viewModel.uiState.value.showSetPartialDialog)
    }

    @Test
    fun `hideSetPartialDialog should update uiState`() = runTest(testDispatcher) {
        backgroundScope.launch { viewModel.uiState.collect() }

        viewModel.showSetPartialDialog()
        viewModel.hideSetPartialDialog()

        assertTrue(!viewModel.uiState.value.showSetPartialDialog)
    }
}
