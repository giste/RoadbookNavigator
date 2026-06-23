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

import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.sp
import org.giste.roadbooknavigator.features.roadbook.domain.Icon
import org.giste.roadbooknavigator.features.roadbook.domain.Road
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import org.giste.roadbooknavigator.features.roadbook.domain.Text as TulipText

internal const val CANVAS_LOGICAL_WIDTH = 200f
internal const val CANVAS_LOGICAL_HEIGHT = 135f
internal val CANVAS_CENTER_POINT = Offset(100f, 85f)
internal const val SMOOTHNESS = 0.2f

internal enum class RoadTermination {
    NONE,
    ARROW,
    PERPENDICULAR
}

internal fun DrawScope.drawWaypointStart(color: Color) {
    val start = CANVAS_CENTER_POINT
    val end = start + Offset(15f, 15f)

    val path = Path().apply {
        moveTo(start.x, start.y)
        lineTo(end.x, end.y)
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(width = 2f, join = StrokeJoin.Miter, cap = StrokeCap.Butt)
    )
    drawCircle(
        radius = 3f,
        color = color,
        center = end,
    )
}

internal fun DrawScope.drawTulipText(
    textElement: TulipText,
    textMeasurer: TextMeasurer,
    color: Color,
    scale: Float
) {
    val center = Offset(
        textElement.center.x.toFloat() * scale,
        textElement.center.y.toFloat() * scale
    )
    val maxWidth = textElement.maxWidth.toFloat() * scale
    val maxHeight = textElement.maxHeight.toFloat() * scale

    // To ensure sharpness, we calculate the font size in pixels and then convert to Sp
    val pixelFontSize = textElement.fontSize.toFloat() * scale
    val fontSize = (pixelFontSize / density).sp

    val style = TextStyle(
        color = color,
        fontSize = fontSize,
        lineHeight = fontSize * (textElement.lineHeight.toFloat() * 1.1f),
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    val textLayoutResult = textMeasurer.measure(
        text = textElement.text,
        style = style,
        constraints = Constraints(
            maxWidth = maxWidth.toInt(),
            maxHeight = maxHeight.toInt()
        )
    )

    // Center the text block within its defined maxWidth/maxHeight box
    val boxTopLeft = Offset(
        center.x - maxWidth / 2f,
        center.y - maxHeight / 2f
    )

    val topLeft = Offset(
        boxTopLeft.x + (maxWidth - textLayoutResult.size.width) / 2f,
        boxTopLeft.y + (maxHeight - textLayoutResult.size.height) / 2f
    )

    clipRect(
        left = boxTopLeft.x,
        top = boxTopLeft.y,
        right = boxTopLeft.x + maxWidth,
        bottom = boxTopLeft.y + maxHeight
    ) {
        drawText(textLayoutResult, topLeft = topLeft)
    }
}

internal fun DrawScope.drawTulipIcon(
    icon: Icon,
    painter: Painter,
    tint: Color? = null,
    scale: Float
) {
    val width = icon.width.toFloat() * scale
    val height = icon.height.toFloat() * scale
    val center = Offset(
        icon.center.x.toFloat() * scale,
        icon.center.y.toFloat() * scale
    )

    val drawSize = Size(width, height)

    withTransform({
        translate(center.x, center.y)
        rotate(icon.angle.toFloat(), pivot = Offset.Zero)
        translate(-drawSize.width / 2f, -drawSize.height / 2f)
    }) {
        with(painter) {
            draw(
                size = drawSize,
                alpha = 1f,
                colorFilter = tint?.let { ColorFilter.tint(it) }
            )
        }
    }
}

internal fun DrawScope.drawRoad(
    road: Road,
    color: Color,
    secondaryColor: Color,
    termination: RoadTermination
) {
    val endRelative = road.end ?: return
    val start = if (road.start != null) {
        CANVAS_CENTER_POINT + Offset(road.start.x.toFloat(), road.start.y.toFloat())
    } else {
        CANVAS_CENTER_POINT
    }
    val end = CANVAS_CENTER_POINT + Offset(endRelative.x.toFloat(), endRelative.y.toFloat())

    val allPoints = mutableListOf<Offset>().let {
        it.add(start)
        road.handles.forEach { handle ->
            it.add(CANVAS_CENTER_POINT + Offset(handle.x.toFloat(), handle.y.toFloat()))
        }
        it.add(end)
        it.toList()
    }
    val angle = getAngle(allPoints, end, start)
    val path = getPath(allPoints)

    when (road.roadType) {
        Road.RoadType.Track -> {
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 6f, join = StrokeJoin.Miter, cap = StrokeCap.Butt)
            )
        }

        Road.RoadType.SmallTrack -> {
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 4f, join = StrokeJoin.Miter, cap = StrokeCap.Butt)
            )
        }

        Road.RoadType.LowVisibleTrack -> {
            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 4f,
                    join = StrokeJoin.Miter,
                    cap = StrokeCap.Butt,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 5f, 5f, 5f), 0f)
                )
            )
        }

        Road.RoadType.OffTrack -> {
            drawPath(
                path = path,
                color = color,
                style = Stroke(
                    width = 4f,
                    join = StrokeJoin.Miter,
                    cap = StrokeCap.Butt,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                )
            )
        }

        Road.RoadType.TarmacRoad -> {
            val nativePath = path.asAndroidPath()
            val outlinePath = android.graphics.Path()
            val outlinePaint = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 6f
                strokeCap = Paint.Cap.BUTT
                strokeJoin = Paint.Join.MITER
                isAntiAlias = true
            }
            outlinePaint.getFillPath(nativePath, outlinePath)

            drawPath(
                path = outlinePath.asComposePath(),
                color = color,
                style = Stroke(width = 2f, join = StrokeJoin.Miter, cap = StrokeCap.Butt)
            )
        }

        Road.RoadType.DualCarriageway -> {
            drawPath(
                path = path,
                color = secondaryColor,
                style = Stroke(width = 2f, join = StrokeJoin.Miter, cap = StrokeCap.Butt)
            )

            val nativePath = path.asAndroidPath()
            val outlinePath = android.graphics.Path()
            val outlinePaint = Paint().apply {
                style = Paint.Style.STROKE
                strokeWidth = 8f
                strokeCap = Paint.Cap.BUTT
                strokeJoin = Paint.Join.MITER
                isAntiAlias = true
            }
            outlinePaint.getFillPath(nativePath, outlinePath)

            drawPath(
                path = outlinePath.asComposePath(),
                color = color,
                style = Stroke(width = 2f, join = StrokeJoin.Miter, cap = StrokeCap.Butt)
            )
        }
    }

    when (termination) {
        RoadTermination.ARROW -> drawArrowHead(angle, end, color)
        RoadTermination.PERPENDICULAR -> drawPerpendicularEnd(angle, end, color)
        RoadTermination.NONE -> {}
    }
}

