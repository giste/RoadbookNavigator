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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookContent
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookSection
import org.giste.roadbooknavigator.features.roadbook.ui.RoadbookUiState

@Composable
fun DashboardScreen(
    windowSizeClass: WindowSizeClass,
    onSettingsClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel(),
    primaryOdometerSlot: @Composable (DashboardUiState, Modifier) -> Unit = { uiState, modifier ->
        val configuration = LocalConfiguration.current
        val locale = if (configuration.locales.size() > 0) configuration.locales[0] else LocalLocale.current.platformLocale
        val partialDistanceStr = try {
            String.format(locale, "%.2f", uiState.odometer.partial / 1000.0)
        } catch (_: Exception) {
            "0.00"
        }
        PartialDistance(
            distance = partialDistanceStr,
            onLongClick = { viewModel.showSetPartialDialog() },
            modifier = modifier
        )
    },
    secondaryOdometerSlot: @Composable (DashboardUiState, Modifier) -> Unit = { uiState, modifier ->
        val configuration = LocalConfiguration.current
        val locale = if (configuration.locales.size() > 0) configuration.locales[0] else LocalLocale.current.platformLocale
        val totalDistanceStr = try {
            String.format(locale, "%.1f", uiState.odometer.total / 1000.0)
        } catch (_: Exception) {
            "0.0"
        }
        TotalDistance(
            distance = totalDistanceStr,
            modifier = modifier
        )
    },
    roadbookSlot: @Composable (Modifier) -> Unit = { modifier ->
        RoadbookSection(
            modifier = modifier,
            onSetPartialClick = { viewModel.setPartialDistance(it) },
        )
    },
    mapSlot: @Composable (Modifier) -> Unit = { modifier -> MapScreen(modifier = modifier) }
) {
    val uiState by viewModel.uiState.collectAsState()

    DashboardContent(
        windowSizeClass = windowSizeClass,
        onSettingsClick = onSettingsClick,
        uiState = uiState,
        primaryOdometerSlot = { modifier -> primaryOdometerSlot(uiState, modifier) },
        secondaryOdometerSlot = { modifier -> secondaryOdometerSlot(uiState, modifier) },
        roadbookSlot = roadbookSlot,
        mapSlot = mapSlot,
        onIncrementPartial = { viewModel.incrementPartialDistance() },
        onDecrementPartial = { viewModel.decrementPartialDistance() },
        onResetPartial = { viewModel.resetPartialDistance() },
        onSetPartialDistance = { viewModel.setPartialDistance(it) },
        onHideSetPartialDialog = { viewModel.hideSetPartialDialog() },
    )
}

@Composable
fun DashboardContent(
    windowSizeClass: WindowSizeClass,
    onSettingsClick: () -> Unit,
    uiState: DashboardUiState,
    primaryOdometerSlot: @Composable (Modifier) -> Unit,
    secondaryOdometerSlot: @Composable (Modifier) -> Unit,
    roadbookSlot: @Composable (Modifier) -> Unit,
    mapSlot: @Composable (Modifier) -> Unit,
    onIncrementPartial: () -> Unit,
    onDecrementPartial: () -> Unit,
    onResetPartial: () -> Unit,
    onSetPartialDistance: (Double) -> Unit,
    onHideSetPartialDialog: () -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("MainScreen")
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown) {
                    val keyCode = event.key.nativeKeyCode
                    when {
                        uiState.increasePartialKeys.contains(keyCode) -> {
                            onIncrementPartial()
                            true
                        }

                        uiState.decreasePartialKeys.contains(keyCode) -> {
                            onDecrementPartial()
                            true
                        }

                        uiState.resetPartialKeys.contains(keyCode) -> {
                            onResetPartial()
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
            onSettingsClick = onSettingsClick,
            primaryOdometerSlot = primaryOdometerSlot,
            secondaryOdometerSlot = secondaryOdometerSlot,
            roadbookSlot = roadbookSlot,
            mapSlot = mapSlot
        )

        if (uiState.showSetPartialDialog) {
            SetPartialDialog(
                windowSizeClass = windowSizeClass,
                onDismiss = onHideSetPartialDialog,
                onConfirm = {
                    onSetPartialDistance(it)
                    onHideSetPartialDialog()
                }
            )
        }
    }
}

