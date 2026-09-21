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

package org.giste.roadbooknavigator.features.settings.domain.location.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.giste.location.LocationSettingsProvider
import org.junit.Assert.assertTrue
import org.junit.Test
import org.giste.location.LocationSettings as ModuleLocationSettings

class UpdateLocationMinDistanceUseCaseTest {

    private val provider: LocationSettingsProvider = mockk()
    private val useCase = UpdateLocationMinDistanceUseCase(provider)

    @Test
    fun `should call updateSettings on provider when valid`() = runTest {
        val initialSettings = ModuleLocationSettings(pollingInterval = 500L, minDistance = 2f)
        every { provider.settings } returns flowOf(initialSettings)
        coEvery { provider.updateSettings(any()) } returns Unit

        val result = useCase(5.0f)

        assertTrue(result.isSuccess)
        coVerify { provider.updateSettings(initialSettings.copy(minDistance = 5.0f)) }
    }

    @Test
    fun `should return failure when distance is invalid`() = runTest {
        val result = useCase(-1.0f)

        assertTrue(result.isFailure)
    }
}
