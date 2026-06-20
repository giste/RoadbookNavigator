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

package org.giste.roadbooknavigator.features.roadbook.data

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream

@OptIn(ExperimentalCoroutinesApi::class)
class RoadbookRepositoryImplTest {

    private lateinit var repository: RoadbookRepositoryImpl
    private val mapper: Rn2Mapper = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        repository = RoadbookRepositoryImpl(mapper, testDispatcher)
    }

    @Test
    fun `loadRoadbook should read input stream and return successful route`() = runTest {
        // Given
        val jsonContent = "{\"test\": \"json\"}"
        val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
        val expectedRoute = mockk<Route>()
        
        every { mapper.mapToDomain(jsonContent) } returns expectedRoute

        // When
        val result = repository.loadRoadbook(inputStream)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedRoute, result.getOrNull())
    }

    @Test
    fun `loadRoadbook should return failure when mapper throws exception`() = runTest {
        // Given
        val jsonContent = "invalid json"
        val inputStream = ByteArrayInputStream(jsonContent.toByteArray())
        val exception = RuntimeException("Parsing failed")
        
        every { mapper.mapToDomain(jsonContent) } throws exception

        // When
        val result = repository.loadRoadbook(inputStream)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
