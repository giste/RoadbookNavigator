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

package org.giste.roadbook.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.BindsOptionalOf
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.giste.roadbook.data.persistence.PersistenceRoadbookSerializer
import org.giste.roadbook.data.persistence.dto.PersistentRoute
import org.giste.roadbook.data.util.AndroidRoadbookLogger
import org.giste.roadbook.AppRoadbookIoDispatcher
import org.giste.roadbook.AppRoadbookLogger
import org.giste.roadbook.domain.repository.RoadbookRepository
import org.giste.roadbook.domain.repository.RoadbookSessionRepository
import org.giste.roadbook.RoadbookLogger
import java.util.Optional
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class RoadbookIoDispatcher

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

    @BindsOptionalOf
    @AppRoadbookIoDispatcher
    internal abstract fun optionalIoDispatcher(): CoroutineDispatcher

    @BindsOptionalOf
    @AppRoadbookLogger
    internal abstract fun optionalRoadbookLogger(): RoadbookLogger

    companion object {
        @Provides
        @Singleton
        @RoadbookIoDispatcher
        internal fun provideIoDispatcher(
            @AppRoadbookIoDispatcher optionalDispatcher: Optional<CoroutineDispatcher>
        ): CoroutineDispatcher = if (optionalDispatcher.isPresent) optionalDispatcher.get() else Dispatchers.IO

        @Provides
        @Singleton
        internal fun provideRoadbookLogger(
            @AppRoadbookLogger optionalLogger: Optional<RoadbookLogger>,
            androidRoadbookLogger: AndroidRoadbookLogger
        ): RoadbookLogger = if (optionalLogger.isPresent) optionalLogger.get() else androidRoadbookLogger

        @Volatile
        private var roadbookDataStore: DataStore<PersistentRoute>? = null

        @Provides
        @Singleton
        @RoadbookDataStoreQualifier
        internal fun provideRoadbookDataStore(
            @ApplicationContext context: Context,
            @RoadbookIoDispatcher ioDispatcher: CoroutineDispatcher,
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
