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

package org.giste.roadbooknavigator.features.location.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [GetLocationPermissionStatusUseCase].
 */
class GetLocationPermissionStatusUseCaseTest {

    private val repository: LocationPermissionRepository = mockk()
    private val useCase = GetLocationPermissionStatusUseCase(repository)

    @Test
    fun `invoke should return GRANTED when repository returns GRANTED`() = runTest {
        // Given
        val expectedStatus = PermissionStatus.GRANTED
        every { repository.getPermissionStatus() } returns flowOf(expectedStatus)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expectedStatus, result)
    }

    @Test
    fun `invoke should return DENIED when repository returns DENIED`() = runTest {
        // Given
        val expectedStatus = PermissionStatus.DENIED
        every { repository.getPermissionStatus() } returns flowOf(expectedStatus)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expectedStatus, result)
    }

    @Test
    fun `invoke should return RATIONALE_REQUIRED when repository returns RATIONALE_REQUIRED`() = runTest {
        // Given
        val expectedStatus = PermissionStatus.RATIONALE_REQUIRED
        every { repository.getPermissionStatus() } returns flowOf(expectedStatus)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expectedStatus, result)
    }

    @Test
    fun `invoke should return PERMANENTLY_DENIED when repository returns PERMANENTLY_DENIED`() = runTest {
        // Given
        val expectedStatus = PermissionStatus.PERMANENTLY_DENIED
        every { repository.getPermissionStatus() } returns flowOf(expectedStatus)

        // When
        val result = useCase().first()

        // Then
        assertEquals(expectedStatus, result)
    }
}
