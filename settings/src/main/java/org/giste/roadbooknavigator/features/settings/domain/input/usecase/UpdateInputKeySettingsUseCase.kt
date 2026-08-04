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
    /**
     * Updates the selected remote control model and applies its default key mappings
     * if the model is not [RemoteModel.CUSTOM].
     */
    suspend fun selectRemoteModel(model: RemoteModel): Result<Unit> = runCatching {
        repository.setRemoteModel(model)

        if (model != RemoteModel.CUSTOM) {
            val (rbUp, rbDown) = when (model) {
                RemoteModel.DND2 -> listOf(19) to listOf(20) // KEYCODE_DPAD_UP/DOWN
                RemoteModel.TERRA_PIRATA -> listOf(87) to listOf(88) // KEYCODE_MEDIA_NEXT/PREVIOUS
                RemoteModel.CUSTOM -> return@runCatching // Should not happen due to check above
            }
            repository.setRoadbookRemoteKeys(rbUp, rbDown)

            val (odoInc, odoDec, odoRes) = when (model) {
                RemoteModel.DND2 -> Triple(listOf(22), listOf(21), listOf(136)) // DPAD_RIGHT/LEFT, F6
                RemoteModel.TERRA_PIRATA -> Triple(
                    listOf(24),
                    listOf(25),
                    listOf(85, 126)
                ) // VOL_UP/DOWN, PLAY_PAUSE/PLAY
                RemoteModel.CUSTOM -> return@runCatching
            }
            repository.setOdometerRemoteKeys(odoInc, odoDec, odoRes)
        }
    }

    /** Updates roadbook-specific key bindings and sets model to CUSTOM. */
    suspend fun updateRoadbookKeys(up: List<Int>, down: List<Int>): Result<Unit> = runCatching {
        repository.setRemoteModel(RemoteModel.CUSTOM)
        repository.setRoadbookRemoteKeys(up, down)
    }

    /** Updates odometer-specific key bindings and sets model to CUSTOM. */
    suspend fun updateOdometerKeys(
        increase: List<Int>,
        decrease: List<Int>,
        reset: List<Int>
    ): Result<Unit> = runCatching {
        repository.setRemoteModel(RemoteModel.CUSTOM)
        repository.setOdometerRemoteKeys(increase, decrease, reset)
    }
}
