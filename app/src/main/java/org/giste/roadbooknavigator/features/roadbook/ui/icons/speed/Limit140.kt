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
val RoadbookIcons.Speed.Limit140: ImageVector
    get() {
        if (_Limit140 != null) {
            return _Limit140!!
        }
        _Limit140 = ImageVector.Builder(
            name = "Speed.Limit140",
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
                    moveTo(12.232f, 15.545f)
                    verticalLineTo(33f)
                    horizontalLineTo(9.59f)
                    verticalLineTo(18.188f)
                    horizontalLineTo(9.487f)
                    lineTo(5.311f, 20.915f)
                    verticalLineTo(18.392f)
                    lineTo(9.666f, 15.545f)
                    horizontalLineTo(12.232f)
                    close()
                    moveTo(15.387f, 29.591f)
                    verticalLineTo(27.46f)
                    lineTo(22.929f, 15.545f)
                    horizontalLineTo(24.608f)
                    verticalLineTo(18.682f)
                    horizontalLineTo(23.543f)
                    lineTo(18.148f, 27.222f)
                    verticalLineTo(27.358f)
                    horizontalLineTo(28.486f)
                    verticalLineTo(29.591f)
                    horizontalLineTo(15.387f)
                    close()
                    moveTo(23.662f, 33f)
                    verticalLineTo(28.943f)
                    lineTo(23.679f, 27.972f)
                    verticalLineTo(15.545f)
                    horizontalLineTo(26.177f)
                    verticalLineTo(33f)
                    horizontalLineTo(23.662f)
                    close()
                    moveTo(36.333f, 33.29f)
                    curveTo(34.986f, 33.284f, 33.836f, 32.929f, 32.881f, 32.224f)
                    curveTo(31.927f, 31.52f, 31.197f, 30.494f, 30.691f, 29.148f)
                    curveTo(30.185f, 27.801f, 29.933f, 26.179f, 29.933f, 24.281f)
                    curveTo(29.933f, 22.389f, 30.185f, 20.773f, 30.691f, 19.432f)
                    curveTo(31.202f, 18.091f, 31.935f, 17.068f, 32.89f, 16.364f)
                    curveTo(33.85f, 15.659f, 34.998f, 15.307f, 36.333f, 15.307f)
                    curveTo(37.668f, 15.307f, 38.813f, 15.662f, 39.768f, 16.372f)
                    curveTo(40.722f, 17.077f, 41.452f, 18.099f, 41.958f, 19.44f)
                    curveTo(42.469f, 20.776f, 42.725f, 22.389f, 42.725f, 24.281f)
                    curveTo(42.725f, 26.185f, 42.472f, 27.81f, 41.967f, 29.156f)
                    curveTo(41.461f, 30.497f, 40.731f, 31.523f, 39.776f, 32.233f)
                    curveTo(38.822f, 32.938f, 37.674f, 33.29f, 36.333f, 33.29f)
                    close()
                    moveTo(36.333f, 31.014f)
                    curveTo(37.515f, 31.014f, 38.438f, 30.438f, 39.103f, 29.284f)
                    curveTo(39.773f, 28.131f, 40.109f, 26.463f, 40.109f, 24.281f)
                    curveTo(40.109f, 22.832f, 39.955f, 21.608f, 39.648f, 20.608f)
                    curveTo(39.347f, 19.602f, 38.913f, 18.841f, 38.344f, 18.324f)
                    curveTo(37.782f, 17.801f, 37.111f, 17.54f, 36.333f, 17.54f)
                    curveTo(35.157f, 17.54f, 34.234f, 18.119f, 33.563f, 19.278f)
                    curveTo(32.893f, 20.438f, 32.555f, 22.105f, 32.549f, 24.281f)
                    curveTo(32.549f, 25.736f, 32.7f, 26.966f, 33.001f, 27.972f)
                    curveTo(33.307f, 28.972f, 33.742f, 29.73f, 34.305f, 30.247f)
                    curveTo(34.867f, 30.758f, 35.543f, 31.014f, 36.333f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit140!!
    }

@Suppress("ObjectPropertyName")
private var _Limit140: ImageVector? = null
