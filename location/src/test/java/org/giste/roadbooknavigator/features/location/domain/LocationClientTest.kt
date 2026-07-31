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

package org.giste.roadbooknavigator.features.location.domain

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class LocationClientTest {

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private val context: Context = mockk()
    private lateinit var dataStoreDir: File

    @Before
    fun setup() {
        dataStoreDir = temporaryFolder.newFolder()
        every { context.applicationContext } returns context
        // Mocking path for DataStore
        every { context.filesDir } returns dataStoreDir
    }

    @Test
    fun `LocationClient should respect custom configuration`() = runTest {
        val customConfig = LocationConfig(
            initialPollingInterval = PollingIntervalThreshold(1234L),
            initialMinDistance = MinDistanceThreshold(5.6f)
        )
        
        val client = LocationClient.create(
            context = context,
            config = customConfig,
            logger = mockk(relaxed = true),
            dataStoreName = "test_config_datastore"
        )
        
        val settings = client.observeLocationSettings().first()
        
        assertEquals(1234L, settings.pollingInterval)
        assertEquals(5.6f, settings.minDistance)
    }
}
