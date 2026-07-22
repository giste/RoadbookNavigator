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

package org.giste.roadbooknavigator.features.map.domain.usecase

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.giste.roadbooknavigator.features.map.domain.model.DownloadedMapStatus
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import org.giste.roadbooknavigator.features.map.domain.repository.MapRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetMapOverviewUseCaseTest {

    private val repository: MapRepository = mockk()
    private val getMapOverviewUseCase = GetMapOverviewUseCase(repository)

    @Test
    fun `invoke should combine local and remote maps and identify status correctly`() = runTest {
        val localMapUpToDate = MapFile("up-to-date.map", "/path1", 100L, 1000L, "folder")
        val localMapObsolete = MapFile("obsolete.map", "/path2", 100L, 1000L, "folder")
        val localMapToUpdate = MapFile("update.map", "/path3", 100L, 1000L, "folder")

        val remoteMapUpToDate = RemoteMapFile("up-to-date.map", "folder", "url1", 100L, 1000L)
        val remoteMapToUpdate = RemoteMapFile("update.map", "folder", "url3", 100L, 2000L)

        val remoteRoot = RemoteMapFolder("root", "/", maps = listOf(remoteMapUpToDate, remoteMapToUpdate))

        every { repository.getLocalMaps() } returns flowOf(listOf(localMapUpToDate, localMapObsolete, localMapToUpdate))
        every { repository.getRemoteMaps() } returns flowOf(listOf(remoteRoot))

        val result = getMapOverviewUseCase().toList().first()

        assertEquals(3, result.downloadedMaps.size)
        
        val upToDateInfo = result.downloadedMaps.find { it.mapFile.name == "up-to-date.map" }
        assertTrue(upToDateInfo?.status is DownloadedMapStatus.UpToDate)
        assertEquals(remoteMapUpToDate, (upToDateInfo?.status as DownloadedMapStatus.UpToDate).remoteMapFile)

        val obsoleteInfo = result.downloadedMaps.find { it.mapFile.name == "obsolete.map" }
        assertEquals(DownloadedMapStatus.Obsolete, obsoleteInfo?.status)

        val updateInfo = result.downloadedMaps.find { it.mapFile.name == "update.map" }
        assertTrue(updateInfo?.status is DownloadedMapStatus.UpdateAvailable)
        assertEquals(remoteMapToUpdate, (updateInfo?.status as DownloadedMapStatus.UpdateAvailable).remoteMapFile)
        
        assertEquals(listOf(remoteRoot), result.remoteFolders)
    }
}
