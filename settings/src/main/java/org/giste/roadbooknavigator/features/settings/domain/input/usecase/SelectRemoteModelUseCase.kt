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
 * Use case to select a remote control model and apply its default key mappings.
 */
class SelectRemoteModelUseCase @Inject constructor(
    private val repository: InputSettingsRepository,
    private val updateRemoteModelUseCase: UpdateRemoteModelUseCase
) {
    /**
     * Updates the selected remote control model and applies its default key mappings
     * if the model is not [RemoteModel.CUSTOM].
     */
    suspend operator fun invoke(model: RemoteModel): Result<Unit> = runCatching {
        updateRemoteModelUseCase(model).getOrThrow()

        if (model != RemoteModel.CUSTOM) {
            val (rbUp, rbDown) = when (model) {
                RemoteModel.DND2 -> listOf(19) to listOf(20) // KEYCODE_DPAD_UP/DOWN
                RemoteModel.TERRA_PIRATA -> listOf(87) to listOf(88) // KEYCODE_MEDIA_NEXT/PREVIOUS
                RemoteModel.CUSTOM -> return@runCatching // Should not happen due to check above
            }
            repository.setRoadbookKeys(rbUp, rbDown)

            val (odoInc, odoDec, odoRes) = when (model) {
                RemoteModel.DND2 -> Triple(listOf(22), listOf(21), listOf(136)) // DPAD_RIGHT/LEFT, F6
                RemoteModel.TERRA_PIRATA -> Triple(
                    listOf(24),
                    listOf(25),
                    listOf(85, 126)
                ) // VOL_UP/DOWN, PLAY_PAUSE/PLAY
                RemoteModel.CUSTOM -> return@runCatching
            }
            repository.setOdometerKeys(odoInc, odoDec, odoRes)
        }
    }
}
