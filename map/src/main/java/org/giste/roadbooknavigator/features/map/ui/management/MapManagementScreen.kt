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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.map.R
import org.giste.roadbooknavigator.features.map.domain.model.DownloadStatus
import org.giste.roadbooknavigator.features.map.domain.model.DownloadedMapInfo
import org.giste.roadbooknavigator.features.map.domain.model.DownloadedMapStatus
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import org.giste.roadbooknavigator.core.R as CoreR

@Composable
fun MapManagementScreen(
    viewModel: MapManagementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    MapManagementContent(
        uiState = uiState,
        onDownloadClick = viewModel::downloadMap,
        onDeleteClick = viewModel::deleteMap,
        onCancelDownloadClick = viewModel::cancelDownload
    )
}

@Composable
fun MapManagementContent(
    uiState: MapManagementUiState,
    onDownloadClick: (RemoteMapFile) -> Unit,
    onDeleteClick: (MapFile) -> Unit,
    onCancelDownloadClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is MapManagementUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("MapManagementLoading")
                )
            }

            is MapManagementUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.iconSize)
                    )
                    Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                }
            }

            is MapManagementUiState.Success -> {
                MapList(
                    downloadedMaps = uiState.downloadedMaps,
                    remoteFolders = uiState.remoteFolders,
                    downloadingStatus = uiState.downloadingStatus,
                    onDownloadClick = onDownloadClick,
                    onDeleteClick = onDeleteClick,
                    onCancelDownloadClick = onCancelDownloadClick
                )
            }
        }
    }
}

@Composable
fun MapList(
    downloadedMaps: List<DownloadedMapInfo>,
    remoteFolders: RemoteMapFolder,
    downloadingStatus: Map<String, DownloadStatus>,
    onDownloadClick: (RemoteMapFile) -> Unit,
    onDeleteClick: (MapFile) -> Unit,
    onCancelDownloadClick: (String) -> Unit
) {
    var expandedSections by rememberSaveable { mutableStateOf(setOf<String>()) }
    val rootTitle = stringResource(R.string.map_management_root_folder)

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Downloaded maps section
        if (downloadedMaps.isNotEmpty()) {
            val sectionKey = "downloaded"
            val isExpanded = sectionKey in expandedSections
            item {
                SectionHeader(
                    title = stringResource(R.string.map_management_downloaded_title),
                    isExpanded = isExpanded,
                    onToggleExpand = {
                        expandedSections = if (isExpanded) expandedSections - sectionKey else expandedSections + sectionKey
                    },
                    modifier = Modifier.testTag("SectionHeader_DownloadedMaps")
                )
            }
            if (isExpanded) {
                items(downloadedMaps) { info ->
                    val downloadStatus = if (info.status is DownloadedMapStatus.UpdateAvailable) {
                        downloadingStatus[info.status.remoteMapFile.url]
                    } else null

                    DownloadedMapItem(
                        info = info,
                        downloadStatus = downloadStatus,
                        onDeleteClick = { onDeleteClick(info.mapFile) },
                        onUpdateClick = {
                            if (info.status is DownloadedMapStatus.UpdateAvailable) {
                                onDownloadClick(info.status.remoteMapFile)
                            }
                        },
                        onCancelUpdateClick = {
                            if (info.status is DownloadedMapStatus.UpdateAvailable) {
                                onCancelDownloadClick(info.status.remoteMapFile.url)
                            }
                        },
                        deleteLabel = stringResource(CoreR.string.action_delete)
                    )
                }
            }
        }

        // Remote folders hierarchy
        remoteFolders.subFolders.forEach {
            renderFolder(
                folder = it,
                level = 0,
                expandedSections = expandedSections,
                onToggleExpand = { key ->
                    expandedSections = if (key in expandedSections) expandedSections - key else expandedSections + key
                },
                downloadingStatus = downloadingStatus,
                onDownloadClick = onDownloadClick,
                onCancelDownloadClick = onCancelDownloadClick,
                rootTitle = rootTitle
            )
        }
    }
}

