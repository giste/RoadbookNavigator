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
val RoadbookIcons.Speed.Limit130: ImageVector
    get() {
        if (_Limit130 != null) {
            return _Limit130!!
        }
        _Limit130 = ImageVector.Builder(
            name = "Speed.Limit130",
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
                    moveTo(12.349f, 15.545f)
                    verticalLineTo(33f)
                    horizontalLineTo(9.707f)
                    verticalLineTo(18.188f)
                    horizontalLineTo(9.604f)
                    lineTo(5.428f, 20.915f)
                    verticalLineTo(18.392f)
                    lineTo(9.783f, 15.545f)
                    horizontalLineTo(12.349f)
                    close()
                    moveTo(21.947f, 33.239f)
                    curveTo(20.777f, 33.239f, 19.731f, 33.037f, 18.811f, 32.633f)
                    curveTo(17.896f, 32.23f, 17.171f, 31.67f, 16.637f, 30.955f)
                    curveTo(16.109f, 30.233f, 15.825f, 29.398f, 15.785f, 28.449f)
                    horizontalLineTo(18.461f)
                    curveTo(18.495f, 28.966f, 18.669f, 29.415f, 18.981f, 29.795f)
                    curveTo(19.299f, 30.17f, 19.714f, 30.46f, 20.226f, 30.665f)
                    curveTo(20.737f, 30.869f, 21.305f, 30.972f, 21.93f, 30.972f)
                    curveTo(22.618f, 30.972f, 23.226f, 30.852f, 23.754f, 30.614f)
                    curveTo(24.288f, 30.375f, 24.706f, 30.043f, 25.007f, 29.617f)
                    curveTo(25.308f, 29.185f, 25.458f, 28.688f, 25.458f, 28.125f)
                    curveTo(25.458f, 27.54f, 25.308f, 27.026f, 25.007f, 26.582f)
                    curveTo(24.711f, 26.133f, 24.277f, 25.781f, 23.703f, 25.526f)
                    curveTo(23.135f, 25.27f, 22.447f, 25.142f, 21.64f, 25.142f)
                    horizontalLineTo(20.166f)
                    verticalLineTo(22.994f)
                    horizontalLineTo(21.64f)
                    curveTo(22.288f, 22.994f, 22.856f, 22.878f, 23.345f, 22.645f)
                    curveTo(23.839f, 22.412f, 24.226f, 22.088f, 24.504f, 21.673f)
                    curveTo(24.782f, 21.253f, 24.921f, 20.761f, 24.921f, 20.199f)
                    curveTo(24.921f, 19.659f, 24.799f, 19.19f, 24.555f, 18.793f)
                    curveTo(24.316f, 18.389f, 23.976f, 18.074f, 23.532f, 17.847f)
                    curveTo(23.095f, 17.619f, 22.578f, 17.506f, 21.981f, 17.506f)
                    curveTo(21.413f, 17.506f, 20.882f, 17.611f, 20.387f, 17.821f)
                    curveTo(19.899f, 18.026f, 19.501f, 18.321f, 19.194f, 18.707f)
                    curveTo(18.887f, 19.088f, 18.723f, 19.545f, 18.7f, 20.08f)
                    horizontalLineTo(16.152f)
                    curveTo(16.18f, 19.136f, 16.458f, 18.307f, 16.987f, 17.591f)
                    curveTo(17.521f, 16.875f, 18.226f, 16.315f, 19.101f, 15.912f)
                    curveTo(19.976f, 15.509f, 20.947f, 15.307f, 22.015f, 15.307f)
                    curveTo(23.135f, 15.307f, 24.101f, 15.526f, 24.913f, 15.963f)
                    curveTo(25.731f, 16.395f, 26.362f, 16.972f, 26.805f, 17.693f)
                    curveTo(27.254f, 18.415f, 27.476f, 19.205f, 27.47f, 20.063f)
                    curveTo(27.476f, 21.04f, 27.203f, 21.869f, 26.652f, 22.551f)
                    curveTo(26.106f, 23.233f, 25.379f, 23.69f, 24.47f, 23.923f)
                    verticalLineTo(24.06f)
                    curveTo(25.629f, 24.236f, 26.527f, 24.696f, 27.163f, 25.44f)
                    curveTo(27.805f, 26.185f, 28.123f, 27.108f, 28.118f, 28.21f)
                    curveTo(28.123f, 29.17f, 27.856f, 30.031f, 27.316f, 30.793f)
                    curveTo(26.782f, 31.554f, 26.052f, 32.153f, 25.126f, 32.591f)
                    curveTo(24.2f, 33.023f, 23.14f, 33.239f, 21.947f, 33.239f)
                    close()
                    moveTo(36.216f, 33.29f)
                    curveTo(34.869f, 33.284f, 33.719f, 32.929f, 32.764f, 32.224f)
                    curveTo(31.81f, 31.52f, 31.08f, 30.494f, 30.574f, 29.148f)
                    curveTo(30.068f, 27.801f, 29.815f, 26.179f, 29.815f, 24.281f)
                    curveTo(29.815f, 22.389f, 30.068f, 20.773f, 30.574f, 19.432f)
                    curveTo(31.085f, 18.091f, 31.818f, 17.068f, 32.773f, 16.364f)
                    curveTo(33.733f, 15.659f, 34.881f, 15.307f, 36.216f, 15.307f)
                    curveTo(37.551f, 15.307f, 38.696f, 15.662f, 39.651f, 16.372f)
                    curveTo(40.605f, 17.077f, 41.335f, 18.099f, 41.841f, 19.44f)
                    curveTo(42.352f, 20.776f, 42.608f, 22.389f, 42.608f, 24.281f)
                    curveTo(42.608f, 26.185f, 42.355f, 27.81f, 41.849f, 29.156f)
                    curveTo(41.344f, 30.497f, 40.614f, 31.523f, 39.659f, 32.233f)
                    curveTo(38.704f, 32.938f, 37.557f, 33.29f, 36.216f, 33.29f)
                    close()
                    moveTo(36.216f, 31.014f)
                    curveTo(37.398f, 31.014f, 38.321f, 30.438f, 38.986f, 29.284f)
                    curveTo(39.656f, 28.131f, 39.991f, 26.463f, 39.991f, 24.281f)
                    curveTo(39.991f, 22.832f, 39.838f, 21.608f, 39.531f, 20.608f)
                    curveTo(39.23f, 19.602f, 38.795f, 18.841f, 38.227f, 18.324f)
                    curveTo(37.665f, 17.801f, 36.994f, 17.54f, 36.216f, 17.54f)
                    curveTo(35.04f, 17.54f, 34.116f, 18.119f, 33.446f, 19.278f)
                    curveTo(32.776f, 20.438f, 32.437f, 22.105f, 32.432f, 24.281f)
                    curveTo(32.432f, 25.736f, 32.582f, 26.966f, 32.883f, 27.972f)
                    curveTo(33.19f, 28.972f, 33.625f, 29.73f, 34.187f, 30.247f)
                    curveTo(34.75f, 30.758f, 35.426f, 31.014f, 36.216f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit130!!
    }

@Suppress("ObjectPropertyName")
private var _Limit130: ImageVector? = null
