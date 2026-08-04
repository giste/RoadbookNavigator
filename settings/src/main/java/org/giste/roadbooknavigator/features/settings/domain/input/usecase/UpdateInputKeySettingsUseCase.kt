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

import org.giste.roadbooknavigator.features.settings.domain.SettingsRepository
import org.giste.roadbooknavigator.features.settings.domain.input.RemoteModel
import javax.inject.Inject

/**
 * Unified use case to update hardware key mappings.
 */
class UpdateInputKeySettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    /** Updates the selected remote control model. */
    suspend fun updateModel(model: RemoteModel): Result<Unit> = runCatching {
        repository.setRemoteModel(model)
    }

    /** Updates roadbook-specific key bindings. */
    suspend fun updateRoadbookKeys(up: List<Int>, down: List<Int>): Result<Unit> = runCatching {
        repository.setRoadbookRemoteKeys(up, down)
    }

    /** Updates odometer-specific key bindings. */
    suspend fun updateOdometerKeys(
        increase: List<Int>,
        decrease: List<Int>,
        reset: List<Int>
    ): Result<Unit> = runCatching {
        repository.setOdometerRemoteKeys(increase, decrease, reset)
    }
}
