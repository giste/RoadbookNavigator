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

package org.giste.map.domain

import kotlinx.coroutines.flow.Flow
import org.giste.map.MapSettings
import org.giste.map.MapSettingsProvider
import org.giste.map.domain.usecase.GetMapSettingsUseCase
import org.giste.map.domain.usecase.SaveMapSettingsUseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RealMapSettingsProvider @Inject constructor(
    private val getMapSettingsUseCase: GetMapSettingsUseCase,
    private val saveMapSettingsUseCase: SaveMapSettingsUseCase
) : MapSettingsProvider {
    override fun observeSettings(): Flow<MapSettings> = getMapSettingsUseCase()
    
    override suspend fun saveSettings(settings: MapSettings) {
        saveMapSettingsUseCase(settings)
    }
}
