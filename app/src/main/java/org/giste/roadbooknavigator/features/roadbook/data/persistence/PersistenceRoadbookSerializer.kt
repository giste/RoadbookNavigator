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

package org.giste.roadbooknavigator.features.roadbook.data.persistence

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.giste.roadbooknavigator.core.util.logger
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentElement
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentIcon
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentRoad
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentRoute
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentText
import org.giste.roadbooknavigator.features.roadbook.data.persistence.dto.PersistentTrack
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * Serializer for [PersistentRoute] using kotlinx.serialization JSON.
 */
class PersistenceRoadbookSerializer @Inject constructor() : Serializer<PersistentRoute> {

    private val json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        isLenient = true
        classDiscriminator = "#type"
        serializersModule = SerializersModule {
            polymorphic(PersistentElement::class) {
                subclass(PersistentTrack::class)
                subclass(PersistentRoad::class)
                subclass(PersistentIcon::class)
                subclass(PersistentText::class)
            }
        }
    }

    override val defaultValue: PersistentRoute = PersistentRoute.empty

    override suspend fun readFrom(input: InputStream): PersistentRoute {
        return try {
            json.decodeFromString(
                deserializer = PersistentRoute.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            logger.e(e, "PersistenceRoadbookSerializer: Failed to read persistent roadbook, using default")
            defaultValue
        }
    }

    override suspend fun writeTo(t: PersistentRoute, output: OutputStream) {
        withContext(Dispatchers.IO) {
            try {
                output.write(
                    json.encodeToString(
                        serializer = PersistentRoute.serializer(),
                        value = t
                    ).encodeToByteArray()
                )
            } catch (e: Exception) {
                logger.e(e, "PersistenceRoadbookSerializer: Failed to write persistent roadbook")
                throw e
            }
        }
    }
}
