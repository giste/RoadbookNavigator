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

package org.giste.roadbooknavigator.features.odometer.domain.usecase

import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettingsRepository
import org.giste.roadbooknavigator.features.odometer.domain.SpeedThreshold
import javax.inject.Inject

/**
 * Use case to update the odometer speed threshold.
 * Uses [SpeedThreshold] to ensure domain validity.
 */
class UpdateOdometerSpeedThresholdUseCase @Inject constructor(
    private val repository: OdometerSettingsRepository
) {
    suspend operator fun invoke(threshold: Float): Result<Unit> = runCatching {
        val validThreshold = SpeedThreshold(threshold)
        repository.setSpeedThreshold(validThreshold.metersPerSecond)
    }
}
