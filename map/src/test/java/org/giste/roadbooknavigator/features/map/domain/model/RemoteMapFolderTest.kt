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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class RemoteMapFolderTest {

    @Test
    fun `filterMaps should remove downloaded maps`() {
        val map1 = RemoteMapFile("map1", "/", "url1", 100L, 0L)
        val map2 = RemoteMapFile("map2", "/", "url2", 100L, 0L)
        val folder = RemoteMapFolder("root", "/", maps = listOf(map1, map2))

        val filtered = folder.filterMaps(setOf("url1"))

        assertNotNull(filtered)
        assertEquals(1, filtered!!.maps.size)
        assertEquals("map2", filtered.maps[0].name)
    }

    @Test
    fun `filterMaps should return null if all maps are filtered and no subfolders`() {
        val map1 = RemoteMapFile("map1", "/", "url1", 100L, 0L)
        val folder = RemoteMapFolder("root", "/", maps = listOf(map1))

        val filtered = folder.filterMaps(setOf("url1"))

        assertNull(filtered)
    }

    @Test
    fun `filterMaps should recursively filter subfolders`() {
        val map1 = RemoteMapFile("map1", "/sub", "url1", 100L, 0L)
        val map2 = RemoteMapFile("map2", "/sub", "url2", 100L, 0L)
        val subFolder = RemoteMapFolder("sub", "/sub", maps = listOf(map1, map2))
        val root = RemoteMapFolder("root", "/", subFolders = listOf(subFolder))

        val filtered = root.filterMaps(setOf("url1"))

        assertNotNull(filtered)
        assertEquals(1, filtered!!.subFolders.size)
        assertEquals(1, filtered.subFolders[0].maps.size)
        assertEquals("map2", filtered.subFolders[0].maps[0].name)
    }

    @Test
    fun `filterMaps should remove empty subfolders`() {
        val map1 = RemoteMapFile("map1", "/sub", "url1", 100L, 0L)
        val subFolder = RemoteMapFolder("sub", "/sub", maps = listOf(map1))
        val root = RemoteMapFolder("root", "/", subFolders = listOf(subFolder))

        val filtered = root.filterMaps(setOf("url1"))

        assertNull(filtered)
    }
}
