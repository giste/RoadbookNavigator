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

package org.giste.roadbooknavigator.features.roadbook.ui.icons.landmark

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.giste.roadbooknavigator.features.roadbook.ui.icons.RoadbookIcons

@get:Suppress("UnusedReceiverParameter")
val RoadbookIcons.Landmark.House: ImageVector
    get() {
        if (_House != null) {
            return _House!!
        }
        _House = ImageVector.Builder(
            name = "Landmark.House",
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
                path(
                    fill = SolidColor(Color(0xFF757575)),
                    stroke = SolidColor(Color(0xFFE6E0E9)),
                    strokeLineWidth = 2f
                ) {
                    moveTo(35f, 10f)
                    horizontalLineToRelative(6f)
                    verticalLineToRelative(9f)
                    horizontalLineToRelative(-6f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF141218)),
                    stroke = SolidColor(Color(0xFFE6E0E9)),
                    strokeLineWidth = 2f
                ) {
                    moveTo(5f, 25f)
                    horizontalLineToRelative(38f)
                    verticalLineToRelative(22f)
                    horizontalLineToRelative(-38f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF757575)),
                    stroke = SolidColor(Color(0xFFE6E0E9)),
                    strokeLineWidth = 2f
                ) {
                    moveTo(2.5f, 25f)
                    lineTo(24f, 2f)
                    lineTo(45.5f, 25f)
                    horizontalLineTo(2.5f)
                    close()
                }
                path(
                    fill = SolidColor(Color(0xFF141218)),
                    stroke = SolidColor(Color(0xFFE6E0E9)),
                    strokeLineWidth = 2f
                ) {
                    moveTo(19f, 33f)
                    horizontalLineToRelative(10f)
                    verticalLineToRelative(14f)
                    horizontalLineToRelative(-10f)
                    close()
                }
            }
        }.build()

        return _House!!
    }

@Suppress("ObjectPropertyName")
private var _House: ImageVector? = null
