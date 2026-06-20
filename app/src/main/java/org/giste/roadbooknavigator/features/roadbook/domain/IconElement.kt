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
