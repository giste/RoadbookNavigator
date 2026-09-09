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

package org.giste.map

import kotlinx.coroutines.flow.Flow

/**
 * Public interface for managing map configuration from outside the module.
 */
interface MapSettingsProvider {
    /**
     * Observes the current map settings.
     */
    fun observeSettings(): Flow<MapSettings>

    /**
     * Persists the given map settings.
     */
    suspend fun saveSettings(settings: MapSettings)
}
