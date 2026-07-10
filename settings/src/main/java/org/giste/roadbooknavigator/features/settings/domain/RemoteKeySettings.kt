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

package org.giste.roadbooknavigator.features.settings.domain

/**
 * Represents the configuration for remote control keys.
 *
 * @property model The selected remote model.
 * @property customKeys Custom key mappings used when [model] is [RemoteModel.CUSTOM].
 */
data class RemoteKeySettings(
    val model: RemoteModel = RemoteModel.DND2,
    val customKeys: RemoteKeys = RemoteKeys.DND2
)

/**
 * Represents a set of key codes for remote control actions.
 *
 * @property roadbookUp Keys to navigate up in the roadbook.
 * @property roadbookDown Keys to navigate down in the roadbook.
 * @property increasePartial Keys to increase the partial distance.
 * @property decreasePartial Keys to decrease the partial distance.
 * @property resetPartial Keys to reset the partial distance.
 */
data class RemoteKeys(
    val roadbookUp: List<Int>,
    val roadbookDown: List<Int>,
    val increasePartial: List<Int>,
    val decreasePartial: List<Int>,
    val resetPartial: List<Int>,
) {
    companion object {
        // Native Android keycodes
        private const val KEYCODE_DPAD_UP = 19
        private const val KEYCODE_DPAD_DOWN = 20
        private const val KEYCODE_DPAD_LEFT = 21
        private const val KEYCODE_DPAD_RIGHT = 22
        private const val KEYCODE_VOLUME_UP = 24
        private const val KEYCODE_VOLUME_DOWN = 25
        private const val KEYCODE_MEDIA_PLAY_PAUSE = 85
        private const val KEYCODE_MEDIA_NEXT = 87
        private const val KEYCODE_MEDIA_PREVIOUS = 88
        private const val KEYCODE_MEDIA_PLAY = 126
        private const val KEYCODE_F6 = 136

        /** Default keys for DND2 remote. */
        val DND2 = RemoteKeys(
            roadbookUp = listOf(KEYCODE_DPAD_UP),
            roadbookDown = listOf(KEYCODE_DPAD_DOWN),
            increasePartial = listOf(KEYCODE_DPAD_RIGHT),
            decreasePartial = listOf(KEYCODE_DPAD_LEFT),
            resetPartial = listOf(KEYCODE_F6),
        )

        /** Default keys for Terra Pirata remote. */
        val TERRA_PIRATA = RemoteKeys(
            roadbookUp = listOf(KEYCODE_MEDIA_NEXT),
            roadbookDown = listOf(KEYCODE_MEDIA_PREVIOUS),
            increasePartial = listOf(KEYCODE_VOLUME_UP),
            decreasePartial = listOf(KEYCODE_VOLUME_DOWN),
            resetPartial = listOf(KEYCODE_MEDIA_PLAY_PAUSE, KEYCODE_MEDIA_PLAY),
        )
    }
}
