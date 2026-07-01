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

package org.giste.roadbooknavigator.features.location.data

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
import org.giste.roadbooknavigator.features.location.domain.LocationRepository
import org.giste.roadbooknavigator.features.location.domain.LocationSettingsRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class LocationSettingsDataStore

private val Context.locationSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "location_settings")

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocationDataModule {

    @Binds
    @Singleton
    internal abstract fun bindLocationRepository(
        gpsLocationRepository: GpsLocationRepository
    ): LocationRepository

    @Binds
    @Singleton
    internal abstract fun bindLocationSettingsRepository(
        dataStoreLocationSettingsRepository: DataStoreLocationSettingsRepository
    ): LocationSettingsRepository

    companion object {
        @Provides
        @Singleton
        @LocationSettingsDataStore
        fun provideLocationSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            context.locationSettingsDataStore
    }
}
