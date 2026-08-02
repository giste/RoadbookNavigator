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

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.settings.domain.location.LocationSettings
import org.giste.roadbooknavigator.features.settings.domain.location.LocationSettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class ObserveLocationSettingsUseCaseTest {

    private val repository: LocationSettingsRepository = mockk()
    private val useCase = ObserveLocationSettingsUseCase(repository)

    @Test
    fun `should emit settings from repository`() = runTest {
        val settings = LocationSettings()
        every { repository.getLocationSettings() } returns flowOf(settings)

        val result = useCase().first()

        assertEquals(settings, result)
    }
}