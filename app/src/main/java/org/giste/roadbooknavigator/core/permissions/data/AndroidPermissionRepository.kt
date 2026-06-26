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

package org.giste.roadbooknavigator.core.permissions.data

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.giste.roadbooknavigator.core.permissions.domain.AppPermission
import org.giste.roadbooknavigator.core.permissions.domain.PermissionRepository
import org.giste.roadbooknavigator.core.permissions.domain.PermissionStatus
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Android implementation of [PermissionRepository].
 */
@Singleton
class AndroidPermissionRepository @Inject constructor(
    @param:ApplicationContext private val context: Context
) : PermissionRepository {

    private val _statusMap = mutableMapOf<AppPermission, MutableStateFlow<PermissionStatus>>()

    override fun getPermissionStatus(permission: AppPermission): Flow<PermissionStatus> {
        val stateFlow = _statusMap.getOrPut(permission) {
            MutableStateFlow(checkCurrentStatus(permission))
        }
        // Refresh status when the flow is collected to ensure it's up to date
        stateFlow.value = checkCurrentStatus(permission)
        return stateFlow.asStateFlow()
    }

    private fun checkCurrentStatus(permission: AppPermission): PermissionStatus {
        val allGranted = permission.manifestPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        return if (allGranted) {
            PermissionStatus.GRANTED
        } else {
            // RATIONALE_REQUIRED and PERMANENTLY_DENIED 
            // require an Activity context. Default to DENIED.
            PermissionStatus.DENIED
        }
    }
}
