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
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.JsonElement as KJsonElement

@Serializable(with = Rn2IconSerializer::class)
internal sealed class Rn2Icon : Rn2Element() {
    abstract val id: String
    abstract val name: String
    abstract val angle: Double?
    abstract val w: Double
    abstract val width: Double?
    abstract val height: Double?
    abstract val x: Double?
    abstract val y: Double?
    abstract val scaleX: Double?
    abstract val scaleY: Double?

    companion object {
        // Cross
        const val CROSS_DANGER_1_ID = "bffeadbd-116b-49a7-921e-20dff8deec4b"
        const val CROSS_DANGER_2_ID = "a6c80c12-49b1-4e68-a21f-a6d48ef0a0ed"
        const val CROSS_DANGER_3_ID = "fab72ac2-f809-4ddc-9a7a-c9a24768bb4e"
        const val CROSS_FUEL_ZONE_ID = "e5167bd4-314b-47d3-ba23-708182be76a9"
        const val CROSS_RESET_DISTANCE_ID = "308c7365-bc3f-451b-9e98-531e9015024f"

        // Landmark
        const val LANDMARK_ABOVE_BRIDGE_ID = "a49a0b2e-3be5-4659-8251-8205fd4e9571"
        const val LANDMARK_FORT_CASTLE_ID = "da5ec2a7-612a-411f-aeb2-d1f9514d3dc7"
        const val LANDMARK_HOUSE_ID = "3965bf45-97ee-4c6b-b087-0e128510c4e3"
        const val LANDMARK_TRAFFIC_LIGHT_ID = "1d752896-09fd-498d-b416-21f31a356be5"
        const val LANDMARK_TREE_ID = "75b46651-d46d-4655-b4ab-6a0dcff4fb38"
        const val LANDMARK_TUNNEL_ID = "0539c8e3-393b-4416-8002-b30700cf68de"
        const val LANDMARK_UNDER_BRIDGE_ID = "79f8c10f-d67b-4ba5-bf12-6a801ed79ed3"

        // Signs
        const val SIGN_ALERT_ID = "2598a2c0-6a8b-4dc5-8211-8ad64d986bde"
        const val SIGN_ROUNDABOUT_ID = "5d157992-6013-4bef-86cb-92fea891944c"
        const val SIGN_STOP_ID = "5a4ced4c-68e2-41d3-a1b4-9c8b86ec2109"

        // Terrain
        const val TERRAIN_RIVER_WATER_ID = "aabe9acd-ab1b-467d-9bbb-877bb0d0da23"

        // Speed Limits (Placeholders)
        const val SPEED_LIMIT_10_ID = "fabb3ac4-ce05-466d-b17a-5799d9be80c4"
        const val SPEED_LIMIT_20_ID = "396d67b9-1991-4c79-8e09-ede6e3834ce4"
        const val SPEED_LIMIT_30_ID = "33b6a49a-6796-44e3-a173-37437993e0e3"
        const val SPEED_LIMIT_40_ID = "0e5e32f1-0b62-467b-b3b9-9066de77df6f"
        const val SPEED_LIMIT_50_ID = "53cb2146-c977-41ec-8132-b648cf87fa78"
        const val SPEED_LIMIT_60_ID = "eda0a1dc-f08f-45a4-bf5a-ca994df19231"
        const val SPEED_LIMIT_70_ID = "c477f231-9416-4780-be47-8313e791d978"
        const val SPEED_LIMIT_80_ID = "3e46b793-17e7-4bf7-8c74-941bb24d0250"
        const val SPEED_LIMIT_90_ID = "2385115f-2f5f-4c93-bd95-2dc06e53a7cd"
        const val SPEED_LIMIT_100_ID = "f1fa5ad3-0a60-4deb-acfa-5505f43e3f7e"
        const val SPEED_LIMIT_110_ID = "c24339cc-a39b-4d5c-afca-1fe59c056fe5"
        const val SPEED_LIMIT_120_ID = "3cf3022b-6f9c-4c39-908a-ac7adc011957"
        const val SPEED_LIMIT_130_ID = "0f58cadc-ab7e-48aa-b3d9-324dcba6a715"
        const val SPEED_LIMIT_140_ID = "5f2d9b33-76e8-4902-919f-2ba12e9cdd54"
        const val SPEED_LIMIT_150_ID = "e96d634c-3c64-4192-a82a-ea0dcb92549a"
    }

