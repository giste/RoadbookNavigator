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

package org.giste.roadbooknavigator.features.roadbook.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.roadbook.R
import org.giste.roadbooknavigator.features.roadbook.domain.model.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.model.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.model.Point
import org.giste.roadbooknavigator.features.roadbook.domain.model.Road
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.model.Track
import org.giste.roadbooknavigator.features.roadbook.domain.model.Waypoint
import java.io.InputStream

@Composable
fun RoadbookSection(
    modifier: Modifier = Modifier,
    viewModel: RoadbookViewModel = hiltViewModel(),
    onSetPartialClick: (Double) -> Unit,
) {
    val state by viewModel.roadbookState.collectAsStateWithLifecycle()
    val initialPosition by viewModel.initialScrollPosition.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    val routeKey = remember((state as? RoadbookUiState.Success)?.route) {
        (state as? RoadbookUiState.Success)?.let {
            "route_${it.route.name}_${it.route.waypoints.size}"
        }
    }

    val listState = rememberSaveable(routeKey, saver = LazyListState.Saver) {
        if (state is RoadbookUiState.Success) {
            LazyListState(
                firstVisibleItemIndex = initialPosition.index,
                firstVisibleItemScrollOffset = initialPosition.offset
            )
        } else {
            LazyListState()
        }
    }

    var targetWaypointIndex by remember(routeKey) {
        mutableIntStateOf(initialPosition.index)
    }

    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            targetWaypointIndex = listState.firstVisibleItemIndex
        }
    }

    RoadbookContent(
        state = state,
        listState = listState,
        modifier = modifier
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                val currentState = state
                if (event.type == KeyEventType.KeyDown && currentState is RoadbookUiState.Success) {
                    val keyCode = event.key.nativeKeyCode
                    when {
                        currentState.roadbookUp.contains(keyCode) -> {
                            val waypointsCount = currentState.route.waypoints.size
                            if (waypointsCount > 0) {
                                coroutineScope.launch {
                                    targetWaypointIndex = (targetWaypointIndex + 1)
                                        .coerceAtMost(waypointsCount - 1)
                                    listState.animateScrollToItem(targetWaypointIndex)
                                }
                            }
                            true
                        }

                        currentState.roadbookDown.contains(keyCode) -> {
                            coroutineScope.launch {
                                targetWaypointIndex =
                                    if (listState.firstVisibleItemScrollOffset > 0) {
                                        listState.firstVisibleItemIndex
                                    } else {
                                        (targetWaypointIndex - 1).coerceAtLeast(0)
                                    }
                                listState.animateScrollToItem(targetWaypointIndex)
                            }
                            true
                        }

                        else -> false
                    }
                } else {
                    false
                }
            },
        onFileSelected = viewModel::importRoute,
        onSetPartialClick = onSetPartialClick,
        onWaypointVisible = viewModel::onWaypointVisible
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun RoadbookContent(
    state: RoadbookUiState,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onFileSelected: (InputStream) -> Unit,
    onSetPartialClick: (Double) -> Unit,
    onWaypointVisible: (Int, Int) -> Unit
) {
    val context = LocalContext.current
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.let(onFileSelected)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .border(
                RoadbookNavigatorTheme.dimensions.sectionBorder,
                MaterialTheme.colorScheme.outline
            )
    ) {
        when (state) {
            is RoadbookUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag("LoadingIndicator")
                )
            }

            is RoadbookUiState.Empty -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.main_no_route),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Button(
                        onClick = {
                            filePickerLauncher.launch("*/*")
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.action_import)
                        )
                    }
                }
            }

            is RoadbookUiState.Error -> {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.main_error_prefix, state.message),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Button(
                        onClick = {
                            filePickerLauncher.launch("*/*")
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.action_import)
                        )
                    }
                }
            }

            is RoadbookUiState.Success -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    RoadbookList(
                        waypoints = state.route.waypoints,
                        shortDistanceThreshold = state.shortDistanceThreshold.meters,
                        listState = listState,
                        onSetPartialClick = onSetPartialClick,
                        onWaypointVisible = onWaypointVisible
                    )
                    FloatingActionButton(
                        onClick = {
                            filePickerLauncher.launch("*/*")
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.action_import)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoadbookList(
    waypoints: List<Waypoint>,
    shortDistanceThreshold: Long,
    listState: LazyListState,
    onSetPartialClick: (Double) -> Unit,
    onWaypointVisible: (Int, Int) -> Unit
) {
    // Monitor visible items to save current position ONLY when scrolling stops
    // We use a flag to skip the initial 'false' emission which could overwrite saved data with 0/0
    var hasStartedScrolling by remember(listState) { mutableStateOf(false) }

    LaunchedEffect(listState.isScrollInProgress) {
        if (listState.isScrollInProgress) {
            hasStartedScrolling = true
        } else if (hasStartedScrolling) {
            onWaypointVisible(
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            )
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .testTag("RoadbookList"),
    ) {
        itemsIndexed(
            items = waypoints,
            key = { _, waypoint -> waypoint.number }
        ) { _, waypoint ->
            WaypointItem(
                waypoint = waypoint,
                shortDistanceThreshold = shortDistanceThreshold,
                onSetPartialClick = onSetPartialClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    name = "Empty State",
    device = "spec:width=1920px,height=1200px,dpi=280,orientation=portrait",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Composable
fun RoadbookSectionEmptyPreview() {
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1200.dp, 1920.dp)),
    ) {
        Surface {
            RoadbookContent(
                state = RoadbookUiState.Empty,
                listState = rememberLazyListState(),
                onFileSelected = {},
                onSetPartialClick = {},
                onWaypointVisible = { _, _ -> }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    name = "Success State",
    device = "spec:width=1920px,height=1200px,dpi=280,orientation=portrait",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Composable
fun RoadbookSectionSuccessPreview() {
    val sampleRoute = Route(
        name = "Sample Route",
        waypoints = listOf(
            Waypoint(
                number = 1,
                coordinates = Coordinates(40.0, -3.0),
                distance = Distance(1200),
                distanceFromPrevious = Distance(1200),
                tulipElements = listOf(
                    Track(
                        roadIn = Road(null, Point(0.0, 35.0)),
                        roadOut = Road(null, Point(0.0, -55.0))
                    )
                )
            ),
            Waypoint(
                number = 2,
                coordinates = Coordinates(40.1, -3.1),
                distance = Distance(2500),
                distanceFromPrevious = Distance(1300),
                dangerLevel = Waypoint.DangerLevel.MEDIUM,
                tulipElements = listOf(
                    Track(
                        roadIn = Road(null, Point(0.0, 35.0)),
                        roadOut = Road(null, Point(50.0, 0.0))
                    )
                )
            )
        )
    )

    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1200.dp, 1920.dp)),
    ) {
        Surface {
            RoadbookContent(
                state = RoadbookUiState.Success(sampleRoute),
                listState = rememberLazyListState(),
                onFileSelected = {},
                onSetPartialClick = {},
                onWaypointVisible = { _, _ -> }
            )
        }
    }
}
