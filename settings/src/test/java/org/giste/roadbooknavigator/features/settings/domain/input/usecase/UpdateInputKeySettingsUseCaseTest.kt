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

package org.giste.roadbooknavigator.features.settings.domain.input.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.settings.domain.SettingsRepository
import org.giste.roadbooknavigator.features.settings.domain.input.RemoteModel
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateInputKeySettingsUseCaseTest {

    private val repository: SettingsRepository = mockk()
    private val useCase = UpdateInputKeySettingsUseCase(repository)

    @Test
    fun `updateModel should call repository`() = runTest {
        val model = RemoteModel.TERRA_PIRATA
        coEvery { repository.setRemoteModel(model) } returns Unit

        val result = useCase.updateModel(model)

        assertTrue(result.isSuccess)
        coVerify { repository.setRemoteModel(model) }
    }

    @Test
    fun `updateRoadbookKeys should call repository`() = runTest {
        val up = listOf(1)
        val down = listOf(2)
        coEvery { repository.setRoadbookRemoteKeys(up, down) } returns Unit

        val result = useCase.updateRoadbookKeys(up, down)

        assertTrue(result.isSuccess)
        coVerify { repository.setRoadbookRemoteKeys(up, down) }
    }

    @Test
    fun `updateOdometerKeys should call repository`() = runTest {
        val inc = listOf(1)
        val dec = listOf(2)
        val res = listOf(3)
        coEvery { repository.setOdometerRemoteKeys(inc, dec, res) } returns Unit

        val result = useCase.updateOdometerKeys(inc, dec, res)

        assertTrue(result.isSuccess)
        coVerify { repository.setOdometerRemoteKeys(inc, dec, res) }
    }
}