    @Serializable
    internal data class Danger1(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Danger2(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Danger3(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class FuelZone(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class ResetDistance(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class AboveBridge(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class FortCastle(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class House(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class TrafficLight(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Tree(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Tunnel(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class UnderBridge(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Alert(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Roundabout(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Stop(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class RiverWater(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit10(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit20(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit30(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit40(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit50(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit60(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit70(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit80(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit90(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit100(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit110(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit120(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit130(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit140(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Limit150(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()

    @Serializable
    internal data class Unknown(
        override val id: String,
        override val name: String,
        override val angle: Double? = null,
        override val w: Double = 50.0,
        override val width: Double? = null,
        override val height: Double? = null,
        override val x: Double? = null,
        override val y: Double? = null,
        override val scaleX: Double? = null,
        override val scaleY: Double? = null
    ) : Rn2Icon()
}

internal object Rn2IconSerializer : JsonContentPolymorphicSerializer<Rn2Icon>(Rn2Icon::class) {
    override fun selectDeserializer(element: KJsonElement): DeserializationStrategy<Rn2Icon> {
        val json = element.jsonObject
        val id = json["id"]?.jsonPrimitive?.content

        val serializerById = when (id) {
            // Cross
            Rn2Icon.CROSS_DANGER_1_ID -> Rn2Icon.Danger1.serializer()
            Rn2Icon.CROSS_DANGER_2_ID -> Rn2Icon.Danger2.serializer()
            Rn2Icon.CROSS_DANGER_3_ID -> Rn2Icon.Danger3.serializer()
            Rn2Icon.CROSS_FUEL_ZONE_ID -> Rn2Icon.FuelZone.serializer()
            Rn2Icon.CROSS_RESET_DISTANCE_ID -> Rn2Icon.ResetDistance.serializer()
            // Landmark
            Rn2Icon.LANDMARK_ABOVE_BRIDGE_ID -> Rn2Icon.AboveBridge.serializer()
            Rn2Icon.LANDMARK_FORT_CASTLE_ID -> Rn2Icon.FortCastle.serializer()
            Rn2Icon.LANDMARK_HOUSE_ID -> Rn2Icon.House.serializer()
            Rn2Icon.LANDMARK_TRAFFIC_LIGHT_ID -> Rn2Icon.TrafficLight.serializer()
            Rn2Icon.LANDMARK_TREE_ID -> Rn2Icon.Tree.serializer()
            Rn2Icon.LANDMARK_TUNNEL_ID -> Rn2Icon.Tunnel.serializer()
            Rn2Icon.LANDMARK_UNDER_BRIDGE_ID -> Rn2Icon.UnderBridge.serializer()
            // Signs
            Rn2Icon.SIGN_ALERT_ID -> Rn2Icon.Alert.serializer()
            Rn2Icon.SIGN_ROUNDABOUT_ID -> Rn2Icon.Roundabout.serializer()
            Rn2Icon.SIGN_STOP_ID -> Rn2Icon.Stop.serializer()
            // Terrain
            Rn2Icon.TERRAIN_RIVER_WATER_ID -> Rn2Icon.RiverWater.serializer()
            // Speed Limits
            Rn2Icon.SPEED_LIMIT_10_ID -> Rn2Icon.Limit10.serializer()
            Rn2Icon.SPEED_LIMIT_20_ID -> Rn2Icon.Limit20.serializer()
            Rn2Icon.SPEED_LIMIT_30_ID -> Rn2Icon.Limit30.serializer()
            Rn2Icon.SPEED_LIMIT_40_ID -> Rn2Icon.Limit40.serializer()
            Rn2Icon.SPEED_LIMIT_50_ID -> Rn2Icon.Limit50.serializer()
            Rn2Icon.SPEED_LIMIT_60_ID -> Rn2Icon.Limit60.serializer()
            Rn2Icon.SPEED_LIMIT_70_ID -> Rn2Icon.Limit70.serializer()
            Rn2Icon.SPEED_LIMIT_80_ID -> Rn2Icon.Limit80.serializer()
            Rn2Icon.SPEED_LIMIT_90_ID -> Rn2Icon.Limit90.serializer()
            Rn2Icon.SPEED_LIMIT_100_ID -> Rn2Icon.Limit100.serializer()
            Rn2Icon.SPEED_LIMIT_110_ID -> Rn2Icon.Limit110.serializer()
            Rn2Icon.SPEED_LIMIT_120_ID -> Rn2Icon.Limit120.serializer()
            Rn2Icon.SPEED_LIMIT_130_ID -> Rn2Icon.Limit130.serializer()
            Rn2Icon.SPEED_LIMIT_140_ID -> Rn2Icon.Limit140.serializer()
            Rn2Icon.SPEED_LIMIT_150_ID -> Rn2Icon.Limit150.serializer()
            else -> null
        }

        if (serializerById != null) return serializerById

        val name = json["name"]?.jsonPrimitive?.content
        return when (name) {
            "Danger Level 1" -> Rn2Icon.Danger1.serializer()
            "Danger Level 2" -> Rn2Icon.Danger2.serializer()
            "Danger Level 3" -> Rn2Icon.Danger3.serializer()
            "Fuel Zone" -> Rn2Icon.FuelZone.serializer()
            "Reset to Distance to Zero" -> Rn2Icon.ResetDistance.serializer()
            "Above Bridge" -> Rn2Icon.AboveBridge.serializer()
            "Fort / Castle" -> Rn2Icon.FortCastle.serializer()
            "House" -> Rn2Icon.House.serializer()
            "Traffic Light" -> Rn2Icon.TrafficLight.serializer()
            "Tree" -> Rn2Icon.Tree.serializer()
            "Tunnel" -> Rn2Icon.Tunnel.serializer()
            "Under Bridge" -> Rn2Icon.UnderBridge.serializer()
            "Alert" -> Rn2Icon.Alert.serializer()
            "Roundabout" -> Rn2Icon.Roundabout.serializer()
            "Stop" -> Rn2Icon.Stop.serializer()
            "River / Water" -> Rn2Icon.RiverWater.serializer()
            // Speed Limits
            "Speed Limit 10" -> Rn2Icon.Limit10.serializer()
            "Speed Limit 20" -> Rn2Icon.Limit20.serializer()
            "Speed Limit 30" -> Rn2Icon.Limit30.serializer()
            "Speed Limit 40" -> Rn2Icon.Limit40.serializer()
            "Speed Limit 50" -> Rn2Icon.Limit50.serializer()
            "Speed Limit 60" -> Rn2Icon.Limit60.serializer()
            "Speed Limit 70" -> Rn2Icon.Limit70.serializer()
            "Speed Limit 80" -> Rn2Icon.Limit80.serializer()
            "Speed Limit 90" -> Rn2Icon.Limit90.serializer()
            "Speed Limit 100" -> Rn2Icon.Limit100.serializer()
            "Speed Limit 110" -> Rn2Icon.Limit110.serializer()
            "Speed Limit 120" -> Rn2Icon.Limit120.serializer()
            "Speed Limit 130" -> Rn2Icon.Limit130.serializer()
            "Speed Limit 140" -> Rn2Icon.Limit140.serializer()
            "Speed Limit 150" -> Rn2Icon.Limit150.serializer()
            else -> Rn2Icon.Unknown.serializer()
        }
    }
}
