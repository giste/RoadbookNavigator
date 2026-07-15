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
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettingsRepository
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateOdometerRemoteKeysUseCaseTest {

    private val repository: OdometerSettingsRepository = mockk()
    private val logger: Logger = mockk(relaxed = true)
    private val useCase = UpdateOdometerRemoteKeysUseCase(repository, logger)

    @Test
    fun `invoke should call repository`() = runTest {
        // Given
        val increase = listOf(1)
        val decrease = listOf(2)
        val reset = listOf(3)
        coEvery { repository.setRemoteKeys(increase, decrease, reset) } returns Unit

        // When
        val result = useCase(increase, decrease, reset)

        // Then
        assertTrue(result.isSuccess)
        coVerify { repository.setRemoteKeys(increase, decrease, reset) }
    }
}
