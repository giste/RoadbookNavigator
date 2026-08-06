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

import kotlinx.coroutines.flow.Flow

/**
 * Interface to be implemented by the consumer (e.g. Settings module)
 * to provide live settings to the Roadbook module.
 */
public interface RoadbookSettingsProvider {
    /**
     * Returns a reactive stream of the current [RoadbookSettings].
     */
    public fun getSettings(): Flow<RoadbookSettings>
}
