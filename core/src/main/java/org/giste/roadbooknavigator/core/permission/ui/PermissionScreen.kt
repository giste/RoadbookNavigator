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

package org.giste.roadbooknavigator.core.permission.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import org.giste.roadbooknavigator.core.permission.domain.AppPermission
import org.giste.roadbooknavigator.core.permission.domain.PermissionState
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme

@Composable
fun PermissionScreen(
    uiState: PermissionUiState,
    onPermissionResults: (Map<String, Boolean>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        // States will be updated via the repository's observation
        onPermissionResults(results)
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Permissions Required",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "To provide the best experience, Roadbook Navigator needs the following permissions:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.deniedPermissions.toList()) { (permission, state) ->
                    PermissionItem(
                        permission = permission,
                        state = state
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.hasPermanentlyDenied) {
                Button(
                    onClick = {
                        context.openAppSettings()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open App Settings")
                }
            } else {
                Button(
                    onClick = {
                        launcher.launch(uiState.androidPermissionsToRequest)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Grant Permissions")
                }
            }
        }
    }
}

@Composable
private fun PermissionItem(
    permission: AppPermission,
    state: PermissionState
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = permission.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = permission.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = permission.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (state is PermissionState.PermanentlyDenied) {
                    Text(
                        text = "Permanently denied. Please enable in settings.",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

private fun Context.openAppSettings() {
    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(this)
    }
}

private val AppPermission.title: String
    get() = when (this) {
        AppPermission.FINE_LOCATION -> "Fine Location"
        AppPermission.COARSE_LOCATION -> "Coarse Location"
    }

private val AppPermission.description: String
    get() = when (this) {
        AppPermission.FINE_LOCATION -> "Used for high-accuracy tracking and navigation."
        AppPermission.COARSE_LOCATION -> "Used for basic positioning when GPS is unavailable."
    }

private val AppPermission.icon: ImageVector
    get() = when (this) {
        AppPermission.FINE_LOCATION, AppPermission.COARSE_LOCATION -> Icons.Default.Place
    }

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
private fun PermissionScreenDeniedPreview() {
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    ) {
        PermissionScreen(
            uiState = PermissionUiState(
                permissions = mapOf(
                    AppPermission.FINE_LOCATION to PermissionState.Denied,
                    AppPermission.COARSE_LOCATION to PermissionState.Denied
                )
            ),
            onPermissionResults = {},
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
private fun PermissionScreenPermanentlyDeniedPreview() {
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    ) {
        PermissionScreen(
            uiState = PermissionUiState(
                permissions = mapOf(
                    AppPermission.FINE_LOCATION to PermissionState.PermanentlyDenied,
                    AppPermission.COARSE_LOCATION to PermissionState.Granted
                )
            ),
            onPermissionResults = {},
        )
    }
}
