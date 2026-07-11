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

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class JsoupRemoteMapDataSourceTest {

    private val server = MockWebServer()
    private lateinit var dataSource: JsoupRemoteMapDataSource

    @Before
    fun setup() {
        server.start()
        dataSource = JsoupRemoteMapDataSource(OkHttpClient(), Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getRemoteMaps should parse HTML correctly`() = runTest {
        val html = """
            <table>
            <tr><td></td><td><a href="europe/">europe/</a></td><td>2024-05-18 10:00</td><td>-</td></tr>
            <tr><td></td><td><a href="spain.map">spain.map</a></td><td>2024-05-18 12:00</td><td>500M</td></tr>
            </table>
        """.trimIndent()
        
        server.enqueue(MockResponse().setBody(html))
        
        val url = server.url("/v5/").toString()
        val result = dataSource.getRemoteMaps(url)
        
        assertEquals(1, result.subFolders.size)
        assertEquals("europe", result.subFolders[0].name)
        
        assertEquals(1, result.maps.size)
        assertEquals("spain.map", result.maps[0].name)
        assertEquals(500L * 1024 * 1024, result.maps[0].size)
    }
}
