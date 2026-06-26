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
 * along with this program.  See <https://www.gnu.org/licenses/>.
 */

package org.giste.roadbooknavigator.core.permissions.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.giste.roadbooknavigator.core.permissions.domain.AppPermission
import org.giste.roadbooknavigator.core.permissions.domain.GetPermissionStatusUseCase
import org.giste.roadbooknavigator.core.permissions.domain.PermissionStatus
import javax.inject.Inject

/**
 * ViewModel to manage global permission states.
 */
@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val getPermissionStatusUseCase: GetPermissionStatusUseCase
) : ViewModel() {

    /**
     * Observable map of permission states.
     */
    private val _permissionStates = mutableMapOf<AppPermission, StateFlow<PermissionStatus>>()

    /**
     * Gets the state flow for a specific permission.
     */
    fun getPermissionStatus(permission: AppPermission): StateFlow<PermissionStatus> {
        return _permissionStates.getOrPut(permission) {
            getPermissionStatusUseCase(permission)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = PermissionStatus.DENIED
                )
        }
    }
}
