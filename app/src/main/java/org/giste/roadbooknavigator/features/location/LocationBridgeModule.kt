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

package org.giste.roadbooknavigator.features.location

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.giste.location.LocationClient
import org.giste.location.LocationLogger
import org.giste.location.LocationProvider
import org.giste.location.LocationSettingsProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocationBridgeModule {

    @Binds
    @Singleton
    abstract fun bindLocationLogger(
        impl: LocationLoggerBridge
    ): LocationLogger

    companion object {
        @Provides
        @Singleton
        fun provideLocationClient(
            @ApplicationContext context: Context,
            locationSettingsProvider: LocationSettingsProvider,
            logger: LocationLogger
        ): LocationClient = LocationClient(
            context = context,
            settings = locationSettingsProvider.settings,
            logger = logger
        )

        @Provides
        @Singleton
        fun provideLocationProvider(
            client: LocationClient
        ): LocationProvider = client
    }
}
