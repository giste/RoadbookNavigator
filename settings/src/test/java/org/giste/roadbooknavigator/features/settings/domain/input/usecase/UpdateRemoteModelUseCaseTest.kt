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
import org.giste.roadbooknavigator.features.settings.domain.input.InputSettingsRepository
import org.giste.roadbooknavigator.features.settings.domain.input.RemoteModel
import org.junit.Assert.assertTrue
import org.junit.Test

class UpdateRemoteModelUseCaseTest {

    private val repository: InputSettingsRepository = mockk(relaxed = true)
    private val useCase = UpdateRemoteModelUseCase(repository)

    @Test
    fun `invoke should update remote model in repository`() = runTest {
        val model = RemoteModel.TERRA_PIRATA
        val result = useCase(model)

        assertTrue(result.isSuccess)
        coVerify { repository.setRemoteModel(model) }
    }
}
