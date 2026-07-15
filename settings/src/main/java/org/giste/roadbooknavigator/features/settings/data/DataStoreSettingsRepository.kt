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
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import org.giste.roadbooknavigator.core.settings.domain.AppTheme
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.RemoteKeySettings
import org.giste.roadbooknavigator.features.settings.domain.RemoteKeys
import org.giste.roadbooknavigator.features.settings.domain.RemoteModel
import org.giste.roadbooknavigator.features.settings.domain.SettingsRepository
import javax.inject.Inject

/**
 * Jetpack DataStore implementation of [SettingsRepository].
 *
 * This class handles the persistence of user preferences in a reactive way
 * using a key-value pair storage system.
 */
internal class DataStoreSettingsRepository @Inject constructor(
    @param:SettingsDataStore private val dataStore: DataStore<Preferences>,
    private val logger: Logger
) : SettingsRepository {

    private object Keys {
        val THEME = stringPreferencesKey("app_theme")
        val ORIENTATION = stringPreferencesKey("app_orientation")
        val FULL_SCREEN = booleanPreferencesKey("full_screen")
        val REMOTE_MODEL = stringPreferencesKey("remote_model")
        val CUSTOM_INCREASE_PARTIAL = stringPreferencesKey("custom_increase_partial")
        val CUSTOM_DECREASE_PARTIAL = stringPreferencesKey("custom_decrease_partial")
        val CUSTOM_RESET_PARTIAL = stringPreferencesKey("custom_reset_partial")
    }

    override fun getSettings(): Flow<AppSettings> = dataStore.data.map { preferences ->
        val remoteModel =
            preferences[Keys.REMOTE_MODEL]?.let { safeRemoteModelOf(it) } ?: RemoteModel.DND2
        AppSettings(
            theme = preferences[Keys.THEME]?.let { safeThemeOf(it) } ?: AppTheme.FOLLOW_SYSTEM,
            orientation = preferences[Keys.ORIENTATION]?.let { safeOrientationOf(it) }
                ?: AppOrientation.FOLLOW_SYSTEM,
            fullScreen = preferences[Keys.FULL_SCREEN] ?: true,
            remoteKeySettings = RemoteKeySettings(
                model = remoteModel,
                customKeys = RemoteKeys(
                    increasePartial = preferences[Keys.CUSTOM_INCREASE_PARTIAL]?.toIntList()
                        ?: RemoteKeys.DND2.increasePartial,
                    decreasePartial = preferences[Keys.CUSTOM_DECREASE_PARTIAL]?.toIntList()
                        ?: RemoteKeys.DND2.decreasePartial,
                    resetPartial = preferences[Keys.CUSTOM_RESET_PARTIAL]?.toIntList()
                        ?: RemoteKeys.DND2.resetPartial,
                )
            )
        )
    }.onEach {
        logger.v("DataStoreSettingsRepository: Settings updated: %s", it)
    }

    override suspend fun setTheme(theme: AppTheme) {
        logger.i("DataStoreSettingsRepository: Setting theme to %s", theme)
        dataStore.edit { preferences ->
            preferences[Keys.THEME] = theme.name
        }
    }

    override suspend fun setOrientation(orientation: AppOrientation) {
        logger.i("DataStoreSettingsRepository: Setting orientation to %s", orientation)
        dataStore.edit { preferences ->
            preferences[Keys.ORIENTATION] = orientation.name
        }
    }

    override suspend fun setFullScreen(enabled: Boolean) {
        logger.i("DataStoreSettingsRepository: Setting full screen to %s", enabled)
        dataStore.edit { preferences ->
            preferences[Keys.FULL_SCREEN] = enabled
        }
    }

    override suspend fun setRemoteModel(model: RemoteModel) {
        logger.i("DataStoreSettingsRepository: Setting remote model to %s", model)
        dataStore.edit { preferences ->
            preferences[Keys.REMOTE_MODEL] = model.name
            if (model != RemoteModel.CUSTOM) {
                val keys = when (model) {
                    RemoteModel.DND2 -> RemoteKeys.DND2
                    RemoteModel.TERRA_PIRATA -> RemoteKeys.TERRA_PIRATA
                    RemoteModel.CUSTOM -> null
                }
                keys?.let {
                    preferences[Keys.CUSTOM_INCREASE_PARTIAL] =
                        it.increasePartial.toPreferenceString()
                    preferences[Keys.CUSTOM_DECREASE_PARTIAL] =
                        it.decreasePartial.toPreferenceString()
                    preferences[Keys.CUSTOM_RESET_PARTIAL] = it.resetPartial.toPreferenceString()
                }
            }
        }
    }

    override suspend fun setCustomKeys(keys: RemoteKeys) {
        logger.i("DataStoreSettingsRepository: Setting custom keys")
        dataStore.edit { preferences ->
            preferences[Keys.REMOTE_MODEL] = RemoteModel.CUSTOM.name
            preferences[Keys.CUSTOM_INCREASE_PARTIAL] = keys.increasePartial.toPreferenceString()
            preferences[Keys.CUSTOM_DECREASE_PARTIAL] = keys.decreasePartial.toPreferenceString()
            preferences[Keys.CUSTOM_RESET_PARTIAL] = keys.resetPartial.toPreferenceString()
        }
    }

    private fun safeThemeOf(name: String): AppTheme = try {
        AppTheme.valueOf(name)
    } catch (_: IllegalArgumentException) {
        AppTheme.FOLLOW_SYSTEM
    }

    private fun safeOrientationOf(name: String): AppOrientation = try {
        AppOrientation.valueOf(name)
    } catch (_: IllegalArgumentException) {
        AppOrientation.FOLLOW_SYSTEM
    }

    private fun safeRemoteModelOf(name: String): RemoteModel = try {
        RemoteModel.valueOf(name)
    } catch (_: IllegalArgumentException) {
        RemoteModel.DND2
    }

    private fun List<Int>.toPreferenceString(): String = joinToString(",")

    private fun String.toIntList(): List<Int> =
        if (isEmpty()) emptyList() else split(",").mapNotNull { it.toIntOrNull() }
}
