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

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Rn2IconSerializerTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `serializer should map Speed Limit IDs correctly`() {
        val speedLimitMappings = mapOf(
            Rn2Icon.SPEED_LIMIT_10_ID to Rn2Icon.Limit10::class,
            Rn2Icon.SPEED_LIMIT_20_ID to Rn2Icon.Limit20::class,
            Rn2Icon.SPEED_LIMIT_30_ID to Rn2Icon.Limit30::class,
            Rn2Icon.SPEED_LIMIT_40_ID to Rn2Icon.Limit40::class,
            Rn2Icon.SPEED_LIMIT_50_ID to Rn2Icon.Limit50::class,
            Rn2Icon.SPEED_LIMIT_60_ID to Rn2Icon.Limit60::class,
            Rn2Icon.SPEED_LIMIT_70_ID to Rn2Icon.Limit70::class,
            Rn2Icon.SPEED_LIMIT_80_ID to Rn2Icon.Limit80::class,
            Rn2Icon.SPEED_LIMIT_90_ID to Rn2Icon.Limit90::class,
            Rn2Icon.SPEED_LIMIT_100_ID to Rn2Icon.Limit100::class,
            Rn2Icon.SPEED_LIMIT_110_ID to Rn2Icon.Limit110::class,
            Rn2Icon.SPEED_LIMIT_120_ID to Rn2Icon.Limit120::class,
            Rn2Icon.SPEED_LIMIT_130_ID to Rn2Icon.Limit130::class,
            Rn2Icon.SPEED_LIMIT_140_ID to Rn2Icon.Limit140::class,
            // 150 is still a placeholder in your last edit
            Rn2Icon.SPEED_LIMIT_150_ID to Rn2Icon.Limit150::class
        )

        speedLimitMappings.forEach { (id, expectedClass) ->
            val jsonElement = buildJsonObject {
                put("id", id)
                put("name", "Irrelevant")
                put("type", "Icon")
            }
            
            val icon = json.decodeFromJsonElement(Rn2IconSerializer, jsonElement)
            
            assertTrue(
                "ID $id should map to ${expectedClass.simpleName} but was ${icon::class.simpleName}",
                expectedClass.isInstance(icon)
            )
            assertEquals(id, icon.id)
        }
    }

    @Test
    fun `serializer should map Speed Limit names correctly as fallback`() {
        for (limit in listOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150)) {
            val name = "Speed Limit $limit"
            val jsonElement = buildJsonObject {
                put("id", "unknown-id")
                put("name", name)
                put("type", "Icon")
            }

            val icon = json.decodeFromJsonElement(Rn2IconSerializer, jsonElement)
            val expectedClassName = "Limit$limit"
            
            assertEquals(
                "Name '$name' should map to $expectedClassName but was ${icon::class.simpleName}",
                expectedClassName,
                icon::class.simpleName
            )
        }
    }
}
