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
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.giste.roadbooknavigator.features.roadbook.ui.icons.RoadbookIcons

@Suppress("UnusedReceiverParameter")
fun RoadbookIcons.Landmark.underBridge(onSurface: Color, surface: Color): ImageVector {
    return ImageVector.Builder(
            name = "Landmark.UnderBridge",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            path(fill = SolidColor(surface)) {
                moveTo(41.5f, 12f)
                lineToRelative(-0f, 24f)
                lineToRelative(-35f, 0f)
                lineToRelative(-0f, -24f)
                close()
            }
            path(
                stroke = SolidColor(onSurface),
                strokeLineWidth = 2f
            ) {
                moveTo(47f, 43f)
                lineTo(41f, 37f)
                horizontalLineTo(7f)
                lineTo(1f, 43f)
            }
            path(
                stroke = SolidColor(onSurface),
                strokeLineWidth = 2f
            ) {
                moveTo(47f, 5f)
                lineTo(41f, 11f)
                horizontalLineTo(7f)
                lineTo(1f, 5f)
            }
        }.build()
    }
