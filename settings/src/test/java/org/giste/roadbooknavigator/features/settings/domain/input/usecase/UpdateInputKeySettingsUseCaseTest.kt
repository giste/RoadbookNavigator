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

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.settings.domain.AppSettingsRepository
import org.giste.roadbooknavigator.features.settings.domain.input.RemoteModel
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateInputKeySettingsUseCaseTest {

    private val repository: AppSettingsRepository = mockk(relaxed = true)
    private val useCase = UpdateInputKeySettingsUseCase(repository)

    @Test
    fun `selectRemoteModel DND2 should update model and apply defaults`() = runTest {
        val result = useCase.selectRemoteModel(RemoteModel.DND2)

        assertTrue(result.isSuccess)
        coVerify { repository.setRemoteModel(RemoteModel.DND2) }
        coVerify { repository.setRoadbookRemoteKeys(listOf(19), listOf(20)) }
        coVerify { repository.setOdometerRemoteKeys(listOf(22), listOf(21), listOf(136)) }
    }

    @Test
    fun `selectRemoteModel TERRA_PIRATA should update model and apply defaults`() = runTest {
        val result = useCase.selectRemoteModel(RemoteModel.TERRA_PIRATA)

        assertTrue(result.isSuccess)
        coVerify { repository.setRemoteModel(RemoteModel.TERRA_PIRATA) }
        coVerify { repository.setRoadbookRemoteKeys(listOf(87), listOf(88)) }
        coVerify { repository.setOdometerRemoteKeys(listOf(24), listOf(25), listOf(85, 126)) }
    }

    @Test
    fun `selectRemoteModel CUSTOM should only update model`() = runTest {
        val result = useCase.selectRemoteModel(RemoteModel.CUSTOM)

        assertTrue(result.isSuccess)
        coVerify { repository.setRemoteModel(RemoteModel.CUSTOM) }
        coVerify(exactly = 0) { repository.setRoadbookRemoteKeys(any(), any()) }
        coVerify(exactly = 0) { repository.setOdometerRemoteKeys(any(), any(), any()) }
    }

    @Test
    fun `updateRoadbookKeys should update keys and set model to CUSTOM`() = runTest {
        val up = listOf(1)
        val down = listOf(2)

        val result = useCase.updateRoadbookKeys(up, down)

        assertTrue(result.isSuccess)
        coVerify { repository.setRemoteModel(RemoteModel.CUSTOM) }
        coVerify { repository.setRoadbookRemoteKeys(up, down) }
    }

    @Test
    fun `updateOdometerKeys should update keys and set model to CUSTOM`() = runTest {
        val inc = listOf(1)
        val dec = listOf(2)
        val res = listOf(3)

        val result = useCase.updateOdometerKeys(inc, dec, res)

        assertTrue(result.isSuccess)
        coVerify { repository.setRemoteModel(RemoteModel.CUSTOM) }
        coVerify { repository.setOdometerRemoteKeys(inc, dec, res) }
    }
}
