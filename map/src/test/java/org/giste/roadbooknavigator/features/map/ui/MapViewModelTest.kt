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

package org.giste.roadbooknavigator.features.map.ui

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.location.domain.UserLocation
import org.giste.roadbooknavigator.features.location.domain.usecase.ObserveLocationUseCase
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.model.MapSettings
import org.giste.roadbooknavigator.features.map.domain.usecase.GetLocalMapsUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.GetMapSettingsUseCase
import org.junit.Assert.assertEquals
import org.junit.Test

class MapViewModelTest {

    private val getLocalMapsUseCase: GetLocalMapsUseCase = mockk()
    private val getMapSettingsUseCase: GetMapSettingsUseCase = mockk()
    private val observeLocationUseCase: ObserveLocationUseCase = mockk()

    @Test
    fun `uiState should combine data from use cases`() = runTest {
        val expectedMaps = listOf(MapFile("spain.map", "/path", 100L, 1000L, "europe"))
        val expectedSettings = MapSettings(initialZoom = 12, initialTilt = 30f)
        val expectedLocation = UserLocation(40.0, -3.0, 0.0, 1f, null, 0f, 0f, 0L)

        every { getLocalMapsUseCase() } returns flowOf(expectedMaps)
        every { getMapSettingsUseCase() } returns flowOf(expectedSettings)
        every { observeLocationUseCase() } returns flowOf(expectedLocation)

        val viewModel = MapViewModel(
            getLocalMapsUseCase,
            getMapSettingsUseCase,
            observeLocationUseCase
        )

        val state = viewModel.uiState.first { it.localMaps.isNotEmpty() }
        
        assertEquals(expectedMaps, state.localMaps)
        assertEquals(expectedSettings, state.settings)
        assertEquals(expectedLocation, state.currentLocation)
    }
}
