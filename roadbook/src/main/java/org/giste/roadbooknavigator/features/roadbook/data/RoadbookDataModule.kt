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

package org.giste.roadbooknavigator.features.roadbook.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
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
import kotlinx.coroutines.SupervisorJob
import org.giste.roadbooknavigator.core.di.IoDispatcher
import org.giste.roadbooknavigator.features.roadbook.data.persistence.PersistenceRoadbookSerializer
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentRoute
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookRepository
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookSessionRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class RoadbookDataStoreQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class RoadbookSessionDataStoreQualifier

private val Context.roadbookSessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "roadbook_session_state")

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RoadbookDataModule {

    @Binds
    @Singleton
    abstract fun bindRoadbookRepository(
        dataStoreRoadbookRepository: DataStoreRoadbookRepository
    ): RoadbookRepository

    @Binds
    @Singleton
    abstract fun bindRoadbookSessionRepository(
        dataStoreRoadbookSessionRepository: DataStoreRoadbookSessionRepository
    ): RoadbookSessionRepository

    companion object {
        @Volatile
        private var roadbookDataStore: DataStore<PersistentRoute>? = null

        @Provides
        @Singleton
        @RoadbookDataStoreQualifier
        internal fun provideRoadbookDataStore(
            @ApplicationContext context: Context,
            @IoDispatcher ioDispatcher: CoroutineDispatcher,
            serializer: PersistenceRoadbookSerializer
        ): DataStore<PersistentRoute> {
            return roadbookDataStore ?: synchronized(this) {
                roadbookDataStore ?: DataStoreFactory.create(
                    serializer = serializer,
                    scope = CoroutineScope(ioDispatcher + SupervisorJob()),
                    produceFile = { context.dataStoreFile("active_roadbook.json") }
                ).also { roadbookDataStore = it }
            }
        }

        @Provides
        @Singleton
        @RoadbookSessionDataStoreQualifier
        internal fun provideRoadbookSessionDataStore(
            @ApplicationContext context: Context
        ): DataStore<Preferences> = context.roadbookSessionDataStore
    }
}
