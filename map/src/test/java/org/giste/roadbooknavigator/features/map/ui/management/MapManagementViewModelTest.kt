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

package org.giste.roadbooknavigator.features.map.ui.management

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.domain.model.DownloadStatus
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.model.MapOverview
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import org.giste.roadbooknavigator.features.map.domain.usecase.DeleteMapUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.DownloadMapUseCase
import org.giste.roadbooknavigator.features.map.domain.usecase.GetMapOverviewUseCase
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapManagementViewModelTest {

    private val getMapOverviewUseCase: GetMapOverviewUseCase = mockk()
    private val downloadMapUseCase: DownloadMapUseCase = mockk()
    private val deleteMapUseCase: DeleteMapUseCase = mockk()
    private val logger: Logger = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()

    private val dummyOverview = MapOverview(
        downloadedMaps = emptyList(),
        remoteFolders = emptyList()
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should be Loading initially`() = runTest {
        every { getMapOverviewUseCase() } returns flowOf(dummyOverview)

        val viewModel = MapManagementViewModel(
            getMapOverviewUseCase,
            downloadMapUseCase,
            deleteMapUseCase,
            logger
        )

        assertEquals(MapManagementUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `uiState should be Success when overview is loaded`() = runTest {
        val overviewFlow = MutableStateFlow(dummyOverview)
        every { getMapOverviewUseCase() } returns overviewFlow

        val viewModel = MapManagementViewModel(
            getMapOverviewUseCase,
            downloadMapUseCase,
            deleteMapUseCase,
            logger
        )

        val job = launch(testDispatcher) {
            viewModel.uiState.collect {}
        }
        runCurrent()

        assertTrue(viewModel.uiState.value is MapManagementUiState.Success)
        val state = viewModel.uiState.value as MapManagementUiState.Success
        assertEquals(dummyOverview.downloadedMaps, state.downloadedMaps)
        assertEquals(dummyOverview.remoteFolders, state.remoteFolders)

        job.cancel()
    }

    @Test
    fun `uiState should be Error when overview loading fails`() = runTest {
        val errorMessage = "Network error"
        every { getMapOverviewUseCase() } returns flow { throw Exception(errorMessage) }

        val viewModel = MapManagementViewModel(
            getMapOverviewUseCase,
            downloadMapUseCase,
            deleteMapUseCase,
            logger
        )

        val job = launch(testDispatcher) {
            viewModel.uiState.collect {}
        }
        runCurrent()

        assertTrue(viewModel.uiState.value is MapManagementUiState.Error)
        assertEquals(errorMessage, (viewModel.uiState.value as MapManagementUiState.Error).message)

        job.cancel()
    }

    @Test
    fun `downloadMap should call downloadMapUseCase and update status`() = runTest {
        val remoteMap = RemoteMapFile("spain", "/", "http://spain.map", 100L, 0L)
        every { getMapOverviewUseCase() } returns flowOf(dummyOverview)
        every { downloadMapUseCase(remoteMap) } returns flowOf(
            DownloadStatus.Progress(0.5f),
            DownloadStatus.Success
        )

        val viewModel = MapManagementViewModel(
            getMapOverviewUseCase,
            downloadMapUseCase,
            deleteMapUseCase,
            logger
        )

        val job = launch(testDispatcher) {
            viewModel.uiState.collect {}
        }
        runCurrent()

        viewModel.downloadMap(remoteMap)
        runCurrent()

        val state = viewModel.uiState.value as MapManagementUiState.Success
        assertEquals(DownloadStatus.Success, state.downloadingStatus[remoteMap.url])

        job.cancel()
    }

    @Test
    fun `deleteMap should call deleteMapUseCase`() = runTest {
        val mapFile = MapFile("spain.map", "/path/spain.map", 100L, 0L, "europe")
        every { getMapOverviewUseCase() } returns flowOf(dummyOverview)
        coEvery { deleteMapUseCase(mapFile) } returns Unit

        val viewModel = MapManagementViewModel(
            getMapOverviewUseCase,
            downloadMapUseCase,
            deleteMapUseCase,
            logger
        )

        viewModel.deleteMap(mapFile)
        runCurrent()

        coVerify { deleteMapUseCase(mapFile) }
    }

    @Test
    fun `cancelDownload should cancel job and remove from status`() = runTest {
        val remoteMap = RemoteMapFile("spain", "/", "http://spain.map", 100L, 0L)
        every { getMapOverviewUseCase() } returns flowOf(dummyOverview)
        // A flow that doesn't complete immediately
        val downloadFlow = MutableStateFlow<DownloadStatus>(DownloadStatus.Progress(0.1f))
        every { downloadMapUseCase(remoteMap) } returns downloadFlow

        val viewModel = MapManagementViewModel(
            getMapOverviewUseCase,
            downloadMapUseCase,
            deleteMapUseCase,
            logger
        )

        val job = launch(testDispatcher) {
            viewModel.uiState.collect {}
        }
        runCurrent()

        viewModel.downloadMap(remoteMap)
        runCurrent()

        assertTrue((viewModel.uiState.value as MapManagementUiState.Success).downloadingStatus.containsKey(remoteMap.url))

        every { downloadMapUseCase.cancelDownload(remoteMap.url) } returns Unit
        viewModel.cancelDownload(remoteMap.url)
        runCurrent()

        assertTrue((viewModel.uiState.value as MapManagementUiState.Success).downloadingStatus.isEmpty())

        job.cancel()
    }
}
