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

package org.giste.roadbooknavigator.features.location.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import org.junit.Test

class ObserveLocationUseCaseTest {

    private val repository: LocationRepository = mockk()
    private val getSettingsUseCase: GetSettingsUseCase = mockk()
    private val useCase = ObserveLocationUseCase(repository, getSettingsUseCase)

    @Test
    fun `should call repository getLocations with parameters from settings`() = runTest {
        val settings = AppSettings(
            odometerPollingInterval = 2000L,
            odometerMinDistance = 10f
        )
        every { getSettingsUseCase() } returns flowOf(settings)
        every { repository.getLocations(any(), any()) } returns flowOf(mockk())

        useCase().first()

        verify { repository.getLocations(2000L, 10f) }
    }
}