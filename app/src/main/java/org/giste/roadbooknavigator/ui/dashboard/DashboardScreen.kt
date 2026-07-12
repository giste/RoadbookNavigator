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

package org.giste.roadbooknavigator.ui.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.giste.roadbooknavigator.R
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.map.ui.MapScreen
import org.giste.roadbooknavigator.features.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.odometer.ui.PartialDistance
import org.giste.roadbooknavigator.features.odometer.ui.SetPartialDialog
import org.giste.roadbooknavigator.features.odometer.ui.TotalDistance
import org.giste.roadbooknavigator.features.roadbook.domain.model.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.model.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.model.Waypoint
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookSection
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookUiState
import java.io.InputStream

@Composable
fun DashboardScreen(
    windowSizeClass: WindowSizeClass,
    onSettingsClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    DashboardContent(
        windowSizeClass = windowSizeClass,
        onSettingsClick = onSettingsClick,
        viewModel = viewModel,
        mapContent = { modifier -> MapScreen(modifier = modifier) }
    )
}

@Composable
fun DashboardContent(
    windowSizeClass: WindowSizeClass,
    onSettingsClick: () -> Unit,
    viewModel: DashboardViewModel,
    mapContent: @Composable (Modifier) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    // Use a stable key to ensure the scroll state is preserved across re-compositions (like theme changes).
    // The key only changes when a DIFFERENT route is loaded.
    val roadbookState = uiState.roadbook
    val routeKey = remember((roadbookState as? RoadbookUiState.Success)?.route) {
        (roadbookState as? RoadbookUiState.Success)?.let {
            "route_${it.route.name}_${it.route.waypoints.size}"
        }
    }

    val listState = rememberSaveable(routeKey, saver = LazyListState.Saver) {
        if (roadbookState is RoadbookUiState.Success) {
            LazyListState(
                firstVisibleItemIndex = uiState.initialScrollPosition.index,
                firstVisibleItemScrollOffset = uiState.initialScrollPosition.offset
            )
        } else {
            LazyListState()
        }
    }

    // Keep track of the waypoint we are currently aiming for to handle rapid key presses
    var targetWaypointIndex by remember(routeKey) {
        mutableIntStateOf(uiState.initialScrollPosition.index)
    }

    // Synchronize targetWaypointIndex with the list state when manual scrolling finishes
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            targetWaypointIndex = listState.firstVisibleItemIndex
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("MainScreen")
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    when (event.key) {
                        Key.MediaNext, Key.DirectionUp -> {
                            val waypointsCount = (uiState.roadbook as? RoadbookUiState.Success)
                                ?.route?.waypoints?.size ?: 0
                            if (waypointsCount > 0) {
                                coroutineScope.launch {
                                    targetWaypointIndex = (targetWaypointIndex + 1)
                                        .coerceAtMost(waypointsCount - 1)
                                    listState.animateScrollToItem(targetWaypointIndex)
                                }
                            }
                            true
                        }

                        Key.MediaPrevious, Key.DirectionDown -> {
                            coroutineScope.launch {
                                targetWaypointIndex =
                                    if (listState.firstVisibleItemScrollOffset > 0) {
                                        // If partially scrolled, first snap to the top of current waypoint
                                        listState.firstVisibleItemIndex
                                    } else {
                                        // Otherwise move to the previous one
                                        (targetWaypointIndex - 1).coerceAtLeast(0)
                                    }
                                listState.animateScrollToItem(targetWaypointIndex)
                            }
                            true
                        }

                        Key.VolumeUp, Key.DirectionRight -> {
                            viewModel.incrementPartialDistance()
                            true
                        }

                        Key.VolumeDown, Key.DirectionLeft -> {
                            viewModel.decrementPartialDistance()
                            true
                        }

                        Key.MediaPlayPause, Key.MediaPlay, Key.MediaPause, Key.F6 -> {
                            viewModel.resetPartialDistance()
                            true
                        }

                        else -> false
                    }
                } else {
                    false
                }
            }
    ) {
        MainContent(
            windowSizeClass = windowSizeClass,
            uiState = uiState,
            listState = listState,
            onSetPartialClick = { viewModel.setPartialDistance(it) },
            onLongClickPartial = { viewModel.showSetPartialDialog() },
            onSettingsClick = onSettingsClick,
            onWaypointVisible = { index, offset -> viewModel.onWaypointVisible(index, offset) },
            onFileSelected = { viewModel.importRoute(it) },
            mapContent = mapContent
        )

        if (uiState.showSetPartialDialog) {
            SetPartialDialog(
                windowSizeClass = windowSizeClass,
                onDismiss = {
                    viewModel.hideSetPartialDialog()
                },
                onConfirm = {
                    viewModel.setPartialDistance(it)
                    viewModel.hideSetPartialDialog()
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun MainContent(
    windowSizeClass: WindowSizeClass,
    uiState: DashboardUiState,
    listState: LazyListState,
    onSetPartialClick: (Double) -> Unit,
    onLongClickPartial: () -> Unit,
    onSettingsClick: () -> Unit,
    onWaypointVisible: (Int, Int) -> Unit,
    onFileSelected: (InputStream) -> Unit,
    mapContent: @Composable (Modifier) -> Unit,
) {
    val configuration = LocalConfiguration.current
    val locale =
        if (configuration.locales.size() > 0) configuration.locales[0] else java.util.Locale.getDefault()

    val totalDistanceStr = try {
        String.format(locale, "%.1f", uiState.odometer.total / 1000.0)
    } catch (e: Exception) {
        "0.0"
    }
    val partialDistanceStr = try {
        String.format(locale, "%.2f", uiState.odometer.partial / 1000.0)
    } catch (e: Exception) {
        "0.00"
    }

    val widthSizeClass = windowSizeClass.widthSizeClass
    val heightSizeClass = windowSizeClass.heightSizeClass

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (!uiState.isFullScreen) Modifier.systemBarsPadding()
                else Modifier
            ),
        color = MaterialTheme.colorScheme.background
    ) {
        val roadbookState = uiState.roadbook

        when {
            // 1. "Landscape-like" on small/short screens (Phone Landscape)
            heightSizeClass == WindowHeightSizeClass.Compact -> {
                CompactLandscapeLayout(
                    roadbookState = roadbookState,
                    listState = listState,
                    totalDistance = totalDistanceStr,
                    partialDistance = partialDistanceStr,
                    onSetPartialClick = onSetPartialClick,
                    onLongClickPartial = onLongClickPartial,
                    onSettingsClick = onSettingsClick,
                    onWaypointVisible = onWaypointVisible,
                    onFileSelected = onFileSelected,
                    mapContent = mapContent,
                    modifier = Modifier.testTag("CompactLandscapeLayout")
                )
            }

            // 2. Large Tablet Landscape (Wide and not short)
            widthSizeClass == WindowWidthSizeClass.Expanded && heightSizeClass != WindowHeightSizeClass.Compact -> {
                ExpandedLandscapeLayout(
                    roadbookState = roadbookState,
                    listState = listState,
                    totalDistance = totalDistanceStr,
                    partialDistance = partialDistanceStr,
                    onSetPartialClick = onSetPartialClick,
                    onLongClickPartial = onLongClickPartial,
                    onSettingsClick = onSettingsClick,
                    onWaypointVisible = onWaypointVisible,
                    onFileSelected = onFileSelected,
                    mapContent = mapContent,
                    modifier = Modifier.testTag("ExpandedLandscapeLayout")
                )
            }

            // 3. Tablet Portrait / Medium-width windows (Medium width + Tall)
            widthSizeClass == WindowWidthSizeClass.Medium -> {
                PortraitLayout(
                    roadbookState = roadbookState,
                    listState = listState,
                    totalDistance = totalDistanceStr,
                    partialDistance = partialDistanceStr,
                    onSetPartialClick = onSetPartialClick,
                    onLongClickPartial = onLongClickPartial,
                    onSettingsClick = onSettingsClick,
                    onWaypointVisible = onWaypointVisible,
                    onFileSelected = onFileSelected,
                    mapContent = mapContent,
                    modifier = Modifier.testTag("PortraitLayout")
                )
            }

            // 4. Phone Portrait / Narrow windows (Compact width)
            else -> {
                CompactPortraitLayout(
                    roadbookState = roadbookState,
                    listState = listState,
                    totalDistance = totalDistanceStr,
                    partialDistance = partialDistanceStr,
                    onSetPartialClick = onSetPartialClick,
                    onLongClickPartial = onLongClickPartial,
                    onSettingsClick = onSettingsClick,
                    onWaypointVisible = onWaypointVisible,
                    onFileSelected = onFileSelected,
                    mapContent = mapContent,
                    modifier = Modifier.testTag("CompactPortraitLayout")
                )
            }
        }
    }
}

// --- LANDSCAPE LAYOUTS ---

@Composable
fun ExpandedLandscapeLayout(
    roadbookState: RoadbookUiState,
    listState: LazyListState,
    totalDistance: String,
    partialDistance: String,
    onSetPartialClick: (Double) -> Unit,
    onLongClickPartial: () -> Unit,
    onSettingsClick: () -> Unit,
    onWaypointVisible: (Int, Int) -> Unit,
    onFileSelected: (InputStream) -> Unit,
    mapContent: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        LandscapeDistanceSection(
            totalDistance = totalDistance,
            partialDistance = partialDistance,
            onLongClickPartial = onLongClickPartial,
            onSettingsClick = onSettingsClick,
            mapContent = mapContent,
            modifier = Modifier.weight(2f)
        )
        RoadbookSection(
            state = roadbookState,
            listState = listState,
            modifier = Modifier.weight(5f),
            onSetPartialClick = onSetPartialClick,
            onWaypointVisible = onWaypointVisible,
            onFileSelected = onFileSelected,
        )
    }
}

@Composable
fun CompactLandscapeLayout(
    roadbookState: RoadbookUiState,
    listState: LazyListState,
    totalDistance: String,
    partialDistance: String,
    onSetPartialClick: (Double) -> Unit,
    onLongClickPartial: () -> Unit,
    onSettingsClick: () -> Unit,
    onWaypointVisible: (Int, Int) -> Unit,
    onFileSelected: (InputStream) -> Unit,
    mapContent: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        LandscapeDistanceSection(
            totalDistance = totalDistance,
            partialDistance = partialDistance,
            onLongClickPartial = onLongClickPartial,
            onSettingsClick = onSettingsClick,
            mapContent = mapContent,
            modifier = Modifier.weight(2f)
        )
        RoadbookSection(
            state = roadbookState,
            listState = listState,
            modifier = Modifier.weight(5f),
            onSetPartialClick = onSetPartialClick,
            onWaypointVisible = onWaypointVisible,
            onFileSelected = onFileSelected,
        )
    }
}

