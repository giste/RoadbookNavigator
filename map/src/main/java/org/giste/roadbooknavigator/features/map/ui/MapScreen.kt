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

package org.giste.roadbooknavigator.features.map.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.oscim.android.MapView
import org.oscim.core.MapPosition
import org.oscim.theme.internal.VtmThemes
import org.oscim.tiling.source.mapfile.MapFileTileSource
import org.oscim.tiling.source.mapfile.MultiMapFileTileSource

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    val mapView = remember { MapView(context) }
    
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                val map = view.map()
                val pos = MapPosition()
                
                uiState.currentLocation?.let { location ->
                    pos.setPosition(location.latitude, location.longitude)
                }
                
                pos.zoomLevel = uiState.settings.initialZoom
                pos.tilt = uiState.settings.initialTilt

                map.mapPosition = pos
            }
        )
        
        // Attribution
        Text(
            text = "Map data (c) OpenStreetMap contributors | VTM",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f))
                .padding(RoadbookNavigatorTheme.dimensions.paddingSmall),
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.labelSmall,
        )
    }

    // Handle map loading when localMaps change
    LaunchedEffect(uiState.localMaps) {
        if (uiState.localMaps.isNotEmpty()) {
            val map = mapView.map()
            
            val multiTileSource = MultiMapFileTileSource()
            var hasFiles = false
            uiState.localMaps.forEach { mapFile ->
                val tileSource = MapFileTileSource()
                if (tileSource.setMapFile(mapFile.path)) {
                    multiTileSource.add(tileSource)
                    hasFiles = true
                }
            }
            
            if (hasFiles) {
                map.setBaseMap(multiTileSource)
                map.setTheme(VtmThemes.DEFAULT)
            }
        }
    }
}
