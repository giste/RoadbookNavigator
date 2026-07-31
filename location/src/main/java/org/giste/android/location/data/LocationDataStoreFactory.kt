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

package org.giste.android.location.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Internal factory to create DataStore instances for the location module.
 */
internal object LocationDataStoreFactory {
    private val dataStores = mutableMapOf<String, DataStore<Preferences>>()

    fun create(
        context: Context,
        name: String,
        scope: CoroutineScope? = null
    ): DataStore<Preferences> {
        return synchronized(dataStores) {
            dataStores.getOrPut(name) {
                PreferenceDataStoreFactory.create(
                    scope = scope ?: CoroutineScope(Dispatchers.IO + SupervisorJob()),
                    produceFile = { context.preferencesDataStoreFile(name) }
                )
            }
        }
    }
}
