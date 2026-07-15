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

import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.odometer.domain.OdometerSettingsRepository
import javax.inject.Inject

/**
 * Use case to update the remote control keys for odometer actions.
 */
class UpdateOdometerRemoteKeysUseCase @Inject constructor(
    private val repository: OdometerSettingsRepository,
    private val logger: Logger
) {
    suspend operator fun invoke(increase: List<Int>, decrease: List<Int>, reset: List<Int>): Result<Unit> {
        logger.i("UpdateOdometerRemoteKeysUseCase: Updating odometer remote keys")
        return runCatching {
            repository.setRemoteKeys(increase, decrease, reset)
        }
    }
}
