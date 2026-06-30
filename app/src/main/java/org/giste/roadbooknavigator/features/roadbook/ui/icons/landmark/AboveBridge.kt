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
fun RoadbookIcons.Landmark.aboveBridge(onSurface: Color, surface: Color): ImageVector {
    return ImageVector.Builder(
        name = "Landmark.AboveBridge",
        defaultWidth = 48.dp,
        defaultHeight = 48.dp,
        viewportWidth = 48f,
        viewportHeight = 48f
    ).apply {
        // Bridge floor
        path(fill = SolidColor(surface)) {
            moveTo(15f, 7f)
            horizontalLineToRelative(20f)
            verticalLineToRelative(34f)
            horizontalLineToRelative(-20f)
            close()
        }
        // Bridge structure - Left
        path(
            stroke = SolidColor(onSurface),
            strokeLineWidth = 3f
        ) {
            moveTo(9f, 1f)
            lineToRelative(6f, 6f)
            verticalLineTo(41f)
            lineToRelative(-6f, 6f)
        }
        // Bridge structure - Right
        path(
            stroke = SolidColor(onSurface),
            strokeLineWidth = 3f
        ) {
            moveTo(39f, 1f)
            lineToRelative(-6f, 6f)
            verticalLineTo(41f)
            lineToRelative(6f, 6f)
        }
    }.build()
}