// --- PORTRAIT LAYOUT ---

@Composable
fun PortraitLayout(
    roadbookState: RoadbookUiState,
    listState: LazyListState,
    totalDistance: String,
    partialDistance: String,
    onSetPartialClick: (Double) -> Unit,
    onLongClickPartial: () -> Unit,
    onSettingsClick: () -> Unit,
    onWaypointVisible: (Int, Int) -> Unit,
    onFileSelected: (InputStream) -> Unit,
    mapContent: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        PortraitDistanceSection(
            totalDistance = totalDistance,
            partialDistance = partialDistance,
            onLongClickPartial = onLongClickPartial,
            onSettingsClick = onSettingsClick,
            modifier = Modifier.fillMaxWidth()
        )
        RoadbookSection(
            state = roadbookState,
            listState = listState,
            modifier = Modifier.weight(3f),
            onSetPartialClick = onSetPartialClick,
            onWaypointVisible = onWaypointVisible,
            onFileSelected = onFileSelected,
        )
        mapContent(Modifier.weight(2f))
    }
}

@Composable
fun CompactPortraitLayout(
    roadbookState: RoadbookUiState,
    listState: LazyListState,
    totalDistance: String,
    partialDistance: String,
    onSetPartialClick: (Double) -> Unit,
    onLongClickPartial: () -> Unit,
    onSettingsClick: () -> Unit,
    onWaypointVisible: (Int, Int) -> Unit,
    onFileSelected: (InputStream) -> Unit,
    mapContent: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        PortraitDistanceSection(
            totalDistance = totalDistance,
            partialDistance = partialDistance,
            onLongClickPartial = onLongClickPartial,
            onSettingsClick = onSettingsClick,
            modifier = Modifier.fillMaxWidth()
        )
        RoadbookSection(
            state = roadbookState,
            listState = listState,
            modifier = Modifier.weight(1f),
            onSetPartialClick = onSetPartialClick,
            onWaypointVisible = onWaypointVisible,
            onFileSelected = onFileSelected,
        )
    }
}

