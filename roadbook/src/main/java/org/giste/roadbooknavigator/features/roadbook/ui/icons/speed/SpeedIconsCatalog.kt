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

package org.giste.roadbooknavigator.features.roadbook.ui.icons.speed

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.features.roadbook.ui.icons.RoadbookIcons

@Composable
private fun IconItem(name: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            imageVector = icon,
            contentDescription = name,
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.surface),
        )
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
private fun SpeedIconsGallery() {
    RoadbookNavigatorTheme(
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Speed Icons",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                val icons = listOf(
                    "10" to RoadbookIcons.Speed.Limit10,
                    "20" to RoadbookIcons.Speed.Limit20,
                    "30" to RoadbookIcons.Speed.Limit30,
                    "40" to RoadbookIcons.Speed.Limit40,
                    "50" to RoadbookIcons.Speed.Limit50,
                    "60" to RoadbookIcons.Speed.Limit60,
                    "70" to RoadbookIcons.Speed.Limit70,
                    "80" to RoadbookIcons.Speed.Limit80,
                    "90" to RoadbookIcons.Speed.Limit90,
                    "100" to RoadbookIcons.Speed.Limit100,
                    "110" to RoadbookIcons.Speed.Limit110,
                    "120" to RoadbookIcons.Speed.Limit120,
                    "130" to RoadbookIcons.Speed.Limit130,
                    "140" to RoadbookIcons.Speed.Limit140,
                    "150" to RoadbookIcons.Speed.Limit150
                )

                icons.chunked(3).forEach { rowIcons ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        rowIcons.forEach { (name, icon) ->
                            IconItem(name, icon)
                        }
                    }
                }
            }
        }
    }
}
