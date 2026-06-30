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
class WaypointProcessor @Inject constructor(
    private val geometryCalculator: RoadbookGeometryCalculator,
    private val rn2ElementMapper: Rn2ElementMapper
) {

    private data class WaypointProcessingState(
        val waypoint: Rn2Waypoint,
        val accumulatedDist: Double,
        val lastVisibleDist: Double,
        val reset: Boolean,
    )

    /**
     * Processes a raw list of JSON waypoints into a list of Domain Waypoints.
     */
    fun processWaypoints(waypoints: List<Rn2Waypoint>): List<Waypoint> {
        if (waypoints.isEmpty()) {
            Logger.w("WaypointProcessor: Waypoint list is empty")
            return emptyList()
        }

        val states = waypoints.asSequence()
            .drop(1)
            .scan(
                WaypointProcessingState(
                    waypoint = waypoints.first(),
                    accumulatedDist = 0.0,
                    lastVisibleDist = 0.0,
                    reset = hasReset(waypoints.first()),
                )
            ) { acc, current ->
                val distance = geometryCalculator.calculateDistance(acc.waypoint, current)
                val newAccumulatedDist =
                    if (acc.reset) distance else acc.accumulatedDist + distance
                val newLastVisibleDist =
                    if (acc.waypoint.show) distance else acc.lastVisibleDist + distance

                val waypointProcessingState = WaypointProcessingState(
                    waypoint = current,
                    accumulatedDist = newAccumulatedDist,
                    lastVisibleDist = newLastVisibleDist,
                    reset = hasReset(current)
                )
                Logger.v("WaypointProcessor: Processed waypoint state %s", waypointProcessingState)
                waypointProcessingState
            }
            .toList()

        var visibleCount = 0
        val mappedWaypoints = states.mapIndexedNotNull { index, state ->
            if (!state.waypoint.show) return@mapIndexedNotNull null

            visibleCount++
            Logger.d("WaypointProcessor: Processing visible waypoint number %s", visibleCount)
            Logger.v("WaypointProcessor: Processing visible waypoint state %s", state)

            val prevWaypoint = if (index > 0) states[index - 1].waypoint else null
            val nextWaypoint = if (index < states.size - 1) states[index + 1].waypoint else null

            val waypoint = Waypoint(
                number = visibleCount,
                coordinates = Coordinates(
                    latitude = state.waypoint.lat,
                    longitude = state.waypoint.lon,
                    elevation = state.waypoint.ele
                ),
                distance = Distance(state.accumulatedDist.roundToLong()),
                distanceFromPrevious = Distance(state.lastVisibleDist.roundToLong()),
                reset = state.reset,
                dangerLevel = mapToDangerLevel(state.waypoint),
                tulipElements = rn2ElementMapper.mapElements(
                    state.waypoint.tulip.elements,
                    prevWaypoint,
                    state.waypoint,
                    nextWaypoint
                ),
                notesElements = rn2ElementMapper.mapElements(
                    state.waypoint.notes.elements,
                    null,
                    state.waypoint,
                    null
                ),
            )
            Logger.d("Processed waypoint number %s",state.waypoint.waypointId)
            Logger.v("Processed waypoint %s", waypoint)

            waypoint
        }

        Logger.d("Processed ${states.size} waypoints, ${mappedWaypoints.size} are visible")
        return mappedWaypoints
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