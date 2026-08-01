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
 * along with this program.  See the <https://www.gnu.org/licenses/>.
 */

package org.giste.android.location.domain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.giste.android.location.domain.LocationLogger
import org.giste.android.location.domain.LocationRepository
import org.junit.Test

class ObserveLocationUseCaseTest {

    private val repository: LocationRepository = mockk()
    private val logger: LocationLogger = mockk(relaxed = true)
    private val useCase = ObserveLocationUseCase(repository, logger)

    @Test
    fun `should call repository getLocations with parameters`() = runTest {
        every { repository.getLocations(any(), any()) } returns flowOf(mockk(relaxed = true))

        useCase(pollingInterval = 2000L, minDistance = 10f).first()

        verify { repository.getLocations(2000L, 10f) }
    }
}
