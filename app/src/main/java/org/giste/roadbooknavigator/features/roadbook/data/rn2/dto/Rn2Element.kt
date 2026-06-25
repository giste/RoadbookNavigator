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

package org.giste.roadbooknavigator.features.roadbook.data.rn2.dto

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.JsonElement as KJsonElement

@Serializable(with = Rn2ElementSerializer::class)
sealed class Rn2Element

object Rn2ElementSerializer : JsonContentPolymorphicSerializer<Rn2Element>(Rn2Element::class) {
    override fun selectDeserializer(element: KJsonElement): DeserializationStrategy<Rn2Element> {
        val jsonObject = element.jsonObject
        val type = jsonObject["type"]?.jsonPrimitive?.content
            ?: throw SerializationException("Missing 'type' field in Rn2Element")

        return when (type) {
            "Road" -> Rn2Road.serializer()
            "Track" -> Rn2Track.serializer()
            "Text" -> Rn2Text.serializer()
            "Icon" -> Rn2IconSerializer
            else -> throw SerializationException("Unknown Rn2Element type: $type")
        }
    }
}

@Serializable
data class Rn2Road(
    val start: Rn2Point? = null,
    val end: Rn2Point? = null,
    val typeId: Int? = null,
    val handles: List<Rn2Point> = emptyList(),
) : Rn2Element()

@Serializable
data class Rn2Track(
    val roadIn: Rn2Road,
    val roadOut: Rn2Road,
) : Rn2Element()

@Serializable
data class Rn2Text(
    val id: String? = null,
    val text: String,
    val fontSize: Int,
    val lineHeight: Double? = null,
    val width: Double,
    val height: Double,
    val maxWidth: Double? = null,
    val maxHeight: Double? = null,
    val x: Double,
    val y: Double,
) : Rn2Element()

@Serializable
data class Rn2Point(
    val x: Double,
    val y: Double
)
