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

import kotlinx.coroutines.CoroutineDispatcher
import org.giste.roadbooknavigator.features.roadbook.domain.RoadbookRepository
import org.giste.roadbooknavigator.features.roadbook.domain.Route
import java.io.InputStream
import javax.inject.Inject
import kotlinx.coroutines.withContext
import org.giste.roadbooknavigator.core.di.IoDispatcher

/**
 * Implementation of [RoadbookRepository] that handles .rn2 files.
 */
class RoadbookRepositoryImpl @Inject constructor(
    private val mapper: Rn2Mapper,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RoadbookRepository {

    override suspend fun loadRoadbook(inputStream: InputStream): Result<Route> = withContext(ioDispatcher) {
        runCatching {
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            mapper.mapToDomain(jsonString)
        }
    }
}
