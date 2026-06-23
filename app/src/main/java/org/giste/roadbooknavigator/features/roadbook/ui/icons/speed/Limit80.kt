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
val RoadbookIcons.Speed.Limit80: ImageVector
    get() {
        if (_Limit80 != null) {
            return _Limit80!!
        }
        _Limit80 = ImageVector.Builder(
            name = "Speed.Limit80",
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
                    moveTo(16.849f, 33.239f)
                    curveTo(15.627f, 33.239f, 14.547f, 33.028f, 13.61f, 32.608f)
                    curveTo(12.678f, 32.188f, 11.948f, 31.608f, 11.42f, 30.869f)
                    curveTo(10.891f, 30.131f, 10.63f, 29.29f, 10.635f, 28.347f)
                    curveTo(10.63f, 27.608f, 10.78f, 26.929f, 11.087f, 26.31f)
                    curveTo(11.4f, 25.685f, 11.823f, 25.165f, 12.357f, 24.75f)
                    curveTo(12.891f, 24.33f, 13.488f, 24.063f, 14.147f, 23.949f)
                    verticalLineTo(23.847f)
                    curveTo(13.278f, 23.636f, 12.582f, 23.17f, 12.059f, 22.449f)
                    curveTo(11.536f, 21.727f, 11.278f, 20.898f, 11.283f, 19.96f)
                    curveTo(11.278f, 19.068f, 11.513f, 18.273f, 11.991f, 17.574f)
                    curveTo(12.474f, 16.869f, 13.135f, 16.315f, 13.976f, 15.912f)
                    curveTo(14.817f, 15.509f, 15.775f, 15.307f, 16.849f, 15.307f)
                    curveTo(17.911f, 15.307f, 18.86f, 15.511f, 19.695f, 15.92f)
                    curveTo(20.536f, 16.324f, 21.198f, 16.878f, 21.681f, 17.582f)
                    curveTo(22.164f, 18.281f, 22.408f, 19.074f, 22.414f, 19.96f)
                    curveTo(22.408f, 20.898f, 22.141f, 21.727f, 21.613f, 22.449f)
                    curveTo(21.084f, 23.17f, 20.397f, 23.636f, 19.55f, 23.847f)
                    verticalLineTo(23.949f)
                    curveTo(20.204f, 24.063f, 20.792f, 24.33f, 21.315f, 24.75f)
                    curveTo(21.843f, 25.165f, 22.263f, 25.685f, 22.576f, 26.31f)
                    curveTo(22.894f, 26.929f, 23.056f, 27.608f, 23.062f, 28.347f)
                    curveTo(23.056f, 29.29f, 22.789f, 30.131f, 22.26f, 30.869f)
                    curveTo(21.732f, 31.608f, 20.999f, 32.188f, 20.062f, 32.608f)
                    curveTo(19.13f, 33.028f, 18.059f, 33.239f, 16.849f, 33.239f)
                    close()
                    moveTo(16.849f, 31.082f)
                    curveTo(17.57f, 31.082f, 18.195f, 30.963f, 18.724f, 30.724f)
                    curveTo(19.252f, 30.48f, 19.661f, 30.142f, 19.951f, 29.71f)
                    curveTo(20.241f, 29.273f, 20.388f, 28.761f, 20.394f, 28.176f)
                    curveTo(20.388f, 27.568f, 20.229f, 27.031f, 19.917f, 26.565f)
                    curveTo(19.61f, 26.099f, 19.192f, 25.733f, 18.664f, 25.466f)
                    curveTo(18.135f, 25.199f, 17.53f, 25.065f, 16.849f, 25.065f)
                    curveTo(16.161f, 25.065f, 15.55f, 25.199f, 15.016f, 25.466f)
                    curveTo(14.482f, 25.733f, 14.062f, 26.099f, 13.755f, 26.565f)
                    curveTo(13.448f, 27.031f, 13.297f, 27.568f, 13.303f, 28.176f)
                    curveTo(13.297f, 28.761f, 13.437f, 29.273f, 13.721f, 29.71f)
                    curveTo(14.01f, 30.142f, 14.422f, 30.48f, 14.957f, 30.724f)
                    curveTo(15.491f, 30.963f, 16.121f, 31.082f, 16.849f, 31.082f)
                    close()
                    moveTo(16.849f, 22.96f)
                    curveTo(17.428f, 22.96f, 17.942f, 22.844f, 18.391f, 22.611f)
                    curveTo(18.84f, 22.378f, 19.192f, 22.054f, 19.448f, 21.639f)
                    curveTo(19.709f, 21.224f, 19.843f, 20.739f, 19.849f, 20.182f)
                    curveTo(19.843f, 19.636f, 19.712f, 19.159f, 19.456f, 18.75f)
                    curveTo(19.206f, 18.341f, 18.857f, 18.026f, 18.408f, 17.804f)
                    curveTo(17.959f, 17.577f, 17.44f, 17.463f, 16.849f, 17.463f)
                    curveTo(16.246f, 17.463f, 15.718f, 17.577f, 15.263f, 17.804f)
                    curveTo(14.814f, 18.026f, 14.465f, 18.341f, 14.215f, 18.75f)
                    curveTo(13.965f, 19.159f, 13.843f, 19.636f, 13.849f, 20.182f)
                    curveTo(13.843f, 20.739f, 13.968f, 21.224f, 14.224f, 21.639f)
                    curveTo(14.479f, 22.054f, 14.832f, 22.378f, 15.28f, 22.611f)
                    curveTo(15.735f, 22.844f, 16.258f, 22.96f, 16.849f, 22.96f)
                    close()
                    moveTo(30.992f, 33.29f)
                    curveTo(29.645f, 33.284f, 28.494f, 32.929f, 27.54f, 32.224f)
                    curveTo(26.585f, 31.52f, 25.855f, 30.494f, 25.35f, 29.148f)
                    curveTo(24.844f, 27.801f, 24.591f, 26.179f, 24.591f, 24.281f)
                    curveTo(24.591f, 22.389f, 24.844f, 20.773f, 25.35f, 19.432f)
                    curveTo(25.861f, 18.091f, 26.594f, 17.068f, 27.548f, 16.364f)
                    curveTo(28.509f, 15.659f, 29.656f, 15.307f, 30.992f, 15.307f)
                    curveTo(32.327f, 15.307f, 33.472f, 15.662f, 34.426f, 16.372f)
                    curveTo(35.381f, 17.077f, 36.111f, 18.099f, 36.617f, 19.44f)
                    curveTo(37.128f, 20.776f, 37.384f, 22.389f, 37.384f, 24.281f)
                    curveTo(37.384f, 26.185f, 37.131f, 27.81f, 36.625f, 29.156f)
                    curveTo(36.119f, 30.497f, 35.389f, 31.523f, 34.435f, 32.233f)
                    curveTo(33.48f, 32.938f, 32.333f, 33.29f, 30.992f, 33.29f)
                    close()
                    moveTo(30.992f, 31.014f)
                    curveTo(32.173f, 31.014f, 33.097f, 30.438f, 33.762f, 29.284f)
                    curveTo(34.432f, 28.131f, 34.767f, 26.463f, 34.767f, 24.281f)
                    curveTo(34.767f, 22.832f, 34.614f, 21.608f, 34.307f, 20.608f)
                    curveTo(34.006f, 19.602f, 33.571f, 18.841f, 33.003f, 18.324f)
                    curveTo(32.44f, 17.801f, 31.77f, 17.54f, 30.992f, 17.54f)
                    curveTo(29.816f, 17.54f, 28.892f, 18.119f, 28.222f, 19.278f)
                    curveTo(27.551f, 20.438f, 27.213f, 22.105f, 27.208f, 24.281f)
                    curveTo(27.208f, 25.736f, 27.358f, 26.966f, 27.659f, 27.972f)
                    curveTo(27.966f, 28.972f, 28.401f, 29.73f, 28.963f, 30.247f)
                    curveTo(29.526f, 30.758f, 30.202f, 31.014f, 30.992f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit80!!
    }

@Suppress("ObjectPropertyName")
private var _Limit80: ImageVector? = null
