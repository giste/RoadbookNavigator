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
val RoadbookIcons.Speed.Limit20: ImageVector
    get() {
        if (_Limit20 != null) {
            return _Limit20!!
        }
        _Limit20 = ImageVector.Builder(
            name = "Speed.Limit20",
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
                    moveTo(11.174f, 33f)
                    verticalLineTo(31.091f)
                    lineTo(17.08f, 24.972f)
                    curveTo(17.71f, 24.307f, 18.23f, 23.724f, 18.639f, 23.224f)
                    curveTo(19.054f, 22.719f, 19.364f, 22.239f, 19.568f, 21.784f)
                    curveTo(19.773f, 21.33f, 19.875f, 20.847f, 19.875f, 20.335f)
                    curveTo(19.875f, 19.756f, 19.739f, 19.256f, 19.466f, 18.835f)
                    curveTo(19.193f, 18.409f, 18.821f, 18.082f, 18.35f, 17.855f)
                    curveTo(17.878f, 17.622f, 17.347f, 17.506f, 16.756f, 17.506f)
                    curveTo(16.131f, 17.506f, 15.585f, 17.633f, 15.12f, 17.889f)
                    curveTo(14.654f, 18.145f, 14.296f, 18.506f, 14.046f, 18.972f)
                    curveTo(13.796f, 19.438f, 13.671f, 19.983f, 13.671f, 20.608f)
                    horizontalLineTo(11.156f)
                    curveTo(11.156f, 19.545f, 11.401f, 18.617f, 11.889f, 17.821f)
                    curveTo(12.378f, 17.026f, 13.049f, 16.409f, 13.901f, 15.972f)
                    curveTo(14.753f, 15.528f, 15.722f, 15.307f, 16.807f, 15.307f)
                    curveTo(17.904f, 15.307f, 18.869f, 15.526f, 19.705f, 15.963f)
                    curveTo(20.546f, 16.395f, 21.202f, 16.986f, 21.674f, 17.736f)
                    curveTo(22.145f, 18.48f, 22.381f, 19.321f, 22.381f, 20.258f)
                    curveTo(22.381f, 20.906f, 22.259f, 21.54f, 22.014f, 22.159f)
                    curveTo(21.776f, 22.778f, 21.358f, 23.469f, 20.762f, 24.23f)
                    curveTo(20.165f, 24.986f, 19.335f, 25.903f, 18.273f, 26.983f)
                    lineTo(14.804f, 30.614f)
                    verticalLineTo(30.742f)
                    horizontalLineTo(22.662f)
                    verticalLineTo(33f)
                    horizontalLineTo(11.174f)
                    close()
                    moveTo(30.769f, 33.29f)
                    curveTo(29.422f, 33.284f, 28.272f, 32.929f, 27.317f, 32.224f)
                    curveTo(26.363f, 31.52f, 25.633f, 30.494f, 25.127f, 29.148f)
                    curveTo(24.621f, 27.801f, 24.368f, 26.179f, 24.368f, 24.281f)
                    curveTo(24.368f, 22.389f, 24.621f, 20.773f, 25.127f, 19.432f)
                    curveTo(25.638f, 18.091f, 26.371f, 17.068f, 27.326f, 16.364f)
                    curveTo(28.286f, 15.659f, 29.434f, 15.307f, 30.769f, 15.307f)
                    curveTo(32.104f, 15.307f, 33.249f, 15.662f, 34.204f, 16.372f)
                    curveTo(35.158f, 17.077f, 35.888f, 18.099f, 36.394f, 19.44f)
                    curveTo(36.905f, 20.776f, 37.161f, 22.389f, 37.161f, 24.281f)
                    curveTo(37.161f, 26.185f, 36.908f, 27.81f, 36.403f, 29.156f)
                    curveTo(35.897f, 30.497f, 35.167f, 31.523f, 34.212f, 32.233f)
                    curveTo(33.258f, 32.938f, 32.11f, 33.29f, 30.769f, 33.29f)
                    close()
                    moveTo(30.769f, 31.014f)
                    curveTo(31.951f, 31.014f, 32.874f, 30.438f, 33.539f, 29.284f)
                    curveTo(34.209f, 28.131f, 34.544f, 26.463f, 34.544f, 24.281f)
                    curveTo(34.544f, 22.832f, 34.391f, 21.608f, 34.084f, 20.608f)
                    curveTo(33.783f, 19.602f, 33.348f, 18.841f, 32.78f, 18.324f)
                    curveTo(32.218f, 17.801f, 31.547f, 17.54f, 30.769f, 17.54f)
                    curveTo(29.593f, 17.54f, 28.67f, 18.119f, 27.999f, 19.278f)
                    curveTo(27.329f, 20.438f, 26.991f, 22.105f, 26.985f, 24.281f)
                    curveTo(26.985f, 25.736f, 27.135f, 26.966f, 27.437f, 27.972f)
                    curveTo(27.743f, 28.972f, 28.178f, 29.73f, 28.741f, 30.247f)
                    curveTo(29.303f, 30.758f, 29.979f, 31.014f, 30.769f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit20!!
    }

@Suppress("ObjectPropertyName")
private var _Limit20: ImageVector? = null
