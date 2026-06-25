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

package org.giste.roadbooknavigator.features.location.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import javax.inject.Inject

/**
 * Use case to observe the current device location.
 */
class ObserveLocationUseCase @Inject constructor(
    private val repository: LocationRepository,
    private val getSettingsUseCase: GetSettingsUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<UserLocation> = getSettingsUseCase()
        .flatMapLatest { settings ->
            repository.getLocations(
                pollingInterval = settings.odometerPollingInterval,
                minDistance = settings.odometerMinDistance
            )
        }
}
