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

package org.giste.roadbooknavigator.features.settings.domain.input

/**
 * Unified Value Object representing all hardware key bindings for the application.
 *
 * @property model The selected remote control model.
 * @property upKeys Keys to navigate up in the roadbook.
 * @property downKeys Keys to navigate down in the roadbook.
 * @property increasePartialKeys Keys to increase the odometer partial distance.
 * @property decreasePartialKeys Keys to decrease the odometer partial distance.
 * @property resetPartialKeys Keys to reset the odometer partial distance.
 */
data class InputSettings(
    val model: RemoteModel = RemoteModel.DND2,
    val upKeys: List<Int> = DEFAULT_UP_KEYS,
    val downKeys: List<Int> = DEFAULT_DOWN_KEYS,
    val increasePartialKeys: List<Int> = DEFAULT_INCREASE_KEYS,
    val decreasePartialKeys: List<Int> = DEFAULT_DECREASE_KEYS,
    val resetPartialKeys: List<Int> = DEFAULT_RESET_KEYS,
) {
    companion object {
        /** Default keys for roadbook up (DPAD_UP). */
        val DEFAULT_UP_KEYS: List<Int> = listOf(19)

        /** Default keys for roadbook down (DPAD_DOWN). */
        val DEFAULT_DOWN_KEYS: List<Int> = listOf(20)

        /** Default keys for increase partial (DPAD_RIGHT). */
        val DEFAULT_INCREASE_KEYS: List<Int> = listOf(22)

        /** Default keys for decrease partial (DPAD_LEFT). */
        val DEFAULT_DECREASE_KEYS: List<Int> = listOf(21)

        /** Default keys for reset partial (F6). */
        val DEFAULT_RESET_KEYS: List<Int> = listOf(136)
    }
}
