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

package org.giste.roadbooknavigator.features.roadbook.domain.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookRepository
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.InputStream

class ImportRoadbookUseCaseTest {

    private val repository: RoadbookRepository = mockk()
    private val resetRoadbookPositionUseCase: ResetRoadbookPositionUseCase = mockk()
    private val useCase = ImportRoadbookUseCase(repository, resetRoadbookPositionUseCase)

    @Test
    fun `invoke should call processNewRoadbook and reset position on success`() = runTest {
        // Given
        val inputStream: InputStream = mockk()
        val route = mockk<Route>(relaxed = true)
        coEvery { repository.processNewRoadbook(inputStream) } returns Result.success(route)
        coEvery { resetRoadbookPositionUseCase() } returns Unit

        // When
        val result = useCase(inputStream)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(route, result.getOrNull())
        coVerify { resetRoadbookPositionUseCase() }
    }

    @Test
    fun `invoke should NOT reset position on failure`() = runTest {
        // Given
        val inputStream: InputStream = mockk()
        val error = RuntimeException("Failed")
        coEvery { repository.processNewRoadbook(inputStream) } returns Result.failure(error)

        // When
        val result = useCase(inputStream)

        // Then
        assertTrue(result.isFailure)
        coVerify(exactly = 0) { resetRoadbookPositionUseCase() }
    }
}
