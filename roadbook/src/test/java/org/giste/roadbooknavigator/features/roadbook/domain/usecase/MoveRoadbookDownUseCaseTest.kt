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
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.roadbook.domain.util.RoadbookLogger
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookSessionRepository
import org.junit.Before
import org.junit.Test

class MoveRoadbookDownUseCaseTest {

    private val repository: RoadbookSessionRepository = mockk()
    private val logger: RoadbookLogger = mockk(relaxed = true)
    private lateinit var useCase: MoveRoadbookDownUseCase

    @Before
    fun setup() {
        useCase = MoveRoadbookDownUseCase(repository, logger)
    }

    @Test
    fun `invoke should decrement index and save position when offset is 0`() = runTest {
        every { repository.scrollPosition } returns flowOf(RoadbookPosition(index = 2, offset = 0))
        coEvery { repository.saveScrollPosition(any()) } returns Unit

        useCase()

        coVerify { repository.saveScrollPosition(RoadbookPosition(index = 1, offset = 0)) }
    }

    @Test
    fun `invoke should reset offset to 0 and keep same index when offset is positive`() = runTest {
        every { repository.scrollPosition } returns flowOf(RoadbookPosition(index = 2, offset = 100))
        coEvery { repository.saveScrollPosition(any()) } returns Unit

        useCase()

        coVerify { repository.saveScrollPosition(RoadbookPosition(index = 2, offset = 0)) }
    }

    @Test
    fun `invoke should clamp to index 0`() = runTest {
        every { repository.scrollPosition } returns flowOf(RoadbookPosition(index = 0, offset = 0))
        coEvery { repository.saveScrollPosition(any()) } returns Unit

        useCase()

        coVerify { repository.saveScrollPosition(RoadbookPosition(index = 0, offset = 0)) }
    }
}
