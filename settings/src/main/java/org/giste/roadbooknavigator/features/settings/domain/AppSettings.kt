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

import org.giste.roadbooknavigator.core.settings.domain.AppTheme

/**
 * Aggregate Value Object representing all user-configurable settings.
 *
 * @property theme Selected visual theme.
 * @property orientation Preferred screen orientation.
 * @property fullScreen Whether the app should be in immersive full-screen mode.
 * @property landscapeDistanceSectionWeight Weight (0.0 to 1.0) of the distance section in landscape.
 * @property remoteKeySettings Configuration for remote control keys.
 */
data class AppSettings(
    val theme: AppTheme = AppTheme.FOLLOW_SYSTEM,
    val orientation: AppOrientation = AppOrientation.FOLLOW_SYSTEM,
    val fullScreen: Boolean = true,
    val landscapeDistanceSectionWeight: Float = 0.3f,
    val remoteKeySettings: RemoteKeySettings = RemoteKeySettings(),
) {
    companion object {
        const val MIN_LANDSCAPE_WEIGHT = 0.25f
        const val MAX_LANDSCAPE_WEIGHT = 0.40f
    }
}
