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
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.giste.roadbooknavigator.core.di.IoDispatcher
import org.giste.roadbooknavigator.features.roadbook.data.dto.persistence.PersistentRoute
import org.giste.roadbooknavigator.features.roadbook.domain.RoadbookRepository
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import java.io.File
import java.io.InputStream
import javax.inject.Inject

/**
 * Implementation of [RoadbookRepository] that handles .rn2 files and internal caching.
 */
class RoadbookRepositoryImpl @Inject constructor(
    private val mapper: Rn2Mapper,
    private val persistenceMapper: PersistenceMapper,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @param:ApplicationContext private val context: Context
) : RoadbookRepository {

    private val _activeRoadbook = MutableStateFlow<Route?>(null)
    override val activeRoadbook: Flow<Route?> = _activeRoadbook.asStateFlow()

    private val cacheFile: File by lazy {
        File(context.filesDir, "active_roadbook.json")
    }

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = false
    }

    override suspend fun loadActiveRoadbook(): Result<Route?> = withContext(ioDispatcher) {
        runCatching {
            if (!cacheFile.exists()) return@runCatching null

            val jsonString = cacheFile.readText()
            val persistentRoute = json.decodeFromString<PersistentRoute>(jsonString)
            val route = persistenceMapper.toDomain(persistentRoute)
            _activeRoadbook.value = route
            route
        }
    }

    override suspend fun processNewRoadbook(inputStream: InputStream): Result<Route> = withContext(ioDispatcher) {
        runCatching {
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val route = mapper.mapToDomain(jsonString)
            
            // Save to internal storage
            val persistentRoute = persistenceMapper.toPersistent(route)
            val cachedJson = json.encodeToString(persistentRoute)
            cacheFile.writeText(cachedJson)
            
            // Emit new state
            _activeRoadbook.value = route
            route
        }
    }
}
