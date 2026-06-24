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

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.roadbook.domain.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.Icon
import org.giste.roadbooknavigator.features.roadbook.domain.Point
import org.giste.roadbooknavigator.features.roadbook.domain.Road
import org.giste.roadbooknavigator.features.roadbook.domain.Track
import org.giste.roadbooknavigator.features.roadbook.domain.Waypoint
import org.giste.roadbooknavigator.features.roadbook.domain.Text as TulipText

@Composable
fun WaypointItem(
    waypoint: Waypoint,
    shortDistanceThreshold: Long,
    onSetPartialClick: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val isHighDanger = waypoint.dangerLevel == Waypoint.DangerLevel.HIGH
    val borderWidth = if (isHighDanger) RoadbookNavigatorTheme.dimensions.dangerHighThickness else RoadbookNavigatorTheme.dimensions.sectionBorder
    val borderColor = if (isHighDanger) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(535f / 135)
    ) {
        if (isHighDanger) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(width = borderWidth, color = borderColor)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = if (isHighDanger) 0.dp else borderWidth,
                    color = borderColor
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First part: Distance info and number
            DistanceSection(
                waypoint = waypoint,
                shortDistanceThreshold = shortDistanceThreshold,
                onSetPartialClick = onSetPartialClick,
                modifier = Modifier
                    .weight(weight = 1f, fill = true)
                    .fillMaxHeight()
            )

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                color = MaterialTheme.colorScheme.onSurface,
                thickness = RoadbookNavigatorTheme.dimensions.sectionBorder
            )

            // Second part: Tulip elements
            TulipSection(
                waypoint = waypoint,
                modifier = Modifier.fillMaxHeight()
            )

            VerticalDivider(
                modifier = Modifier.fillMaxHeight(),
                color = MaterialTheme.colorScheme.onSurface,
                thickness = RoadbookNavigatorTheme.dimensions.sectionBorder
            )

            // Third part: Notes elements
            NotesSection(
                waypoint = waypoint,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    name = "Light Mode",
    device = "spec:width=1920px,height=1200px,dpi=280,orientation=portrait",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Preview(
    name = "Dark Mode",
    device = "spec:width=1920px,height=1200px,dpi=280,orientation=portrait",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
fun WaypointItemPreview() {
    val waypointWithLowDangerAndRoadTypes = Waypoint(
        number = 1,
        coordinates = Coordinates(40.0, -3.0),
        distance = Distance(1200),
        distanceFromPrevious = Distance(1200),
        reset = false,
        dangerLevel = Waypoint.DangerLevel.LOW,
        tulipElements = listOf(
            Road(null, Point(-60.0, 0.0), Road.RoadType.Track),
            Road(null, Point(-40.0, -40.0), Road.RoadType.SmallTrack),
            Road(null, Point(-40.0, 40.0), Road.RoadType.LowVisibleTrack),
            Road(null, Point(40.0, -40.0), Road.RoadType.OffTrack),
            Road(null, Point(60.0, 0.0), Road.RoadType.TarmacRoad),
            Road(null, Point(40.0, 40.0), Road.RoadType.DualCarriageway),
            Track(
                roadIn = Road(null, Point(0.0, 35.0)),
                roadOut = Road(null, Point(0.0, -55.0))
            ),
        ),
        notesElements = listOf(
            Icon(
                type = Icon.IconType.Danger1,
                center = Point(100.0, 67.5),
                width = 80,
                height = 80,
                angle = 0
            )
        )
    )

    val waypointWithMediumDangerAndText = Waypoint(
        number = 3,
        coordinates = Coordinates(40.0, -3.0),
        distance = Distance(3500),
        distanceFromPrevious = Distance(1000),
        dangerLevel = Waypoint.DangerLevel.MEDIUM,
        reset = true,
        tulipElements = listOf(
            Track(
                roadIn = Road(null, Point(0.0, 40.0)),
                roadOut = Road(null, Point(0.0, -40.0))
            ),
            TulipText(
                text = "KM 1.2",
                center = Point(150.0, 100.0),
                fontSize = 12,
                lineHeight = 1.0,
                width = 40.0,
                height = 20.0,
                maxWidth = 180.0,
                maxHeight = 100.0,
            )
        ),
        notesElements = listOf(
            TulipText(
                text = "Attention!",
                center = Point(100.0, 40.0),
                fontSize = 14,
                lineHeight = 1.0,
                width = 100.0,
                height = 20.0,
                maxWidth = 180.0,
                maxHeight = 100.0,
            ),
            Icon(
                type = Icon.IconType.Danger2,
                center = Point(100.0, 90.0),
                width = 40,
                height = 40,
                angle = 0
            )
        )
    )

    val waypointWithHighDangerAndHandles = Waypoint(
        number = 5,
        coordinates = Coordinates(40.0, -3.0),
        distance = Distance(5500),
        distanceFromPrevious = Distance(2000),
        reset = false,
        dangerLevel = Waypoint.DangerLevel.HIGH,
        tulipElements = listOf(
            Track(
                roadIn = Road(null, Point(0.0, 40.0)),
                roadOut = Road(
                    start = null,
                    end = Point(83.5, 35.0),
                )
            ),
            Road(
                start = null,
                end = Point(-91.5, -76.0),
                roadType = Road.RoadType.TarmacRoad,
            ),
            Road(
                start = null,
                end = Point(91.5, -76.0),
                handles = listOf(
                    Point(17.75703517587941, -51.797777777777775),
                    Point(76.69773869346733, -54.79333333333334),
                ),
            ),
            Road(
                start = null,
                end = Point(-91.5, 43.0),
                roadType = Road.RoadType.SmallTrack,
                handles = listOf(Point(-20.204773869346738, 25.088148148148164)),
            ),
        ),
        notesElements = listOf(
            Icon(
                type = Icon.IconType.Danger3,
                center = Point(100.0, 67.5),
                width = 80,
                height = 80,
                angle = 0
            )
        )
    )

    val waypointWithIconsAndShortDistance = Waypoint(
        number = 6,
        coordinates = Coordinates(40.0, -3.0),
        distance = Distance(9500),
        distanceFromPrevious = Distance(200),
        tulipElements = listOf(
            Icon(type = Icon.IconType.Danger1, center = Point(40.0, 40.0), width = 30, height = 30),
            Icon(type = Icon.IconType.Danger2, center = Point(80.0, 40.0), width = 30, height = 30),
            Icon(type = Icon.IconType.Danger3, center = Point(120.0, 40.0), width = 30, height = 30),
            Icon(type = Icon.IconType.FuelZone, center = Point(160.0, 40.0), width = 30, height = 30),
            Icon(type = Icon.IconType.ResetDistance, center = Point(40.0, 80.0), width = 30, height = 30),
            Icon(type = Icon.IconType.AboveBridge, center = Point(80.0, 80.0), width = 30, height = 30),
            Icon(type = Icon.IconType.FortCastle, center = Point(120.0, 80.0), width = 30, height = 30),
            Icon(type = Icon.IconType.House, center = Point(160.0, 80.0), width = 30, height = 30),
        ),
        notesElements = listOf(
            Icon(type = Icon.IconType.TrafficLight, center = Point(40.0, 40.0), width = 30, height = 30),
            Icon(type = Icon.IconType.Tunnel, center = Point(80.0, 40.0), width = 30, height = 30),
            Icon(type = Icon.IconType.UnderBridge, center = Point(120.0, 40.0), width = 30, height = 30),
            Icon(type = Icon.IconType.Alert, center = Point(160.0, 40.0), width = 30, height = 30),
            Icon(type = Icon.IconType.Roundabout, center = Point(40.0, 80.0), width = 30, height = 30),
            Icon(type = Icon.IconType.Stop, center = Point(80.0, 80.0), width = 30, height = 30),
            Icon(type = Icon.IconType.RiverWater, center = Point(120.0, 80.0), width = 30, height = 30),
        ),
    )

    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1200.dp, 1920.dp)),
    ) {
        Surface {
            Column(
                modifier = Modifier.padding(RoadbookNavigatorTheme.dimensions.paddingMinimal),
            ) {
                WaypointItem(
                    waypoint = waypointWithLowDangerAndRoadTypes,
                    shortDistanceThreshold = 300L,
                    onSetPartialClick = {}
                )
                WaypointItem(
                    waypoint = waypointWithMediumDangerAndText,
                    shortDistanceThreshold = 300L,
                    onSetPartialClick = {}
                )
                WaypointItem(
                    waypoint = waypointWithHighDangerAndHandles,
                    shortDistanceThreshold = 300L,
                    onSetPartialClick = {}
                )
                WaypointItem(
                    waypoint = waypointWithIconsAndShortDistance,
                    shortDistanceThreshold = 300L,
                    onSetPartialClick = {}
                )
            }
        }
    }
}
