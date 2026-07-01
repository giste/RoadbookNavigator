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

package org.giste.roadbooknavigator.features.settings.domain

/**
 * Aggregate Value Object representing all user-configurable settings.
 *
 * @property theme Selected visual theme.
 * @property orientation Preferred screen orientation.
 * @property fullScreen Whether the app should be in immersive full-screen mode.
 * @property shortDistanceThreshold Threshold in meters to highlight "short distance" instructions.
 * @property remoteKeySettings Configuration for remote control keys.
 */
data class AppSettings(
    val theme: AppTheme = AppTheme.FOLLOW_SYSTEM,
    val orientation: AppOrientation = AppOrientation.FOLLOW_SYSTEM,
    val fullScreen: Boolean = true,
    val shortDistanceThreshold: Long = DEFAULT_SHORT_DISTANCE_THRESHOLD,
    val remoteKeySettings: RemoteKeySettings = RemoteKeySettings(),
) {
    companion object {
        const val DEFAULT_SHORT_DISTANCE_THRESHOLD = 300L
    }
}
