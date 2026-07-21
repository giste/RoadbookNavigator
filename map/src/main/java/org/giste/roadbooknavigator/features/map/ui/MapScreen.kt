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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.location.domain.UserLocation
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.R
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
    val uiState = viewModel.uiState.collectAsState().value
    val location = uiState.currentLocation

    if (uiState.localMaps.isEmpty()) {
        LocationView(
            location = location,
            modifier = modifier
        )
    } else {
        VtmMapView(
            mapSources = uiState.localMaps,
            location = location,
            zoom = uiState.settings.initialZoom,
            tilt = uiState.settings.initialTilt,
            modifier = modifier,
        )
    }
}

@Composable
private fun LocationView(
    location: UserLocation?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(RoadbookNavigatorTheme.dimensions.paddingMedium)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.map_load_instruction),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            HorizontalDivider()
            Text(
                text = stringResource(R.string.map_location_title),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                textDecoration = TextDecoration.Underline,
            )
            val unknown = stringResource(R.string.map_location_unknown)
            LocationDetail(
                text = stringResource(
                    R.string.map_location_latitude,
                    location?.latitude?.toString() ?: unknown
                )
            )
            LocationDetail(
                text = stringResource(
                    R.string.map_location_longitude,
                    location?.longitude?.toString() ?: unknown
                )
            )
            LocationDetail(
                text = stringResource(
                    R.string.map_location_accuracy,
                    location?.accuracy?.toString() ?: unknown
                )
            )
            LocationDetail(
                text = stringResource(
                    R.string.map_location_altitude,
                    location?.altitude?.toString() ?: unknown
                )
            )
            LocationDetail(
                text = stringResource(
                    R.string.map_location_vertical_accuracy,
                    location?.verticalAccuracy?.toString() ?: unknown
                )
            )
            LocationDetail(
                text = stringResource(
                    R.string.map_location_bearing,
                    location?.bearing?.toString() ?: unknown
                )
            )
            LocationDetail(
                text = stringResource(
                    R.string.map_location_speed,
                    location?.speed?.toString() ?: unknown
                )
            )
        }
    }
}

@Composable
private fun LocationDetail(text: String, modifier: Modifier = Modifier) {
    Text(text = text, style = MaterialTheme.typography.labelSmall, modifier = modifier)
}

@Composable
private fun VtmMapView(
    mapSources: List<MapFile>,
    location: UserLocation?,
    zoom: Int,
    tilt: Float,
    modifier: Modifier = Modifier,

) {
    val mapView = rememberMapViewWithLifecycle()

    Box (modifier = modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                with(mapView.map()) {
                    // Tile source from maps
                    val tileSource = MultiMapFileTileSource()
                    mapSources.forEach {
                        val map = MapFileTileSource()
                        map.setMapFile(it.path)
                        tileSource.add(map)
                    }

                    // Vector layer
                    val tileLayer: VectorTileLayer = setBaseMap(tileSource)

                    // Building layer
                    layers().add(BuildingLayer(this, tileLayer))

                    // Label layer
                    layers().add(LabelLayer(this, tileLayer))

                    // Scale bar
                    val mapScaleBar: MapScaleBar = DefaultMapScaleBar(this)
                    val mapScaleBarLayer = MapScaleBarLayer(this, mapScaleBar)
                    mapScaleBarLayer.renderer.setPosition(GLViewport.Position.TOP_RIGHT)
                    mapScaleBarLayer.renderer.setOffset(5 * CanvasAdapter.getScale(), 0f)
                    layers().add(mapScaleBarLayer)

                    // Render theme
                    setTheme(VtmThemes.DEFAULT)

                    // Set center at the bottom
                    viewport().mapViewCenterY = 0.6f

                    // Initial position, scale and tilt
                    val initialPosition = mapPosition
                    initialPosition
                        .setPosition(40.60092, -3.70806)
                        .setScale((1 shl zoom).toDouble())
                        .setTilt(tilt)
                    mapPosition = initialPosition
                }

                mapView
            },
            modifier = Modifier.fillMaxSize(),
            update = { view ->
                with(view.map()) {
                    location?.let { location ->
                        val newPosition = mapPosition
                        newPosition.setPosition(location.latitude, location.longitude)
                        location.bearing.let { newPosition.setBearing(360f - location.bearing) }
                        this.animator().animateTo(newPosition)
                    }
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
}

@Composable
private fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val observer = remember { VtmMapViewLifecycleObserver(mapView) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(Unit) {
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    return mapView
}

private class VtmMapViewLifecycleObserver(private val mapView: MapView) : DefaultLifecycleObserver {
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        mapView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        mapView.onPause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        mapView.onDestroy()
    }
}