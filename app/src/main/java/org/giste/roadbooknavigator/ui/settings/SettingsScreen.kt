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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.giste.roadbooknavigator.R
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.settings.domain.AccuracyThreshold
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.AppTheme
import org.giste.roadbooknavigator.features.settings.domain.MinDistanceThreshold
import org.giste.roadbooknavigator.features.settings.domain.PollingIntervalThreshold
import org.giste.roadbooknavigator.features.settings.domain.ShortDistanceThreshold
import org.giste.roadbooknavigator.features.settings.domain.SpeedThreshold
import org.giste.roadbooknavigator.features.settings.domain.VerticalAccuracyThreshold

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
        onOdometerPollingIntervalChange = viewModel::setOdometerPollingInterval,
        onOdometerMinDistanceChange = viewModel::setOdometerMinDistance,
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
    onFullScreenChange: (Boolean) -> Unit,
    onShortDistanceThresholdChange: (Long) -> Unit,
    onOdometerSpeedThresholdChange: (Float) -> Unit,
    onOdometerMinAccuracyChange: (Float) -> Unit,
    onOdometerMinVerticalAccuracyChange: (Float) -> Unit,
    onOdometerPollingIntervalChange: (Long) -> Unit,
    onOdometerMinDistanceChange: (Float) -> Unit,
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
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.headlineMedium
                    )
                        },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(RoadbookNavigatorTheme.dimensions.actionIconSize)
                            .testTag("SettingsBackButton")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
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
                        modifier = Modifier.testTag("SettingsTab_$index"),
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineSmall
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
                                onFullScreenChange = onFullScreenChange,
                                onShortDistanceThresholdChange = onShortDistanceThresholdChange
                            )

                            1 -> AdvancedTab(
                                settings = settings,
                                onOdometerSpeedThresholdChange = onOdometerSpeedThresholdChange,
                                onOdometerMinAccuracyChange = onOdometerMinAccuracyChange,
                                onOdometerMinVerticalAccuracyChange = onOdometerMinVerticalAccuracyChange,
                                onOdometerPollingIntervalChange = onOdometerPollingIntervalChange,
                                onOdometerMinDistanceChange = onOdometerMinDistanceChange,
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
    onFullScreenChange: (Boolean) -> Unit,
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

        // Full Screen Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SettingsSectionTitle(stringResource(R.string.settings_full_screen_title))
                Text(
                    text = stringResource(R.string.settings_full_screen_helper),
                    style = labelStyle()
                )
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
            value = settings.shortDistanceThreshold.toFloat(),
            onValueChange = { onShortDistanceThresholdChange(it.toLong()) },
            valueRange = ShortDistanceThreshold.MIN.toFloat()..ShortDistanceThreshold.MAX.toFloat(),
            label = "${settings.shortDistanceThreshold} m",
            testTag = "ShortDistanceSlider"
        )
    }
}

@Composable
fun AdvancedTab(
    settings: AppSettings,
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
                style = labelStyle(),
                fontWeight = FontWeight.Bold
            )
        }

        SliderSettingItem(
            title = stringResource(R.string.settings_advanced_speed_threshold_title),
            helper = stringResource(R.string.settings_advanced_speed_threshold_helper),
            value = settings.odometerSpeedThreshold,
            onValueChange = onOdometerSpeedThresholdChange,
            valueRange = SpeedThreshold.MIN..SpeedThreshold.MAX,
            label = "${"%.1f".format(settings.odometerSpeedThreshold)} m/s",
            testTag = "SpeedThresholdSlider"
        )

        SliderSettingItem(
            title = stringResource(R.string.settings_advanced_min_accuracy_title),
            helper = stringResource(R.string.settings_advanced_min_accuracy_helper),
            value = settings.odometerMinAccuracy,
            onValueChange = onOdometerMinAccuracyChange,
            valueRange = AccuracyThreshold.MIN..AccuracyThreshold.MAX,
            label = "${"%.0f".format(settings.odometerMinAccuracy)} m",
            testTag = "MinAccuracySlider"
        )

        SliderSettingItem(
            title = stringResource(R.string.settings_advanced_min_vertical_accuracy_title),
            helper = stringResource(R.string.settings_advanced_min_vertical_accuracy_helper),
            value = settings.odometerMinVerticalAccuracy,
            onValueChange = onOdometerMinVerticalAccuracyChange,
            valueRange = VerticalAccuracyThreshold.MIN..VerticalAccuracyThreshold.MAX,
            label = "${"%.0f".format(settings.odometerMinVerticalAccuracy)} m",
            testTag = "MinVerticalAccuracySlider"
        )

        SliderSettingItem(
            title = stringResource(R.string.settings_advanced_polling_interval_title),
            helper = stringResource(R.string.settings_advanced_polling_interval_helper),
            value = settings.odometerPollingInterval.toFloat(),
            onValueChange = { onOdometerPollingIntervalChange(it.toLong()) },
            valueRange = PollingIntervalThreshold.MIN.toFloat()..PollingIntervalThreshold.MAX.toFloat(),
            label = "${settings.odometerPollingInterval} ms",
            testTag = "PollingIntervalSlider"
        )

        SliderSettingItem(
            title = stringResource(R.string.settings_advanced_min_distance_title),
            helper = stringResource(R.string.settings_advanced_min_distance_helper),
            value = settings.odometerMinDistance,
            onValueChange = onOdometerMinDistanceChange,
            valueRange = MinDistanceThreshold.MIN..MinDistanceThreshold.MAX,
            label = "${"%.1f".format(settings.odometerMinDistance)} m",
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
        Text(text = helper, style = labelStyle())
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
fun MapTab() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Map Management - Coming Soon", style = MaterialTheme.typography.headlineSmall)
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = titleStyle(),
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
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
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
                    style = MaterialTheme.typography.labelLarge,
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

private fun Modifier.minWidth(width: androidx.compose.ui.unit.Dp) = this.defaultMinSize(minWidth = width)

@Composable
fun OrientationSelector(currentOrientation: AppOrientation, onOrientationSelected: (AppOrientation) -> Unit) {
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

@Composable
fun titleStyle(): TextStyle = MaterialTheme.typography.titleLarge

@Composable
fun labelStyle(): TextStyle = MaterialTheme.typography.bodyLarge

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
            onFullScreenChange = {},
            onShortDistanceThresholdChange = {},
            onOdometerSpeedThresholdChange = {},
            onOdometerMinAccuracyChange = {},
            onOdometerMinVerticalAccuracyChange = {},
            onOdometerPollingIntervalChange = {},
            onOdometerMinDistanceChange = {},
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
            onFullScreenChange = {},
            onShortDistanceThresholdChange = {},
            onOdometerSpeedThresholdChange = {},
            onOdometerMinAccuracyChange = {},
            onOdometerMinVerticalAccuracyChange = {},
            onOdometerPollingIntervalChange = {},
            onOdometerMinDistanceChange = {},
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
            onFullScreenChange = {},
            onShortDistanceThresholdChange = {},
            onOdometerSpeedThresholdChange = {},
            onOdometerMinAccuracyChange = {},
            onOdometerMinVerticalAccuracyChange = {},
            onOdometerPollingIntervalChange = {},
            onOdometerMinDistanceChange = {},
            onRestoreOdometerDefaults = {}
        )
    }
}
