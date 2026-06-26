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

package org.giste.roadbooknavigator.features.location.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.giste.roadbooknavigator.features.location.domain.LocationPermissionRepository
import org.giste.roadbooknavigator.features.location.domain.PermissionStatus
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Android implementation of [LocationPermissionRepository].
 */
@Singleton
class AndroidLocationPermissionRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) : LocationPermissionRepository {

    private val _permissionStatus = MutableStateFlow(checkCurrentStatus())

    override fun getPermissionStatus(): Flow<PermissionStatus> {
        // Refresh status when the flow is collected to ensure it's up to date
        _permissionStatus.value = checkCurrentStatus()
        return _permissionStatus.asStateFlow()
    }

    private fun checkCurrentStatus(): PermissionStatus {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return if (hasFineLocation || hasCoarseLocation) {
            PermissionStatus.GRANTED
        } else {
            // Note: RATIONALE_REQUIRED and PERMANENTLY_DENIED 
            // require an Activity context to be determined accurately.
            // For now, we default to DENIED.
            PermissionStatus.DENIED
        }
    }
}
