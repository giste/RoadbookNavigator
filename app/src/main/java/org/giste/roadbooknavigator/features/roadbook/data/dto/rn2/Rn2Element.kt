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

package org.giste.roadbooknavigator.features.roadbook.data.dto.rn2

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement as KJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = Rn2ElementSerializer::class)
sealed class Rn2Element

object Rn2ElementSerializer : JsonContentPolymorphicSerializer<Rn2Element>(Rn2Element::class) {
    override fun selectDeserializer(element: KJsonElement): DeserializationStrategy<Rn2Element> {
        val jsonObject = element.jsonObject
        return when (val type = jsonObject["type"]!!.jsonPrimitive.content) {
            "Road" -> Rn2Road.serializer()
            "Track" -> Rn2Track.serializer()
            "Text" -> Rn2Text.serializer()
            "Icon" -> Rn2IconSerializer
            else -> throw SerializationException("Unknown JsonElement type: $type")
        }
    }
}
