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

package org.giste.roadbooknavigator.features.roadbook.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
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
import org.giste.roadbooknavigator.features.roadbook.data.DataStoreRoadbookRepository
import org.giste.roadbooknavigator.features.roadbook.data.DataStoreRoadbookSessionRepository
import org.giste.roadbooknavigator.features.roadbook.data.persistence.PersistenceRoadbookSerializer
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentRoute
import org.giste.roadbooknavigator.features.roadbook.domain.RoadbookRepository
import org.giste.roadbooknavigator.features.roadbook.domain.RoadbookSessionRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RoadbookDataStoreQualifier

@Module
@InstallIn(SingletonComponent::class)
abstract class RoadbookDataModule {

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
        @Provides
        @Singleton
        @RoadbookDataStoreQualifier
        fun provideRoadbookDataStore(
            @ApplicationContext context: Context,
            @IoDispatcher ioDispatcher: CoroutineDispatcher,
            serializer: PersistenceRoadbookSerializer
        ): DataStore<PersistentRoute> = DataStoreFactory.create(
            serializer = serializer,
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
            produceFile = { context.dataStoreFile("active_roadbook.json") }
        )
    }
}
