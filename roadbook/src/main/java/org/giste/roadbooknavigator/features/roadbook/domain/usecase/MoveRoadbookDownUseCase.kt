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

import kotlinx.coroutines.flow.first
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookSessionRepository
import javax.inject.Inject

/**
 * Use case to move the roadbook scroll position one waypoint backward.
 * If the current item is partially scrolled, it first resets to the top of that item.
 */
class MoveRoadbookDownUseCase @Inject constructor(
    private val repository: RoadbookSessionRepository,
    private val logger: Logger
) {
    suspend operator fun invoke() {
        val currentPosition = repository.scrollPosition.first()
        
        val newIndex = if (currentPosition.offset > 0) {
            currentPosition.index
        } else {
            (currentPosition.index - 1).coerceAtLeast(0)
        }
        
        logger.d("MoveRoadbookDownUseCase: Moving from index %d to %d", currentPosition.index, newIndex)
        repository.saveScrollPosition(RoadbookPosition(newIndex, 0))
    }
}
