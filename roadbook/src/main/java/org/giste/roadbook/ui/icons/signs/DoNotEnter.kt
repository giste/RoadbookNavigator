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

package org.giste.roadbook.ui.icons.signs

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.giste.roadbook.ui.icons.RoadbookIcons

@Suppress("UnusedReceiverParameter")
internal fun RoadbookIcons.Signs.doNotEnter(onBackground: Color): ImageVector {
    return ImageVector.Builder(
            name = "DoNotEnter",
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
                    fill = SolidColor(Color.Red),
                    stroke = SolidColor(onBackground),
                    strokeLineWidth = 1f
                ) {
                    moveTo(24f, 24f)
                    moveToRelative(-23.5f, 0f)
                    arcToRelative(23.5f, 23.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, 47f, 0f)
                    arcToRelative(23.5f, 23.5f, 0f, isMoreThanHalf = true, isPositiveArc = true, -47f, 0f)
                }
                path(fill = SolidColor(Color(0xFFD9D9D9))) {
                    moveTo(6f, 17f)
                    horizontalLineToRelative(36f)
                    verticalLineToRelative(14f)
                    horizontalLineToRelative(-36f)
                    close()
                }
            }
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 0.1f
            ) {
                moveTo(0.05f, 0.05f)
                horizontalLineToRelative(47.9f)
                verticalLineToRelative(47.9f)
                horizontalLineToRelative(-47.9f)
                close()
            }
        }.build()
    }
