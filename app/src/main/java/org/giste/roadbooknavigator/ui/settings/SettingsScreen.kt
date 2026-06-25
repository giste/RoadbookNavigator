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

package org.giste.roadbooknavigator.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.giste.roadbooknavigator.R
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.AppTheme

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
        onShortDistanceThresholdChange = viewModel::setShortDistanceThreshold,
        onOdometerSpeedThresholdChange = viewModel::setOdometerSpeedThreshold,
        onOdometerMinAccuracyChange = viewModel::setOdometerMinAccuracy,
        onOdometerPollingIntervalChange = viewModel::setOdometerPollingInterval,
        onRestoreOdometerDefaults = viewModel::restoreOdometerDefaults
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onThemeSelected: (AppTheme) -> Unit,
    onOrientationSelected: (AppOrientation) -> Unit,
    onShortDistanceThresholdChange: (Long) -> Unit,
    onOdometerSpeedThresholdChange: (Float) -> Unit,
    onOdometerMinAccuracyChange: (Float) -> Unit,
    onOdometerPollingIntervalChange: (Long) -> Unit,
    onRestoreOdometerDefaults: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.settings_tab_user),
        stringResource(R.string.settings_tab_advanced),
        stringResource(R.string.settings_tab_maps)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
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
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium
                            )
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
                        val settings = uiState.settings
                        when (selectedTab) {
                            0 -> UserTab(
                                settings = settings,
                                onThemeSelected = onThemeSelected,
                                onOrientationSelected = onOrientationSelected,
                                onShortDistanceThresholdChange = onShortDistanceThresholdChange
                            )

                            1 -> AdvancedTab(
                                settings = settings,
                                onOdometerSpeedThresholdChange = onOdometerSpeedThresholdChange,
                                onOdometerMinAccuracyChange = onOdometerMinAccuracyChange,
                                onOdometerPollingIntervalChange = onOdometerPollingIntervalChange,
                                onRestoreOdometerDefaults = onRestoreOdometerDefaults
                            )

                            2 -> MapTab()
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
    onThemeSelected: (AppTheme) -> Unit,
    onOrientationSelected: (AppOrientation) -> Unit,
    onShortDistanceThresholdChange: (Long) -> Unit,
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

        // Short Distance Threshold
        SettingsSectionTitle(stringResource(R.string.settings_user_short_distance_title))
        Text(
            text = stringResource(R.string.settings_user_short_distance_helper),
            style = MaterialTheme.typography.bodyLarge
        )
        Slider(
            value = settings.shortDistanceThreshold.toFloat(),
            onValueChange = { onShortDistanceThresholdChange(it.toLong()) },
            valueRange = 0f..1000f,
            steps = 20
        )
        Text(
            text = "${settings.shortDistanceThreshold} m",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun AdvancedTab(
    settings: AppSettings,
    onOdometerSpeedThresholdChange: (Float) -> Unit,
    onOdometerMinAccuracyChange: (Float) -> Unit,
    onOdometerPollingIntervalChange: (Long) -> Unit,
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
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        AdvancedSettingItem(
            title = stringResource(R.string.settings_advanced_speed_threshold_title),
            helper = stringResource(R.string.settings_advanced_speed_threshold_helper),
            value = settings.odometerSpeedThreshold.toString(),
            onValueChange = { it.toFloatOrNull()?.let(onOdometerSpeedThresholdChange) }
        )

        AdvancedSettingItem(
            title = stringResource(R.string.settings_advanced_min_accuracy_title),
            helper = stringResource(R.string.settings_advanced_min_accuracy_helper),
            value = settings.odometerMinAccuracy.toString(),
            onValueChange = { it.toFloatOrNull()?.let(onOdometerMinAccuracyChange) }
        )

        AdvancedSettingItem(
            title = stringResource(R.string.settings_advanced_polling_interval_title),
            helper = stringResource(R.string.settings_advanced_polling_interval_helper),
            value = settings.odometerPollingInterval.toString(),
            onValueChange = { it.toLongOrNull()?.let(onOdometerPollingIntervalChange) }
        )

        Button(
            onClick = onRestoreOdometerDefaults,
            modifier = Modifier.fillMaxWidth(),
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
fun MapTab() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Map Management - Coming Soon", style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun ThemeSelector(currentTheme: AppTheme, onThemeSelected: (AppTheme) -> Unit) {
    val options = listOf(
        AppTheme.LIGHT to stringResource(R.string.settings_theme_light),
        AppTheme.DARK to stringResource(R.string.settings_theme_dark),
        AppTheme.FOLLOW_SYSTEM to stringResource(R.string.settings_theme_system),
        AppTheme.FIA to stringResource(R.string.settings_theme_fia),
        AppTheme.DYNAMIC to stringResource(R.string.settings_theme_dynamic)
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { (theme, label) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currentTheme == theme,
                    onClick = { onThemeSelected(theme) },
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@Composable
fun OrientationSelector(currentOrientation: AppOrientation, onOrientationSelected: (AppOrientation) -> Unit) {
    val options = listOf(
        AppOrientation.VERTICAL to stringResource(R.string.settings_orientation_vertical),
        AppOrientation.HORIZONTAL to stringResource(R.string.settings_orientation_horizontal),
        AppOrientation.FOLLOW_SYSTEM to stringResource(R.string.settings_orientation_system)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        options.forEach { (orientation, label) ->
            FilterChip(
                selected = currentOrientation == orientation,
                onClick = { onOrientationSelected(orientation) },
                label = { Text(label, style = MaterialTheme.typography.bodyLarge) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun AdvancedSettingItem(
    title: String,
    helper: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(text = helper, style = MaterialTheme.typography.bodyMedium)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.headlineSmall
        )
    }
}

// --- PREVIEWS ---

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
            uiState = SettingsUiState.Success(AppSettings()),
            onBackClick = {},
            onThemeSelected = {},
            onOrientationSelected = {},
            onShortDistanceThresholdChange = {},
            onOdometerSpeedThresholdChange = {},
            onOdometerMinAccuracyChange = {},
            onOdometerPollingIntervalChange = {},
            onRestoreOdometerDefaults = {}
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
            uiState = SettingsUiState.Success(AppSettings()),
            onBackClick = {},
            onThemeSelected = {},
            onOrientationSelected = {},
            onShortDistanceThresholdChange = {},
            onOdometerSpeedThresholdChange = {},
            onOdometerMinAccuracyChange = {},
            onOdometerPollingIntervalChange = {},
            onRestoreOdometerDefaults = {}
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
            uiState = SettingsUiState.Success(AppSettings()),
            onBackClick = {},
            onThemeSelected = {},
            onOrientationSelected = {},
            onShortDistanceThresholdChange = {},
            onOdometerSpeedThresholdChange = {},
            onOdometerMinAccuracyChange = {},
            onOdometerPollingIntervalChange = {},
            onRestoreOdometerDefaults = {}
        )
    }
}
