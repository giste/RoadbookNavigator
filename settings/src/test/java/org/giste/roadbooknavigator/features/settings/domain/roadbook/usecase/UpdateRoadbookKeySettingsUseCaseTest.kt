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

package org.giste.roadbooknavigator.features.settings.domain.roadbook.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.settings.domain.SettingsRepository
import org.junit.Test

class UpdateRoadbookKeySettingsUseCaseTest {

    private val repository: SettingsRepository = mockk()
    private val useCase = UpdateRoadbookKeySettingsUseCase(repository)

    @Test
    fun `invoke should call repository setRoadbookRemoteKeys`() = runTest {
        val up = listOf(1)
        val down = listOf(2)
        coEvery { repository.setRoadbookRemoteKeys(up, down) } returns Unit

        useCase(up, down)

        coVerify { repository.setRoadbookRemoteKeys(up, down) }
    }
}
