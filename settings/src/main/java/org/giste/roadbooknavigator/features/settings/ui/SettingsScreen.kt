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

package org.giste.roadbooknavigator.features.settings.ui

import android.content.res.Configuration
import android.view.KeyEvent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.StayCurrentLandscape
import androidx.compose.material.icons.filled.StayCurrentPortrait
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.giste.roadbooknavigator.core.settings.domain.AppTheme
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.location.domain.LocationSettings
import org.giste.roadbooknavigator.features.location.domain.MinDistanceThreshold
import org.giste.roadbooknavigator.features.location.domain.PollingIntervalThreshold
import org.giste.roadbooknavigator.features.map.domain.model.MapSettings
import org.giste.roadbooknavigator.features.map.ui.management.MapManagementScreen
import org.giste.roadbooknavigator.features.odometer.domain.AccuracyThreshold
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettings
import org.giste.roadbooknavigator.features.odometer.domain.SpeedThreshold
import org.giste.roadbooknavigator.features.odometer.domain.VerticalAccuracyThreshold
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookSettings
import org.giste.roadbooknavigator.features.roadbook.domain.model.ShortDistanceThreshold
import org.giste.roadbooknavigator.features.settings.R
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.RemoteModel
import kotlin.math.roundToInt
import org.giste.roadbooknavigator.core.R as CoreR

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    SettingsContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onThemeSelected = viewModel::setTheme,
        onOrientationSelected = viewModel::setOrientation,
        onFullScreenChange = viewModel::setFullScreen,
        onShortDistanceThresholdChange = viewModel::setShortDistanceThreshold,
        onOdometerSpeedThresholdChange = viewModel::setOdometerSpeedThreshold,
        onOdometerMinAccuracyChange = viewModel::setOdometerMinAccuracy,
        onOdometerMinVerticalAccuracyChange = viewModel::setOdometerMinVerticalAccuracy,
        onLocationPollingIntervalChange = viewModel::setLocationPollingInterval,
        onLocationMinDistanceChange = viewModel::setLocationMinDistance,
        onRestoreOdometerDefaults = viewModel::restoreOdometerDefaults,
        onRemoteModelSelected = viewModel::setRemoteModel,
        onOdometerKeysChanged = viewModel::setOdometerKeys,
        onRoadbookKeysChanged = viewModel::setRoadbookKeys,
        onMapInitialZoomChange = viewModel::setMapInitialZoom,
        onMapInitialTiltChange = viewModel::setMapInitialTilt,
        onLandscapeWeightChange = viewModel::setLandscapeDistanceSectionWeight,
        mapManagementContent = { MapManagementScreen() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onThemeSelected: (AppTheme) -> Unit,
    onOrientationSelected: (AppOrientation) -> Unit,
    onFullScreenChange: (Boolean) -> Unit,
    onShortDistanceThresholdChange: (Long) -> Unit,
    onOdometerSpeedThresholdChange: (Float) -> Unit,
    onOdometerMinAccuracyChange: (Float) -> Unit,
    onOdometerMinVerticalAccuracyChange: (Float) -> Unit,
    onLocationPollingIntervalChange: (Long) -> Unit,
    onLocationMinDistanceChange: (Float) -> Unit,
    onRestoreOdometerDefaults: () -> Unit,
    onRemoteModelSelected: (RemoteModel) -> Unit,
    onOdometerKeysChanged: (List<Int>, List<Int>, List<Int>) -> Unit,
    onRoadbookKeysChanged: (List<Int>, List<Int>) -> Unit,
    onMapInitialZoomChange: (Int) -> Unit,
    onMapInitialTiltChange: (Float) -> Unit,
    onLandscapeWeightChange: (Float) -> Unit,
    mapManagementContent: @Composable () -> Unit,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.settings_tab_user),
        stringResource(R.string.settings_tab_remote),
        stringResource(R.string.settings_tab_advanced),
        stringResource(R.string.settings_tab_maps)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.settings_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClick()
                        },
                        modifier = Modifier
                            .size(RoadbookNavigatorTheme.dimensions.actionIconSize)
                            .testTag("SettingsBackButton")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(CoreR.string.action_back)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            PrimaryTabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                        },
                        modifier = Modifier.testTag("SettingsTab_$index"),
                        text = {
                            Text(text = title)
                        }
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                when (uiState) {
                    is SettingsUiState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    is SettingsUiState.Success -> {
                        val settings = uiState.appSettings
                        when (selectedTab) {
                            0 -> UserTab(
                                settings = settings,
                                roadbookSettings = uiState.roadbookSettings,
                                mapSettings = uiState.mapSettings,
                                onThemeSelected = onThemeSelected,
                                onOrientationSelected = onOrientationSelected,
                                onFullScreenChange = onFullScreenChange,
                                onShortDistanceThresholdChange = onShortDistanceThresholdChange,
                                onMapInitialZoomChange = onMapInitialZoomChange,
                                onMapInitialTiltChange = onMapInitialTiltChange,
                                onLandscapeWeightChange = onLandscapeWeightChange
                            )

                            1 -> RemoteTab(
                                settings = settings.remoteKeySettings,
                                roadbookSettings = uiState.roadbookSettings,
                                odometerSettings = uiState.odometerSettings,
                                onModelSelected = onRemoteModelSelected,
                                onOdometerKeysChanged = onOdometerKeysChanged,
                                onRoadbookKeysChanged = onRoadbookKeysChanged
                            )

                            2 -> AdvancedTab(
                                locationSettings = uiState.locationSettings,
                                odometerSettings = uiState.odometerSettings,
                                onOdometerSpeedThresholdChange = onOdometerSpeedThresholdChange,
                                onOdometerMinAccuracyChange = onOdometerMinAccuracyChange,
                                onOdometerMinVerticalAccuracyChange = onOdometerMinVerticalAccuracyChange,
                                onOdometerPollingIntervalChange = onLocationPollingIntervalChange,
                                onOdometerMinDistanceChange = onLocationMinDistanceChange,
                                onRestoreOdometerDefaults = onRestoreOdometerDefaults
                            )

                            3 -> MapTab(mapManagementContent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserTab(
    settings: AppSettings,
    roadbookSettings: RoadbookSettings,
    mapSettings: MapSettings,
    onThemeSelected: (AppTheme) -> Unit,
    onOrientationSelected: (AppOrientation) -> Unit,
    onFullScreenChange: (Boolean) -> Unit,
    onShortDistanceThresholdChange: (Long) -> Unit,
    onMapInitialZoomChange: (Int) -> Unit,
    onMapInitialTiltChange: (Float) -> Unit,
    onLandscapeWeightChange: (Float) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(RoadbookNavigatorTheme.dimensions.paddingLarge),
        verticalArrangement = Arrangement.spacedBy(RoadbookNavigatorTheme.dimensions.paddingLarge)
    ) {
        // Theme Selection
        SettingsSectionTitle(stringResource(R.string.settings_theme_title))
        ThemeSelector(settings.theme, onThemeSelected = onThemeSelected)

        HorizontalDivider()

        // Orientation Selection
        SettingsSectionTitle(stringResource(R.string.settings_orientation_title))
        OrientationSelector(settings.orientation, onOrientationSelected = onOrientationSelected)

        HorizontalDivider()

        // Full Screen Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SettingsSectionTitle(stringResource(R.string.settings_full_screen_title))
                Text(text = stringResource(R.string.settings_full_screen_helper))
            }
            Switch(
                checked = settings.fullScreen,
                onCheckedChange = onFullScreenChange,
                modifier = Modifier.testTag("FullScreenSwitch")
            )
        }

        HorizontalDivider()

        // Short Distance Threshold
        SettingsSectionTitle(stringResource(R.string.settings_user_short_distance_title))
        SliderSettingItem(
            helper = stringResource(R.string.settings_user_short_distance_helper),
            value = roadbookSettings.shortDistanceThreshold.meters.toFloat(),
            onValueChange = { onShortDistanceThresholdChange(it.toLong()) },
            valueRange = ShortDistanceThreshold.MIN.toFloat()..ShortDistanceThreshold.MAX.toFloat(),
            label = "${roadbookSettings.shortDistanceThreshold.meters} m",
            testTag = "ShortDistanceSlider"
        )

        HorizontalDivider()

        // Map Settings
        SettingsSectionTitle(stringResource(R.string.settings_map_zoom_title))
        SliderSettingItem(
            helper = stringResource(R.string.settings_map_zoom_helper),
            value = mapSettings.initialZoom.toFloat(),
            onValueChange = { onMapInitialZoomChange(it.toInt()) },
            valueRange = 1f..22f,
            label = mapSettings.initialZoom.toString(),
            testTag = "MapZoomSlider"
        )

        HorizontalDivider()

        SettingsSectionTitle(stringResource(R.string.settings_map_tilt_title))
        SliderSettingItem(
            helper = stringResource(R.string.settings_map_tilt_helper),
            value = mapSettings.initialTilt,
            onValueChange = onMapInitialTiltChange,
            valueRange = 0f..85f,
            label = "${mapSettings.initialTilt.toInt()}°",
            testTag = "MapTiltSlider"
        )

        HorizontalDivider()

        // Landscape Weight split
        SettingsSectionTitle(stringResource(R.string.settings_landscape_weight_title))
        SliderSettingItem(
            helper = stringResource(R.string.settings_landscape_weight_helper),
            value = settings.landscapeDistanceSectionWeight,
            onValueChange = onLandscapeWeightChange,
            valueRange = AppSettings.MIN_LANDSCAPE_WEIGHT..AppSettings.MAX_LANDSCAPE_WEIGHT,
            label = "${(settings.landscapeDistanceSectionWeight * 100).roundToInt()}% / ${(100 - settings.landscapeDistanceSectionWeight * 100).roundToInt()}%",
            testTag = "LandscapeWeightSlider"
        )
    }
}

@Composable
fun AdvancedTab(
    locationSettings: LocationSettings,
    odometerSettings: OdometerSettings,
    onOdometerSpeedThresholdChange: (Float) -> Unit,
    onOdometerMinAccuracyChange: (Float) -> Unit,
    onOdometerMinVerticalAccuracyChange: (Float) -> Unit,
    onOdometerPollingIntervalChange: (Long) -> Unit,
    onOdometerMinDistanceChange: (Float) -> Unit,
    onRestoreOdometerDefaults: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(RoadbookNavigatorTheme.dimensions.paddingLarge),
        verticalArrangement = Arrangement.spacedBy(RoadbookNavigatorTheme.dimensions.paddingLarge)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = stringResource(R.string.settings_advanced_warning),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onErrorContainer,
                fontWeight = FontWeight.Bold
            )
        }

        SliderSettingItem(
            title = stringResource(R.string.settings_advanced_speed_threshold_title),
            helper = stringResource(R.string.settings_advanced_speed_threshold_helper),
            value = odometerSettings.speedThreshold,
            onValueChange = onOdometerSpeedThresholdChange,
            valueRange = SpeedThreshold.MIN..SpeedThreshold.MAX,
            label = "${"%.1f".format(odometerSettings.speedThreshold)} m/s",
            testTag = "SpeedThresholdSlider"
        )

        SliderSettingItem(
            title = stringResource(R.string.settings_advanced_min_accuracy_title),
            helper = stringResource(R.string.settings_advanced_min_accuracy_helper),
            value = odometerSettings.minAccuracy,
            onValueChange = onOdometerMinAccuracyChange,
            valueRange = AccuracyThreshold.MIN..AccuracyThreshold.MAX,
            label = "${"%.0f".format(odometerSettings.minAccuracy)} m",
            testTag = "MinAccuracySlider"
        )

        SliderSettingItem(
            title = stringResource(R.string.settings_advanced_min_vertical_accuracy_title),
            helper = stringResource(R.string.settings_advanced_min_vertical_accuracy_helper),
            value = odometerSettings.minVerticalAccuracy,
            onValueChange = onOdometerMinVerticalAccuracyChange,
            valueRange = VerticalAccuracyThreshold.MIN..VerticalAccuracyThreshold.MAX,
            label = "${"%.0f".format(odometerSettings.minVerticalAccuracy)} m",
            testTag = "MinVerticalAccuracySlider"
        )

        SliderSettingItem(
            title = stringResource(R.string.settings_advanced_polling_interval_title),
            helper = stringResource(R.string.settings_advanced_polling_interval_helper),
            value = locationSettings.pollingInterval.toFloat(),
            onValueChange = { onOdometerPollingIntervalChange(it.toLong()) },
            valueRange = PollingIntervalThreshold.MIN.toFloat()..PollingIntervalThreshold.MAX.toFloat(),
            label = "${locationSettings.pollingInterval} ms",
            testTag = "PollingIntervalSlider"
        )

        SliderSettingItem(
            title = stringResource(R.string.settings_advanced_min_distance_title),
            helper = stringResource(R.string.settings_advanced_min_distance_helper),
            value = locationSettings.minDistance,
            onValueChange = onOdometerMinDistanceChange,
            valueRange = MinDistanceThreshold.MIN..MinDistanceThreshold.MAX,
            label = "${"%.1f".format(locationSettings.minDistance)} m",
            testTag = "MinDistanceSlider"
        )

        Button(
            onClick = onRestoreOdometerDefaults,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("RestoreDefaultsButton"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(
                text = stringResource(R.string.settings_advanced_restore_defaults),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun SliderSettingItem(
    title: String? = null,
    helper: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    label: String,
    testTag: String? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        title?.let {
            SettingsSectionTitle(it)
        }
        Text(text = helper)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = (if (testTag != null) Modifier.testTag(testTag) else Modifier)
                .fillMaxWidth()
        )
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun RemoteTab(
    settings: org.giste.roadbooknavigator.features.settings.domain.RemoteKeySettings,
    roadbookSettings: RoadbookSettings,
    odometerSettings: OdometerSettings,
    onModelSelected: (RemoteModel) -> Unit,
    onOdometerKeysChanged: (List<Int>, List<Int>, List<Int>) -> Unit,
    onRoadbookKeysChanged: (List<Int>, List<Int>) -> Unit
) {
    var capturingAction by remember { mutableStateOf<RemoteAction?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(RoadbookNavigatorTheme.dimensions.paddingLarge),
        verticalArrangement = Arrangement.spacedBy(RoadbookNavigatorTheme.dimensions.paddingLarge)
    ) {
        SettingsSectionTitle(stringResource(R.string.settings_remote_model_title))
        RemoteModelSelector(currentModel = settings.model, onModelSelected = onModelSelected)

        HorizontalDivider()

        SettingsSectionTitle(stringResource(R.string.settings_remote_keys_title))

        val isCustom = settings.model == RemoteModel.CUSTOM
        RemoteAction.entries.forEach { action ->
            val keyCodes = when (action) {
                RemoteAction.ROADBOOK_UP -> roadbookSettings.roadbookUp
                RemoteAction.ROADBOOK_DOWN -> roadbookSettings.roadbookDown
                RemoteAction.INCREASE_PARTIAL -> odometerSettings.increasePartial
                RemoteAction.DECREASE_PARTIAL -> odometerSettings.decreasePartial
                RemoteAction.RESET_PARTIAL -> odometerSettings.resetPartial
            }

            KeyMappingItem(
                action = action,
                keyCodes = keyCodes,
                enabled = isCustom,
                onClick = { capturingAction = action }
            )
        }
    }

    capturingAction?.let { action ->
        KeyCaptureDialog(
            action = action,
            onKeyCaptured = { keyCode ->
                when (action) {
                    RemoteAction.ROADBOOK_UP -> onRoadbookKeysChanged(
                        listOf(keyCode),
                        roadbookSettings.roadbookDown
                    )

                    RemoteAction.ROADBOOK_DOWN -> onRoadbookKeysChanged(
                        roadbookSettings.roadbookUp,
                        listOf(keyCode)
                    )

                    RemoteAction.INCREASE_PARTIAL -> onOdometerKeysChanged(
                        listOf(keyCode),
                        odometerSettings.decreasePartial,
                        odometerSettings.resetPartial
                    )

                    RemoteAction.DECREASE_PARTIAL -> onOdometerKeysChanged(
                        odometerSettings.increasePartial,
                        listOf(keyCode),
                        odometerSettings.resetPartial
                    )

                    RemoteAction.RESET_PARTIAL -> onOdometerKeysChanged(
                        odometerSettings.increasePartial,
                        odometerSettings.decreasePartial,
                        listOf(keyCode)
                    )
                }
                capturingAction = null
            },
            onDismiss = { capturingAction = null }
        )
    }
}

@Composable
fun RemoteModelSelector(
    currentModel: RemoteModel,
    onModelSelected: (RemoteModel) -> Unit
) {
    val options = listOf(
        RemoteModel.DND2 to stringResource(R.string.settings_remote_model_dnd2),
        RemoteModel.TERRA_PIRATA to stringResource(R.string.settings_remote_model_terra_pirata),
        RemoteModel.CUSTOM to stringResource(R.string.settings_remote_model_custom)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { (model, label) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        if (currentModel == model) MaterialTheme.colorScheme.primaryContainer
                        else Color.Transparent,
                        MaterialTheme.shapes.small
                    )
                    .border(
                        1.dp,
                        if (currentModel == model) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant,
                        MaterialTheme.shapes.small
                    )
                    .clickable { onModelSelected(model) }
                    .padding(horizontal = 16.dp)
                    .testTag("RemoteModelButton_${model.name}"),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = label,
                    fontWeight = if (currentModel == model) FontWeight.Bold else FontWeight.Normal
                )
                Icon(
                    imageVector = if (currentModel == model) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (currentModel == model) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun KeyMappingItem(
    action: RemoteAction,
    keyCodes: List<Int>,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(
                if (enabled) Color.Transparent else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                MaterialTheme.shapes.medium
            )
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.medium)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(action.labelRes),
            fontWeight = FontWeight.Medium,
            color = if (enabled) Color.Unspecified else MaterialTheme.colorScheme.onSurface.copy(
                alpha = 0.38f
            )
        )
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = 64.dp)
                .background(
                    if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    MaterialTheme.shapes.small
                )
                .padding(horizontal = 12.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = keyCodes.joinToString(", ") { keyCodeToName(it) },
                style = MaterialTheme.typography.labelLarge,
                color = if (enabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun KeyCaptureDialog(
    action: RemoteAction,
    onKeyCaptured: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var detectedKeyCode by remember { mutableIntStateOf(-1) }
    val focusRequester = remember { FocusRequester() }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .focusRequester(focusRequester)
                .onPreviewKeyEvent { event ->
                    detectedKeyCode = event.key.nativeKeyCode
                    true
                },
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.settings_remote_capture_title),
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = stringResource(
                        R.string.settings_remote_capture_helper,
                        stringResource(action.labelRes)
                    ),
                    textAlign = TextAlign.Center
                )

                if (detectedKeyCode != -1) {
                    Text(
                        text = stringResource(
                            R.string.settings_remote_capture_detected,
                            keyCodeToName(detectedKeyCode)
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.textButtonColors()) {
                        Text(stringResource(CoreR.string.action_cancel))
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    Button(
                        onClick = { if (detectedKeyCode != -1) onKeyCaptured(detectedKeyCode) },
                        enabled = detectedKeyCode != -1
                    ) {
                        Text(stringResource(CoreR.string.action_confirm))
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

enum class RemoteAction(val labelRes: Int) {
    ROADBOOK_UP(R.string.settings_remote_action_rb_up),
    ROADBOOK_DOWN(R.string.settings_remote_action_rb_down),
    INCREASE_PARTIAL(R.string.settings_remote_action_inc_partial),
    DECREASE_PARTIAL(R.string.settings_remote_action_dec_partial),
    RESET_PARTIAL(R.string.settings_remote_action_reset_partial)
}

/**
 * Converts an Android native key code to a human-readable name.
 */
private fun keyCodeToName(keyCode: Int): String {
    return when (keyCode) {
        KeyEvent.KEYCODE_DPAD_UP -> "Up"
        KeyEvent.KEYCODE_DPAD_DOWN -> "Down"
        KeyEvent.KEYCODE_DPAD_LEFT -> "Left"
        KeyEvent.KEYCODE_DPAD_RIGHT -> "Right"
        KeyEvent.KEYCODE_VOLUME_UP -> "Vol +"
        KeyEvent.KEYCODE_VOLUME_DOWN -> "Vol -"
        KeyEvent.KEYCODE_MEDIA_NEXT -> "Next"
        KeyEvent.KEYCODE_MEDIA_PREVIOUS -> "Prev"
        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> "Play/Pause"
        KeyEvent.KEYCODE_MEDIA_PLAY -> "Play"
        KeyEvent.KEYCODE_F6 -> "F6"
        KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> "Enter"
        KeyEvent.KEYCODE_BACK -> "Back"
        KeyEvent.KEYCODE_MENU -> "Menu"
        KeyEvent.KEYCODE_SPACE -> "Space"
        KeyEvent.KEYCODE_ESCAPE -> "Esc"
        else -> {
            KeyEvent.keyCodeToString(keyCode)
                ?.removePrefix("KEYCODE_")
                ?.replace("_", " ")
                ?.lowercase()
                ?.replaceFirstChar { it.uppercase() }
                ?: keyCode.toString()
        }
    }
}

@Composable
fun MapTab(mapManagementContent: @Composable () -> Unit) {
    mapManagementContent()
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ThemeSelector(currentTheme: AppTheme, onThemeSelected: (AppTheme) -> Unit) {
    val options = listOf(
        AppTheme.LIGHT to stringResource(R.string.settings_theme_light),
        AppTheme.DARK to stringResource(R.string.settings_theme_dark),
        AppTheme.FOLLOW_SYSTEM to stringResource(R.string.settings_theme_system),
        AppTheme.FIA to stringResource(R.string.settings_theme_fia),
        AppTheme.DYNAMIC to stringResource(R.string.settings_theme_dynamic)
    )

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        maxItemsInEachRow = 3
    ) {
        options.forEach { (theme, label) ->
            ThemePreviewCard(
                theme = theme,
                label = label,
                isSelected = currentTheme == theme,
                onClick = { onThemeSelected(theme) },
                modifier = Modifier
                    .weight(1f)
                    .minWidth(100.dp)
            )
        }
        // Spacer for alignment if last row is incomplete
        @Suppress("KotlinConstantConditions")
        if (options.size % 2 != 0) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ThemePreviewCard(
    theme: AppTheme,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outlineVariant
    val borderWidth = if (isSelected) 3.dp else 1.dp

    Card(
        onClick = onClick,
        modifier = modifier.height(120.dp),
        border = BorderStroke(borderWidth, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ThemeMiniPreview(theme = theme, modifier = Modifier.size(60.dp, 40.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = label,
                    //style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    textAlign = TextAlign.Center,
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(20.dp)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                )
            }
        }
    }
}

@Composable
fun ThemeMiniPreview(theme: AppTheme, modifier: Modifier = Modifier) {
    // colors from org.giste.roadbooknavigator.core.ui.theme
    val lightPrimary = Color(0xFFF28E1C)
    val lightSurface = Color(0xFFFFFBF0)
    val darkPrimary = Color(0xFFFFB300)
    val darkSurface = Color(0xFF0F0908)
    val fiaPrimary = Color(0xFF0000FF)
    val fiaSurface = Color(0xFFFFFFFF)

    val surfaceBrush = when (theme) {
        AppTheme.LIGHT -> Brush.linearGradient(listOf(lightSurface, lightSurface))
        AppTheme.DARK -> Brush.linearGradient(listOf(darkSurface, darkSurface))
        AppTheme.FIA -> Brush.linearGradient(listOf(fiaSurface, fiaSurface))
        AppTheme.FOLLOW_SYSTEM -> Brush.linearGradient(
            0.0f to lightSurface,
            0.5f to lightSurface,
            0.5f to darkSurface,
            1.0f to darkSurface,
            start = Offset.Zero,
            end = Offset.Infinite
        )

        AppTheme.DYNAMIC -> Brush.linearGradient(
            listOf(Color(0xFF80DEEA), Color(0xFFCE93D8), Color(0xFFFFF59D))
        )
    }

    val primaryBrush = when (theme) {
        AppTheme.LIGHT -> Brush.linearGradient(listOf(lightPrimary, lightPrimary))
        AppTheme.DARK -> Brush.linearGradient(listOf(darkPrimary, darkPrimary))
        AppTheme.FIA -> Brush.linearGradient(listOf(fiaPrimary, fiaPrimary))
        AppTheme.FOLLOW_SYSTEM -> Brush.linearGradient(
            0.0f to lightPrimary,
            0.5f to lightPrimary,
            0.5f to darkPrimary,
            1.0f to darkPrimary,
            start = Offset.Zero,
            end = Offset.Infinite
        )

        AppTheme.DYNAMIC -> Brush.linearGradient(
            listOf(Color(0xFF42A5F5), Color(0xFF66BB6A), Color(0xFFFFA726))
        )
    }

    Box(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, MaterialTheme.shapes.extraSmall)
            .background(surfaceBrush, MaterialTheme.shapes.extraSmall)
            .padding(4.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(primaryBrush, MaterialTheme.shapes.extraSmall)
            )
            // Content rows
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(primaryBrush, CircleShape, alpha = 0.6f)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            )
        }
    }
}

private fun Modifier.minWidth(width: androidx.compose.ui.unit.Dp) =
    this.defaultMinSize(minWidth = width)

@Composable
fun OrientationSelector(
    currentOrientation: AppOrientation,
    onOrientationSelected: (AppOrientation) -> Unit
) {
    val options = listOf(
        AppOrientation.VERTICAL to Icons.Default.StayCurrentPortrait,
        AppOrientation.HORIZONTAL to Icons.Default.StayCurrentLandscape,
        AppOrientation.FOLLOW_SYSTEM to Icons.Default.ScreenRotation
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        options.forEach { (orientation, icon) ->
            val isSelected = currentOrientation == orientation
            IconToggleButton(
                checked = isSelected,
                onCheckedChange = { if (it) onOrientationSelected(orientation) },
                modifier = Modifier
                    .weight(1f)
                    .height(RoadbookNavigatorTheme.dimensions.dialogButtonHeight)
                    .testTag("OrientationButton_${orientation.name}"),
                colors = IconButtonDefaults.iconToggleButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    checkedContainerColor = MaterialTheme.colorScheme.primary,
                    checkedContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(RoadbookNavigatorTheme.dimensions.actionIconSize)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    name = "Light Theme - Phone",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,orientation=portrait"
)
@Composable
fun SettingsPreviewLight() {
    val size = androidx.compose.ui.unit.DpSize(411.dp, 891.dp)
    val windowSizeClass = WindowSizeClass.calculateFromSize(size)
    RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
        SettingsContent(
            uiState = SettingsUiState.Success(
                appSettings = AppSettings(),
                locationSettings = LocationSettings(),
                odometerSettings = OdometerSettings(),
                mapSettings = MapSettings(),
                roadbookSettings = RoadbookSettings()
            ),
            onBackClick = {},
            onThemeSelected = {},
            onOrientationSelected = {},
            onFullScreenChange = {},
            onShortDistanceThresholdChange = {},
            onOdometerSpeedThresholdChange = {},
            onOdometerMinAccuracyChange = {},
            onOdometerMinVerticalAccuracyChange = {},
            onLocationPollingIntervalChange = {},
            onLocationMinDistanceChange = {},
            onRestoreOdometerDefaults = {},
            onRemoteModelSelected = {},
            onOdometerKeysChanged = { _, _, _ -> },
            onRoadbookKeysChanged = { _, _ -> },
            onMapInitialZoomChange = {},
            onMapInitialTiltChange = {},
            onLandscapeWeightChange = {},
            mapManagementContent = {}
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    name = "Dark Theme - Phone",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,orientation=portrait"
)
@Composable
fun SettingsPreviewDark() {
    val size = androidx.compose.ui.unit.DpSize(411.dp, 891.dp)
    val windowSizeClass = WindowSizeClass.calculateFromSize(size)
    RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
        SettingsContent(
            uiState = SettingsUiState.Success(
                appSettings = AppSettings(),
                locationSettings = LocationSettings(),
                odometerSettings = OdometerSettings(),
                mapSettings = MapSettings(),
                roadbookSettings = RoadbookSettings()
            ),
            onBackClick = {},
            onThemeSelected = {},
            onOrientationSelected = {},
            onFullScreenChange = {},
            onShortDistanceThresholdChange = {},
            onOdometerSpeedThresholdChange = {},
            onOdometerMinAccuracyChange = {},
            onOdometerMinVerticalAccuracyChange = {},
            onLocationPollingIntervalChange = {},
            onLocationMinDistanceChange = {},
            onRestoreOdometerDefaults = {},
            onRemoteModelSelected = {},
            onOdometerKeysChanged = { _, _, _ -> },
            onRoadbookKeysChanged = { _, _ -> },
            onMapInitialZoomChange = {},
            onMapInitialTiltChange = {},
            onLandscapeWeightChange = {},
            mapManagementContent = {}
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    name = "Tablet Landscape",
    showBackground = true,
    device = "spec:width=1280dp,height=800dp,orientation=landscape"
)
@Composable
fun SettingsPreviewTablet() {
    val size = androidx.compose.ui.unit.DpSize(1280.dp, 800.dp)
    val windowSizeClass = WindowSizeClass.calculateFromSize(size)
    RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
        SettingsContent(
            uiState = SettingsUiState.Success(
                appSettings = AppSettings(),
                locationSettings = LocationSettings(),
                odometerSettings = OdometerSettings(),
                mapSettings = MapSettings(),
                roadbookSettings = RoadbookSettings()
            ),
            onBackClick = {},
            onThemeSelected = {},
            onOrientationSelected = {},
            onFullScreenChange = {},
            onShortDistanceThresholdChange = {},
            onOdometerSpeedThresholdChange = {},
            onOdometerMinAccuracyChange = {},
            onOdometerMinVerticalAccuracyChange = {},
            onLocationPollingIntervalChange = {},
            onLocationMinDistanceChange = {},
            onRestoreOdometerDefaults = {},
            onRemoteModelSelected = {},
            onOdometerKeysChanged = { _, _, _ -> },
            onRoadbookKeysChanged = { _, _ -> },
            onMapInitialZoomChange = {},
            onMapInitialTiltChange = {},
            onLandscapeWeightChange = {},
            mapManagementContent = {}
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(name = "User Tab", showBackground = true)
@Composable
fun UserTabPreview() {
    val size = androidx.compose.ui.unit.DpSize(411.dp, 891.dp)
    val windowSizeClass = WindowSizeClass.calculateFromSize(size)
    RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
        UserTab(
            settings = AppSettings(),
            roadbookSettings = RoadbookSettings(),
            mapSettings = MapSettings(),
            onThemeSelected = {},
            onOrientationSelected = {},
            onFullScreenChange = {},
            onShortDistanceThresholdChange = {},
            onMapInitialZoomChange = {},
            onMapInitialTiltChange = {},
            onLandscapeWeightChange = {}
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(name = "Remote Tab", showBackground = true)
@Composable
fun RemoteTabPreview() {
    val size = androidx.compose.ui.unit.DpSize(411.dp, 891.dp)
    val windowSizeClass = WindowSizeClass.calculateFromSize(size)
    RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
        RemoteTab(
            settings = org.giste.roadbooknavigator.features.settings.domain.RemoteKeySettings(),
            roadbookSettings = RoadbookSettings(),
            odometerSettings = OdometerSettings(),
            onModelSelected = {},
            onOdometerKeysChanged = { _, _, _ -> },
            onRoadbookKeysChanged = { _, _ -> }
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(name = "Advanced Tab", showBackground = true)
@Composable
fun AdvancedTabPreview() {
    val size = androidx.compose.ui.unit.DpSize(411.dp, 891.dp)
    val windowSizeClass = WindowSizeClass.calculateFromSize(size)
    RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
        AdvancedTab(
            locationSettings = LocationSettings(),
            odometerSettings = OdometerSettings(),
            onOdometerSpeedThresholdChange = {},
            onOdometerMinAccuracyChange = {},
            onOdometerMinVerticalAccuracyChange = {},
            onOdometerPollingIntervalChange = {},
            onOdometerMinDistanceChange = {},
            onRestoreOdometerDefaults = {}
        )
    }
}
