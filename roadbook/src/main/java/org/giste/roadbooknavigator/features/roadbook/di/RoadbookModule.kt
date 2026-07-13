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

package org.giste.roadbooknavigator.features.roadbook.di

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
import org.giste.roadbooknavigator.features.roadbook.data.repository.DataStoreRoadbookSettingsRepository
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookSettingsRepository
import javax.inject.Singleton

private val Context.roadbookDataStore: DataStore<Preferences> by preferencesDataStore(name = "roadbook_settings")

@Module
@InstallIn(SingletonComponent::class)
abstract class RoadbookSettingsModule {

    @Binds
    @Singleton
    abstract fun bindRoadbookSettingsRepository(
        impl: DataStoreRoadbookSettingsRepository
    ): RoadbookSettingsRepository

    companion object {
        @Provides
        @Singleton
        fun provideRoadbookDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
            return context.roadbookDataStore
        }
    }
}
