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

package org.giste.roadbooknavigator.features.map.data.repository

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.data.datasource.RemoteMapDataSource
import org.giste.roadbooknavigator.features.map.domain.model.DownloadStatus
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HttpRemoteMapRepositoryTest {

    private val dataSource: RemoteMapDataSource = mockk()
    private val logger: Logger = mockk(relaxed = true)
    private val repository = HttpRemoteMapRepository(dataSource, Dispatchers.Unconfined, logger)

    @Test
    fun `getRemoteMaps should fetch and return full folder tree recursively`() = runTest {
        val rootUrl = "https://ftp-stud.hs-esslingen.de/pub/Mirrors/download.mapsforge.org/maps/v5/"
        val europeUrl = "${rootUrl}europe/"
        val germanyUrl = "${europeUrl}germany/"
        
        val rootFolder = RemoteMapFolder("v5", rootUrl, subFolders = listOf(RemoteMapFolder("europe", europeUrl)))
        val europeFolder = RemoteMapFolder("europe", europeUrl, subFolders = listOf(RemoteMapFolder("germany", germanyUrl)))
        val germanyFolder = RemoteMapFolder("germany", germanyUrl, maps = listOf(RemoteMapFile("berlin.map", "europe/germany", "url", 100L, 1000L)))
        
        coEvery { dataSource.getRemoteMaps(rootUrl) } returns rootFolder
        coEvery { dataSource.getRemoteMaps(europeUrl) } returns europeFolder
        coEvery { dataSource.getRemoteMaps(germanyUrl) } returns germanyFolder
        
        val result = repository.getRemoteMaps().first()
        
        assertEquals("v5", result.name)
        assertEquals(1, result.subFolders.size)
        val europe = result.subFolders[0]
        assertEquals("europe", europe.name)
        assertEquals(1, europe.subFolders.size)
        val germany = europe.subFolders[0]
        assertEquals("germany", germany.name)
        assertEquals(1, germany.maps.size)
        assertEquals("berlin.map", germany.maps[0].name)
    }

    @Test
    fun `downloadMap should emit progress and success`() = runTest {
        val remoteMapFile = RemoteMapFile("spain.map", "europe", "http://url", 100L, 1000L)
        val destinationPath = "/tmp/spain.map"
        val responseBody = mockk<okhttp3.ResponseBody>(relaxed = true)
        
        coEvery { dataSource.downloadFile(remoteMapFile.url) } returns responseBody
        coEvery { responseBody.contentLength() } returns 10L
        coEvery { responseBody.source().read(any<ByteArray>()) } returns 5 andThen 5 andThen -1
        
        val results = repository.downloadMap(remoteMapFile, destinationPath).toList()
        
        assertTrue(results.contains(DownloadStatus.Idle))
        assertTrue(results.any { it is DownloadStatus.Progress })
        assertTrue(results.contains(DownloadStatus.Success))
    }
}