// --- SHARED COMPONENTS ---

@Composable
fun LandscapeDistanceSection(
    totalDistance: String,
    partialDistance: String,
    onLongClickPartial: () -> Unit,
    onSettingsClick: () -> Unit,
    mapContent: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .border(
                RoadbookNavigatorTheme.dimensions.sectionBorder,
                MaterialTheme.colorScheme.outline
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .border(
                    RoadbookNavigatorTheme.dimensions.sectionBorder,
                    MaterialTheme.colorScheme.outline
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.actionIconSize)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.action_settings),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            TotalDistance(
                distance = totalDistance,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        PartialDistance(
            distance = partialDistance,
            onLongClick = onLongClickPartial
        )

        // Map Area (Bottom) - Fills ALL remaining space
        mapContent(Modifier.weight(1f))
    }
}

@Composable
fun PortraitDistanceSection(
    totalDistance: String,
    partialDistance: String,
    onLongClickPartial: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .border(
                RoadbookNavigatorTheme.dimensions.sectionBorder,
                MaterialTheme.colorScheme.outline
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.actionIconSize)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.action_settings),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        // Total | Partial side-by-side
        TotalDistance(
            distance = totalDistance,
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight()
        )
        PartialDistance(
            distance = partialDistance,
            onLongClick = onLongClickPartial,
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
        )
    }
}

