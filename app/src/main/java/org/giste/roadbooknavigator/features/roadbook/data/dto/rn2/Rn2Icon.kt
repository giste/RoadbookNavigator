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
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement as KJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = Rn2IconSerializer::class)
sealed class Rn2Icon : Rn2Element() {
    abstract val id: String
    abstract val angle: Double?
    abstract val width: Double?
    abstract val height: Double?
    abstract val x: Double?
    abstract val y: Double?
    abstract val scaleX: Double?
    abstract val scaleY: Double?

    companion object {
        const val CROSS_DANGER_1_ID = "bffeadbd-116b-49a7-921e-20dff8deec4b"
        const val CROSS_DANGER_2_ID = "a6c80c12-49b1-4e68-a21f-a6d48ef0a0ed"
        const val CROSS_DANGER_3_ID = "fab72ac2-f809-4ddc-9a7a-c9a24768bb4e"
        const val CROSS_FUEL_ZONE_ID = "e5167bd4-314b-47d3-ba23-708182be76a9"
        const val CROSS_RESET_DISTANCE_ID = "308c7365-bc3f-451b-9e98-531e9015024f"
        const val LANDMARK_ABOVE_BRIDGE_ID = "a49a0b2e-3be5-4659-8251-8205fd4e9571"
        const val LANDMARK_FORT_CASTLE_ID = "da5ec2a7-612a-411f-aeb2-d1f9514d3dc7"
        const val LANDMARK_HOUSE_ID = "3965bf45-97ee-4c6b-b087-0e128510c4e3"
        const val LANDMARK_TRAFFIC_LIGHT_ID = "1d752896-09fd-498d-b416-21f31a356be5"
        const val LANDMARK_TUNNEL_ID = "0539c8e3-393b-4416-8002-b30700cf68de"
        const val LANDMARK_UNDER_BRIDGE_ID = "79f8c10f-d67b-4ba5-bf12-6a801ed79ed3"
        const val SIGN_ALERT_ID = "2598a2c0-6a8b-4dc5-8211-8ad64d986bde"
        const val SIGN_ROUNDABOUT_ID = "5d157992-6013-4bef-86cb-92fea891944c"
        const val SIGN_STOP_ID = "5a4ced4c-68e2-41d3-a1b4-9c8b86ec2109"
        const val TERRAIN_RIVER_WATER_ID = "aabe9acd-ab1b-467d-9bbb-877bb0d0da23"
    }

    @Serializable data class Danger1(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class Danger2(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class Danger3(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class FuelZone(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class ResetDistance(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class AboveBridge(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class FortCastle(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class House(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class TrafficLight(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class Tunnel(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class UnderBridge(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class Alert(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class Roundabout(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class Stop(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class RiverWater(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
    @Serializable data class Unknown(override val id: String, override val angle: Double? = null, override val width: Double? = null, override val height: Double? = null, override val x: Double? = null, override val y: Double? = null, override val scaleX: Double? = null, override val scaleY: Double? = null) : Rn2Icon()
}

object Rn2IconSerializer : JsonContentPolymorphicSerializer<Rn2Icon>(Rn2Icon::class) {
    override fun selectDeserializer(element: KJsonElement): DeserializationStrategy<Rn2Icon> {
        val id = element.jsonObject["id"]!!.jsonPrimitive.content
        return when (id) {
            Rn2Icon.CROSS_DANGER_1_ID -> Rn2Icon.Danger1.serializer()
            Rn2Icon.CROSS_DANGER_2_ID -> Rn2Icon.Danger2.serializer()
            Rn2Icon.CROSS_DANGER_3_ID -> Rn2Icon.Danger3.serializer()
            Rn2Icon.CROSS_FUEL_ZONE_ID -> Rn2Icon.FuelZone.serializer()
            Rn2Icon.CROSS_RESET_DISTANCE_ID -> Rn2Icon.ResetDistance.serializer()
            Rn2Icon.LANDMARK_ABOVE_BRIDGE_ID -> Rn2Icon.AboveBridge.serializer()
            Rn2Icon.LANDMARK_FORT_CASTLE_ID -> Rn2Icon.FortCastle.serializer()
            Rn2Icon.LANDMARK_HOUSE_ID -> Rn2Icon.House.serializer()
            Rn2Icon.LANDMARK_TRAFFIC_LIGHT_ID -> Rn2Icon.TrafficLight.serializer()
            Rn2Icon.LANDMARK_TUNNEL_ID -> Rn2Icon.Tunnel.serializer()
            Rn2Icon.LANDMARK_UNDER_BRIDGE_ID -> Rn2Icon.UnderBridge.serializer()
            Rn2Icon.SIGN_ALERT_ID -> Rn2Icon.Alert.serializer()
            Rn2Icon.SIGN_ROUNDABOUT_ID -> Rn2Icon.Roundabout.serializer()
            Rn2Icon.SIGN_STOP_ID -> Rn2Icon.Stop.serializer()
            Rn2Icon.TERRAIN_RIVER_WATER_ID -> Rn2Icon.RiverWater.serializer()
            else -> Rn2Icon.Unknown.serializer()
        }
    }
}
