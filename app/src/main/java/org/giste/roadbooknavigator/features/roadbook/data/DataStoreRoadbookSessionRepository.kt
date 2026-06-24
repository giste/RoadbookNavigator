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

package org.giste.roadbooknavigator.features.roadbook.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.giste.roadbooknavigator.core.di.SessionDataStoreQualifier
import org.giste.roadbooknavigator.features.roadbook.domain.RoadbookPosition
import org.giste.roadbooknavigator.features.roadbook.domain.RoadbookSessionRepository
import javax.inject.Inject

/**
 * Implementation of [RoadbookSessionRepository] using Preferences DataStore.
 */
class DataStoreRoadbookSessionRepository @Inject constructor(
    @param:SessionDataStoreQualifier private val dataStore: DataStore<Preferences>
) : RoadbookSessionRepository {

    private object Keys {
        val SCROLL_INDEX = intPreferencesKey("roadbook_scroll_index")
        val SCROLL_OFFSET = intPreferencesKey("roadbook_scroll_offset")
    }

    override val scrollPosition: Flow<RoadbookPosition> = dataStore.data.map { preferences ->
        val index = preferences[Keys.SCROLL_INDEX] ?: 0
        val offset = preferences[Keys.SCROLL_OFFSET] ?: 0
        RoadbookPosition(index, offset)
    }

    override suspend fun saveScrollPosition(position: RoadbookPosition) {
        dataStore.edit { preferences ->
            preferences[Keys.SCROLL_INDEX] = position.index
            preferences[Keys.SCROLL_OFFSET] = position.offset
        }
    }
}
