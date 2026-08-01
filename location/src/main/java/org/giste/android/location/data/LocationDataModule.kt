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

package org.giste.android.location.data

import android.content.Context
import dagger.BindsOptionalOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.giste.android.location.domain.LocationClient
import org.giste.android.location.domain.LocationLogger
import org.giste.android.location.domain.LocationRepository
import java.util.Optional
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
public annotation class AppLocationLogger

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocationDataModule {

    @BindsOptionalOf
    @AppLocationLogger
    internal abstract fun optionalLocationLogger(): LocationLogger

    companion object {
        @Provides
        @Singleton
        internal fun provideLocationLogger(
            @AppLocationLogger optionalLogger: Optional<LocationLogger>,
            androidLocationLogger: AndroidLocationLogger
        ): LocationLogger = if (optionalLogger.isPresent) optionalLogger.get() else androidLocationLogger

        @Provides
        @Singleton
        internal fun provideLocationClient(
            @ApplicationContext context: Context,
            logger: LocationLogger
        ): LocationClient = LocationClient.create(
            context = context,
            logger = logger
        )

        @Provides
        internal fun provideLocationRepository(client: LocationClient): LocationRepository = client.locationRepository
    }
}
