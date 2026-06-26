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

package org.giste.roadbooknavigator.core.permissions.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.giste.roadbooknavigator.core.permissions.domain.GetLocationPermissionStatusUseCase
import org.giste.roadbooknavigator.core.permissions.domain.PermissionStatus
import javax.inject.Inject

/**
 * ViewModel to manage global permission states.
 */
@HiltViewModel
class PermissionViewModel @Inject constructor(
    getLocationPermissionStatusUseCase: GetLocationPermissionStatusUseCase
) : ViewModel() {

    /**
     * State of the location permission.
     */
    val locationPermissionStatus: StateFlow<PermissionStatus> = getLocationPermissionStatusUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PermissionStatus.DENIED
        )
}
