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

package org.giste.android.location.domain.usecase

import androidx.annotation.IntRange
import org.giste.android.location.domain.LocationSettingsRepository
import org.giste.android.location.domain.PollingIntervalThreshold
import javax.inject.Inject

/**
 * Use case to update the location polling interval.
 */
public class UpdateLocationPollingIntervalUseCase @Inject internal constructor(
    private val repository: LocationSettingsRepository
) {
    public suspend operator fun invoke(
        @IntRange(from = PollingIntervalThreshold.MIN, to = PollingIntervalThreshold.MAX)
        interval: Long
    ): Result<Unit> = runCatching {
        PollingIntervalThreshold(interval)
        repository.updatePollingInterval(interval)
    }
}
