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

package org.giste.roadbooknavigator.features.roadbook.data.dto

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement as KJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = JsonElementSerializer::class)
sealed class JsonElement

object JsonElementSerializer : JsonContentPolymorphicSerializer<JsonElement>(JsonElement::class) {
    override fun selectDeserializer(element: KJsonElement): DeserializationStrategy<JsonElement> {
        val jsonObject = element.jsonObject
        return when (val type = jsonObject["type"]!!.jsonPrimitive.content) {
            "Road" -> JsonRoad.serializer()
            "Track" -> JsonTrack.serializer()
            "Text" -> JsonText.serializer()
            "Icon" -> JsonIconSerializer
            else -> throw SerializationException("Unknown JsonElement type: $type")
        }
    }
}
