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

class UpdateMinVerticalAccuracyUseCaseTest {

    private val repository: LocationSettingsRepository = mockk()
    private val useCase = UpdateMinVerticalAccuracyUseCase(repository)

    @Test
    fun `invoke should call repository when accuracy is valid`() = runTest {
        // Given
        val accuracy = 10.0f
        coEvery { repository.updateMinVerticalAccuracy(accuracy) } returns Unit

        // When
        val result = useCase(accuracy)

        // Then
        assertTrue(result.isSuccess)
        coVerify { repository.updateMinVerticalAccuracy(accuracy) }
    }

    @Test
    fun `invoke should return failure when accuracy is invalid`() = runTest {
        // Given
        val accuracy = -1.0f

        // When
        val result = useCase(accuracy)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
        coVerify(exactly = 0) { repository.updateMinVerticalAccuracy(any()) }
    }
}
