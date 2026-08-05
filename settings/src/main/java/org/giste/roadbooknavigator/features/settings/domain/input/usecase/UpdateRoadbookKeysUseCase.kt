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

import org.giste.roadbooknavigator.features.settings.domain.input.InputSettingsRepository
import org.giste.roadbooknavigator.features.settings.domain.input.RemoteModel
import javax.inject.Inject

/**
 * Use case to update roadbook-specific key bindings.
 */
class UpdateRoadbookKeysUseCase @Inject constructor(
    private val repository: InputSettingsRepository,
    private val updateRemoteModelUseCase: UpdateRemoteModelUseCase
) {
    /** Updates roadbook-specific key bindings and sets model to CUSTOM. */
    suspend operator fun invoke(up: List<Int>, down: List<Int>): Result<Unit> = runCatching {
        updateRemoteModelUseCase(RemoteModel.CUSTOM).getOrThrow()
        repository.setRoadbookKeys(up, down)
    }
}