private fun getPath(
    allPoints: List<Offset>
): Path = Path().apply {
    if (allPoints.size >= 2) {
        moveTo(allPoints[0].x, allPoints[0].y)
        if (allPoints.size == 2) {
            lineTo(allPoints[1].x, allPoints[1].y)
        } else {
            for (i in 0 until allPoints.size - 1) {
                val current = allPoints[i]
                val next = allPoints[i + 1]

                when (i) {
                    0 -> {
                        val post = allPoints[2]
                        val cpX = next.x - (post.x - current.x) * SMOOTHNESS
                        val cpY = next.y - (post.y - current.y) * SMOOTHNESS
                        quadraticTo(cpX, cpY, next.x, next.y)
                    }

                    allPoints.size - 2 -> {
                        val prev = allPoints[i - 1]
                        val cpX = current.x + (next.x - prev.x) * SMOOTHNESS
                        val cpY = current.y + (next.y - prev.y) * SMOOTHNESS
                        quadraticTo(cpX, cpY, next.x, next.y)
                    }

                    else -> {
                        val prev = allPoints[i - 1]
                        val post = allPoints[i + 2]

                        val cp1 = current + (next - prev) * SMOOTHNESS
                        val cp2 = next - (post - current) * SMOOTHNESS

                        cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, next.x, next.y)
                    }
                }
            }
        }
    }
}

private fun getAngle(
    allPoints: List<Offset>,
    end: Offset,
    start: Offset
): Float = if (allPoints.size > 2) {
    val next = allPoints.last()
    val current = allPoints[allPoints.size - 2]
    val prev = allPoints[allPoints.size - 3]
    val cpX = current.x + (next.x - prev.x) * SMOOTHNESS
    val cpY = current.y + (next.y - prev.y) * SMOOTHNESS
    atan2(next.y - cpY, next.x - cpX)
} else {
    atan2(end.y - start.y, end.x - start.x)
}

private fun DrawScope.drawArrowHead(angle: Float, lineEnd: Offset, color: Color) {
    val arrowSize = 20f
    val arrowHalfAngle = Math.toRadians(30.0).toFloat()
    val height = arrowSize * cos(arrowHalfAngle)

    val tip = Offset(
        lineEnd.x + height * cos(angle),
        lineEnd.y + height * sin(angle)
    )

    val path = Path().apply {
        moveTo(tip.x, tip.y)
        lineTo(
            tip.x - arrowSize * cos(angle - arrowHalfAngle),
            tip.y - arrowSize * sin(angle - arrowHalfAngle)
        )
        lineTo(
            tip.x - arrowSize * cos(angle + arrowHalfAngle),
            tip.y - arrowSize * sin(angle + arrowHalfAngle)
        )
        close()
    }
    drawPath(
        path = path,
        color = color
    )
}

private fun DrawScope.drawPerpendicularEnd(angle: Float, end: Offset, color: Color) {
    val segmentLength = 20f
    val dx = cos(angle)
    val dy = sin(angle)

    val p1 = Offset(end.x - dy * segmentLength / 2, end.y + dx * segmentLength / 2)
    val p2 = Offset(end.x + dy * segmentLength / 2, end.y - dx * segmentLength / 2)

    drawLine(
        color = color,
        start = p1,
        end = p2,
        strokeWidth = 2f,
        cap = StrokeCap.Butt
    )
}
