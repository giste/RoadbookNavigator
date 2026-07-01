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

package org.giste.roadbooknavigator.features.odometer.domain.usecase

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettings
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class GetOdometerSettingsUseCaseTest {

    private val repository: OdometerSettingsRepository = mockk()
    private val useCase = GetOdometerSettingsUseCase(repository)

    @Test
    fun `invoke should return odometer settings flow from repository`() = runTest {
        // Given
        val settings = OdometerSettings()
        every { repository.getSettings() } returns flowOf(settings)

        // When
        val result = useCase().first()

        // Then
        assertEquals(settings, result)
    }
}
