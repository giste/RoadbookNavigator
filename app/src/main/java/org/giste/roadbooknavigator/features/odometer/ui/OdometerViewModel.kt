/*
 * Rn2 Viewer
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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.giste.roadbooknavigator.features.odometer.domain.Odometer
import org.giste.roadbooknavigator.features.odometer.domain.usecase.DecrementPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.GetOdometerUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.IncrementPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.ResetAllDistancesUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.ResetPartialDistanceUseCase
import org.giste.roadbooknavigator.features.odometer.domain.usecase.SetPartialDistanceUseCase
import javax.inject.Inject

@HiltViewModel
class OdometerViewModel @Inject constructor(
    getOdometerUseCase: GetOdometerUseCase,
    private val resetPartialDistanceUseCase: ResetPartialDistanceUseCase,
    private val resetAllDistancesUseCase: ResetAllDistancesUseCase,
    private val incrementPartialDistanceUseCase: IncrementPartialDistanceUseCase,
    private val decrementPartialDistanceUseCase: DecrementPartialDistanceUseCase,
    private val setPartialDistanceUseCase: SetPartialDistanceUseCase,
) : ViewModel() {

    private val _showSetPartialDialog = MutableStateFlow(false)

    val uiState: StateFlow<OdometerUiState> = combine(
        getOdometerUseCase().onStart { emit(Odometer()) },
        _showSetPartialDialog,
    ) { odometer, showDialog ->
        OdometerUiState(
            odometer = odometer,
            showSetPartialDialog = showDialog,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = OdometerUiState()
    )

    fun showSetPartialDialog() {
        _showSetPartialDialog.value = true
    }

    fun hideSetPartialDialog() {
        _showSetPartialDialog.value = false
    }

    fun resetPartialDistance() {
        viewModelScope.launch {
            resetPartialDistanceUseCase()
        }
    }

    fun resetAllDistances() {
        viewModelScope.launch {
            resetAllDistancesUseCase()
        }
    }

    fun incrementPartialDistance() {
        viewModelScope.launch {
            incrementPartialDistanceUseCase()
        }
    }

    fun decrementPartialDistance() {
        viewModelScope.launch {
            decrementPartialDistanceUseCase()
        }
    }

    fun setPartialDistance(distance: Double) {
        viewModelScope.launch {
            setPartialDistanceUseCase(distance)
        }
    }
}

/**
 * Represents the odometer screen state.
 */
data class OdometerUiState(
    val odometer: Odometer = Odometer(),
    val showSetPartialDialog: Boolean = false,
)
