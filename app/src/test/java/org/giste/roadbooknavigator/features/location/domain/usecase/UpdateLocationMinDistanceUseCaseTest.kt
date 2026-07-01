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

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.location.domain.LocationSettingsRepository
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateLocationMinDistanceUseCaseTest {

    private val repository: LocationSettingsRepository = mockk()
    private val useCase = UpdateLocationMinDistanceUseCase(repository)

    @Test
    fun `invoke should call repository when distance is valid`() = runTest {
        // Given
        val distance = 5.0f
        coEvery { repository.updateMinDistance(distance) } returns Unit

        // When
        val result = useCase(distance)

        // Then
        assertTrue(result.isSuccess)
        coVerify { repository.updateMinDistance(distance) }
    }

    @Test
    fun `invoke should return failure when distance is invalid`() = runTest {
        // Given
        val distance = -1.0f

        // When
        val result = useCase(distance)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        coVerify(exactly = 0) { repository.updateMinDistance(any()) }
    }
}
