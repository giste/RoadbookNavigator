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
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.model.Waypoint
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookSessionRepository
import org.junit.Before
import org.junit.Test

class MoveRoadbookUpUseCaseTest {

    private val getActiveRoadbookUseCase: GetActiveRoadbookUseCase = mockk()
    private val repository: RoadbookSessionRepository = mockk()
    private val logger: Logger = mockk(relaxed = true)
    private lateinit var useCase: MoveRoadbookUpUseCase

    @Before
    fun setup() {
        useCase = MoveRoadbookUpUseCase(getActiveRoadbookUseCase, repository, logger)
    }

    @Test
    fun `invoke should increment index and save position`() = runTest {
        val waypoints = List(5) { mockk<Waypoint>() }
        val route = mockk<Route> {
            every { this@mockk.waypoints } returns waypoints
        }
        every { getActiveRoadbookUseCase() } returns flowOf(route)
        every { repository.scrollPosition } returns flowOf(RoadbookPosition(index = 2, offset = 10))
        coEvery { repository.saveScrollPosition(any()) } returns Unit

        useCase()

        coVerify { repository.saveScrollPosition(RoadbookPosition(index = 3, offset = 0)) }
    }

    @Test
    fun `invoke should clamp to last waypoint`() = runTest {
        val waypoints = List(5) { mockk<Waypoint>() }
        val route = mockk<Route> {
            every { this@mockk.waypoints } returns waypoints
        }
        every { getActiveRoadbookUseCase() } returns flowOf(route)
        every { repository.scrollPosition } returns flowOf(RoadbookPosition(index = 4, offset = 0))
        coEvery { repository.saveScrollPosition(any()) } returns Unit

        useCase()

        coVerify { repository.saveScrollPosition(RoadbookPosition(index = 4, offset = 0)) }
    }

    @Test
    fun `invoke should do nothing if no active roadbook`() = runTest {
        every { getActiveRoadbookUseCase() } returns flowOf(null)

        useCase()

        coVerify(exactly = 0) { repository.saveScrollPosition(any()) }
    }

    @Test
    fun `invoke should do nothing if roadbook has no waypoints`() = runTest {
        val route = mockk<Route> {
            every { waypoints } returns emptyList()
        }
        every { getActiveRoadbookUseCase() } returns flowOf(route)

        useCase()

        coVerify(exactly = 0) { repository.saveScrollPosition(any()) }
    }
}