@Composable
fun MainContent(
    windowSizeClass: WindowSizeClass,
    uiState: DashboardUiState,
    onSettingsClick: () -> Unit,
    primaryOdometerSlot: @Composable (Modifier) -> Unit,
    secondaryOdometerSlot: @Composable (Modifier) -> Unit,
    roadbookSlot: @Composable (Modifier) -> Unit,
    mapSlot: @Composable (Modifier) -> Unit,
) {
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
        when {
            heightSizeClass == WindowHeightSizeClass.Compact -> {
                LandscapeLayout(
                    onSettingsClick = onSettingsClick,
                    primaryOdometerSlot = primaryOdometerSlot,
                    secondaryOdometerSlot = secondaryOdometerSlot,
                    roadbookSlot = roadbookSlot,
                    mapSlot = mapSlot,
                    modifier = Modifier.testTag("CompactLandscapeLayout")
                )
            }
            widthSizeClass == WindowWidthSizeClass.Expanded -> {
                LandscapeLayout(
                    onSettingsClick = onSettingsClick,
                    primaryOdometerSlot = primaryOdometerSlot,
                    secondaryOdometerSlot = secondaryOdometerSlot,
                    roadbookSlot = roadbookSlot,
                    mapSlot = mapSlot,
                    modifier = Modifier.testTag("ExpandedLandscapeLayout")
                )
            }
            else -> {
                PortraitLayout(
                    onSettingsClick = onSettingsClick,
                    primaryOdometerSlot = primaryOdometerSlot,
                    secondaryOdometerSlot = secondaryOdometerSlot,
                    roadbookSlot = roadbookSlot,
                    mapSlot = mapSlot,
                    modifier = Modifier.testTag(
                        if (widthSizeClass == WindowWidthSizeClass.Medium) "PortraitLayout" 
                        else "CompactPortraitLayout"
                    )
                )
            }
        }
    }
}

// --- LANDSCAPE LAYOUT ---

@Composable
fun LandscapeLayout(
    onSettingsClick: () -> Unit,
    primaryOdometerSlot: @Composable (Modifier) -> Unit,
    secondaryOdometerSlot: @Composable (Modifier) -> Unit,
    roadbookSlot: @Composable (Modifier) -> Unit,
    mapSlot: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxSize()) {
        LandscapeDistanceSection(
            primaryOdometerSlot = primaryOdometerSlot,
            secondaryOdometerSlot = secondaryOdometerSlot,
            onSettingsClick = onSettingsClick,
            mapSlot = mapSlot,
            modifier = Modifier.weight(3f)
        )
        roadbookSlot(Modifier.weight(7f))
    }
}

// --- PORTRAIT LAYOUT ---

@Composable
fun PortraitLayout(
    onSettingsClick: () -> Unit,
    primaryOdometerSlot: @Composable (Modifier) -> Unit,
    secondaryOdometerSlot: @Composable (Modifier) -> Unit,
    roadbookSlot: @Composable (Modifier) -> Unit,
    mapSlot: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        PortraitDistanceSection(
            primaryOdometerSlot = primaryOdometerSlot,
            secondaryOdometerSlot = secondaryOdometerSlot,
            onSettingsClick = onSettingsClick,
            mapSlot = mapSlot,
            modifier = Modifier.weight(2f)
        )
        roadbookSlot(Modifier.weight(3f))
    }
}

// --- SHARED COMPONENTS ---

@Composable
fun LandscapeDistanceSection(
    primaryOdometerSlot: @Composable (Modifier) -> Unit,
    secondaryOdometerSlot: @Composable (Modifier) -> Unit,
    onSettingsClick: () -> Unit,
    mapSlot: @Composable (Modifier) -> Unit,
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
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsButton(onClick = onSettingsClick)
            secondaryOdometerSlot(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        primaryOdometerSlot(Modifier.fillMaxWidth())

        mapSlot(Modifier.weight(1f))
    }
}

