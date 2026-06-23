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
val RoadbookIcons.Speed.Limit60: ImageVector
    get() {
        if (_Limit60 != null) {
            return _Limit60!!
        }
        _Limit60 = ImageVector.Builder(
            name = "Speed.Limit60",
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
                    moveTo(16.998f, 33.239f)
                    curveTo(16.219f, 33.227f, 15.452f, 33.085f, 14.697f, 32.813f)
                    curveTo(13.947f, 32.54f, 13.265f, 32.085f, 12.651f, 31.449f)
                    curveTo(12.038f, 30.813f, 11.546f, 29.957f, 11.177f, 28.883f)
                    curveTo(10.813f, 27.81f, 10.631f, 26.469f, 10.631f, 24.861f)
                    curveTo(10.631f, 23.338f, 10.782f, 21.986f, 11.083f, 20.804f)
                    curveTo(11.39f, 19.622f, 11.83f, 18.625f, 12.404f, 17.813f)
                    curveTo(12.978f, 16.994f, 13.671f, 16.372f, 14.483f, 15.946f)
                    curveTo(15.296f, 15.52f, 16.208f, 15.307f, 17.219f, 15.307f)
                    curveTo(18.259f, 15.307f, 19.182f, 15.511f, 19.989f, 15.92f)
                    curveTo(20.796f, 16.33f, 21.449f, 16.895f, 21.949f, 17.617f)
                    curveTo(22.455f, 18.338f, 22.776f, 19.159f, 22.913f, 20.08f)
                    horizontalLineTo(20.313f)
                    curveTo(20.137f, 19.352f, 19.788f, 18.758f, 19.265f, 18.298f)
                    curveTo(18.742f, 17.838f, 18.06f, 17.608f, 17.219f, 17.608f)
                    curveTo(15.941f, 17.608f, 14.944f, 18.165f, 14.228f, 19.278f)
                    curveTo(13.518f, 20.392f, 13.16f, 21.94f, 13.154f, 23.923f)
                    horizontalLineTo(13.282f)
                    curveTo(13.583f, 23.429f, 13.952f, 23.008f, 14.39f, 22.662f)
                    curveTo(14.833f, 22.31f, 15.327f, 22.04f, 15.873f, 21.852f)
                    curveTo(16.424f, 21.659f, 17.003f, 21.563f, 17.611f, 21.563f)
                    curveTo(18.623f, 21.563f, 19.538f, 21.81f, 20.356f, 22.304f)
                    curveTo(21.18f, 22.793f, 21.836f, 23.469f, 22.324f, 24.332f)
                    curveTo(22.813f, 25.196f, 23.057f, 26.185f, 23.057f, 27.298f)
                    curveTo(23.057f, 28.412f, 22.805f, 29.42f, 22.299f, 30.324f)
                    curveTo(21.799f, 31.227f, 21.094f, 31.943f, 20.185f, 32.472f)
                    curveTo(19.276f, 32.994f, 18.214f, 33.25f, 16.998f, 33.239f)
                    close()
                    moveTo(16.989f, 31.023f)
                    curveTo(17.66f, 31.023f, 18.259f, 30.858f, 18.788f, 30.528f)
                    curveTo(19.316f, 30.199f, 19.733f, 29.756f, 20.04f, 29.199f)
                    curveTo(20.347f, 28.642f, 20.501f, 28.02f, 20.501f, 27.332f)
                    curveTo(20.501f, 26.662f, 20.35f, 26.051f, 20.049f, 25.5f)
                    curveTo(19.753f, 24.949f, 19.344f, 24.511f, 18.822f, 24.188f)
                    curveTo(18.305f, 23.864f, 17.714f, 23.702f, 17.049f, 23.702f)
                    curveTo(16.543f, 23.702f, 16.074f, 23.798f, 15.643f, 23.992f)
                    curveTo(15.217f, 24.185f, 14.842f, 24.452f, 14.518f, 24.793f)
                    curveTo(14.194f, 25.133f, 13.938f, 25.526f, 13.751f, 25.969f)
                    curveTo(13.569f, 26.406f, 13.478f, 26.869f, 13.478f, 27.358f)
                    curveTo(13.478f, 28.011f, 13.628f, 28.617f, 13.929f, 29.173f)
                    curveTo(14.236f, 29.73f, 14.654f, 30.179f, 15.182f, 30.52f)
                    curveTo(15.717f, 30.855f, 16.319f, 31.023f, 16.989f, 31.023f)
                    close()
                    moveTo(31.038f, 33.29f)
                    curveTo(29.692f, 33.284f, 28.541f, 32.929f, 27.587f, 32.224f)
                    curveTo(26.632f, 31.52f, 25.902f, 30.494f, 25.396f, 29.148f)
                    curveTo(24.891f, 27.801f, 24.638f, 26.179f, 24.638f, 24.281f)
                    curveTo(24.638f, 22.389f, 24.891f, 20.773f, 25.396f, 19.432f)
                    curveTo(25.908f, 18.091f, 26.641f, 17.068f, 27.595f, 16.364f)
                    curveTo(28.556f, 15.659f, 29.703f, 15.307f, 31.038f, 15.307f)
                    curveTo(32.374f, 15.307f, 33.519f, 15.662f, 34.473f, 16.372f)
                    curveTo(35.428f, 17.077f, 36.158f, 18.099f, 36.664f, 19.44f)
                    curveTo(37.175f, 20.776f, 37.431f, 22.389f, 37.431f, 24.281f)
                    curveTo(37.431f, 26.185f, 37.178f, 27.81f, 36.672f, 29.156f)
                    curveTo(36.166f, 30.497f, 35.436f, 31.523f, 34.482f, 32.233f)
                    curveTo(33.527f, 32.938f, 32.379f, 33.29f, 31.038f, 33.29f)
                    close()
                    moveTo(31.038f, 31.014f)
                    curveTo(32.22f, 31.014f, 33.144f, 30.438f, 33.808f, 29.284f)
                    curveTo(34.479f, 28.131f, 34.814f, 26.463f, 34.814f, 24.281f)
                    curveTo(34.814f, 22.832f, 34.661f, 21.608f, 34.354f, 20.608f)
                    curveTo(34.053f, 19.602f, 33.618f, 18.841f, 33.05f, 18.324f)
                    curveTo(32.487f, 17.801f, 31.817f, 17.54f, 31.038f, 17.54f)
                    curveTo(29.862f, 17.54f, 28.939f, 18.119f, 28.269f, 19.278f)
                    curveTo(27.598f, 20.438f, 27.26f, 22.105f, 27.254f, 24.281f)
                    curveTo(27.254f, 25.736f, 27.405f, 26.966f, 27.706f, 27.972f)
                    curveTo(28.013f, 28.972f, 28.448f, 29.73f, 29.01f, 30.247f)
                    curveTo(29.573f, 30.758f, 30.249f, 31.014f, 31.038f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit60!!
    }

@Suppress("ObjectPropertyName")
private var _Limit60: ImageVector? = null
