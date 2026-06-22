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

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.giste.roadbooknavigator.features.roadbook.data.dto.persistence.PersistentRoute
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * Serializer for [PersistentRoute] using kotlinx.serialization JSON.
 */
class RoadbookSerializer @Inject constructor() : Serializer<PersistentRoute> {

    override val defaultValue: PersistentRoute = PersistentRoute.empty

    override suspend fun readFrom(input: InputStream): PersistentRoute {
        return try {
            Json.decodeFromString(
                deserializer = PersistentRoute.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (_: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: PersistentRoute, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = PersistentRoute.serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}
