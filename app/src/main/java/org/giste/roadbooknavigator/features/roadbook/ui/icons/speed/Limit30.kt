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
val RoadbookIcons.Speed.Limit30: ImageVector
    get() {
        if (_Limit30 != null) {
            return _Limit30!!
        }
        _Limit30 = ImageVector.Builder(
            name = "Speed.Limit30",
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
                    moveTo(16.875f, 33.239f)
                    curveTo(15.705f, 33.239f, 14.659f, 33.037f, 13.739f, 32.633f)
                    curveTo(12.824f, 32.23f, 12.1f, 31.67f, 11.566f, 30.955f)
                    curveTo(11.037f, 30.233f, 10.753f, 29.398f, 10.713f, 28.449f)
                    horizontalLineTo(13.389f)
                    curveTo(13.424f, 28.966f, 13.597f, 29.415f, 13.909f, 29.795f)
                    curveTo(14.227f, 30.17f, 14.642f, 30.46f, 15.154f, 30.665f)
                    curveTo(15.665f, 30.869f, 16.233f, 30.972f, 16.858f, 30.972f)
                    curveTo(17.546f, 30.972f, 18.154f, 30.852f, 18.682f, 30.614f)
                    curveTo(19.216f, 30.375f, 19.634f, 30.043f, 19.935f, 29.617f)
                    curveTo(20.236f, 29.185f, 20.387f, 28.688f, 20.387f, 28.125f)
                    curveTo(20.387f, 27.54f, 20.236f, 27.026f, 19.935f, 26.582f)
                    curveTo(19.639f, 26.133f, 19.205f, 25.781f, 18.631f, 25.526f)
                    curveTo(18.063f, 25.27f, 17.375f, 25.142f, 16.568f, 25.142f)
                    horizontalLineTo(15.094f)
                    verticalLineTo(22.994f)
                    horizontalLineTo(16.568f)
                    curveTo(17.216f, 22.994f, 17.784f, 22.878f, 18.273f, 22.645f)
                    curveTo(18.767f, 22.412f, 19.154f, 22.088f, 19.432f, 21.673f)
                    curveTo(19.71f, 21.253f, 19.85f, 20.761f, 19.85f, 20.199f)
                    curveTo(19.85f, 19.659f, 19.728f, 19.19f, 19.483f, 18.793f)
                    curveTo(19.244f, 18.389f, 18.904f, 18.074f, 18.46f, 17.847f)
                    curveTo(18.023f, 17.619f, 17.506f, 17.506f, 16.909f, 17.506f)
                    curveTo(16.341f, 17.506f, 15.81f, 17.611f, 15.316f, 17.821f)
                    curveTo(14.827f, 18.026f, 14.429f, 18.321f, 14.122f, 18.707f)
                    curveTo(13.816f, 19.088f, 13.651f, 19.545f, 13.628f, 20.08f)
                    horizontalLineTo(11.08f)
                    curveTo(11.108f, 19.136f, 11.387f, 18.307f, 11.915f, 17.591f)
                    curveTo(12.449f, 16.875f, 13.154f, 16.315f, 14.029f, 15.912f)
                    curveTo(14.904f, 15.509f, 15.875f, 15.307f, 16.943f, 15.307f)
                    curveTo(18.063f, 15.307f, 19.029f, 15.526f, 19.841f, 15.963f)
                    curveTo(20.659f, 16.395f, 21.29f, 16.972f, 21.733f, 17.693f)
                    curveTo(22.182f, 18.415f, 22.404f, 19.205f, 22.398f, 20.063f)
                    curveTo(22.404f, 21.04f, 22.131f, 21.869f, 21.58f, 22.551f)
                    curveTo(21.034f, 23.233f, 20.307f, 23.69f, 19.398f, 23.923f)
                    verticalLineTo(24.06f)
                    curveTo(20.557f, 24.236f, 21.455f, 24.696f, 22.091f, 25.44f)
                    curveTo(22.733f, 26.185f, 23.051f, 27.108f, 23.046f, 28.21f)
                    curveTo(23.051f, 29.17f, 22.784f, 30.031f, 22.244f, 30.793f)
                    curveTo(21.71f, 31.554f, 20.98f, 32.153f, 20.054f, 32.591f)
                    curveTo(19.128f, 33.023f, 18.068f, 33.239f, 16.875f, 33.239f)
                    close()
                    moveTo(31.144f, 33.29f)
                    curveTo(29.797f, 33.284f, 28.647f, 32.929f, 27.692f, 32.224f)
                    curveTo(26.738f, 31.52f, 26.008f, 30.494f, 25.502f, 29.148f)
                    curveTo(24.996f, 27.801f, 24.743f, 26.179f, 24.743f, 24.281f)
                    curveTo(24.743f, 22.389f, 24.996f, 20.773f, 25.502f, 19.432f)
                    curveTo(26.013f, 18.091f, 26.746f, 17.068f, 27.701f, 16.364f)
                    curveTo(28.661f, 15.659f, 29.809f, 15.307f, 31.144f, 15.307f)
                    curveTo(32.479f, 15.307f, 33.624f, 15.662f, 34.579f, 16.372f)
                    curveTo(35.533f, 17.077f, 36.263f, 18.099f, 36.769f, 19.44f)
                    curveTo(37.28f, 20.776f, 37.536f, 22.389f, 37.536f, 24.281f)
                    curveTo(37.536f, 26.185f, 37.283f, 27.81f, 36.778f, 29.156f)
                    curveTo(36.272f, 30.497f, 35.542f, 31.523f, 34.587f, 32.233f)
                    curveTo(33.633f, 32.938f, 32.485f, 33.29f, 31.144f, 33.29f)
                    close()
                    moveTo(31.144f, 31.014f)
                    curveTo(32.326f, 31.014f, 33.249f, 30.438f, 33.914f, 29.284f)
                    curveTo(34.584f, 28.131f, 34.919f, 26.463f, 34.919f, 24.281f)
                    curveTo(34.919f, 22.832f, 34.766f, 21.608f, 34.459f, 20.608f)
                    curveTo(34.158f, 19.602f, 33.723f, 18.841f, 33.155f, 18.324f)
                    curveTo(32.593f, 17.801f, 31.922f, 17.54f, 31.144f, 17.54f)
                    curveTo(29.968f, 17.54f, 29.045f, 18.119f, 28.374f, 19.278f)
                    curveTo(27.704f, 20.438f, 27.366f, 22.105f, 27.36f, 24.281f)
                    curveTo(27.36f, 25.736f, 27.51f, 26.966f, 27.812f, 27.972f)
                    curveTo(28.118f, 28.972f, 28.553f, 29.73f, 29.116f, 30.247f)
                    curveTo(29.678f, 30.758f, 30.354f, 31.014f, 31.144f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit30!!
    }

@Suppress("ObjectPropertyName")
private var _Limit30: ImageVector? = null
