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

package org.giste.location.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.giste.location.LocationLogger
import org.giste.location.LocationProvider
import org.giste.location.LocationSettingsProvider
import org.giste.location.controller.LocationController
import org.giste.location.data.DataStoreLocationSettingsRepository
import org.giste.location.data.GpsLocationRepository
import org.giste.location.domain.LocationRepository
import org.giste.location.domain.LocationSettingsRepository
import javax.inject.Qualifier
import javax.inject.Singleton

private val Context.locationSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "org.giste.location.settings")

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class LocationIoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class LocationApplicationScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class LocationSettingsDataStore

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocationModule {

    @Binds
    @Singleton
    internal abstract fun bindLocationRepository(
        impl: GpsLocationRepository
    ): LocationRepository

    @Binds
    @Singleton
    internal abstract fun bindLocationSettingsRepository(
        impl: DataStoreLocationSettingsRepository
    ): LocationSettingsRepository

    @Binds
    @Singleton
    internal abstract fun bindLocationProvider(
        impl: LocationController
    ): LocationProvider

    @Binds
    @Singleton
    internal abstract fun bindLocationSettingsProvider(
        impl: LocationController
    ): LocationSettingsProvider

    companion object {
        @Provides
        @Singleton
        @LocationIoDispatcher
        internal fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Singleton
        @LocationApplicationScope
        internal fun provideApplicationScope(
            @LocationIoDispatcher ioDispatcher: CoroutineDispatcher
        ): CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)

        @Provides
        @Singleton
        @LocationSettingsDataStore
        internal fun provideLocationSettingsDataStore(
            @ApplicationContext context: Context
        ): DataStore<Preferences> = context.locationSettingsDataStore

        @Provides
        @Singleton
        internal fun provideGpsLocationRepository(
            @ApplicationContext context: Context,
            logger: LocationLogger
        ): GpsLocationRepository = GpsLocationRepository(
            context = context,
            logger = logger
        )
    }
}