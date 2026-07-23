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

package org.giste.roadbooknavigator.features.roadbook.ui

import androidx.compose.ui.graphics.Color
import org.giste.roadbooknavigator.features.roadbook.domain.model.Icon
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class IconMapperTest {

    @Test
    fun `getIcon should return a non-null ImageVector for every valid IconType`() {
        // This test ensures that every domain IconType has been correctly
        // mapped to a visual icon in the UI layer.
        
        val onSurface = Color.Black
        val surface = Color.White

        Icon.IconType.entries.forEach { type ->
            val result = IconMapper.getIcon(type, onSurface, surface)
            
            if (type == Icon.IconType.Unknown) {
                assertNull("IconType.Unknown should be mapped to null", result)
            } else {
                assertNotNull(
                    "IconType.$type has no mapped ImageVector in IconMapper. " +
                            "Did you forget to update the 'when' block?",
                    result
                )
            }
        }
    }
}
