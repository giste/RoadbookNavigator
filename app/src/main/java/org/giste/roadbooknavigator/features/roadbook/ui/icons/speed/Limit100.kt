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
val RoadbookIcons.Speed.Limit100: ImageVector
    get() {
        if (_Limit100 != null) {
            return _Limit100!!
        }
        _Limit100 = ImageVector.Builder(
            name = "Speed.Limit100",
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
                    arcToRelative(
                        22f,
                        22f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        44f,
                        0f
                    )
                    arcToRelative(
                        22f,
                        22f,
                        0f,
                        isMoreThanHalf = true,
                        isPositiveArc = true,
                        -44f,
                        0f
                    )
                }
                path(fill = SolidColor(Color.Black)) {
                    moveTo(12.337f, 15.545f)
                    verticalLineTo(33f)
                    horizontalLineTo(9.695f)
                    verticalLineTo(18.188f)
                    horizontalLineTo(9.593f)
                    lineTo(5.417f, 20.915f)
                    verticalLineTo(18.392f)
                    lineTo(9.772f, 15.545f)
                    horizontalLineTo(12.337f)
                    close()
                    moveTo(21.935f, 33.29f)
                    curveTo(20.589f, 33.284f, 19.438f, 32.929f, 18.484f, 32.224f)
                    curveTo(17.529f, 31.52f, 16.799f, 30.494f, 16.293f, 29.148f)
                    curveTo(15.788f, 27.801f, 15.535f, 26.179f, 15.535f, 24.281f)
                    curveTo(15.535f, 22.389f, 15.788f, 20.773f, 16.293f, 19.432f)
                    curveTo(16.805f, 18.091f, 17.538f, 17.068f, 18.492f, 16.364f)
                    curveTo(19.452f, 15.659f, 20.6f, 15.307f, 21.935f, 15.307f)
                    curveTo(23.271f, 15.307f, 24.416f, 15.662f, 25.37f, 16.372f)
                    curveTo(26.325f, 17.077f, 27.055f, 18.099f, 27.56f, 19.44f)
                    curveTo(28.072f, 20.776f, 28.327f, 22.389f, 28.327f, 24.281f)
                    curveTo(28.327f, 26.185f, 28.075f, 27.81f, 27.569f, 29.156f)
                    curveTo(27.063f, 30.497f, 26.333f, 31.523f, 25.379f, 32.233f)
                    curveTo(24.424f, 32.938f, 23.276f, 33.29f, 21.935f, 33.29f)
                    close()
                    moveTo(21.935f, 31.014f)
                    curveTo(23.117f, 31.014f, 24.041f, 30.438f, 24.705f, 29.284f)
                    curveTo(25.376f, 28.131f, 25.711f, 26.463f, 25.711f, 24.281f)
                    curveTo(25.711f, 22.832f, 25.558f, 21.608f, 25.251f, 20.608f)
                    curveTo(24.95f, 19.602f, 24.515f, 18.841f, 23.947f, 18.324f)
                    curveTo(23.384f, 17.801f, 22.714f, 17.54f, 21.935f, 17.54f)
                    curveTo(20.759f, 17.54f, 19.836f, 18.119f, 19.166f, 19.278f)
                    curveTo(18.495f, 20.438f, 18.157f, 22.105f, 18.151f, 24.281f)
                    curveTo(18.151f, 25.736f, 18.302f, 26.966f, 18.603f, 27.972f)
                    curveTo(18.91f, 28.972f, 19.344f, 29.73f, 19.907f, 30.247f)
                    curveTo(20.469f, 30.758f, 21.146f, 31.014f, 21.935f, 31.014f)
                    close()
                    moveTo(36.228f, 33.29f)
                    curveTo(34.881f, 33.284f, 33.73f, 32.929f, 32.776f, 32.224f)
                    curveTo(31.821f, 31.52f, 31.091f, 30.494f, 30.586f, 29.148f)
                    curveTo(30.08f, 27.801f, 29.827f, 26.179f, 29.827f, 24.281f)
                    curveTo(29.827f, 22.389f, 30.08f, 20.773f, 30.586f, 19.432f)
                    curveTo(31.097f, 18.091f, 31.83f, 17.068f, 32.784f, 16.364f)
                    curveTo(33.745f, 15.659f, 34.892f, 15.307f, 36.228f, 15.307f)
                    curveTo(37.563f, 15.307f, 38.708f, 15.662f, 39.662f, 16.372f)
                    curveTo(40.617f, 17.077f, 41.347f, 18.099f, 41.853f, 19.44f)
                    curveTo(42.364f, 20.776f, 42.62f, 22.389f, 42.62f, 24.281f)
                    curveTo(42.62f, 26.185f, 42.367f, 27.81f, 41.861f, 29.156f)
                    curveTo(41.355f, 30.497f, 40.625f, 31.523f, 39.671f, 32.233f)
                    curveTo(38.716f, 32.938f, 37.569f, 33.29f, 36.228f, 33.29f)
                    close()
                    moveTo(36.228f, 31.014f)
                    curveTo(37.409f, 31.014f, 38.333f, 30.438f, 38.997f, 29.284f)
                    curveTo(39.668f, 28.131f, 40.003f, 26.463f, 40.003f, 24.281f)
                    curveTo(40.003f, 22.832f, 39.85f, 21.608f, 39.543f, 20.608f)
                    curveTo(39.242f, 19.602f, 38.807f, 18.841f, 38.239f, 18.324f)
                    curveTo(37.676f, 17.801f, 37.006f, 17.54f, 36.228f, 17.54f)
                    curveTo(35.051f, 17.54f, 34.128f, 18.119f, 33.458f, 19.278f)
                    curveTo(32.787f, 20.438f, 32.449f, 22.105f, 32.444f, 24.281f)
                    curveTo(32.444f, 25.736f, 32.594f, 26.966f, 32.895f, 27.972f)
                    curveTo(33.202f, 28.972f, 33.637f, 29.73f, 34.199f, 30.247f)
                    curveTo(34.762f, 30.758f, 35.438f, 31.014f, 36.228f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit100!!
    }

@Suppress("ObjectPropertyName")
private var _Limit100: ImageVector? = null
