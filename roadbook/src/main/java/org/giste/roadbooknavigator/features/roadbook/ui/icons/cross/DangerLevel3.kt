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
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.giste.roadbooknavigator.features.roadbook.ui.icons.RoadbookIcons

@get:Suppress("UnusedReceiverParameter")
val RoadbookIcons.Cross.DangerLevel3: ImageVector
    get() {
        if (_DangerLevel3 != null) {
            return _DangerLevel3!!
        }
        _DangerLevel3 = ImageVector.Builder(
            name = "Cross.DangerLevel3",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            group(
                clipPathData = PathData {
                    moveTo(0f, 0f)
                    horizontalLineToRelative(48f)
                    verticalLineToRelative(48f)
                    horizontalLineToRelative(-48f)
                    close()
                }
            ) {
                path(fill = SolidColor(Color(0xFFB3261E))) {
                    moveTo(36f, 34f)
                    horizontalLineToRelative(12f)
                    verticalLineToRelative(14f)
                    horizontalLineToRelative(-12f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB3261E))) {
                    moveTo(36f, 12f)
                    lineTo(38f, 32f)
                    horizontalLineTo(46f)
                    lineTo(48f, 12f)
                    verticalLineTo(0f)
                    horizontalLineTo(36f)
                    verticalLineTo(12f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB3261E))) {
                    moveTo(18f, 34f)
                    horizontalLineToRelative(12f)
                    verticalLineToRelative(14f)
                    horizontalLineToRelative(-12f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB3261E))) {
                    moveTo(18f, 12f)
                    lineTo(20f, 32f)
                    horizontalLineTo(28f)
                    lineTo(30f, 12f)
                    verticalLineTo(0f)
                    horizontalLineTo(18f)
                    verticalLineTo(12f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB3261E))) {
                    moveTo(0f, 34f)
                    horizontalLineToRelative(12f)
                    verticalLineToRelative(14f)
                    horizontalLineToRelative(-12f)
                    close()
                }
                path(fill = SolidColor(Color(0xFFB3261E))) {
                    moveTo(0f, 12f)
                    lineTo(2f, 32f)
                    horizontalLineTo(10f)
                    lineTo(12f, 12f)
                    verticalLineTo(0f)
                    horizontalLineTo(0f)
                    verticalLineTo(12f)
                    close()
                }
            }
        }.build()

        return _DangerLevel3!!
    }

@Suppress("ObjectPropertyName")
private var _DangerLevel3: ImageVector? = null
