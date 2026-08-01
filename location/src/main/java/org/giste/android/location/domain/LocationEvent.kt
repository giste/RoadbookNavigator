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

/**
 * Sealed class representing various GPS-related events.
 */
public sealed class LocationEvent {
    /**
     * Emitted when a new location fix is received.
     *
     * @property location The new user location.
     */
    public data class LocationUpdated(val location: UserLocation) : LocationEvent()

    /**
     * Emitted when the GPS signal is lost.
     */
    public data object SignalLost : LocationEvent()

    /**
     * Emitted when the GPS signal is restored after being lost.
     */
    public data object SignalRestored : LocationEvent()

    /**
     * Emitted when the location provider is disabled (e.g., user turned off GPS).
     */
    public data object ProviderDisabled : LocationEvent()

    /**
     * Emitted when an error occurs in the location engine.
     *
     * @property message A developer-friendly error message.
     */
    public data class Error(val message: String) : LocationEvent()
}
