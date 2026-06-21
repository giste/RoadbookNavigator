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

package org.giste.roadbooknavigator.features.roadbook.data.dto.persistence

import kotlinx.serialization.Serializable

@Serializable
data class PersistentRoad(
    val start: PersistentPoint?,
    val end: PersistentPoint?,
    val roadType: String,
    val handles: List<PersistentPoint> = emptyList(),
) : PersistentElement()

@Serializable
data class PersistentTrack(
    val roadIn: PersistentRoad,
    val roadOut: PersistentRoad,
) : PersistentElement()
