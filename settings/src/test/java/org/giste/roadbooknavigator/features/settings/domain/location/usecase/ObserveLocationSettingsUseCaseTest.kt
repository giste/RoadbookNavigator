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

package org.giste.roadbooknavigator.features.settings.domain.location.usecase

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.giste.location.LocationSettingsProvider
import org.giste.roadbooknavigator.features.settings.domain.location.LocationSettings
import org.junit.Assert.assertEquals
import org.junit.Test
import org.giste.location.LocationSettings as ModuleLocationSettings

class ObserveLocationSettingsUseCaseTest {

    private val provider: LocationSettingsProvider = mockk()
    private val useCase = ObserveLocationSettingsUseCase(provider)

    @Test
    fun `should emit settings from provider`() = runTest {
        val moduleSettings = ModuleLocationSettings(pollingInterval = 1000L, minDistance = 5f)
        every { provider.settings } returns flowOf(moduleSettings)

        val result = useCase().first()

        assertEquals(1000L, result.pollingInterval)
        assertEquals(5f, result.minDistance)
    }
}
