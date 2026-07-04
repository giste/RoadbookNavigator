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

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.odometer.domain.OdometerRepository
import org.junit.Before
import org.junit.Test

class OdometerUseCasesTest {

    private val repository: OdometerRepository = mockk()
    private val logger: Logger = mockk(relaxed = true)

    @Before
    fun setup() {
        coEvery { repository.updatePartialDistance(any()) } returns Unit
        coEvery { repository.resetPartialDistance() } returns Unit
        coEvery { repository.resetAllDistances() } returns Unit
        coEvery { repository.setPartialDistance(any()) } returns Unit
    }

    @Test
    fun `IncrementPartialDistanceUseCase should call repository with +10 meters`() = runTest {
        val useCase = IncrementPartialDistanceUseCase(repository, logger)
        useCase()
        coVerify { repository.updatePartialDistance(10.0) }
    }

    @Test
    fun `DecrementPartialDistanceUseCase should call repository with -10 meters`() = runTest {
        val useCase = DecrementPartialDistanceUseCase(repository, logger)
        useCase()
        coVerify { repository.updatePartialDistance(-10.0) }
    }

    @Test
    fun `ResetPartialDistanceUseCase should call repository resetPartialDistance`() = runTest {
        val useCase = ResetPartialDistanceUseCase(repository, logger)
        useCase()
        coVerify { repository.resetPartialDistance() }
    }

    @Test
    fun `ResetAllDistancesUseCase should call repository resetAllDistances`() = runTest {
        val useCase = ResetAllDistancesUseCase(repository, logger)
        useCase()
        coVerify { repository.resetAllDistances() }
    }

    @Test
    fun `SetPartialDistanceUseCase should call repository with specific distance`() = runTest {
        val useCase = SetPartialDistanceUseCase(repository, logger)
        val distance = 123.45
        useCase(distance)
        coVerify { repository.setPartialDistance(distance) }
    }
}
