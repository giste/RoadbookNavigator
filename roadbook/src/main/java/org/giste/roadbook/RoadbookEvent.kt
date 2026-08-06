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

package org.giste.roadbook

/**
 * Domain events emitted by the Roadbook module to notify the consumer of internal state changes
 * that require external action.
 */
public sealed class RoadbookEvent {
    /**
     * Emitted when the user requests to sync the odometer's partial distance with a waypoint.
     *
     * @property distance The distance in meters to set (e.g., 0.0 for a reset waypoint).
     */
    public data class DistanceSectionLongPressed(val distance: Double) : RoadbookEvent()

    /**
     * Emitted when a new roadbook has been successfully imported and processed.
     */
    public data object RouteImported : RoadbookEvent()
}
