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

package org.giste.roadbooknavigator.features.location.domain.usecase

import org.giste.roadbooknavigator.features.location.domain.LocationSettingsRepository
import org.giste.roadbooknavigator.features.location.domain.VerticalAccuracyThreshold
import javax.inject.Inject

/**
 * Use case to update the minimum vertical accuracy.
 */
class UpdateMinVerticalAccuracyUseCase @Inject constructor(
    private val repository: LocationSettingsRepository
) {
    suspend operator fun invoke(accuracy: Float): Result<Unit> = runCatching {
        VerticalAccuracyThreshold(accuracy)
        repository.updateMinVerticalAccuracy(accuracy)
    }
}
