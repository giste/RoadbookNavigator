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
val RoadbookIcons.Speed.Limit70: ImageVector
    get() {
        if (_Limit70 != null) {
            return _Limit70!!
        }
        _Limit70 = ImageVector.Builder(
            name = "Speed.Limit70",
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
                    moveTo(12.359f, 33f)
                    lineTo(19.979f, 17.932f)
                    verticalLineTo(17.804f)
                    horizontalLineTo(11.166f)
                    verticalLineTo(15.545f)
                    horizontalLineTo(22.706f)
                    verticalLineTo(17.881f)
                    lineTo(15.112f, 33f)
                    horizontalLineTo(12.359f)
                    close()
                    moveTo(30.171f, 33.29f)
                    curveTo(28.825f, 33.284f, 27.674f, 32.929f, 26.72f, 32.224f)
                    curveTo(25.765f, 31.52f, 25.035f, 30.494f, 24.529f, 29.148f)
                    curveTo(24.024f, 27.801f, 23.771f, 26.179f, 23.771f, 24.281f)
                    curveTo(23.771f, 22.389f, 24.024f, 20.773f, 24.529f, 19.432f)
                    curveTo(25.041f, 18.091f, 25.774f, 17.068f, 26.728f, 16.364f)
                    curveTo(27.688f, 15.659f, 28.836f, 15.307f, 30.171f, 15.307f)
                    curveTo(31.507f, 15.307f, 32.651f, 15.662f, 33.606f, 16.372f)
                    curveTo(34.561f, 17.077f, 35.291f, 18.099f, 35.796f, 19.44f)
                    curveTo(36.308f, 20.776f, 36.563f, 22.389f, 36.563f, 24.281f)
                    curveTo(36.563f, 26.185f, 36.311f, 27.81f, 35.805f, 29.156f)
                    curveTo(35.299f, 30.497f, 34.569f, 31.523f, 33.614f, 32.233f)
                    curveTo(32.66f, 32.938f, 31.512f, 33.29f, 30.171f, 33.29f)
                    close()
                    moveTo(30.171f, 31.014f)
                    curveTo(31.353f, 31.014f, 32.276f, 30.438f, 32.941f, 29.284f)
                    curveTo(33.612f, 28.131f, 33.947f, 26.463f, 33.947f, 24.281f)
                    curveTo(33.947f, 22.832f, 33.793f, 21.608f, 33.487f, 20.608f)
                    curveTo(33.186f, 19.602f, 32.751f, 18.841f, 32.183f, 18.324f)
                    curveTo(31.62f, 17.801f, 30.95f, 17.54f, 30.171f, 17.54f)
                    curveTo(28.995f, 17.54f, 28.072f, 18.119f, 27.401f, 19.278f)
                    curveTo(26.731f, 20.438f, 26.393f, 22.105f, 26.387f, 24.281f)
                    curveTo(26.387f, 25.736f, 26.538f, 26.966f, 26.839f, 27.972f)
                    curveTo(27.146f, 28.972f, 27.58f, 29.73f, 28.143f, 30.247f)
                    curveTo(28.705f, 30.758f, 29.382f, 31.014f, 30.171f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit70!!
    }

@Suppress("ObjectPropertyName")
private var _Limit70: ImageVector? = null
