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

package org.giste.roadbooknavigator.features.roadbook.domain

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
