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
val RoadbookIcons.Speed.Limit150: ImageVector
    get() {
        if (_Limit150 != null) {
            return _Limit150!!
        }
        _Limit150 = ImageVector.Builder(
            name = "Speed.Limit150",
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
                    moveTo(12.642f, 15.545f)
                    verticalLineTo(33f)
                    horizontalLineTo(10f)
                    verticalLineTo(18.188f)
                    horizontalLineTo(9.897f)
                    lineTo(5.721f, 20.915f)
                    verticalLineTo(18.392f)
                    lineTo(10.076f, 15.545f)
                    horizontalLineTo(12.642f)
                    close()
                    moveTo(21.925f, 33.239f)
                    curveTo(20.857f, 33.239f, 19.896f, 33.034f, 19.044f, 32.625f)
                    curveTo(18.197f, 32.21f, 17.521f, 31.642f, 17.016f, 30.92f)
                    curveTo(16.51f, 30.199f, 16.24f, 29.375f, 16.206f, 28.449f)
                    horizontalLineTo(18.763f)
                    curveTo(18.825f, 29.199f, 19.158f, 29.815f, 19.76f, 30.298f)
                    curveTo(20.362f, 30.781f, 21.084f, 31.023f, 21.925f, 31.023f)
                    curveTo(22.595f, 31.023f, 23.189f, 30.869f, 23.706f, 30.563f)
                    curveTo(24.229f, 30.25f, 24.638f, 29.821f, 24.933f, 29.276f)
                    curveTo(25.234f, 28.73f, 25.385f, 28.108f, 25.385f, 27.409f)
                    curveTo(25.385f, 26.699f, 25.232f, 26.065f, 24.925f, 25.508f)
                    curveTo(24.618f, 24.952f, 24.195f, 24.514f, 23.655f, 24.196f)
                    curveTo(23.121f, 23.878f, 22.507f, 23.716f, 21.814f, 23.71f)
                    curveTo(21.285f, 23.71f, 20.754f, 23.801f, 20.22f, 23.983f)
                    curveTo(19.686f, 24.165f, 19.254f, 24.403f, 18.925f, 24.699f)
                    lineTo(16.513f, 24.341f)
                    lineTo(17.493f, 15.545f)
                    horizontalLineTo(27.09f)
                    verticalLineTo(17.804f)
                    horizontalLineTo(19.683f)
                    lineTo(19.129f, 22.688f)
                    horizontalLineTo(19.232f)
                    curveTo(19.572f, 22.358f, 20.024f, 22.082f, 20.587f, 21.861f)
                    curveTo(21.155f, 21.639f, 21.763f, 21.528f, 22.41f, 21.528f)
                    curveTo(23.473f, 21.528f, 24.419f, 21.781f, 25.249f, 22.287f)
                    curveTo(26.084f, 22.793f, 26.74f, 23.483f, 27.217f, 24.358f)
                    curveTo(27.7f, 25.227f, 27.939f, 26.227f, 27.933f, 27.358f)
                    curveTo(27.939f, 28.489f, 27.683f, 29.497f, 27.166f, 30.383f)
                    curveTo(26.655f, 31.27f, 25.945f, 31.969f, 25.035f, 32.48f)
                    curveTo(24.132f, 32.986f, 23.095f, 33.239f, 21.925f, 33.239f)
                    close()
                    moveTo(35.923f, 33.29f)
                    curveTo(34.576f, 33.284f, 33.426f, 32.929f, 32.471f, 32.224f)
                    curveTo(31.517f, 31.52f, 30.787f, 30.494f, 30.281f, 29.148f)
                    curveTo(29.775f, 27.801f, 29.522f, 26.179f, 29.522f, 24.281f)
                    curveTo(29.522f, 22.389f, 29.775f, 20.773f, 30.281f, 19.432f)
                    curveTo(30.792f, 18.091f, 31.525f, 17.068f, 32.48f, 16.364f)
                    curveTo(33.44f, 15.659f, 34.588f, 15.307f, 35.923f, 15.307f)
                    curveTo(37.258f, 15.307f, 38.403f, 15.662f, 39.357f, 16.372f)
                    curveTo(40.312f, 17.077f, 41.042f, 18.099f, 41.548f, 19.44f)
                    curveTo(42.059f, 20.776f, 42.315f, 22.389f, 42.315f, 24.281f)
                    curveTo(42.315f, 26.185f, 42.062f, 27.81f, 41.556f, 29.156f)
                    curveTo(41.051f, 30.497f, 40.321f, 31.523f, 39.366f, 32.233f)
                    curveTo(38.411f, 32.938f, 37.264f, 33.29f, 35.923f, 33.29f)
                    close()
                    moveTo(35.923f, 31.014f)
                    curveTo(37.105f, 31.014f, 38.028f, 30.438f, 38.693f, 29.284f)
                    curveTo(39.363f, 28.131f, 39.698f, 26.463f, 39.698f, 24.281f)
                    curveTo(39.698f, 22.832f, 39.545f, 21.608f, 39.238f, 20.608f)
                    curveTo(38.937f, 19.602f, 38.502f, 18.841f, 37.934f, 18.324f)
                    curveTo(37.372f, 17.801f, 36.701f, 17.54f, 35.923f, 17.54f)
                    curveTo(34.747f, 17.54f, 33.823f, 18.119f, 33.153f, 19.278f)
                    curveTo(32.482f, 20.438f, 32.145f, 22.105f, 32.139f, 24.281f)
                    curveTo(32.139f, 25.736f, 32.289f, 26.966f, 32.59f, 27.972f)
                    curveTo(32.897f, 28.972f, 33.332f, 29.73f, 33.895f, 30.247f)
                    curveTo(34.457f, 30.758f, 35.133f, 31.014f, 35.923f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit150!!
    }

@Suppress("ObjectPropertyName")
private var _Limit150: ImageVector? = null
