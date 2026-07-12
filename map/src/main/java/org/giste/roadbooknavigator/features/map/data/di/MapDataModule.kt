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
 * along with this program.  See the License for more details.
 */

package org.giste.roadbooknavigator.features.map.data.di

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
import okhttp3.OkHttpClient
import org.giste.roadbooknavigator.features.map.data.datasource.JsoupRemoteMapDataSource
import org.giste.roadbooknavigator.features.map.data.datasource.RemoteMapDataSource
import org.giste.roadbooknavigator.features.map.data.repository.DataStoreMapSettingsRepository
import org.giste.roadbooknavigator.features.map.data.repository.MapRepositoryImpl
import org.giste.roadbooknavigator.features.map.domain.repository.MapRepository
import org.giste.roadbooknavigator.features.map.domain.repository.MapSettingsRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class MapSettingsDataStore

private val Context.mapSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "map_settings")

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MapDataModule {

    @Binds
    @Singleton
    abstract fun bindMapSettingsRepository(
        dataStoreMapSettingsRepository: DataStoreMapSettingsRepository
    ): MapSettingsRepository

    @Binds
    @Singleton
    abstract fun bindMapRepository(
        mapRepositoryImpl: MapRepositoryImpl
    ): MapRepository

    @Binds
    @Singleton
    abstract fun bindRemoteMapDataSource(
        jsoupRemoteMapDataSource: JsoupRemoteMapDataSource
    ): RemoteMapDataSource

    companion object {
        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

        @Provides
        @Singleton
        @MapSettingsDataStore
        internal fun provideMapSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
            context.mapSettingsDataStore
    }
}
