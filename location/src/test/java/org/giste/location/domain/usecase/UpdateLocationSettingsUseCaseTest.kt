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

package org.giste.location.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.giste.location.LocationSettings
import org.giste.location.domain.LocationSettingsRepository
import org.junit.Test

class UpdateLocationSettingsUseCaseTest {

    private val repository: LocationSettingsRepository = mockk()
    private val useCase = UpdateLocationSettingsUseCase(repository)

    @Test
    fun `should call repository saveLocationSettings`() = runTest {
        val settings = LocationSettings(pollingInterval = 1000L, minDistance = 5f)
        coEvery { repository.saveLocationSettings(any()) } returns Unit

        useCase(settings)

        coVerify { repository.saveLocationSettings(settings) }
    }
}
