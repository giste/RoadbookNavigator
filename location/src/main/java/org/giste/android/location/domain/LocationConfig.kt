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

package org.giste.android.location.domain

import kotlinx.coroutines.CoroutineScope

/**
 * Initial configuration for the location module.
 *
 * @property initialPollingInterval Minimum time interval between location updates.
 * @property initialMinDistance Minimum distance between location updates.
 * @property coroutineScope Optional CoroutineScope for internal background tasks (e.g. DataStore).
 * If null, a default scope will be created internally.
 */
public data class LocationConfig(
    public val initialPollingInterval: PollingIntervalThreshold = PollingIntervalThreshold(LocationSettings.DEFAULT_POLLING_INTERVAL),
    public val initialMinDistance: MinDistanceThreshold = MinDistanceThreshold(LocationSettings.DEFAULT_MIN_DISTANCE),
    public val coroutineScope: CoroutineScope? = null
)
