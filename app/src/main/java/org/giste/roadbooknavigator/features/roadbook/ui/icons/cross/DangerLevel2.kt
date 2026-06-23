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

package org.giste.roadbooknavigator.features.roadbook.ui.icons.cross

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.giste.roadbooknavigator.features.roadbook.ui.icons.RoadbookIcons

@get:Suppress("UnusedReceiverParameter")
val RoadbookIcons.Cross.DangerLevel2: ImageVector
    get() {
        if (_DangerLevel2 != null) {
            return _DangerLevel2!!
        }
        _DangerLevel2 = ImageVector.Builder(
            name = "Cross.DangerLevel2",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            path(fill = SolidColor(Color(0xFFB3261E))) {
                moveTo(27f, 34f)
                horizontalLineToRelative(12f)
                verticalLineToRelative(14f)
                horizontalLineToRelative(-12f)
                close()
            }
            path(fill = SolidColor(Color(0xFFB3261E))) {
                moveTo(27f, 12f)
                lineTo(29f, 32f)
                horizontalLineTo(37f)
                lineTo(39f, 12f)
                verticalLineTo(0f)
                horizontalLineTo(27f)
                verticalLineTo(12f)
                close()
            }
            path(fill = SolidColor(Color(0xFFB3261E))) {
                moveTo(9f, 34f)
                horizontalLineToRelative(12f)
                verticalLineToRelative(14f)
                horizontalLineToRelative(-12f)
                close()
            }
            path(fill = SolidColor(Color(0xFFB3261E))) {
                moveTo(9f, 12f)
                lineTo(11f, 32f)
                horizontalLineTo(19f)
                lineTo(21f, 12f)
                verticalLineTo(0f)
                horizontalLineTo(9f)
                verticalLineTo(12f)
                close()
            }
        }.build()

        return _DangerLevel2!!
    }

@Suppress("ObjectPropertyName")
private var _DangerLevel2: ImageVector? = null
