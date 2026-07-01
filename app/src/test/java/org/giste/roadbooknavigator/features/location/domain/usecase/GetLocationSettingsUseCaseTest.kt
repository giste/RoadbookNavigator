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

package org.giste.roadbooknavigator.features.location.domain.usecase

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.location.domain.LocationSettings
import org.giste.roadbooknavigator.features.location.domain.LocationSettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class GetLocationSettingsUseCaseTest {

    private val repository: LocationSettingsRepository = mockk()
    private val useCase = GetLocationSettingsUseCase(repository)

    @Test
    fun `invoke should return settings from repository`() = runTest {
        // Given
        val settings = LocationSettings()
        every { repository.getLocationSettings() } returns flowOf(settings)

        // When
        val result = useCase().first()

        // Then
        assertEquals(settings, result)
    }
}
