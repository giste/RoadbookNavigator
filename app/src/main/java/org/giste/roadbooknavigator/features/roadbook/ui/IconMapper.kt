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

package org.giste.roadbooknavigator.features.roadbook.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.giste.roadbooknavigator.features.roadbook.domain.Icon
import org.giste.roadbooknavigator.features.roadbook.ui.icons.RoadbookIcons
import org.giste.roadbooknavigator.features.roadbook.ui.icons.cross.DangerLevel1
import org.giste.roadbooknavigator.features.roadbook.ui.icons.cross.DangerLevel2
import org.giste.roadbooknavigator.features.roadbook.ui.icons.cross.DangerLevel3
import org.giste.roadbooknavigator.features.roadbook.ui.icons.cross.fuelZone
import org.giste.roadbooknavigator.features.roadbook.ui.icons.cross.resetDistance
import org.giste.roadbooknavigator.features.roadbook.ui.icons.landmark.underBridge
import org.giste.roadbooknavigator.features.roadbook.ui.icons.landmark.aboveBridge
import org.giste.roadbooknavigator.features.roadbook.ui.icons.landmark.fortCastle
import org.giste.roadbooknavigator.features.roadbook.ui.icons.landmark.house
import org.giste.roadbooknavigator.features.roadbook.ui.icons.landmark.trafficLight
import org.giste.roadbooknavigator.features.roadbook.ui.icons.landmark.tunnel
import org.giste.roadbooknavigator.features.roadbook.ui.icons.signs.Alert
import org.giste.roadbooknavigator.features.roadbook.ui.icons.signs.Stop
import org.giste.roadbooknavigator.features.roadbook.ui.icons.signs.roundabout
import org.giste.roadbooknavigator.features.roadbook.ui.icons.terrain.river

object IconMapper {
    fun getIcon(type: Icon.IconType, onSurface: Color, surface: Color): ImageVector? {
        return when (type) {
            Icon.IconType.Danger1 -> RoadbookIcons.Cross.DangerLevel1
            Icon.IconType.Danger2 -> RoadbookIcons.Cross.DangerLevel2
            Icon.IconType.Danger3 -> RoadbookIcons.Cross.DangerLevel3
            Icon.IconType.FuelZone -> RoadbookIcons.Cross.fuelZone(onSurface)
            Icon.IconType.ResetDistance -> RoadbookIcons.Cross.resetDistance(onSurface, surface)
            Icon.IconType.AboveBridge -> RoadbookIcons.Landmark.aboveBridge(onSurface, surface)
            Icon.IconType.FortCastle -> RoadbookIcons.Landmark.fortCastle(onSurface)
            Icon.IconType.House -> RoadbookIcons.Landmark.house(onSurface, surface)
            Icon.IconType.TrafficLight -> RoadbookIcons.Landmark.trafficLight(onSurface, surface)
            Icon.IconType.Tunnel -> RoadbookIcons.Landmark.tunnel(onSurface)
            Icon.IconType.UnderBridge -> RoadbookIcons.Landmark.underBridge(onSurface, surface)
            Icon.IconType.Alert -> RoadbookIcons.Signs.Alert
            Icon.IconType.Roundabout -> RoadbookIcons.Signs.roundabout(onSurface)
            Icon.IconType.Stop -> RoadbookIcons.Signs.Stop
            Icon.IconType.RiverWater -> RoadbookIcons.Terrain.river(onSurface)
            Icon.IconType.Unknown -> null
        }
    }
}
