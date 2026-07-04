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
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookSessionRepository
import javax.inject.Inject

/**
 * Use case to save the current roadbook scroll position.
 */
class SaveRoadbookPositionUseCase @Inject constructor(
    private val repository: RoadbookSessionRepository,
    private val logger: Logger
) {
    suspend operator fun invoke(index: Int, offset: Int) {
        logger.v("SaveRoadbookPositionUseCase: Saving position index=%d, offset=%d", index, offset)
        repository.saveScrollPosition(RoadbookPosition(index, offset))
    }
}
