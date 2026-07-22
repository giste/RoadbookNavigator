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

package org.giste.roadbooknavigator.features.map.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.giste.roadbooknavigator.features.map.domain.model.DownloadedMapInfo
import org.giste.roadbooknavigator.features.map.domain.model.DownloadedMapStatus
import org.giste.roadbooknavigator.features.map.domain.model.MapOverview
import org.giste.roadbooknavigator.features.map.domain.repository.MapRepository
import javax.inject.Inject

class GetMapOverviewUseCase @Inject constructor(
    private val repository: MapRepository
) {
    operator fun invoke(): Flow<MapOverview> {
        return combine(
            repository.getLocalMaps(),
            repository.getRemoteMaps()
        ) { localMaps, remoteFolders ->
            val downloadedInfo = localMaps.map { localMap ->
                val remoteMap = remoteFolders.firstNotNullOfOrNull {
                    it.findMap(
                        localMap.parentPath,
                        localMap.name
                    )
                }
                val status = when {
                    remoteMap == null -> DownloadedMapStatus.Obsolete
                    remoteMap.lastModified > localMap.lastModified -> DownloadedMapStatus.UpdateAvailable(
                        remoteMap
                    )

                    else -> DownloadedMapStatus.UpToDate(remoteMap)
                }
                DownloadedMapInfo(localMap, status)
            }
            MapOverview(downloadedInfo, remoteFolders)
        }
    }
}