private fun LazyListScope.renderFolder(
    folder: RemoteMapFolder,
    level: Int,
    expandedSections: Set<String>,
    onToggleExpand: (String) -> Unit,
    downloadingStatus: Map<String, DownloadStatus>,
    onDownloadClick: (RemoteMapFile) -> Unit,
    onCancelDownloadClick: (String) -> Unit,
    rootTitle: String
) {
    val sectionKey = folder.path + folder.name
    val isExpanded = sectionKey in expandedSections
    val hasContent = folder.maps.isNotEmpty() || folder.subFolders.isNotEmpty()

    if (hasContent) {
        item {
            val title = folder.name.ifEmpty { rootTitle }
            SectionHeader(
                title = title,
                isExpanded = isExpanded,
                onToggleExpand = { onToggleExpand(sectionKey) },
                level = level,
                modifier = Modifier.testTag("SectionHeader_${folder.name.ifEmpty { "Root" }}")
            )
        }

        if (isExpanded) {
            items(folder.maps) { remoteMap ->
                Box(modifier = Modifier.padding(start = RoadbookNavigatorTheme.dimensions.paddingLarge.times(level))) {
                    RemoteMapItem(
                        remoteMap = remoteMap,
                        downloadStatus = downloadingStatus[remoteMap.url],
                        onDownloadClick = { onDownloadClick(remoteMap) },
                        onCancelClick = { onCancelDownloadClick(remoteMap.url) }
                    )
                }
            }

            folder.subFolders.forEach { subFolder ->
                renderFolder(
                    folder = subFolder,
                    level = level + 1,
                    expandedSections = expandedSections,
                    onToggleExpand = onToggleExpand,
                    downloadingStatus = downloadingStatus,
                    onDownloadClick = onDownloadClick,
                    onCancelDownloadClick = onCancelDownloadClick,
                    rootTitle = rootTitle
                )
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier,
    level: Int = 0
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggleExpand() }
            .padding(
                start = RoadbookNavigatorTheme.dimensions.paddingLarge.times(level + 1),
                end = RoadbookNavigatorTheme.dimensions.paddingLarge,
                top = RoadbookNavigatorTheme.dimensions.paddingLarge,
                bottom = RoadbookNavigatorTheme.dimensions.paddingLarge
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = if (isExpanded) {
                    stringResource(CoreR.string.action_collapse)
                } else {
                    stringResource(CoreR.string.action_expand)
                },
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.iconSize),
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = RoadbookNavigatorTheme.dimensions.paddingSmall))
    }
}

@Composable
fun DownloadedMapItem(
    info: DownloadedMapInfo,
    downloadStatus: DownloadStatus?,
    onDeleteClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onCancelUpdateClick: () -> Unit,
    deleteLabel: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Map, contentDescription = null, modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.iconSize))
        Spacer(modifier = Modifier.width(RoadbookNavigatorTheme.dimensions.paddingLarge))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = info.mapFile.name.removeSuffix(".map").replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = formatSize(info.mapFile.size),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            when (info.status) {
                is DownloadedMapStatus.Obsolete -> {
                    Text(
                        text = stringResource(R.string.map_management_status_obsolete),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is DownloadedMapStatus.UpdateAvailable -> {
                    Text(
                        text = stringResource(R.string.map_management_status_update_available),
                        style = MaterialTheme.typography.labelSmall,
                        //color = Color(0xFFF28E1C) // Warning color
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                DownloadedMapStatus.UpToDate -> {
                    // Nothing
                }
            }

            if (downloadStatus is DownloadStatus.Progress) {
                val progress by animateFloatAsState(targetValue = downloadStatus.progress, label = "DownloadProgress")
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
            } else if (downloadStatus is DownloadStatus.Error) {
                Text(
                    text = downloadStatus.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        if (downloadStatus is DownloadStatus.Progress) {
            IconButton(onClick = onCancelUpdateClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.iconSize),
                )
            }
        } else if (info.status is DownloadedMapStatus.UpdateAvailable) {
            IconButton(onClick = onUpdateClick) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.map_management_action_update),
                    //tint = Color(0xFFF28E1C),
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.iconSize),
                )
            }
        }

        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier
                .semantics { contentDescription = deleteLabel }
                .testTag("DeleteMapButton")
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.iconSize),
            )
        }
    }
}

@Composable
fun RemoteMapItem(
    remoteMap: RemoteMapFile,
    downloadStatus: DownloadStatus?,
    onDownloadClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Map,
            contentDescription = null,
            modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.iconSize),
        )
        Spacer(modifier = Modifier.width(RoadbookNavigatorTheme.dimensions.paddingLarge))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = remoteMap.name.removeSuffix(".map").replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = formatSize(remoteMap.size),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (downloadStatus is DownloadStatus.Progress) {
                val progress by animateFloatAsState(targetValue = downloadStatus.progress, label = "DownloadProgress")
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
            } else if (downloadStatus is DownloadStatus.Error) {
                Text(
                    text = downloadStatus.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        if (downloadStatus is DownloadStatus.Progress) {
            IconButton(onClick = onCancelClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.iconSize),
                )
            }
        } else {
            val downloadLabel = stringResource(R.string.map_management_action_download)
            IconButton(
                onClick = onDownloadClick,
                modifier = Modifier
                    .semantics { contentDescription = downloadLabel }
                    .testTag("DownloadMapButton")
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.iconSize),
                )
            }
        }
    }
}

private fun flattenFolders(folder: RemoteMapFolder): List<RemoteMapFolder> {
    val result = mutableListOf<RemoteMapFolder>()
    result.add(folder)
    folder.subFolders.forEach {
        result.addAll(flattenFolders(it))
    }
    return result
}

private fun formatSize(size: Long): String {
    val kb = size / 1024
    val mb = kb / 1024
    return if (mb > 0) "$mb MB" else "$kb KB"
}
