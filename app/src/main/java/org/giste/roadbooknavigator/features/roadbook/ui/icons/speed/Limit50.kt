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
val RoadbookIcons.Speed.Limit50: ImageVector
    get() {
        if (_Limit50 != null) {
            return _Limit50!!
        }
        _Limit50 = ImageVector.Builder(
            name = "Speed.Limit50",
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
                    moveTo(16.853f, 33.239f)
                    curveTo(15.785f, 33.239f, 14.824f, 33.034f, 13.972f, 32.625f)
                    curveTo(13.126f, 32.21f, 12.449f, 31.642f, 11.944f, 30.92f)
                    curveTo(11.438f, 30.199f, 11.168f, 29.375f, 11.134f, 28.449f)
                    horizontalLineTo(13.691f)
                    curveTo(13.753f, 29.199f, 14.086f, 29.815f, 14.688f, 30.298f)
                    curveTo(15.29f, 30.781f, 16.012f, 31.023f, 16.853f, 31.023f)
                    curveTo(17.523f, 31.023f, 18.117f, 30.869f, 18.634f, 30.563f)
                    curveTo(19.157f, 30.25f, 19.566f, 29.821f, 19.861f, 29.276f)
                    curveTo(20.163f, 28.73f, 20.313f, 28.108f, 20.313f, 27.409f)
                    curveTo(20.313f, 26.699f, 20.16f, 26.065f, 19.853f, 25.508f)
                    curveTo(19.546f, 24.952f, 19.123f, 24.514f, 18.583f, 24.196f)
                    curveTo(18.049f, 23.878f, 17.435f, 23.716f, 16.742f, 23.71f)
                    curveTo(16.214f, 23.71f, 15.682f, 23.801f, 15.148f, 23.983f)
                    curveTo(14.614f, 24.165f, 14.182f, 24.403f, 13.853f, 24.699f)
                    lineTo(11.441f, 24.341f)
                    lineTo(12.421f, 15.545f)
                    horizontalLineTo(22.018f)
                    verticalLineTo(17.804f)
                    horizontalLineTo(14.611f)
                    lineTo(14.057f, 22.688f)
                    horizontalLineTo(14.16f)
                    curveTo(14.501f, 22.358f, 14.952f, 22.082f, 15.515f, 21.861f)
                    curveTo(16.083f, 21.639f, 16.691f, 21.528f, 17.339f, 21.528f)
                    curveTo(18.401f, 21.528f, 19.347f, 21.781f, 20.177f, 22.287f)
                    curveTo(21.012f, 22.793f, 21.668f, 23.483f, 22.146f, 24.358f)
                    curveTo(22.628f, 25.227f, 22.867f, 26.227f, 22.861f, 27.358f)
                    curveTo(22.867f, 28.489f, 22.611f, 29.497f, 22.094f, 30.383f)
                    curveTo(21.583f, 31.27f, 20.873f, 31.969f, 19.964f, 32.48f)
                    curveTo(19.06f, 32.986f, 18.023f, 33.239f, 16.853f, 33.239f)
                    close()
                    moveTo(30.851f, 33.29f)
                    curveTo(29.504f, 33.284f, 28.354f, 32.929f, 27.399f, 32.224f)
                    curveTo(26.445f, 31.52f, 25.715f, 30.494f, 25.209f, 29.148f)
                    curveTo(24.703f, 27.801f, 24.45f, 26.179f, 24.45f, 24.281f)
                    curveTo(24.45f, 22.389f, 24.703f, 20.773f, 25.209f, 19.432f)
                    curveTo(25.72f, 18.091f, 26.453f, 17.068f, 27.408f, 16.364f)
                    curveTo(28.368f, 15.659f, 29.516f, 15.307f, 30.851f, 15.307f)
                    curveTo(32.186f, 15.307f, 33.331f, 15.662f, 34.286f, 16.372f)
                    curveTo(35.24f, 17.077f, 35.97f, 18.099f, 36.476f, 19.44f)
                    curveTo(36.987f, 20.776f, 37.243f, 22.389f, 37.243f, 24.281f)
                    curveTo(37.243f, 26.185f, 36.99f, 27.81f, 36.485f, 29.156f)
                    curveTo(35.979f, 30.497f, 35.249f, 31.523f, 34.294f, 32.233f)
                    curveTo(33.34f, 32.938f, 32.192f, 33.29f, 30.851f, 33.29f)
                    close()
                    moveTo(30.851f, 31.014f)
                    curveTo(32.033f, 31.014f, 32.956f, 30.438f, 33.621f, 29.284f)
                    curveTo(34.291f, 28.131f, 34.627f, 26.463f, 34.627f, 24.281f)
                    curveTo(34.627f, 22.832f, 34.473f, 21.608f, 34.166f, 20.608f)
                    curveTo(33.865f, 19.602f, 33.431f, 18.841f, 32.862f, 18.324f)
                    curveTo(32.3f, 17.801f, 31.629f, 17.54f, 30.851f, 17.54f)
                    curveTo(29.675f, 17.54f, 28.752f, 18.119f, 28.081f, 19.278f)
                    curveTo(27.411f, 20.438f, 27.073f, 22.105f, 27.067f, 24.281f)
                    curveTo(27.067f, 25.736f, 27.218f, 26.966f, 27.519f, 27.972f)
                    curveTo(27.825f, 28.972f, 28.26f, 29.73f, 28.823f, 30.247f)
                    curveTo(29.385f, 30.758f, 30.061f, 31.014f, 30.851f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit50!!
    }

@Suppress("ObjectPropertyName")
private var _Limit50: ImageVector? = null
