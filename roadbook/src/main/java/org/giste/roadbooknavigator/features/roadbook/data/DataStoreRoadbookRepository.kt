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

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import org.giste.roadbooknavigator.core.di.IoDispatcher
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.data.persistence.PersistenceMapper
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentRoute
import org.giste.roadbooknavigator.features.roadbook.data.rn2.Rn2Mapper
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import org.giste.roadbooknavigator.features.roadbook.domain.repository.RoadbookRepository
import java.io.InputStream
import javax.inject.Inject

/**
 * Implementation of [RoadbookRepository] that handles .rn2 files and internal caching
 * using Jetpack DataStore.
 */
internal class DataStoreRoadbookRepository @Inject constructor(
    private val mapper: Rn2Mapper,
    private val persistenceMapper: PersistenceMapper,
    @param:RoadbookDataStoreQualifier private val dataStore: DataStore<PersistentRoute>,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val logger: Logger
) : RoadbookRepository {

    override val activeRoadbook: Flow<Route?> = dataStore.data.map { persistentRoute ->
        if (persistentRoute.name.isEmpty() && persistentRoute.waypoints.isEmpty()) {
            null
        } else {
            persistenceMapper.toDomain(persistentRoute)
        }
    }.onEach { route ->
        if (route != null) {
            logger.d("DataStoreRoadbookRepository: Loaded active roadbook: %s (%d waypoints)", route.name, route.waypoints.size)
        } else {
            logger.v("DataStoreRoadbookRepository: No active roadbook loaded")
        }
    }

    override suspend fun processNewRoadbook(inputStream: InputStream): Result<Route> =
        withContext(ioDispatcher) {
            logger.i("DataStoreRoadbookRepository: Processing new roadbook from input stream")
            runCatching {
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                logger.v("DataStoreRoadbookRepository: JSON string read (size: %d bytes)", jsonString.length)
                
                val route = mapper.mapToDomain(jsonString)
                logger.d("DataStoreRoadbookRepository: Domain mapping complete for: %s", route.name)

                val persistentRoute = persistenceMapper.toPersistent(route)
                dataStore.updateData { persistentRoute }
                logger.i("DataStoreRoadbookRepository: Roadbook %s persisted successfully", route.name)

                route
            }.onFailure { error ->
                logger.e(error, "DataStoreRoadbookRepository: Failed to process new roadbook")
            }
        }
}
