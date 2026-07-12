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

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.oscim.android.MapView
import org.oscim.backend.CanvasAdapter
import org.oscim.layers.tile.buildings.BuildingLayer
import org.oscim.layers.tile.vector.VectorTileLayer
import org.oscim.layers.tile.vector.labeling.LabelLayer
import org.oscim.renderer.GLViewport
import org.oscim.scalebar.DefaultMapScaleBar
import org.oscim.scalebar.MapScaleBar
import org.oscim.scalebar.MapScaleBarLayer
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

    // Lifecycle management
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                with(mapView.map()) {
                    // Scale bar
                    val mapScaleBar: MapScaleBar = DefaultMapScaleBar(this)
                    val mapScaleBarLayer = MapScaleBarLayer(this, mapScaleBar)
                    mapScaleBarLayer.renderer.setPosition(GLViewport.Position.BOTTOM_LEFT)
                    mapScaleBarLayer.renderer.setOffset(5 * CanvasAdapter.getScale(), 0f)
                    layers().add(mapScaleBarLayer)

                    // Initial layers setup (empty multi-source initially)
                    val multiTileSource = MultiMapFileTileSource()
                    val baseLayer = setBaseMap(multiTileSource)
                    layers().add(BuildingLayer(this, baseLayer))
                    layers().add(LabelLayer(this, baseLayer))

                    setTheme(VtmThemes.DEFAULT)
                    viewport().mapViewCenterY = 0.6f

                    // Initial position, scale and tilt
                    val initialPosition = mapPosition
                    initialPosition
                        .setPosition(40.60092, -3.70806)
                        .setScale((1 shl uiState.settings.initialZoom).toDouble())
                        .setTilt(uiState.settings.initialTilt)
                    mapPosition = initialPosition
                }
                mapView
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                val map = view.map()
                uiState.currentLocation?.let { location ->
                    val pos = map.mapPosition
                    pos.setPosition(location.latitude, location.longitude)
                    pos.bearing = 360f - location.bearing
                    map.animator().animateTo(pos)
                }
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
        Log.d("MapScreen", "Local maps changed: ${uiState.localMaps}")
        val map = mapView.map()

        val multiTileSource = MultiMapFileTileSource()
        var hasFiles = false
        uiState.localMaps.forEach { mapFile ->
            val tileSource = MapFileTileSource()
            if (tileSource.setMapFile(mapFile.path)) {
                multiTileSource.add(tileSource)
                Log.d("MapScreen", "Added tile source ${mapFile.name}")
                hasFiles = true
            }
        }

        if (hasFiles) {
            // Remove previous layers we added to avoid duplication
            val layers = map.layers()
            val toRemove = layers.filter {
                it is VectorTileLayer || it is BuildingLayer || it is LabelLayer
            }
            layers.removeAll(toRemove)

            val baseLayer = map.setBaseMap(multiTileSource)
            map.layers().add(BuildingLayer(map, baseLayer))
            map.layers().add(LabelLayer(map, baseLayer))

            map.setTheme(VtmThemes.DEFAULT)
            map.render()
        }
    }
}
