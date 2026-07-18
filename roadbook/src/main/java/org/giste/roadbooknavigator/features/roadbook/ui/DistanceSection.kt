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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.giste.roadbooknavigator.core.ui.components.RoadbookAutoSizeText
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.roadbook.domain.model.Coordinates
import org.giste.roadbooknavigator.features.roadbook.domain.model.Distance
import org.giste.roadbooknavigator.features.roadbook.domain.model.Waypoint

@Composable
internal fun DistanceSection(
    waypoint: Waypoint,
    shortDistanceThreshold: Long,
    onSetPartialClick: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val locale = LocalConfiguration.current.locales[0]
    val isShortDistance = waypoint.distanceFromPrevious.meters in 1..<shortDistanceThreshold
    val contentColor = if (isShortDistance) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("WaypointDistanceInfo_${waypoint.number}")
            .combinedClickable(
                onLongClick = {
                    val valueToSet =
                        if (waypoint.reset) 0.0 else waypoint.distance.meters.toDouble()
                    onSetPartialClick(valueToSet)
                },
                onClick = {}
            ),
    ) {
        // Accumulated distance (large)
        RoadbookAutoSizeText(
            text = String.format(locale, "%.2f", waypoint.distance.meters / 1000.0),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = contentColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall,
            maxFontSize = 36.sp
        )

        if (waypoint.reset) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = RoadbookNavigatorTheme.dimensions.paddingLarge),
                thickness = RoadbookNavigatorTheme.dimensions.resetDividerThickness,
                color = contentColor,
            )
        }

        if (waypoint.dangerLevel == Waypoint.DangerLevel.MEDIUM) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = RoadbookNavigatorTheme.dimensions.paddingMedium),
                thickness = RoadbookNavigatorTheme.dimensions.dangerMediumThickness,
                color = MaterialTheme.colorScheme.error,
            )
        }

        // Reset
        if (waypoint.reset) {
            RoadbookAutoSizeText(
                text = String.format(locale, "%.2f", 0.0),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = contentColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge,
                maxFontSize = 32.sp
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            // Partial distance (small)
            RoadbookAutoSizeText(
                text = String.format(locale, "%.2f", waypoint.distanceFromPrevious.meters / 1000.0),
                modifier = Modifier
                    .weight(0.5f)
                    .border(
                        width = RoadbookNavigatorTheme.dimensions.sectionBorder,
                        color = contentColor
                    )
                    .padding(horizontal = RoadbookNavigatorTheme.dimensions.paddingMinimal),
                color = contentColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                maxFontSize = 22.sp
            )

            VerticalDivider(
                modifier = Modifier
                    .weight(0.25f)
                    .height(IntrinsicSize.Min),
                color = contentColor
            )

            // Waypoint number
            RoadbookAutoSizeText(
                text = waypoint.number.toString(),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.inverseSurface)
                    .weight(0.25f)
                    .padding(horizontal = RoadbookNavigatorTheme.dimensions.paddingMinimal),
                color = MaterialTheme.colorScheme.inverseOnSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                maxFontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, name = "Standard Waypoint")
@Composable
private fun DistanceSectionStandardPreview() {
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    ) {
        Surface {
            DistanceSection(
                waypoint = Waypoint(
                    number = 1,
                    coordinates = Coordinates(0.0, 0.0),
                    distance = Distance(1250),
                    distanceFromPrevious = Distance(1250)
                ),
                shortDistanceThreshold = 300L,
                onSetPartialClick = {},
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, name = "Reset Waypoint")
@Composable
private fun DistanceSectionResetPreview() {
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    ) {
        Surface {
            DistanceSection(
                waypoint = Waypoint(
                    number = 2,
                    coordinates = Coordinates(0.0, 0.0),
                    distance = Distance(2500),
                    distanceFromPrevious = Distance(1250),
                    reset = true
                ),
                shortDistanceThreshold = 300L,
                onSetPartialClick = {},
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, name = "Danger Medium")
@Composable
private fun DistanceSectionDangerPreview() {
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    ) {
        Surface {
            DistanceSection(
                waypoint = Waypoint(
                    number = 3,
                    coordinates = Coordinates(0.0, 0.0),
                    distance = Distance(3750),
                    distanceFromPrevious = Distance(1250),
                    dangerLevel = Waypoint.DangerLevel.MEDIUM
                ),
                shortDistanceThreshold = 300L,
                onSetPartialClick = {},
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, name = "Short Distance")
@Composable
private fun DistanceSectionShortDistancePreview() {
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    ) {
        Surface {
            DistanceSection(
                waypoint = Waypoint(
                    number = 4,
                    coordinates = Coordinates(0.0, 0.0),
                    distance = Distance(3950),
                    distanceFromPrevious = Distance(200)
                ),
                shortDistanceThreshold = 300L,
                onSetPartialClick = {},
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, name = "Reset + Danger Medium")
@Composable
private fun DistanceSectionResetDangerPreview() {
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    ) {
        Surface {
            DistanceSection(
                waypoint = Waypoint(
                    number = 2,
                    coordinates = Coordinates(0.0, 0.0),
                    distance = Distance(2500),
                    distanceFromPrevious = Distance(1250),
                    reset = true,
                    dangerLevel = Waypoint.DangerLevel.MEDIUM
                ),
                shortDistanceThreshold = 300L,
                onSetPartialClick = {},
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, name = "Large Distance Auto-size")
@Composable
private fun DistanceSectionLargeDistancePreview() {
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    ) {
        Surface {
            DistanceSection(
                waypoint = Waypoint(
                    number = 999,
                    coordinates = Coordinates(0.0, 0.0),
                    distance = Distance(125430),
                    distanceFromPrevious = Distance(15430)
                ),
                shortDistanceThreshold = 300L,
                onSetPartialClick = {},
                modifier = Modifier
                    .width(100.dp)
                    .aspectRatio(1f)
            )
        }
    }
}
