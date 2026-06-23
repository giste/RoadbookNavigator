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
val RoadbookIcons.Speed.Limit10: ImageVector
    get() {
        if (_Limit10 != null) {
            return _Limit10!!
        }
        _Limit10 = ImageVector.Builder(
            name = "Speed.Limit10",
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
                    moveTo(19.483f, 15.545f)
                    verticalLineTo(33f)
                    horizontalLineTo(16.841f)
                    verticalLineTo(18.188f)
                    horizontalLineTo(16.739f)
                    lineTo(12.563f, 20.915f)
                    verticalLineTo(18.392f)
                    lineTo(16.918f, 15.545f)
                    horizontalLineTo(19.483f)
                    close()
                    moveTo(29.081f, 33.29f)
                    curveTo(27.735f, 33.284f, 26.584f, 32.929f, 25.63f, 32.224f)
                    curveTo(24.675f, 31.52f, 23.945f, 30.494f, 23.439f, 29.148f)
                    curveTo(22.934f, 27.801f, 22.681f, 26.179f, 22.681f, 24.281f)
                    curveTo(22.681f, 22.389f, 22.934f, 20.773f, 23.439f, 19.432f)
                    curveTo(23.951f, 18.091f, 24.684f, 17.068f, 25.638f, 16.364f)
                    curveTo(26.598f, 15.659f, 27.746f, 15.307f, 29.081f, 15.307f)
                    curveTo(30.417f, 15.307f, 31.562f, 15.662f, 32.516f, 16.372f)
                    curveTo(33.471f, 17.077f, 34.201f, 18.099f, 34.707f, 19.44f)
                    curveTo(35.218f, 20.776f, 35.473f, 22.389f, 35.473f, 24.281f)
                    curveTo(35.473f, 26.185f, 35.221f, 27.81f, 34.715f, 29.156f)
                    curveTo(34.209f, 30.497f, 33.479f, 31.523f, 32.525f, 32.233f)
                    curveTo(31.57f, 32.938f, 30.422f, 33.29f, 29.081f, 33.29f)
                    close()
                    moveTo(29.081f, 31.014f)
                    curveTo(30.263f, 31.014f, 31.187f, 30.438f, 31.851f, 29.284f)
                    curveTo(32.522f, 28.131f, 32.857f, 26.463f, 32.857f, 24.281f)
                    curveTo(32.857f, 22.832f, 32.704f, 21.608f, 32.397f, 20.608f)
                    curveTo(32.096f, 19.602f, 31.661f, 18.841f, 31.093f, 18.324f)
                    curveTo(30.53f, 17.801f, 29.86f, 17.54f, 29.081f, 17.54f)
                    curveTo(27.905f, 17.54f, 26.982f, 18.119f, 26.312f, 19.278f)
                    curveTo(25.641f, 20.438f, 25.303f, 22.105f, 25.297f, 24.281f)
                    curveTo(25.297f, 25.736f, 25.448f, 26.966f, 25.749f, 27.972f)
                    curveTo(26.056f, 28.972f, 26.491f, 29.73f, 27.053f, 30.247f)
                    curveTo(27.616f, 30.758f, 28.292f, 31.014f, 29.081f, 31.014f)
                    close()
                }
            }
        }.build()

        return _Limit10!!
    }

@Suppress("ObjectPropertyName")
private var _Limit10: ImageVector? = null