@Composable
private fun MapSection(
    modifier: Modifier = Modifier,
) {
    MapScreen(modifier = modifier)
}

// --- PREVIEWS ---

private val sampleWaypoints = listOf(
    Waypoint(
        number = 1,
        coordinates = Coordinates(40.0, -3.0),
        distance = Distance(0),
        distanceFromPrevious = Distance(0),
    ),
    Waypoint(
        number = 2,
        coordinates = Coordinates(40.01, -3.01),
        distance = Distance(1250),
        distanceFromPrevious = Distance(1250),
    ),
    Waypoint(
        number = 999,
        coordinates = Coordinates(40.02, -3.02),
        distance = Distance(240000),
        distanceFromPrevious = Distance(11500),
        reset = true,
    ),
    Waypoint(
        number = 4,
        coordinates = Coordinates(40.03, -3.03),
        distance = Distance(3800),
        distanceFromPrevious = Distance(1400),
    ),
    Waypoint(
        number = 5,
        coordinates = Coordinates(40.04, -3.04),
        distance = Distance(5100),
        distanceFromPrevious = Distance(1300),
    ),
)

private val sampleUiState = DashboardUiState(
    roadbook = RoadbookUiState.Success(
        route = Route(
            name = "Test Route",
            waypoints = sampleWaypoints
        )
    ),
    odometer = Odometer(
        total = 2400.0,
        partial = 1150.0
    )
)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    name = "Tab Active 3 - Landscape - Light",
    device = "spec:width=1920px,height=1200px,dpi=280,orientation=landscape",
    showBackground = true,
    locale = "es",
)
@Preview(
    name = "Tab Active 3 - Landscape - Dark",
    device = "spec:width=1920px,height=1200px,dpi=280,orientation=landscape",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TabletLandPreview() {
    val listState = rememberLazyListState()
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(
            DpSize(
                1097.dp,
                686.dp
            )
        )
    ) {
        MainContent(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1097.dp, 686.dp)),
            uiState = sampleUiState,
            listState = listState,
            onSetPartialClick = {},
            onLongClickPartial = {},
            onSettingsClick = {},
            onWaypointVisible = { _, _ -> },
            onFileSelected = {},
            mapContent = { modifier -> Box(modifier.background(MaterialTheme.colorScheme.tertiary)) }
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    name = "Tab Active 3 - Portrait - Light",
    device = "spec:width=1200px,height=1920px,dpi=280,orientation=portrait",
    showBackground = true
)
@Preview(
    name = "Tab Active 3 - Portrait - Dark",
    device = "spec:width=1200px,height=1920px,dpi=280,orientation=portrait",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TabletPortPreview() {
    val listState = rememberLazyListState()
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(
            DpSize(
                686.dp,
                1097.dp
            )
        )
    ) {
        MainContent(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(686.dp, 1097.dp)),
            uiState = sampleUiState,
            listState = listState,
            onSetPartialClick = {},
            onLongClickPartial = {},
            onSettingsClick = {},
            onWaypointVisible = { _, _ -> },
            onFileSelected = {},
            mapContent = { modifier -> Box(modifier.background(MaterialTheme.colorScheme.tertiary)) }
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    name = "Phone - Portrait - Light",
    device = "spec:width=411dp,height=891dp,orientation=portrait",
    showBackground = true
)
@Preview(
    name = "Phone - Portrait - Dark",
    device = "spec:width=411dp,height=891dp,orientation=portrait",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PhonePortPreview() {
    val listState = rememberLazyListState()
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(
            DpSize(
                411.dp,
                891.dp
            )
        )
    ) {
        MainContent(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
            uiState = sampleUiState,
            listState = listState,
            onSetPartialClick = {},
            onLongClickPartial = {},
            onSettingsClick = {},
            onWaypointVisible = { _, _ -> },
            onFileSelected = {},
            mapContent = { modifier -> Box(modifier.background(MaterialTheme.colorScheme.tertiary)) }
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    name = "Phone - Landscape - Light",
    device = "spec:width=891dp,height=411dp,orientation=landscape",
    showBackground = true
)
@Preview(
    name = "Phone - Landscape - Dark",
    device = "spec:width=891dp,height=411dp,orientation=landscape",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PhoneLandPreview() {
    val listState = rememberLazyListState()
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(
            DpSize(
                891.dp,
                411.dp
            )
        )
    ) {
        MainContent(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(891.dp, 411.dp)),
            uiState = sampleUiState,
            listState = listState,
            onSetPartialClick = {},
            onLongClickPartial = {},
            onSettingsClick = {},
            onWaypointVisible = { _, _ -> },
            onFileSelected = {},
            mapContent = { modifier -> Box(modifier.background(MaterialTheme.colorScheme.tertiary)) }
        )
    }
}
