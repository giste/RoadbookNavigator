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

package org.giste.roadbook.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.rememberTextMeasurer
import org.giste.roadbook.domain.model.Icon
import org.giste.roadbook.domain.model.Road
import org.giste.roadbook.domain.model.Track
import org.giste.roadbook.domain.model.Waypoint
import org.giste.roadbook.ui.theme.RoadbookTheme
import org.giste.roadbook.domain.model.Text as TulipText

@Composable
internal fun TulipSection(waypoint: Waypoint, modifier: Modifier = Modifier) {
    val onSurfaceColor = RoadbookTheme.colors.onBackground
    val surfaceColor = RoadbookTheme.colors.background
    val trackColor = RoadbookTheme.colors.track
    val secondaryTrackColor = RoadbookTheme.colors.trackSecondary
    val errorColor = RoadbookTheme.colors.danger
    val disabledOnSurface = RoadbookTheme.colors.onBackground.copy(alpha = 0.5f)
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
                withTransform({
                    scale(scale, scale, pivot = Offset.Zero)
                }) {
                    drawWaypointStart(onSurfaceColor)
                }
                waypoint.tulipElements.forEach { element ->
                    when (element) {
                        is Road -> withTransform({
                            scale(scale, scale, pivot = Offset.Zero)
                        }) {
                            drawRoad(
                                element,
                                onSurfaceColor,
                                disabledOnSurface,
                                RoadTermination.PERPENDICULAR
                            )
                        }

                        is Track -> withTransform({
                            scale(scale, scale, pivot = Offset.Zero)
                        }) {
                            drawRoad(
                                element.roadIn,
                                trackColor,
                                secondaryTrackColor,
                                RoadTermination.NONE
                            )
                            drawRoad(
                                element.roadOut,
                                trackColor,
                                secondaryTrackColor,
                                RoadTermination.ARROW
                            )
                        }

                        is Icon -> {
                            val painter = iconPainters[element]
                            if (painter != null) {
                                val tint = when (element.type) {
                                    Icon.IconType.Danger1,
                                    Icon.IconType.Danger2,
                                    Icon.IconType.Danger3 -> errorColor

                                    else -> null
                                }
                                drawTulipIcon(element, painter, tint, scale)
                            } else {
                                drawUnknownIcon(element, textMeasurer, errorColor, scale)
                            }
                        }

                        is TulipText -> {
                            drawTulipText(element, textMeasurer, onSurfaceColor, scale)
                        }
                    }
                }
            }
        }
    }
}
