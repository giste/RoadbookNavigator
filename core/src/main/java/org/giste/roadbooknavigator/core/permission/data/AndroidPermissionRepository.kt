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

package org.giste.roadbooknavigator.core.permission.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.giste.roadbooknavigator.core.permission.domain.AppPermission
import org.giste.roadbooknavigator.core.permission.domain.PermissionRepository
import org.giste.roadbooknavigator.core.permission.domain.PermissionState
import org.giste.roadbooknavigator.core.util.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidPermissionRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val logger: Logger
) : PermissionRepository {

    private val _states = MutableStateFlow(calculatePermissionStates())

    override fun observeAllPermissions(): Flow<Map<AppPermission, PermissionState>> =
        _states.asStateFlow()

    override fun refreshPermissionStates() {
        logger.d("AndroidPermissionRepository: Refreshing permission states in repository")
        _states.value = calculatePermissionStates()
    }

    private fun calculatePermissionStates(): Map<AppPermission, PermissionState> {
        logger.v("AndroidPermissionRepository: Calculating permission states")
        val states = AppPermission.entries.associateWith { permission ->
            val androidPermission =
                permission.toAndroidPermission ?: return@associateWith PermissionState.Granted

            val isGranted = ContextCompat.checkSelfPermission(
                context,
                androidPermission
            ) == PackageManager.PERMISSION_GRANTED

            if (isGranted) {
                PermissionState.Granted
            } else {
                PermissionState.Denied
            }
        }
        logger.d("AndroidPermissionRepository: New permission states calculated: %s", states)
        return states
    }

    private val AppPermission.toAndroidPermission: String?
        get() = when (this) {
            AppPermission.FINE_LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
            AppPermission.COARSE_LOCATION -> Manifest.permission.ACCESS_COARSE_LOCATION
            AppPermission.NOTIFICATIONS -> Manifest.permission.POST_NOTIFICATIONS
        }
}
