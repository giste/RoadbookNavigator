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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
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
    val backgroundColor =
        if (isShortDistance) MaterialTheme.colorScheme.tertiaryContainer else Color.Unspecified
    val contentColor = if (isShortDistance) {
        MaterialTheme.colorScheme.onTertiaryContainer
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .testTag("WaypointDistanceInfo_${waypoint.number}")
            .combinedClickable(
                onLongClick = { onSetPartialClick(if (waypoint.reset) 0.0 else waypoint.distance.kilometers * 1000) },
                onClick = {}
            ),
    ) {
        // Accumulated distance (large)
        Text(
            text = String.format(locale, "%.2f", waypoint.distance.meters / 1000.0),
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = contentColor,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
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
            Text(
                text = String.format(locale, "%.2f", 0.0),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = contentColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge,
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            // Partial distance (small)
            Text(
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
            )

            VerticalDivider(
                modifier = Modifier
                    .weight(0.25f)
                    .height(IntrinsicSize.Min),
                color = contentColor
            )

            // Waypoint number
            Text(
                text = waypoint.number.toString(),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.inverseSurface)
                    .weight(0.25f)
                    .padding(horizontal = RoadbookNavigatorTheme.dimensions.paddingMinimal),
                color = MaterialTheme.colorScheme.inverseOnSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}
