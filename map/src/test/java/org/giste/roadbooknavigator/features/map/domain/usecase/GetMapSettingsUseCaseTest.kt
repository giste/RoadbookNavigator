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
 * along with this program.  See the License for more details.
 */

package org.giste.roadbooknavigator.features.map.domain.usecase

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.map.domain.model.MapSettings
import org.giste.roadbooknavigator.features.map.domain.repository.MapSettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class GetMapSettingsUseCaseTest {

    private val repository: MapSettingsRepository = mockk()
    private val getMapSettingsUseCase = GetMapSettingsUseCase(repository)

    @Test
    fun `invoke should return map settings from repository`() = runTest {
        val expectedSettings = MapSettings(initialZoom = 18, initialTilt = 45f)
        every { repository.getMapSettings() } returns flowOf(expectedSettings)

        val result = getMapSettingsUseCase().toList()

        assertEquals(listOf(expectedSettings), result)
    }
}
