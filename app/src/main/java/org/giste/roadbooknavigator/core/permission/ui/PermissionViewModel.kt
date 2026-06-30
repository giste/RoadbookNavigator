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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.giste.roadbooknavigator.core.permission.domain.ObserveAllPermissionsUseCase
import org.giste.roadbooknavigator.core.permission.domain.PermissionState
import org.giste.roadbooknavigator.core.permission.domain.RefreshPermissionStatesUseCase
import org.giste.roadbooknavigator.core.util.logger
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    observeAllPermissionsUseCase: ObserveAllPermissionsUseCase,
    private val refreshPermissionStatesUseCase: RefreshPermissionStatesUseCase
) : ViewModel() {

    val uiState: StateFlow<PermissionUiState> = observeAllPermissionsUseCase()
        .map { states ->
            PermissionUiState(
                permissions = states
            )
        }
        .onEach { state ->
            logger.d("PermissionUiState updated: allGranted=%s", state.allGranted)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PermissionUiState()
        )

    fun refresh() {
        logger.d("Permission refresh requested from ViewModel")
        refreshPermissionStatesUseCase()
    }
}

data class PermissionUiState(
    val permissions: Map<org.giste.roadbooknavigator.core.permission.domain.AppPermission, PermissionState> = emptyMap()
) {
    val allGranted: Boolean = permissions.isNotEmpty() && permissions.values.all { it is PermissionState.Granted }
}
