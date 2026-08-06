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

package org.giste.roadbook.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.giste.roadbook.domain.model.Icon
import org.giste.roadbook.ui.icons.RoadbookIcons
import org.giste.roadbook.ui.icons.cross.DangerLevel1
import org.giste.roadbook.ui.icons.cross.DangerLevel2
import org.giste.roadbook.ui.icons.cross.DangerLevel3
import org.giste.roadbook.ui.icons.cross.fuelZone
import org.giste.roadbook.ui.icons.cross.resetDistance
import org.giste.roadbook.ui.icons.landmark.aboveBridge
import org.giste.roadbook.ui.icons.landmark.fortCastle
import org.giste.roadbook.ui.icons.landmark.house
import org.giste.roadbook.ui.icons.landmark.trafficLight
import org.giste.roadbook.ui.icons.landmark.tree
import org.giste.roadbook.ui.icons.landmark.tunnel
import org.giste.roadbook.ui.icons.landmark.underBridge
import org.giste.roadbook.ui.icons.signs.Alert
import org.giste.roadbook.ui.icons.signs.Stop
import org.giste.roadbook.ui.icons.signs.roundabout
import org.giste.roadbook.ui.icons.speed.Limit10
import org.giste.roadbook.ui.icons.speed.Limit20
import org.giste.roadbook.ui.icons.speed.Limit30
import org.giste.roadbook.ui.icons.speed.Limit40
import org.giste.roadbook.ui.icons.speed.Limit50
import org.giste.roadbook.ui.icons.speed.Limit60
import org.giste.roadbook.ui.icons.speed.Limit70
import org.giste.roadbook.ui.icons.speed.Limit80
import org.giste.roadbook.ui.icons.speed.Limit90
import org.giste.roadbook.ui.icons.speed.Limit100
import org.giste.roadbook.ui.icons.speed.Limit110
import org.giste.roadbook.ui.icons.speed.Limit120
import org.giste.roadbook.ui.icons.speed.Limit130
import org.giste.roadbook.ui.icons.speed.Limit140
import org.giste.roadbook.ui.icons.speed.Limit150
import org.giste.roadbook.ui.icons.terrain.river

internal object IconMapper {
    fun getIcon(type: Icon.IconType, onSurface: Color, surface: Color): ImageVector? {
        return when (type) {
            // Cross
            Icon.IconType.Danger1 -> RoadbookIcons.Cross.DangerLevel1
            Icon.IconType.Danger2 -> RoadbookIcons.Cross.DangerLevel2
            Icon.IconType.Danger3 -> RoadbookIcons.Cross.DangerLevel3
            Icon.IconType.FuelZone -> RoadbookIcons.Cross.fuelZone(onSurface)
            Icon.IconType.ResetDistance -> RoadbookIcons.Cross.resetDistance(onSurface, surface)
            // Landmark
            Icon.IconType.AboveBridge -> RoadbookIcons.Landmark.aboveBridge(onSurface, surface)
            Icon.IconType.FortCastle -> RoadbookIcons.Landmark.fortCastle(onSurface)
            Icon.IconType.House -> RoadbookIcons.Landmark.house(onSurface, surface)
            Icon.IconType.TrafficLight -> RoadbookIcons.Landmark.trafficLight(onSurface, surface)
            Icon.IconType.Tree -> RoadbookIcons.Landmark.tree(onSurface)
            Icon.IconType.Tunnel -> RoadbookIcons.Landmark.tunnel(onSurface)
            Icon.IconType.UnderBridge -> RoadbookIcons.Landmark.underBridge(onSurface, surface)
            // Signs
            Icon.IconType.Alert -> RoadbookIcons.Signs.Alert
            Icon.IconType.Roundabout -> RoadbookIcons.Signs.roundabout(onSurface)
            Icon.IconType.Stop -> RoadbookIcons.Signs.Stop
            // Terrain
            Icon.IconType.RiverWater -> RoadbookIcons.Terrain.river(onSurface)
            // Speed
            Icon.IconType.SpeedLimit10 -> RoadbookIcons.Speed.Limit10
            Icon.IconType.SpeedLimit20 -> RoadbookIcons.Speed.Limit20
            Icon.IconType.SpeedLimit30 -> RoadbookIcons.Speed.Limit30
            Icon.IconType.SpeedLimit40 -> RoadbookIcons.Speed.Limit40
            Icon.IconType.SpeedLimit50 -> RoadbookIcons.Speed.Limit50
            Icon.IconType.SpeedLimit60 -> RoadbookIcons.Speed.Limit60
            Icon.IconType.SpeedLimit70 -> RoadbookIcons.Speed.Limit70
            Icon.IconType.SpeedLimit80 -> RoadbookIcons.Speed.Limit80
            Icon.IconType.SpeedLimit90 -> RoadbookIcons.Speed.Limit90
            Icon.IconType.SpeedLimit100 -> RoadbookIcons.Speed.Limit100
            Icon.IconType.SpeedLimit110 -> RoadbookIcons.Speed.Limit110
            Icon.IconType.SpeedLimit120 -> RoadbookIcons.Speed.Limit120
            Icon.IconType.SpeedLimit130 -> RoadbookIcons.Speed.Limit130
            Icon.IconType.SpeedLimit140 -> RoadbookIcons.Speed.Limit140
            Icon.IconType.SpeedLimit150 -> RoadbookIcons.Speed.Limit150
            // Unknown
            Icon.IconType.Unknown -> null
        }
    }
}
