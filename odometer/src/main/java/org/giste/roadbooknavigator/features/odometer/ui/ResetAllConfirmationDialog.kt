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

package org.giste.roadbooknavigator.features.odometer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.feature.odometer.R
import org.giste.roadbooknavigator.core.R as CoreR

@Composable
fun ResetAllConfirmationDialog(
    windowSizeClass: WindowSizeClass,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val isWide = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact
    val isShort = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
    val useLandscapeLayout = isWide || isShort
    val currentAppTheme = RoadbookNavigatorTheme.appTheme

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        RoadbookNavigatorTheme(
            windowSizeClass = windowSizeClass,
            appTheme = currentAppTheme
        ) {
            if (useLandscapeLayout) {
                ResetAllLandscape(
                    onDismiss = onDismiss,
                    onConfirm = onConfirm
                )
            } else {
                ResetAllPortrait(
                    onDismiss = onDismiss,
                    onConfirm = onConfirm
                )
            }
        }
    }
}

@Composable
fun ResetAllPortrait(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val dimensions = RoadbookNavigatorTheme.dimensions
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DialogHeader(title = stringResource(R.string.reset_all_title))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(dimensions.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MessageArea(message = stringResource(R.string.reset_all_message))
                Spacer(modifier = Modifier.height(dimensions.paddingLarge))
                ActionButtons(
                    onDismiss = onDismiss,
                    onConfirm = onConfirm
                )
            }
        }
    }
}

@Composable
fun ResetAllLandscape(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val dimensions = RoadbookNavigatorTheme.dimensions
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.9f),
        color = MaterialTheme.colorScheme.background,
        shape = RoundedCornerShape(dimensions.cornerRadius)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            DialogHeader(title = stringResource(R.string.reset_all_title))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(dimensions.paddingLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                MessageArea(message = stringResource(R.string.reset_all_message))
                Spacer(modifier = Modifier.height(dimensions.paddingLarge))
                ActionButtons(
                    onDismiss = onDismiss,
                    onConfirm = onConfirm
                )
            }
        }
    }
}

@Composable
private fun DialogHeader(title: String) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ResetAllDialogHeader")
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(RoadbookNavigatorTheme.dimensions.paddingMedium),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onErrorContainer,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MessageArea(message: String) {
    Text(
        text = message,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ResetAllMessage")
    )
}

@Composable
private fun ActionButtons(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val dimensions = RoadbookNavigatorTheme.dimensions
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(dimensions.paddingSmall)
    ) {
        Button(
            onClick = onDismiss,
            modifier = Modifier
                .weight(1f)
                .height(dimensions.dialogButtonHeight)
                .testTag("ResetAllCancelButton"),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = RoundedCornerShape(dimensions.cornerRadius)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = stringResource(CoreR.string.action_cancel),
                modifier = Modifier.size(dimensions.actionIconSize)
            )
        }

        Button(
            onClick = onConfirm,
            modifier = Modifier
                .weight(1f)
                .height(dimensions.dialogButtonHeight)
                .testTag("ResetAllConfirmButton"),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            shape = RoundedCornerShape(dimensions.cornerRadius)
        ) {
            Icon(
                Icons.Default.Check,
                contentDescription = stringResource(CoreR.string.action_confirm),
                modifier = Modifier.size(dimensions.actionIconSize)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
private fun ResetAllPortraitPreview() {
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))
    RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
        ResetAllPortrait(
            onDismiss = {},
            onConfirm = {}
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,orientation=landscape")
@Composable
private fun ResetAllLandscapePreview() {
    val windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1280.dp, 800.dp))
    RoadbookNavigatorTheme(windowSizeClass = windowSizeClass) {
        ResetAllLandscape(
            onDismiss = {},
            onConfirm = {}
        )
    }
}
