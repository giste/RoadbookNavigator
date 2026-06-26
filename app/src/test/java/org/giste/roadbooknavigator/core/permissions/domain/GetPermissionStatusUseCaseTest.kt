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

package org.giste.roadbooknavigator.core.permissions.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [GetPermissionStatusUseCase].
 */
class GetPermissionStatusUseCaseTest {

    private val repository: PermissionRepository = mockk()
    private val useCase = GetPermissionStatusUseCase(repository)

    @Test
    fun `invoke should return GRANTED when repository returns GRANTED`() = runTest {
        // Given
        val permission = AppPermission.Location
        val expectedStatus = PermissionStatus.GRANTED
        every { repository.getPermissionStatus(permission) } returns flowOf(expectedStatus)

        // When
        val result = useCase(permission).first()

        // Then
        assertEquals(expectedStatus, result)
    }

    @Test
    fun `invoke should return DENIED when repository returns DENIED`() = runTest {
        // Given
        val permission = AppPermission.Location
        val expectedStatus = PermissionStatus.DENIED
        every { repository.getPermissionStatus(permission) } returns flowOf(expectedStatus)

        // When
        val result = useCase(permission).first()

        // Then
        assertEquals(expectedStatus, result)
    }
}
