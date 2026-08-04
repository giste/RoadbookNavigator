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

package org.giste.odometer.domain

import kotlinx.coroutines.flow.Flow

/**
 * Interface that defines the settings required by the Odometer module.
 * This allows the module to be decoupled from the actual settings persistence.
 */
public interface OdometerSettingsProvider {
    /**
     * Observes odometer-related settings.
     */
    public fun getSettings(): Flow<OdometerSettings>
}
