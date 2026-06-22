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

package org.giste.roadbooknavigator.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.giste.roadbooknavigator.features.roadbook.domain.RoadbookRepository
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import java.io.InputStream
import javax.inject.Inject

/**
 * UI State for the Dashboard screen.
 */
sealed interface DashboardUiState {
    /** Initial state when the app is checking for cached data. */
    data object Loading : DashboardUiState

    /** State when no roadbook has been loaded yet. */
    data object Empty : DashboardUiState

    /** State when a roadbook is successfully loaded and ready to display. */
    data class Success(val route: Route) : DashboardUiState

    /** State when an error occurred during loading or parsing. */
    data class Error(val message: String) : DashboardUiState
}

/**
 * ViewModel for the Dashboard screen, responsible for orchestrating roadbook data
 * from the repository to the UI.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: RoadbookRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Automatically sync UI state with the repository's single source of truth.
        repository.activeRoadbook
            .onEach { route ->
                _uiState.update {
                    if (route != null) DashboardUiState.Success(route)
                    else DashboardUiState.Empty
                }
            }
            .launchIn(viewModelScope)

        // Attempt to restore the last active roadbook from internal storage.
        loadCachedRoadbook()
    }

    private fun loadCachedRoadbook() {
        viewModelScope.launch {
            repository.loadActiveRoadbook()
                .onFailure { error ->
                    _uiState.update { DashboardUiState.Error(error.message ?: "Failed to load cache") }
                }
        }
    }

    /**
     * Triggered when the user selects a new .rn2 file to load.
     *
     * @param inputStream The stream of the selected file.
     */
    fun onFileSelected(inputStream: InputStream) {
        viewModelScope.launch {
            _uiState.update { DashboardUiState.Loading }
            repository.processNewRoadbook(inputStream)
                .onFailure { error ->
                    _uiState.update { DashboardUiState.Error(error.message ?: "Failed to process file") }
                }
        }
    }
}
