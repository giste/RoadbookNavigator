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

package org.giste.roadbooknavigator.features.roadbook.ui.icons.signs

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.giste.roadbooknavigator.features.roadbook.ui.icons.RoadbookIcons

@get:Suppress("UnusedReceiverParameter")
val RoadbookIcons.Signs.Alert: ImageVector
    get() {
        if (_Alert != null) {
            return _Alert!!
        }
        _Alert = ImageVector.Builder(
            name = "Signs.Alert",
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
                    fill = SolidColor(Color(0xFFD9D9D9)),
                    stroke = SolidColor(Color.Red),
                    strokeLineWidth = 3f
                ) {
                    moveTo(23.567f, 5.75f)
                    curveTo(23.76f, 5.417f, 24.24f, 5.417f, 24.433f, 5.75f)
                    lineTo(45.218f, 41.75f)
                    curveTo(45.41f, 42.083f, 45.169f, 42.5f, 44.784f, 42.5f)
                    horizontalLineTo(3.216f)
                    curveTo(2.831f, 42.5f, 2.59f, 42.083f, 2.782f, 41.75f)
                    lineTo(23.567f, 5.75f)
                    close()
                }
            }
        }.build()

        return _Alert!!
    }

@Suppress("ObjectPropertyName")
private var _Alert: ImageVector? = null
