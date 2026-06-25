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

package org.giste.roadbooknavigator.features.roadbook.domain.model

/**
 * Base class for all graphical or structural elements in a roadbook page.
 * Elements can be tracks, roads, icons, or text annotations.
 */
sealed class Element {
    abstract val elementType: ElementType

    enum class ElementType(val value: String) {
        Track("Track"),
        Road("Road"),
        Icon("Icon"),
        Text("Text"),
    }
}

/**
 * A complex road structure usually representing an intersection.
 * It combines an incoming road and an outgoing road to show the main path.
 */
data class Track(
    val roadIn: Road,
    val roadOut: Road,
) : Element() {
    override val elementType: ElementType = ElementType.Track
}

/**
 * A single road line or curve in a tulip diagram.
 *
 * @property start Starting point on the canvas.
 * @property end Ending point on the canvas.
 * @property handles Bezier control points for drawing curves.
 */
data class Road(
    val start: Point?,
    val end: Point?,
    val roadType: RoadType = RoadType.Track,
    val handles: List<Point> = emptyList(),
) : Element() {
    override val elementType: ElementType = ElementType.Road

    enum class RoadType(val value: Int) {
        LowVisibleTrack(15),
        OffTrack(16),
        SmallTrack(4),
        Track(17),
        TarmacRoad(18),
        DualCarriageway(12),
    }
}

/**
 * A graphical icon element in the roadbook (e.g., Danger, Fuel, Tunnel).
 *
 * @property type The specific kind of icon.
 * @property angle Rotation angle in degrees.
 * @property scaleX Horizontal scaling factor for drawing.
 * @property originalId The raw ID from the source file, used for debugging or unknown icons.
 */
data class Icon(
    val type: IconType,
    val width: Int,
    val height: Int,
    val center: Point,
    val angle: Int = 0,
    val scaleX: Double = 1.0,
    val scaleY: Double = 1.0,
    val originalId: String? = null,
) : Element() {
    override val elementType: ElementType = ElementType.Icon

    enum class IconType {
        Danger1, Danger2, Danger3, FuelZone, ResetDistance,
        AboveBridge, UnderBridge, FortCastle, House,
        TrafficLight, Tunnel, Alert, Roundabout, Stop,
        RiverWater, Unknown
    }
}

data class Text(
    val text: String,
    val fontSize: Int = 18,
    val lineHeight: Double,
    val width: Double,
    val height: Double,
    val maxWidth: Double,
    val maxHeight: Double,
    val center: Point,
) : Element() {
    override val elementType: ElementType = ElementType.Text
}

/**
 * Represents a 2D point in the drawing canvas (tulips/icons).
 * This is different from geographic Coordinates.
 */
data class Point(
    val x: Double,
    val y: Double,
)