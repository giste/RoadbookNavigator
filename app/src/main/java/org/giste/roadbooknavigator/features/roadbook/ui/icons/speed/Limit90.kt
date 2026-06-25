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
val RoadbookIcons.Speed.Limit90: ImageVector
    get() {
        if (_Limit90 != null) {
            return _Limit90!!
        }
        _Limit90 = ImageVector.Builder(
            name = "Speed.Limit90",
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
                    moveTo(16.708f, 15.307f)
                    curveTo(17.486f, 15.313f, 18.253f, 15.455f, 19.009f, 15.733f)
                    curveTo(19.765f, 16.011f, 20.447f, 16.466f, 21.055f, 17.097f)
                    curveTo(21.668f, 17.727f, 22.157f, 18.58f, 22.521f, 19.653f)
                    curveTo(22.89f, 20.722f, 23.077f, 22.051f, 23.083f, 23.642f)
                    curveTo(23.083f, 25.17f, 22.93f, 26.531f, 22.623f, 27.724f)
                    curveTo(22.316f, 28.912f, 21.876f, 29.915f, 21.302f, 30.733f)
                    curveTo(20.733f, 31.551f, 20.043f, 32.173f, 19.231f, 32.599f)
                    curveTo(18.418f, 33.026f, 17.503f, 33.239f, 16.486f, 33.239f)
                    curveTo(15.447f, 33.239f, 14.523f, 33.034f, 13.717f, 32.625f)
                    curveTo(12.91f, 32.216f, 12.253f, 31.651f, 11.748f, 30.929f)
                    curveTo(11.242f, 30.202f, 10.927f, 29.367f, 10.802f, 28.423f)
                    horizontalLineTo(13.401f)
                    curveTo(13.572f, 29.173f, 13.918f, 29.781f, 14.441f, 30.247f)
                    curveTo(14.969f, 30.707f, 15.651f, 30.938f, 16.486f, 30.938f)
                    curveTo(17.765f, 30.938f, 18.762f, 30.381f, 19.478f, 29.267f)
                    curveTo(20.194f, 28.148f, 20.555f, 26.585f, 20.56f, 24.58f)
                    horizontalLineTo(20.424f)
                    curveTo(20.128f, 25.068f, 19.759f, 25.489f, 19.316f, 25.841f)
                    curveTo(18.878f, 26.193f, 18.387f, 26.466f, 17.841f, 26.659f)
                    curveTo(17.296f, 26.852f, 16.714f, 26.949f, 16.094f, 26.949f)
                    curveTo(15.089f, 26.949f, 14.174f, 26.702f, 13.35f, 26.207f)
                    curveTo(12.526f, 25.713f, 11.87f, 25.034f, 11.381f, 24.17f)
                    curveTo(10.893f, 23.307f, 10.648f, 22.321f, 10.648f, 21.213f)
                    curveTo(10.648f, 20.111f, 10.898f, 19.111f, 11.398f, 18.213f)
                    curveTo(11.904f, 17.315f, 12.608f, 16.605f, 13.512f, 16.082f)
                    curveTo(14.421f, 15.554f, 15.486f, 15.295f, 16.708f, 15.307f)
                    close()
                    moveTo(16.716f, 17.523f)
                    curveTo(16.052f, 17.523f, 15.452f, 17.688f, 14.918f, 18.017f)
                    curveTo(14.39f, 18.341f, 13.972f, 18.781f, 13.665f, 19.338f)
                    curveTo(13.358f, 19.889f, 13.205f, 20.503f, 13.205f, 21.179f)
                    curveTo(13.205f, 21.855f, 13.353f, 22.469f, 13.648f, 23.02f)
                    curveTo(13.949f, 23.565f, 14.358f, 24f, 14.876f, 24.324f)
                    curveTo(15.398f, 24.642f, 15.995f, 24.801f, 16.665f, 24.801f)
                    curveTo(17.165f, 24.801f, 17.631f, 24.705f, 18.063f, 24.511f)
                    curveTo(18.495f, 24.318f, 18.873f, 24.051f, 19.197f, 23.71f)
                    curveTo(19.521f, 23.364f, 19.773f, 22.972f, 19.955f, 22.534f)
                    curveTo(20.137f, 22.097f, 20.228f, 21.636f, 20.228f, 21.153f)
                    curveTo(20.228f, 20.511f, 20.074f, 19.915f, 19.768f, 19.364f)
                    curveTo(19.466f, 18.813f, 19.052f, 18.369f, 18.523f, 18.034f)
                    curveTo(17.995f, 17.693f, 17.393f, 17.523f, 16.716f, 17.523f)
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

        return _Limit90!!
    }

@Suppress("ObjectPropertyName")
private var _Limit90: ImageVector? = null
