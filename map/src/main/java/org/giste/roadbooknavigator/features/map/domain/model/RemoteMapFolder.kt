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

package org.giste.roadbooknavigator.features.map.domain.model

data class RemoteMapFolder(
    val name: String,
    val path: String,
    val subFolders: List<RemoteMapFolder> = emptyList(),
    val maps: List<RemoteMapFile> = emptyList()
) {
    fun findMap(parentPath: String, name: String): RemoteMapFile? {
        maps.find { it.parentPath == parentPath && it.name == name }?.let { return it }
        for (folder in subFolders) {
            folder.findMap(parentPath, name)?.let { return it }
        }
        return null
    }

    fun filterMaps(downloadedUrls: Set<String>): RemoteMapFolder? {
        val filteredMaps = maps.filter { it.url !in downloadedUrls }
        val filteredSubFolders = subFolders.mapNotNull { it.filterMaps(downloadedUrls) }

        return if (filteredMaps.isEmpty() && filteredSubFolders.isEmpty()) {
            null
        } else {
            copy(maps = filteredMaps, subFolders = filteredSubFolders)
        }
    }
}
