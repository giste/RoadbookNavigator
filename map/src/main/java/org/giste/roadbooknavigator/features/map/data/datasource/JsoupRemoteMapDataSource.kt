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

package org.giste.roadbooknavigator.features.map.data.datasource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import org.giste.roadbooknavigator.core.di.IoDispatcher
import org.jsoup.Jsoup
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFile
import org.giste.roadbooknavigator.features.map.domain.model.RemoteMapFolder
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class JsoupRemoteMapDataSource @Inject constructor(
    private val okHttpClient: OkHttpClient,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RemoteMapDataSource {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

    override suspend fun getRemoteMaps(url: String): RemoteMapFolder = withContext(ioDispatcher) {
        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()
        
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        
        val html = response.body.string()
        val doc = Jsoup.parse(html, url)
        
        val folderName = url.removeSuffix("/").substringAfterLast("/")
        
        val maps = mutableListOf<RemoteMapFile>()
        val subFolders = mutableListOf<RemoteMapFolder>()

        // This is a generic parser for Apache/Nginx autoindex
        doc.select("tr").forEach { row ->
            val cells = row.select("td")
            if (cells.size >= 2) {
                val link = cells[1].select("a").first()
                val name = link?.text() ?: ""
                val href = link?.attr("abs:href") ?: ""
                
                if (name.isNotEmpty() && name != "Parent Directory") {
                    if (name.endsWith("/")) {
                        // It's a folder. We don't recurse here for performance, 
                        // but the domain model allows it. 
                        // Usually we'd fetch subfolders only when needed.
                        // But the requirement says "grouped by folders".
                        subFolders.add(RemoteMapFolder(name.removeSuffix("/"), href))
                    } else if (name.endsWith(".map")) {
                        // Extract size and date if possible from other cells
                        // This depends highly on the server format.
                        // For Esslingen FTP: 
                        // [Icon] Name Last modified Size
                        val lastModStr = cells.getOrNull(2)?.text() ?: ""
                        val sizeStr = cells.getOrNull(3)?.text() ?: ""
                        
                        val lastMod = try {
                            dateFormat.parse(lastModStr)?.time ?: 0L
                        } catch (_: Exception) {
                            0L
                        }
                        
                        val size = parseSize(sizeStr)
                        
                        val parentPath = url.substringAfter("v5/").removeSuffix("/")

                        maps.add(RemoteMapFile(name, parentPath, href, size, lastMod))
                    }
                }
            }
        }
        
        RemoteMapFolder(folderName, url, subFolders, maps)
    }

    override suspend fun downloadFile(url: String): ResponseBody = withContext(ioDispatcher) {
        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()
        if (!response.isSuccessful) throw IOException("Unexpected code $response")
        response.body
    }

    private fun parseSize(sizeStr: String): Long {
        val cleanSize = sizeStr.trim().uppercase()
        return when {
            cleanSize.endsWith("K") -> (cleanSize.removeSuffix("K").toDouble() * 1024).toLong()
            cleanSize.endsWith("M") -> (cleanSize.removeSuffix("M").toDouble() * 1024 * 1024).toLong()
            cleanSize.endsWith("G") -> (cleanSize.removeSuffix("G").toDouble() * 1024 * 1024 * 1024).toLong()
            else -> cleanSize.toLongOrNull() ?: 0L
        }
    }
}
