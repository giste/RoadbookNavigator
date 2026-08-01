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

import android.content.Context
import org.giste.android.location.data.AndroidLocationLogger
import org.giste.android.location.data.GpsLocationRepository
import org.giste.android.location.domain.usecase.ObserveLocationUseCase

/**
 * The main entry point for the location module.
 * Provides access to all location features and can be used with or without Hilt.
 */
public class LocationClient private constructor(
    private val context: Context,
    private val logger: LocationLogger
) {
    internal val locationRepository: LocationRepository by lazy {
        GpsLocationRepository(
            context = context,
            logger = logger
        )
    }

    /**
     * Use case to observe the current device location.
     */
    public val observeLocation: ObserveLocationUseCase by lazy {
        ObserveLocationUseCase(
            locationRepository = locationRepository,
            logger = logger
        )
    }

    public companion object {
        /**
         * Creates a new instance of [LocationClient].
         *
         * @param context The application context.
         * @param logger An optional logger. Defaults to [AndroidLocationLogger].
         */
        public fun create(
            context: Context,
            logger: LocationLogger = AndroidLocationLogger()
        ): LocationClient {
            return LocationClient(
                context = context.applicationContext,
                logger = logger
            )
        }
    }
}
