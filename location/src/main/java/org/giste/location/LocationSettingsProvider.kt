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

package org.giste.location

import kotlinx.coroutines.flow.Flow

/**
 * Interface that defines the configuration required by the location module.
 * This allows the module to be decoupled from the actual settings persistence.
 */
public interface LocationSettingsProvider {
    /**
     * Emits the current location settings.
     */
    public val settings: Flow<LocationSettings>

    /**
     * Updates the location settings.
     */
    public suspend fun updateSettings(settings: LocationSettings)

    /**
     * Restores the default location settings.
     */
    public suspend fun restoreDefaults()

    /**
     * Polling interval for GPS updates in milliseconds.
     */
    @Deprecated("Use settings flow instead", ReplaceWith("settings.map { it.pollingInterval }"))
    public val pollingInterval: Flow<Long>

    /**
     * Minimum distance between GPS updates in meters.
     */
    @Deprecated("Use settings flow instead", ReplaceWith("settings.map { it.minDistance }"))
    public val minDistance: Flow<Float>
}
