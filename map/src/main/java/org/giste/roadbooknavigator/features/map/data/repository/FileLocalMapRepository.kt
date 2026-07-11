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

package org.giste.roadbooknavigator.features.map.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.map.domain.model.MapFile
import org.giste.roadbooknavigator.features.map.domain.repository.MapRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class FileLocalMapRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val logger: Logger
) : MapRepository {

    private val mapsDir: File
        get() = File(context.filesDir, "maps").apply {
            if (!exists()) mkdirs()
        }

    override fun getLocalMaps(): Flow<List<MapFile>> = callbackFlow {
        val scan = {
            val maps = mapsDir.walkTopDown()
                .filter { it.isFile && it.extension == "map" }
                .map { file ->
                    MapFile(
                        name = file.name,
                        path = file.absolutePath,
                        size = file.length(),
                        lastModified = file.lastModified(),
                        parentPath = file.parentFile?.relativeTo(mapsDir)?.path ?: ""
                    )
                }.toList()
            trySend(maps)
        }

        scan()

        // For simplicity in this first step, we just scan once.
        // A more advanced implementation could use a FileObserver.
        
        awaitClose { }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteMap(mapFile: MapFile) {
        val file = File(mapFile.path)
        if (file.exists()) {
            val deleted = file.delete()
            if (deleted) {
                logger.d("LocalMapRepositoryImpl: Deleted map %s", mapFile.name)
                // Clean up empty parent directories
                var parent = file.parentFile
                while (parent != null && parent != mapsDir && parent.list()?.isEmpty() == true) {
                    parent.delete()
                    parent = parent.parentFile
                }
            } else {
                logger.e("LocalMapRepositoryImpl: Failed to delete map %s", mapFile.name)
            }
        }
    }

    override fun getMapInternalStorageDir(): String = mapsDir.absolutePath
}
