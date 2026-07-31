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
import org.giste.android.location.data.DataStoreLocationSettingsRepository
import org.giste.android.location.data.GpsLocationRepository
import org.giste.android.location.data.LocationDataStoreFactory
import org.giste.android.location.domain.usecase.ObserveLocationSettingsUseCase
import org.giste.android.location.domain.usecase.ObserveLocationUseCase
import org.giste.android.location.domain.usecase.RestoreLocationDefaultsUseCase
import org.giste.android.location.domain.usecase.UpdateLocationMinDistanceUseCase
import org.giste.android.location.domain.usecase.UpdateLocationPollingIntervalUseCase

/**
 * The main entry point for the location module.
 * Provides access to all location features and can be used with or without Hilt.
 */
public class LocationClient private constructor(
    private val context: Context,
    private val logger: LocationLogger,
    private val config: LocationConfig,
    private val dataStoreName: String
) {
    // Repositories are lazily initialized
    internal val locationSettingsRepository: LocationSettingsRepository by lazy {
        DataStoreLocationSettingsRepository(
            dataStore = LocationDataStoreFactory.create(
                context = context,
                name = dataStoreName,
                scope = config.coroutineScope
            ),
            logger = logger,
            initialConfig = config
        )
    }

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
            locationSettingsRepository = locationSettingsRepository,
            logger = logger
        )
    }

    /**
     * Use case to observe location settings.
     */
    public val observeLocationSettings: ObserveLocationSettingsUseCase by lazy {
        ObserveLocationSettingsUseCase(locationSettingsRepository)
    }

    /**
     * Use case to restore location settings to their default values.
     */
    public val restoreLocationDefaults: RestoreLocationDefaultsUseCase by lazy {
        RestoreLocationDefaultsUseCase(locationSettingsRepository)
    }

    /**
     * Use case to update the minimum distance between location updates.
     */
    public val updateLocationMinDistance: UpdateLocationMinDistanceUseCase by lazy {
        UpdateLocationMinDistanceUseCase(locationSettingsRepository)
    }

    /**
     * Use case to update the location polling interval.
     */
    public val updateLocationPollingInterval: UpdateLocationPollingIntervalUseCase by lazy {
        UpdateLocationPollingIntervalUseCase(locationSettingsRepository)
    }

    public companion object {
        /**
         * Creates a new instance of [LocationClient].
         *
         * @param context The application context.
         * @param config The initial configuration. Defaults to [LocationConfig].
         * @param logger An optional logger. Defaults to [AndroidLocationLogger].
         * @param dataStoreName The name of the DataStore file. Defaults to "location_settings".
         */
        public fun create(
            context: Context,
            config: LocationConfig = LocationConfig(),
            logger: LocationLogger = AndroidLocationLogger(),
            dataStoreName: String = "location_settings"
        ): LocationClient {
            return LocationClient(
                context = context.applicationContext,
                logger = logger,
                config = config,
                dataStoreName = dataStoreName
            )
        }
    }
}
