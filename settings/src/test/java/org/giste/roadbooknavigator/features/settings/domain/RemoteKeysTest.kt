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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteKeysTest {

    @Test
    fun `DND2 preset has correct key codes`() {
        val keys = RemoteKeys.DND2
        assertEquals(listOf(22), keys.increasePartial)   // DPAD_RIGHT
        assertEquals(listOf(21), keys.decreasePartial)   // DPAD_LEFT
        assertEquals(listOf(136), keys.resetPartial)     // F6
    }

    @Test
    fun `Terra Pirata preset has correct key codes`() {
        val keys = RemoteKeys.TERRA_PIRATA
        assertEquals(listOf(24), keys.increasePartial)   // VOLUME_UP
        assertEquals(listOf(25), keys.decreasePartial)   // VOLUME_DOWN
        assertTrue(keys.resetPartial.contains(85))       // MEDIA_PLAY_PAUSE
        assertTrue(keys.resetPartial.contains(126))      // MEDIA_PLAY
    }
}
