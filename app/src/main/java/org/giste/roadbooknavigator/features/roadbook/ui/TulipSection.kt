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

package org.giste.roadbooknavigator.features.roadbook.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.rememberTextMeasurer
import org.giste.roadbooknavigator.features.roadbook.domain.model.Icon
import org.giste.roadbooknavigator.features.roadbook.domain.model.Road
import org.giste.roadbooknavigator.features.roadbook.domain.model.Track
import org.giste.roadbooknavigator.features.roadbook.domain.model.Waypoint
import org.giste.roadbooknavigator.features.roadbook.domain.model.Text as TulipText

@Composable
internal fun TulipSection(waypoint: Waypoint, modifier: Modifier = Modifier) {
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val surfaceColor = MaterialTheme.colorScheme.surface
    val trackColor = MaterialTheme.colorScheme.primary
    val secondaryTrackColor = MaterialTheme.colorScheme.secondary
    val errorColor = MaterialTheme.colorScheme.error
    val disabledOnSurface = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val textMeasurer = rememberTextMeasurer()

    // Preload painters for icons to use them inside Canvas
    val iconPainters = mutableMapOf<Icon, Painter>()
    waypoint.tulipElements.filterIsInstance<Icon>().forEach { icon ->
        IconMapper.getIcon(icon.type, onSurfaceColor, surfaceColor)?.let { vector ->
            iconPainters[icon] = rememberVectorPainter(vector)
        }
    }

    Box(
        modifier = modifier
            .aspectRatio(CANVAS_LOGICAL_WIDTH / CANVAS_LOGICAL_HEIGHT)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scale = size.width / CANVAS_LOGICAL_WIDTH
            clipRect {
                // 1. Draw scaled elements (Roads)
                withTransform({
                    scale(scale, scale, pivot = Offset.Zero)
                }) {
                    drawWaypointStart(onSurfaceColor)
                    waypoint.tulipElements.forEach { element ->
                        when (element) {
                            is Road -> drawRoad(element, onSurfaceColor, disabledOnSurface, RoadTermination.PERPENDICULAR)
                            is Track -> {
                                drawRoad(element.roadIn, trackColor, secondaryTrackColor, RoadTermination.NONE)
                                drawRoad(element.roadOut, trackColor, secondaryTrackColor, RoadTermination.ARROW)
                            }
                            else -> {}
                        }
                    }
                }

                // 2. Draw pixel-perfect elements (Icons and Text)
                waypoint.tulipElements.forEach { element ->
                    when (element) {
                        is Icon -> {
                            iconPainters[element]?.let { painter ->
                                val tint = when (element.type) {
                                    Icon.IconType.Danger1,
                                    Icon.IconType.Danger2,
                                    Icon.IconType.Danger3 -> errorColor
                                    else -> null
                                }
                                drawTulipIcon(element, painter, tint, scale)
                            }
                        }

                        is TulipText -> {
                            drawTulipText(element, textMeasurer, onSurfaceColor, scale)
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
