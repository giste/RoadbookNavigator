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

package org.giste.roadbooknavigator.features.roadbook.domain.usecase

import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookRepository
import java.io.InputStream
import javax.inject.Inject

/**
 * Use case to import and process a new roadbook from a stream.
 */
class ImportRoadbookUseCase @Inject constructor(
    private val repository: RoadbookRepository,
    private val resetRoadbookPositionUseCase: ResetRoadbookPositionUseCase
) {
    suspend operator fun invoke(inputStream: InputStream): Result<Route> {
        Logger.i("ImportRoadbookUseCase: Starting roadbook import")
        return repository.processNewRoadbook(inputStream)
            .onSuccess { route ->
                Logger.i("ImportRoadbookUseCase: Successfully imported roadbook: ${route.name}")
                resetRoadbookPositionUseCase()
            }
            .onFailure { error ->
                Logger.e(error, "ImportRoadbookUseCase: Failed to import roadbook")
            }
    }
}
