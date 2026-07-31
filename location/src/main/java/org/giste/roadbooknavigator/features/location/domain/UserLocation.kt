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

package org.giste.roadbooknavigator.features.location.domain

/**
 * Domain representation of a GPS location to decouple from Android Framework.
 */
public data class UserLocation(
    public val latitude: Double,
    public val longitude: Double,
    public val altitude: Double,
    public val accuracy: Float,
    public val verticalAccuracy: Float? = null,
    public val speed: Float, // in m/s
    public val bearing: Float, // in degrees
    public val time: Long
)
