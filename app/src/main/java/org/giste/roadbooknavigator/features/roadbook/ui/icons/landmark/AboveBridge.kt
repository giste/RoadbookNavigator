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

@get:Suppress("UnusedReceiverParameter")
val RoadbookIcons.Landmark.AboveBridge: ImageVector
    get() {
        if (_AboveBridge != null) {
            return _AboveBridge!!
        }
        _AboveBridge = ImageVector.Builder(
            name = "Landmark.AboveBridge",
            defaultWidth = 48.dp,
            defaultHeight = 48.dp,
            viewportWidth = 48f,
            viewportHeight = 48f
        ).apply {
            path(fill = SolidColor(Color(0xFF141218))) {
                moveTo(13f, 6.5f)
                horizontalLineToRelative(24f)
                verticalLineToRelative(35f)
                horizontalLineToRelative(-24f)
                close()
            }
            path(
                stroke = SolidColor(Color(0xFFE6E0E9)),
                strokeLineWidth = 2f
            ) {
                moveTo(44f, 1f)
                lineTo(38f, 7f)
                verticalLineTo(41f)
                lineTo(44f, 47f)
            }
            path(
                stroke = SolidColor(Color(0xFFE6E0E9)),
                strokeLineWidth = 2f
            ) {
                moveTo(6f, 1f)
                lineTo(12f, 7f)
                verticalLineTo(41f)
                lineTo(6f, 47f)
            }
        }.build()

        return _AboveBridge!!
    }

@Suppress("ObjectPropertyName")
private var _AboveBridge: ImageVector? = null
