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
 * along with this program.  See the License for more details.
 */

package org.giste.roadbooknavigator.features.map.domain.repository

import kotlinx.coroutines.flow.Flow
import org.giste.roadbooknavigator.features.map.domain.model.MapFile

interface MapRepository {
    fun getLocalMaps(): Flow<List<MapFile>>
    suspend fun deleteMap(mapFile: MapFile)
    fun getMapInternalStorageDir(): String
    suspend fun refresh()
}
