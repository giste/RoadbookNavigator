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
import org.giste.roadbooknavigator.features.roadbook.domain.util.RoadbookLogger
import org.giste.roadbooknavigator.features.roadbook.domain.model.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookSessionRepository
import javax.inject.Inject

/**
 * Use case to move the roadbook scroll position one waypoint forward.
 */
internal class MoveRoadbookUpUseCase @Inject constructor(
    private val getActiveRoadbookUseCase: GetActiveRoadbookUseCase,
    private val repository: RoadbookSessionRepository,
    private val logger: RoadbookLogger
) {
    suspend operator fun invoke() {
        val route = getActiveRoadbookUseCase().first() ?: return
        val waypointsCount = route.waypoints.size
        if (waypointsCount == 0) return

        val currentPosition = repository.scrollPosition.first()

        val newIndex = (currentPosition.index + 1).coerceAtMost(waypointsCount - 1)
        
        logger.d("MoveRoadbookUpUseCase: Moving from index %d to %d", currentPosition.index, newIndex)
        repository.saveScrollPosition(RoadbookPosition(newIndex, 0))
    }
}
