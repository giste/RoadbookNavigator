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
val RoadbookIcons.Speed.Limit120: ImageVector
    get() {
        if (_Limit120 != null) {
            return _Limit120!!
        }
        _Limit120 = ImageVector.Builder(
            name = "Speed.Limit120",
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
                    moveTo(12.724f, 15.545f)
                    verticalLineTo(33f)
                    horizontalLineTo(10.082f)
                    verticalLineTo(18.188f)
                    horizontalLineTo(9.979f)
                    lineTo(5.803f, 20.915f)
                    verticalLineTo(18.392f)
                    lineTo(10.158f, 15.545f)
                    horizontalLineTo(12.724f)
                    close()
                    moveTo(16.245f, 33f)
                    verticalLineTo(31.091f)
                    lineTo(22.152f, 24.972f)
                    curveTo(22.782f, 24.307f, 23.302f, 23.724f, 23.711f, 23.224f)
                    curveTo(24.126f, 22.719f, 24.436f, 22.239f, 24.64f, 21.784f)
                    curveTo(24.845f, 21.33f, 24.947f, 20.847f, 24.947f, 20.335f)
                    curveTo(24.947f, 19.756f, 24.811f, 19.256f, 24.538f, 18.835f)
                    curveTo(24.265f, 18.409f, 23.893f, 18.082f, 23.421f, 17.855f)
                    curveTo(22.95f, 17.622f, 22.419f, 17.506f, 21.828f, 17.506f)
                    curveTo(21.203f, 17.506f, 20.657f, 17.633f, 20.191f, 17.889f)
                    curveTo(19.726f, 18.145f, 19.368f, 18.506f, 19.118f, 18.972f)
                    curveTo(18.868f, 19.438f, 18.743f, 19.983f, 18.743f, 20.608f)
                    horizontalLineTo(16.228f)
                    curveTo(16.228f, 19.545f, 16.473f, 18.617f, 16.961f, 17.821f)
                    curveTo(17.45f, 17.026f, 18.12f, 16.409f, 18.973f, 15.972f)
                    curveTo(19.825f, 15.528f, 20.794f, 15.307f, 21.879f, 15.307f)
                    curveTo(22.976f, 15.307f, 23.941f, 15.526f, 24.777f, 15.963f)
                    curveTo(25.618f, 16.395f, 26.274f, 16.986f, 26.745f, 17.736f)
                    curveTo(27.217f, 18.48f, 27.453f, 19.321f, 27.453f, 20.258f)
                    curveTo(27.453f, 20.906f, 27.331f, 21.54f, 27.086f, 22.159f)
                    curveTo(26.848f, 22.778f, 26.43f, 23.469f, 25.833f, 24.23f)
                    curveTo(25.237f, 24.986f, 24.407f, 25.903f, 23.345f, 26.983f)
                    lineTo(19.876f, 30.614f)
                    verticalLineTo(30.742f)
                    horizontalLineTo(27.734f)
                    verticalLineTo(33f)
                    horizontalLineTo(16.245f)
                    close()
                    moveTo(35.841f, 33.29f)
                    curveTo(34.494f, 33.284f, 33.344f, 32.929f, 32.389f, 32.224f)
                    curveTo(31.435f, 31.52f, 30.705f, 30.494f, 30.199f, 29.148f)
                    curveTo(29.693f, 27.801f, 29.44f, 26.179f, 29.44f, 24.281f)
                    curveTo(29.44f, 22.389f, 29.693f, 20.773f, 30.199f, 19.432f)
                    curveTo(30.71f, 18.091f, 31.443f, 17.068f, 32.398f, 16.364f)
                    curveTo(33.358f, 15.659f, 34.506f, 15.307f, 35.841f, 15.307f)
                    curveTo(37.176f, 15.307f, 38.321f, 15.662f, 39.276f, 16.372f)
                    curveTo(40.23f, 17.077f, 40.96f, 18.099f, 41.466f, 19.44f)
                    curveTo(41.977f, 20.776f, 42.233f, 22.389f, 42.233f, 24.281f)
                    curveTo(42.233f, 26.185f, 41.98f, 27.81f, 41.474f, 29.156f)
                    curveTo(40.969f, 30.497f, 40.239f, 31.523f, 39.284f, 32.233f)
                    curveTo(38.329f, 32.938f, 37.182f, 33.29f, 35.841f, 33.29f)
                    close()
                    moveTo(35.841f, 31.014f)
                    curveTo(37.023f, 31.014f, 37.946f, 30.438f, 38.611f, 29.284f)
                    curveTo(39.281f, 28.131f, 39.616f, 26.463f, 39.616f, 24.281f)
                    curveTo(39.616f, 22.832f, 39.463f, 21.608f, 39.156f, 20.608f)
                    curveTo(38.855f, 19.602f, 38.42f, 18.841f, 37.852f, 18.324f)
                    curveTo(37.29f, 17.801f, 36.619f, 17.54f, 35.841f, 17.54f)
                    curveTo(34.665f, 17.54f, 33.741f, 18.119f, 33.071f, 19.278f)
                    curveTo(32.401f, 20.438f, 32.062f, 22.105f, 32.057f, 24.281f)
                    curveTo(32.057f, 25.736f, 32.207f, 26.966f, 32.508f, 27.972f)
                    curveTo(32.815f, 28.972f, 33.25f, 29.73f, 33.812f, 30.247f)
                    curveTo(34.375f, 30.758f, 35.051f, 31.014f, 35.841f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit120!!
    }

@Suppress("ObjectPropertyName")
private var _Limit120: ImageVector? = null
