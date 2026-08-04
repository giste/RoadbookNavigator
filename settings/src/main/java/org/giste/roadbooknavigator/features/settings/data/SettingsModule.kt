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
 * along with this program.  See <https://www.gnu.org/licenses/>.
 */

package org.giste.roadbooknavigator.features.settings.data

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
import org.giste.roadbooknavigator.features.settings.domain.odometer.OdometerSettingsRepository
import org.giste.roadbooknavigator.features.settings.domain.location.LocationSettingsRepository
import org.giste.roadbooknavigator.features.settings.domain.SettingsRepository
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookSettingsProvider
import org.giste.roadbooknavigator.features.settings.domain.roadbook.RoadbookSettingsRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class RoadbookSettingsDataStore

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
private val Context.locationSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "location_settings")
private val Context.odometerSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "odometer_settings")
private val Context.roadbookSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "roadbook_settings")

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    @Singleton
    internal abstract fun bindSettingsRepository(
        settingsRepository: DataStoreSettingsRepository
    ): SettingsRepository

    @Binds
    @Singleton
    internal abstract fun bindLocationSettingsRepository(
        locationSettingsRepository: DataStoreLocationSettingsRepository
    ): LocationSettingsRepository

    @Binds
    @Singleton
    internal abstract fun bindOdometerSettingsRepository(
        odometerSettingsRepository: DataStoreOdometerSettingsRepository
    ): OdometerSettingsRepository

    @Binds
    @Singleton
    internal abstract fun bindRoadbookSettingsRepository(
        roadbookSettingsRepository: DataStoreRoadbookSettingsRepository
    ): RoadbookSettingsRepository

    @Binds
    @Singleton
    internal abstract fun bindRoadbookSettingsProvider(
        roadbookSettingsRepository: RoadbookSettingsRepository
    ): RoadbookSettingsProvider

    companion object {
        @Provides
        @Singleton
        @SettingsDataStore
        internal fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            context.settingsDataStore

        @Provides
        @Singleton
        @LocationSettingsDataStore
        internal fun provideLocationSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            context.locationSettingsDataStore

        @Provides
        @Singleton
        @OdometerSettingsDataStore
        internal fun provideOdometerSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            context.odometerSettingsDataStore

        @Provides
        @Singleton
        @RoadbookSettingsDataStore
        internal fun provideRoadbookSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            context.roadbookSettingsDataStore
    }
}
