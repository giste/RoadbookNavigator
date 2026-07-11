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

package org.giste.roadbooknavigator.features.map.domain.usecase

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.repository.MapRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class GetLocalMapsUseCaseTest {

    private val repository: MapRepository = mockk()
    private val getLocalMapsUseCase = GetLocalMapsUseCase(repository)

    @Test
    fun `invoke should return local maps from repository`() = runTest {
        val expectedMaps = listOf(
            MapFile("spain.map", "/storage/spain.map", 1000L, 123456L, "europe")
        )
        every { repository.getLocalMaps() } returns flowOf(expectedMaps)

        val result = getLocalMapsUseCase().toList()

        assertEquals(listOf(expectedMaps), result)
    }
}
