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

package org.giste.roadbooknavigator.features.odometer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme

@Composable
fun TotalDistance(
    distance: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = RoadbookNavigatorTheme.dimensions.paddingTiny)
            .padding(horizontal = RoadbookNavigatorTheme.dimensions.paddingSmall),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = distance,
            modifier = Modifier.testTag("TotalOdometerValue"),
            style = MaterialTheme.typography.displaySmall,
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
fun PartialDistance(
    distance: String,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .border(
                RoadbookNavigatorTheme.dimensions.sectionBorder,
                MaterialTheme.colorScheme.outline
            )
            .combinedClickable(
                onClick = { },
                onLongClick = onLongClick
            )
            .padding(vertical = RoadbookNavigatorTheme.dimensions.paddingTiny)
            .padding(horizontal = RoadbookNavigatorTheme.dimensions.paddingSmall)
            .testTag("PartialOdometer"),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = distance,
            modifier = Modifier.testTag("PartialOdometerValue"),
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            softWrap = false
        )
    }
}