@Composable
fun PortraitDistanceSection(
    primaryOdometerSlot: @Composable (Modifier) -> Unit,
    secondaryOdometerSlot: @Composable (Modifier) -> Unit,
    onSettingsClick: () -> Unit,
    mapSlot: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                RoadbookNavigatorTheme.dimensions.sectionBorder,
                MaterialTheme.colorScheme.outline
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsButton(onClick = onSettingsClick)
            secondaryOdometerSlot(
                Modifier
                    .weight(0.45f)
                    .fillMaxHeight()
            )
            primaryOdometerSlot(
                Modifier
                    .weight(0.55f)
                    .fillMaxHeight()
            )
        }
        mapSlot(Modifier.weight(1f))
    }
}

@Composable
fun SettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(RoadbookNavigatorTheme.dimensions.actionIconSize)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = stringResource(R.string.action_settings),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
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
    odometer = Odometer(
        total = 2400.0,
        partial = 1150.0
    )
)

private val sampleRoadbookState = RoadbookUiState.Success(
    route = Route(
        name = "Test Route",
        waypoints = sampleWaypoints
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
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(
            DpSize(1097.dp, 686.dp)
        )
    ) {
        MainContent(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1097.dp, 686.dp)),
            uiState = sampleUiState,
            onSettingsClick = {},
            primaryOdometerSlot = { modifier -> PartialDistance(distance = "999.99", modifier = modifier, onLongClick = {}) },
            secondaryOdometerSlot = { modifier -> TotalDistance("9,999.9", modifier) },
            roadbookSlot = { modifier -> 
                RoadbookContent(
                    state = sampleRoadbookState,
                    listState = rememberLazyListState(),
                    modifier = modifier,
                    onSetPartialClick = {},
                    onWaypointVisible = { _, _ -> },
                    onFileSelected = {},
                )
            },
            mapSlot = { modifier -> Box(modifier.background(MaterialTheme.colorScheme.tertiary)) }
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
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(
            DpSize(686.dp, 1097.dp)
        )
    ) {
        MainContent(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(686.dp, 1097.dp)),
            uiState = sampleUiState,
            onSettingsClick = {},
            primaryOdometerSlot = { modifier -> PartialDistance(distance = "999.99", modifier = modifier, onLongClick = {}) },
            secondaryOdometerSlot = { modifier -> TotalDistance("9,999.9", modifier) },
            roadbookSlot = { modifier -> 
                RoadbookContent(
                    state = sampleRoadbookState,
                    listState = rememberLazyListState(),
                    modifier = modifier,
                    onSetPartialClick = {},
                    onWaypointVisible = { _, _ -> },
                    onFileSelected = {},
                )
            },
            mapSlot = { modifier -> Box(modifier.background(MaterialTheme.colorScheme.tertiary)) }
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
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(
            DpSize(411.dp, 891.dp)
        )
    ) {
        MainContent(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
            uiState = sampleUiState,
            onSettingsClick = {},
            primaryOdometerSlot = { modifier -> PartialDistance(distance = "999.99", modifier = modifier, onLongClick = {}) },
            secondaryOdometerSlot = { modifier -> TotalDistance("9,999.9", modifier) },
            roadbookSlot = { modifier -> 
                RoadbookContent(
                    state = sampleRoadbookState,
                    listState = rememberLazyListState(),
                    modifier = modifier,
                    onSetPartialClick = {},
                    onWaypointVisible = { _, _ -> },
                    onFileSelected = {},
                )
            },
            mapSlot = { modifier -> Box(modifier.background(MaterialTheme.colorScheme.tertiary)) }
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
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(
            DpSize(891.dp, 411.dp)
        )
    ) {
        MainContent(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(891.dp, 411.dp)),
            uiState = sampleUiState,
            onSettingsClick = {},
            primaryOdometerSlot = { modifier -> PartialDistance(distance = "999.99", modifier = modifier, onLongClick = {}) },
            secondaryOdometerSlot = { modifier -> TotalDistance("9,999.9", modifier) },
            roadbookSlot = { modifier -> 
                RoadbookContent(
                    state = sampleRoadbookState,
                    listState = rememberLazyListState(),
                    modifier = modifier,
                    onSetPartialClick = {},
                    onWaypointVisible = { _, _ -> },
                    onFileSelected = {},
                )
            },
            mapSlot = { modifier -> Box(modifier.background(MaterialTheme.colorScheme.tertiary)) }
        )
    }
}
