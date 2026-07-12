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

package org.giste.roadbooknavigator.features.roadbook.data.rn2

import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Icon
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Waypoint
import org.giste.roadbooknavigator.features.roadbook.domain.model.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.model.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.model.Waypoint
import javax.inject.Inject
import kotlin.math.roundToLong

/**
 * Service responsible for the sequential processing of waypoints,
 * including distance accumulation, reset logic, and visibility filtering.
 */
internal class WaypointProcessor @Inject constructor(
    private val geometryCalculator: RoadbookGeometryCalculator,
    private val rn2ElementMapper: Rn2ElementMapper,
    private val logger: Logger
) {

    private data class ProcessingState(
        val prev: Rn2Waypoint?,
        val current: Rn2Waypoint,
        val next: Rn2Waypoint?,
        val accDist: Double,
        val distFromVisible: Double,
        val visibleCount: Int,
        val isReset: Boolean
    )

    /**
     * Processes a raw list of JSON waypoints into a list of Domain Waypoints.
     */
    fun processWaypoints(waypoints: List<Rn2Waypoint>): List<Waypoint> {
        if (waypoints.isEmpty()) {
            logger.w("WaypointProcessor: Waypoint list is empty")
            return emptyList()
        }

        val initialWP = waypoints.first()
        val initialState = ProcessingState(
            prev = null,
            current = initialWP,
            next = waypoints.getOrNull(1),
            accDist = 0.0,
            distFromVisible = 0.0,
            visibleCount = if (initialWP.show) 1 else 0,
            isReset = hasReset(initialWP)
        )

        return waypoints.asSequence()
            .zipWithNext { current, next -> current to (next as Rn2Waypoint?) }
            .plus(sequenceOf(waypoints.last() to null))
            .drop(1)
            .scan(initialState) { acc, (current, next) ->
                logger.v("Processing state for waypoint %d: %s", current.waypointId, current)
                val distance = geometryCalculator.calculateDistance(acc.current, current)
                val newAccDist = if (acc.isReset) distance else acc.accDist + distance
                val newDistFromVisible =
                    if (acc.current.show) distance else acc.distFromVisible + distance
                val newVisibleCount = if (current.show) acc.visibleCount + 1 else acc.visibleCount

                val currentProcessingState = ProcessingState(
                    prev = acc.current,
                    current = current,
                    next = next,
                    accDist = newAccDist,
                    distFromVisible = newDistFromVisible,
                    visibleCount = newVisibleCount,
                    isReset = hasReset(current)
                )
                logger.v("Processed state for waypoint %s: %s", current, currentProcessingState)

                currentProcessingState
            }
            .filter { it.current.show }
            .map { state ->
                logger.v("Mapping waypoint %d to Domain model: %s", state.current.waypointId, state)
                Waypoint(
                    number = state.visibleCount,
                    coordinates = Coordinates(
                        latitude = state.current.lat,
                        longitude = state.current.lon,
                        elevation = state.current.ele
                    ),
                    distance = Distance(state.accDist.roundToLong()),
                    distanceFromPrevious = if (state.visibleCount <= 1) {
                        Distance.ZERO
                    } else {
                        Distance(state.distFromVisible.roundToLong())
                    },
                    reset = state.isReset,
                    dangerLevel = mapToDangerLevel(state.current),
                    tulipElements = rn2ElementMapper.mapElements(
                        state.current.tulip.elements,
                        state.prev,
                        state.current,
                        state.next
                    ),
                    notesElements = rn2ElementMapper.mapElements(
                        state.current.notes.elements,
                        null,
                        state.current,
                        null
                    ),
                ).also {
                    logger.v("WaypointProcessor: Processed waypoint %d: %s", it.number, it)
                }
            }
            .toList()
            .also {
                logger.i("WaypointProcessor: Finished processing waypoints. Total visible: ${it.size}")
            }
    }

    private fun hasReset(waypoint: Rn2Waypoint): Boolean {
        return waypoint.notes.elements.any { it is Rn2Icon.ResetDistance }
    }

    private fun mapToDangerLevel(waypoint: Rn2Waypoint): Waypoint.DangerLevel {
        waypoint.notes.elements.filterIsInstance<Rn2Icon>().forEach {
            when (it) {
                is Rn2Icon.Danger1 -> return Waypoint.DangerLevel.LOW
                is Rn2Icon.Danger2 -> return Waypoint.DangerLevel.MEDIUM
                is Rn2Icon.Danger3 -> return Waypoint.DangerLevel.HIGH
                else -> {}
            }
        }
        return Waypoint.DangerLevel.NONE
    }
}