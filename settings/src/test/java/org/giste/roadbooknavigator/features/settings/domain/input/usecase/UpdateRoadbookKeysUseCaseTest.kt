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

class UpdateRoadbookKeysUseCaseTest {

    private val repository: InputSettingsRepository = mockk(relaxed = true)
    private val updateRemoteModelUseCase: UpdateRemoteModelUseCase = mockk()
    private lateinit var useCase: UpdateRoadbookKeysUseCase

    @Before
    fun setup() {
        useCase = UpdateRoadbookKeysUseCase(repository, updateRemoteModelUseCase)
        coEvery { updateRemoteModelUseCase(any()) } returns Result.success(Unit)
    }

    @Test
    fun `invoke should update keys and set model to CUSTOM`() = runTest {
        val up = listOf(1)
        val down = listOf(2)

        val result = useCase(up, down)

        assertTrue(result.isSuccess)
        coVerify { updateRemoteModelUseCase(RemoteModel.CUSTOM) }
        coVerify { repository.setRoadbookKeys(up, down) }
    }
}
