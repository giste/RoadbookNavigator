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
        val resetFromPrevious: Boolean,
    )

    /**
     * Processes a raw list of JSON waypoints into a list of Domain Waypoints.
     */
    fun processWaypoints(waypoints: List<Rn2Waypoint>): List<Waypoint> {
        if (waypoints.isEmpty()) {
            Logger.w("WaypointProcessor: Waypoint list is empty")
            return emptyList()
        }
        Logger.d("WaypointProcessor: Processing ${waypoints.size} raw waypoints")

        val states = mutableListOf<WaypointProcessingState>()
        var currentAccDist = 0.0
        var currentLastVisibleDist = 0.0
        var previousWaypoint: Rn2Waypoint? = null
        var previousWasReset = false

        for (waypoint in waypoints) {
            if (previousWaypoint != null) {
                val distance = geometryCalculator.calculateDistance(previousWaypoint, waypoint)
                currentAccDist = if (previousWasReset) distance else currentAccDist + distance
            }

            states.add(
                WaypointProcessingState(
                    waypoint = waypoint,
                    accumulatedDist = currentAccDist,
                    lastVisibleDist = currentLastVisibleDist,
                    resetFromPrevious = previousWasReset
                )
            )

            if (waypoint.show) {
                currentLastVisibleDist = currentAccDist
            }

            previousWaypoint = waypoint
            previousWasReset = hasReset(waypoint)
        }

        var visibleCount = 0
        return states.mapIndexedNotNull { index, state ->
            if (!state.waypoint.show) return@mapIndexedNotNull null

            visibleCount++
            val distFromPrev =
                if (visibleCount == 1) 0.0 else state.accumulatedDist - state.lastVisibleDist

            val prevWaypoint = if (index > 0) states[index - 1].waypoint else null
            val nextWaypoint = if (index < states.size - 1) states[index + 1].waypoint else null

            val domainWaypoint = Waypoint(
                number = visibleCount,
                coordinates = Coordinates(
                    latitude = state.waypoint.lat,
                    longitude = state.waypoint.lon,
                    elevation = state.waypoint.ele
                ),
                distance = Distance(state.accumulatedDist.roundToLong()),
                distanceFromPrevious = Distance(distFromPrev.roundToLong()),
                reset = hasReset(state.waypoint),
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
            Logger.v("WaypointProcessor: Processed waypoint ${state.waypoint.waypointId} (visible index: $visibleCount): $domainWaypoint")

            domainWaypoint
        }.also {
            Logger.i("WaypointProcessor: Finished processing waypoints. Total visible: ${it.size}")
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