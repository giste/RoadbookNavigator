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
val RoadbookIcons.Speed.Limit40: ImageVector
    get() {
        if (_Limit40 != null) {
            return _Limit40!!
        }
        _Limit40 = ImageVector.Builder(
            name = "Speed.Limit40",
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
                    moveTo(10.315f, 29.591f)
                    verticalLineTo(27.46f)
                    lineTo(17.858f, 15.545f)
                    horizontalLineTo(19.536f)
                    verticalLineTo(18.682f)
                    horizontalLineTo(18.471f)
                    lineTo(13.076f, 27.222f)
                    verticalLineTo(27.358f)
                    horizontalLineTo(23.414f)
                    verticalLineTo(29.591f)
                    horizontalLineTo(10.315f)
                    close()
                    moveTo(18.59f, 33f)
                    verticalLineTo(28.943f)
                    lineTo(18.608f, 27.972f)
                    verticalLineTo(15.545f)
                    horizontalLineTo(21.105f)
                    verticalLineTo(33f)
                    horizontalLineTo(18.59f)
                    close()
                    moveTo(31.261f, 33.29f)
                    curveTo(29.915f, 33.284f, 28.764f, 32.929f, 27.809f, 32.224f)
                    curveTo(26.855f, 31.52f, 26.125f, 30.494f, 25.619f, 29.148f)
                    curveTo(25.113f, 27.801f, 24.861f, 26.179f, 24.861f, 24.281f)
                    curveTo(24.861f, 22.389f, 25.113f, 20.773f, 25.619f, 19.432f)
                    curveTo(26.131f, 18.091f, 26.863f, 17.068f, 27.818f, 16.364f)
                    curveTo(28.778f, 15.659f, 29.926f, 15.307f, 31.261f, 15.307f)
                    curveTo(32.596f, 15.307f, 33.741f, 15.662f, 34.696f, 16.372f)
                    curveTo(35.65f, 17.077f, 36.381f, 18.099f, 36.886f, 19.44f)
                    curveTo(37.397f, 20.776f, 37.653f, 22.389f, 37.653f, 24.281f)
                    curveTo(37.653f, 26.185f, 37.4f, 27.81f, 36.895f, 29.156f)
                    curveTo(36.389f, 30.497f, 35.659f, 31.523f, 34.704f, 32.233f)
                    curveTo(33.75f, 32.938f, 32.602f, 33.29f, 31.261f, 33.29f)
                    close()
                    moveTo(31.261f, 31.014f)
                    curveTo(32.443f, 31.014f, 33.366f, 30.438f, 34.031f, 29.284f)
                    curveTo(34.701f, 28.131f, 35.037f, 26.463f, 35.037f, 24.281f)
                    curveTo(35.037f, 22.832f, 34.883f, 21.608f, 34.576f, 20.608f)
                    curveTo(34.275f, 19.602f, 33.841f, 18.841f, 33.272f, 18.324f)
                    curveTo(32.71f, 17.801f, 32.04f, 17.54f, 31.261f, 17.54f)
                    curveTo(30.085f, 17.54f, 29.162f, 18.119f, 28.491f, 19.278f)
                    curveTo(27.821f, 20.438f, 27.483f, 22.105f, 27.477f, 24.281f)
                    curveTo(27.477f, 25.736f, 27.628f, 26.966f, 27.929f, 27.972f)
                    curveTo(28.236f, 28.972f, 28.67f, 29.73f, 29.233f, 30.247f)
                    curveTo(29.795f, 30.758f, 30.471f, 31.014f, 31.261f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit40!!
    }

@Suppress("ObjectPropertyName")
private var _Limit40: ImageVector? = null
