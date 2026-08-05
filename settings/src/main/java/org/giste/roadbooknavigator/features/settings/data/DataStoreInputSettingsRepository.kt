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

package org.giste.roadbooknavigator.features.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.settings.domain.input.InputSettings
import org.giste.roadbooknavigator.features.settings.domain.input.InputSettingsRepository
import org.giste.roadbooknavigator.features.settings.domain.input.RemoteModel
import javax.inject.Inject

/**
 * Jetpack DataStore implementation of [InputSettingsRepository].
 */
internal class DataStoreInputSettingsRepository @Inject constructor(
    @param:InputKeySettingsDataStore private val dataStore: DataStore<Preferences>,
    private val logger: Logger
) : InputSettingsRepository {

    private object Keys {
        val REMOTE_MODEL = stringPreferencesKey("remote_model")
        val ROADBOOK_KEYS_UP = stringPreferencesKey("roadbook_keys_up")
        val ROADBOOK_KEYS_DOWN = stringPreferencesKey("roadbook_keys_down")
        val ODOMETER_KEYS_INC = stringPreferencesKey("odometer_keys_inc")
        val ODOMETER_KEYS_DEC = stringPreferencesKey("odometer_keys_dec")
        val ODOMETER_KEYS_RESET = stringPreferencesKey("odometer_keys_reset")
    }

    override fun getInputSettings(): Flow<InputSettings> = dataStore.data.map { preferences ->
        val remoteModel =
            preferences[Keys.REMOTE_MODEL]?.let { safeRemoteModelOf(it) } ?: RemoteModel.DND2
        InputSettings(
            model = remoteModel,
            upKeys = preferences[Keys.ROADBOOK_KEYS_UP]?.toIntList()
                ?: InputSettings.DEFAULT_UP_KEYS,
            downKeys = preferences[Keys.ROADBOOK_KEYS_DOWN]?.toIntList()
                ?: InputSettings.DEFAULT_DOWN_KEYS,
            increasePartialKeys = preferences[Keys.ODOMETER_KEYS_INC]?.toIntList()
                ?: InputSettings.DEFAULT_INCREASE_KEYS,
            decreasePartialKeys = preferences[Keys.ODOMETER_KEYS_DEC]?.toIntList()
                ?: InputSettings.DEFAULT_DECREASE_KEYS,
            resetPartialKeys = preferences[Keys.ODOMETER_KEYS_RESET]?.toIntList()
                ?: InputSettings.DEFAULT_RESET_KEYS,
        )
    }.onEach {
        logger.v("DataStoreInputKeySettingsRepository: Settings updated: %s", it)
    }

    override suspend fun setRemoteModel(model: RemoteModel) {
        logger.i("DataStoreInputKeySettingsRepository: Setting remote model to %s", model)
        dataStore.edit { preferences ->
            preferences[Keys.REMOTE_MODEL] = model.name
        }
    }

    override suspend fun setRoadbookKeys(up: List<Int>, down: List<Int>) {
        logger.i("DataStoreInputKeySettingsRepository: Setting roadbook remote keys")
        dataStore.edit { preferences ->
            preferences[Keys.ROADBOOK_KEYS_UP] = up.toPreferenceString()
            preferences[Keys.ROADBOOK_KEYS_DOWN] = down.toPreferenceString()
        }
    }

    override suspend fun setOdometerKeys(
        increase: List<Int>,
        decrease: List<Int>,
        reset: List<Int>
    ) {
        logger.i("DataStoreInputKeySettingsRepository: Setting odometer remote keys")
        dataStore.edit { preferences ->
            preferences[Keys.ODOMETER_KEYS_INC] = increase.toPreferenceString()
            preferences[Keys.ODOMETER_KEYS_DEC] = decrease.toPreferenceString()
            preferences[Keys.ODOMETER_KEYS_RESET] = reset.toPreferenceString()
        }
    }

    private fun List<Int>.toPreferenceString(): String = joinToString(",")

    private fun String.toIntList(): List<Int> =
        if (isEmpty()) emptyList() else split(",").mapNotNull { it.toIntOrNull() }

    private fun safeRemoteModelOf(name: String): RemoteModel = try {
        RemoteModel.valueOf(name)
    } catch (_: IllegalArgumentException) {
        RemoteModel.DND2
    }
}
