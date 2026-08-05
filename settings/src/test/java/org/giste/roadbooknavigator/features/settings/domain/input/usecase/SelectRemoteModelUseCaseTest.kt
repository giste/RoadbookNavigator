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
 * along with this program.  See <https://www.gnu.org/licenses/>.
 */

package org.giste.roadbooknavigator.features.settings.domain.input.usecase

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.settings.domain.input.InputSettingsRepository
import org.giste.roadbooknavigator.features.settings.domain.input.RemoteModel
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SelectRemoteModelUseCaseTest {

    private val repository: InputSettingsRepository = mockk(relaxed = true)
    private val updateRemoteModelUseCase: UpdateRemoteModelUseCase = mockk()
    private lateinit var useCase: SelectRemoteModelUseCase

    @Before
    fun setup() {
        useCase = SelectRemoteModelUseCase(repository, updateRemoteModelUseCase)
        coEvery { updateRemoteModelUseCase(any()) } returns Result.success(Unit)
    }

    @Test
    fun `invoke DND2 should update model and apply defaults`() = runTest {
        val result = useCase(RemoteModel.DND2)

        assertTrue(result.isSuccess)
        coVerify { updateRemoteModelUseCase(RemoteModel.DND2) }
        coVerify { repository.setRoadbookKeys(listOf(19), listOf(20)) }
        coVerify { repository.setOdometerKeys(listOf(22), listOf(21), listOf(136)) }
    }

    @Test
    fun `invoke TERRA_PIRATA should update model and apply defaults`() = runTest {
        val result = useCase(RemoteModel.TERRA_PIRATA)

        assertTrue(result.isSuccess)
        coVerify { updateRemoteModelUseCase(RemoteModel.TERRA_PIRATA) }
        coVerify { repository.setRoadbookKeys(listOf(87), listOf(88)) }
        coVerify { repository.setOdometerKeys(listOf(24), listOf(25), listOf(85, 126)) }
    }

    @Test
    fun `invoke CUSTOM should only update model`() = runTest {
        val result = useCase(RemoteModel.CUSTOM)

        assertTrue(result.isSuccess)
        coVerify { updateRemoteModelUseCase(RemoteModel.CUSTOM) }
        coVerify(exactly = 0) { repository.setRoadbookKeys(any(), any()) }
        coVerify(exactly = 0) { repository.setOdometerKeys(any(), any(), any()) }
    }
}
