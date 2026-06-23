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

package org.giste.roadbooknavigator.features.roadbook.ui.icons.speed

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import org.giste.roadbooknavigator.features.roadbook.ui.icons.RoadbookIcons

@get:Suppress("UnusedReceiverParameter")
val RoadbookIcons.Speed.Limit110: ImageVector
    get() {
        if (_Limit110 != null) {
            return _Limit110!!
        }
        _Limit110 = ImageVector.Builder(
            name = "Speed.Limit110",
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
                    fill = SolidColor(Color.White),
                    stroke = SolidColor(Color.Red),
                    strokeLineWidth = 4f
                ) {
                    moveTo(24f, 24f)
                    moveToRelative(-22f, 0f)
                    arcToRelative(22f, 22f, 0f, isMoreThanHalf = true, isPositiveArc = true, 44f, 0f)
                    arcToRelative(22f, 22f, 0f, isMoreThanHalf = true, isPositiveArc = true, -44f, 0f)
                }
                path(fill = SolidColor(Color.Black)) {
                    moveTo(14.411f, 15.545f)
                    verticalLineTo(33f)
                    horizontalLineTo(11.769f)
                    verticalLineTo(18.188f)
                    horizontalLineTo(11.667f)
                    lineTo(7.491f, 20.915f)
                    verticalLineTo(18.392f)
                    lineTo(11.846f, 15.545f)
                    horizontalLineTo(14.411f)
                    close()
                    moveTo(24.555f, 15.545f)
                    verticalLineTo(33f)
                    horizontalLineTo(21.913f)
                    verticalLineTo(18.188f)
                    horizontalLineTo(21.811f)
                    lineTo(17.635f, 20.915f)
                    verticalLineTo(18.392f)
                    lineTo(21.99f, 15.545f)
                    horizontalLineTo(24.555f)
                    close()
                    moveTo(34.153f, 33.29f)
                    curveTo(32.807f, 33.284f, 31.656f, 32.929f, 30.702f, 32.224f)
                    curveTo(29.747f, 31.52f, 29.017f, 30.494f, 28.511f, 29.148f)
                    curveTo(28.006f, 27.801f, 27.753f, 26.179f, 27.753f, 24.281f)
                    curveTo(27.753f, 22.389f, 28.006f, 20.773f, 28.511f, 19.432f)
                    curveTo(29.023f, 18.091f, 29.756f, 17.068f, 30.71f, 16.364f)
                    curveTo(31.67f, 15.659f, 32.818f, 15.307f, 34.153f, 15.307f)
                    curveTo(35.489f, 15.307f, 36.633f, 15.662f, 37.588f, 16.372f)
                    curveTo(38.542f, 17.077f, 39.273f, 18.099f, 39.778f, 19.44f)
                    curveTo(40.29f, 20.776f, 40.545f, 22.389f, 40.545f, 24.281f)
                    curveTo(40.545f, 26.185f, 40.292f, 27.81f, 39.787f, 29.156f)
                    curveTo(39.281f, 30.497f, 38.551f, 31.523f, 37.597f, 32.233f)
                    curveTo(36.642f, 32.938f, 35.494f, 33.29f, 34.153f, 33.29f)
                    close()
                    moveTo(34.153f, 31.014f)
                    curveTo(35.335f, 31.014f, 36.258f, 30.438f, 36.923f, 29.284f)
                    curveTo(37.594f, 28.131f, 37.929f, 26.463f, 37.929f, 24.281f)
                    curveTo(37.929f, 22.832f, 37.776f, 21.608f, 37.469f, 20.608f)
                    curveTo(37.167f, 19.602f, 36.733f, 18.841f, 36.165f, 18.324f)
                    curveTo(35.602f, 17.801f, 34.932f, 17.54f, 34.153f, 17.54f)
                    curveTo(32.977f, 17.54f, 32.054f, 18.119f, 31.383f, 19.278f)
                    curveTo(30.713f, 20.438f, 30.375f, 22.105f, 30.369f, 24.281f)
                    curveTo(30.369f, 25.736f, 30.52f, 26.966f, 30.821f, 27.972f)
                    curveTo(31.128f, 28.972f, 31.562f, 29.73f, 32.125f, 30.247f)
                    curveTo(32.687f, 30.758f, 33.364f, 31.014f, 34.153f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit110!!
    }

@Suppress("ObjectPropertyName")
private var _Limit110: ImageVector? = null
