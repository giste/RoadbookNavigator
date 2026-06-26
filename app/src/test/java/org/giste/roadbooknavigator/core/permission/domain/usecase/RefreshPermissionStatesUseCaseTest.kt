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

package org.giste.roadbooknavigator.core.permission.domain.usecase

import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.giste.roadbooknavigator.core.permission.domain.repository.PermissionRepository
import org.junit.Test

class RefreshPermissionStatesUseCaseTest {

    private val repository: PermissionRepository = mockk()
    private val useCase = RefreshPermissionStatesUseCase(repository)

    @Test
    fun `when invoked then refreshes repository`() {
        // Given
        every { repository.refreshPermissionStates() } just runs

        // When
        useCase()

        // Then
        verify { repository.refreshPermissionStates() }
    }
}
