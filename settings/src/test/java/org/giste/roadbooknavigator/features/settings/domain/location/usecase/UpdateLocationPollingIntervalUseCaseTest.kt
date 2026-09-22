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
import org.giste.roadbooknavigator.features.settings.domain.location.LocationSettingsRepository
import org.junit.Assert.assertTrue
import org.junit.Test
import org.giste.location.LocationSettings as ModuleLocationSettings

class UpdateLocationPollingIntervalUseCaseTest {

    private val repository: LocationSettingsRepository = mockk()
    private val useCase = UpdateLocationPollingIntervalUseCase(repository)

    @Test
    fun `should call updateSettings on repository when valid`() = runTest {
        val initialSettings = ModuleLocationSettings(pollingInterval = 500L, minDistance = 2f)
        every { repository.settings } returns flowOf(initialSettings)
        coEvery { repository.updateSettings(any()) } returns Unit

        val result = useCase(1000L)

        assertTrue(result.isSuccess)
        coVerify { repository.updateSettings(initialSettings.copy(pollingInterval = 1000L)) }
    }

    @Test
    fun `should return failure when interval is invalid`() = runTest {
        val result = useCase(50L)

        assertTrue(result.isFailure)
    }
}
