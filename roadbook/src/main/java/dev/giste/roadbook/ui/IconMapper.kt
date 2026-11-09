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

package dev.giste.roadbook.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import dev.giste.roadbook.domain.model.Icon
import dev.giste.roadbook.ui.icons.RoadbookIcons
import dev.giste.roadbook.ui.icons.cross.DangerLevel1
import dev.giste.roadbook.ui.icons.cross.DangerLevel2
import dev.giste.roadbook.ui.icons.cross.DangerLevel3
import dev.giste.roadbook.ui.icons.cross.fuelZone
import dev.giste.roadbook.ui.icons.cross.resetDistance
import dev.giste.roadbook.ui.icons.landmark.aboveBridge
import dev.giste.roadbook.ui.icons.landmark.church
import dev.giste.roadbook.ui.icons.landmark.fortCastle
import dev.giste.roadbook.ui.icons.landmark.house
import dev.giste.roadbook.ui.icons.landmark.trafficLight
import dev.giste.roadbook.ui.icons.landmark.tree
import dev.giste.roadbook.ui.icons.landmark.tunnel
import dev.giste.roadbook.ui.icons.landmark.underBridge
import dev.giste.roadbook.ui.icons.signs.alert
import dev.giste.roadbook.ui.icons.signs.doNotEnter
import dev.giste.roadbook.ui.icons.signs.roundabout
import dev.giste.roadbook.ui.icons.signs.stop
import dev.giste.roadbook.ui.icons.speed.limit20
import dev.giste.roadbook.ui.icons.speed.limit30
import dev.giste.roadbook.ui.icons.speed.limit40
import dev.giste.roadbook.ui.icons.speed.limit50
import dev.giste.roadbook.ui.icons.speed.limit60
import dev.giste.roadbook.ui.icons.speed.limit70
import dev.giste.roadbook.ui.icons.speed.limit80
import dev.giste.roadbook.ui.icons.speed.limit90
import dev.giste.roadbook.ui.icons.speed.limit100
import dev.giste.roadbook.ui.icons.speed.limit110
import dev.giste.roadbook.ui.icons.speed.limit120
import dev.giste.roadbook.ui.icons.speed.limit130
import dev.giste.roadbook.ui.icons.speed.limit140
import dev.giste.roadbook.ui.icons.speed.limit150
import dev.giste.roadbook.ui.icons.speed.limit10
import dev.giste.roadbook.ui.icons.terrain.river

internal object IconMapper {
    fun getIcon(type: Icon.IconType, onBackground: Color, background: Color): ImageVector? {
        return when (type) {
            // Cross
            Icon.IconType.Danger1 -> RoadbookIcons.Cross.DangerLevel1
            Icon.IconType.Danger2 -> RoadbookIcons.Cross.DangerLevel2
            Icon.IconType.Danger3 -> RoadbookIcons.Cross.DangerLevel3
            Icon.IconType.FuelZone -> RoadbookIcons.Cross.fuelZone(onBackground)
            Icon.IconType.ResetDistance -> RoadbookIcons.Cross.resetDistance(onBackground, background)
            // Landmark
            Icon.IconType.AboveBridge -> RoadbookIcons.Landmark.aboveBridge(onBackground, background)
            Icon.IconType.Church -> RoadbookIcons.Landmark.church(onBackground, background)
            Icon.IconType.FortCastle -> RoadbookIcons.Landmark.fortCastle(onBackground)
            Icon.IconType.House -> RoadbookIcons.Landmark.house(onBackground, background)
            Icon.IconType.TrafficLight -> RoadbookIcons.Landmark.trafficLight(onBackground, background)
            Icon.IconType.Tree -> RoadbookIcons.Landmark.tree(onBackground)
            Icon.IconType.Tunnel -> RoadbookIcons.Landmark.tunnel(onBackground)
            Icon.IconType.UnderBridge -> RoadbookIcons.Landmark.underBridge(onBackground, background)
            // Signs
            Icon.IconType.Alert -> RoadbookIcons.Signs.alert(onBackground)
            Icon.IconType.DoNotEnter -> RoadbookIcons.Signs.doNotEnter(onBackground)
            Icon.IconType.Roundabout -> RoadbookIcons.Signs.roundabout(onBackground)
            Icon.IconType.Stop -> RoadbookIcons.Signs.stop(onBackground)
            // Terrain
            Icon.IconType.RiverWater -> RoadbookIcons.Terrain.river(onBackground)
            // Speed
            Icon.IconType.SpeedLimit10 -> RoadbookIcons.Speed.limit10(onBackground)
            Icon.IconType.SpeedLimit20 -> RoadbookIcons.Speed.limit20(onBackground)
            Icon.IconType.SpeedLimit30 -> RoadbookIcons.Speed.limit30(onBackground)
            Icon.IconType.SpeedLimit40 -> RoadbookIcons.Speed.limit40(onBackground)
            Icon.IconType.SpeedLimit50 -> RoadbookIcons.Speed.limit50(onBackground)
            Icon.IconType.SpeedLimit60 -> RoadbookIcons.Speed.limit60(onBackground)
            Icon.IconType.SpeedLimit70 -> RoadbookIcons.Speed.limit70(onBackground)
            Icon.IconType.SpeedLimit80 -> RoadbookIcons.Speed.limit80(onBackground)
            Icon.IconType.SpeedLimit90 -> RoadbookIcons.Speed.limit90(onBackground)
            Icon.IconType.SpeedLimit100 -> RoadbookIcons.Speed.limit100(onBackground)
            Icon.IconType.SpeedLimit110 -> RoadbookIcons.Speed.limit110(onBackground)
            Icon.IconType.SpeedLimit120 -> RoadbookIcons.Speed.limit120(onBackground)
            Icon.IconType.SpeedLimit130 -> RoadbookIcons.Speed.limit130(onBackground)
            Icon.IconType.SpeedLimit140 -> RoadbookIcons.Speed.limit140(onBackground)
            Icon.IconType.SpeedLimit150 -> RoadbookIcons.Speed.limit150(onBackground)
            // Unknown
            Icon.IconType.Unknown -> null
        }
    }
}
