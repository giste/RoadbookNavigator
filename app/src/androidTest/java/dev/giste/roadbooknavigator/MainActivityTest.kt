/*
 * Copyright (C) 2026 Giste
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

package dev.giste.roadbooknavigator

import android.content.pm.ActivityInfo
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import dev.giste.location.LocationSettingsProvider
import dev.giste.odometer.OdometerSettings
import dev.giste.odometer.OdometerSettingsProvider
import dev.giste.roadbook.RoadbookSettings
import dev.giste.roadbook.RoadbookSettingsProvider
import dev.giste.roadbooknavigator.features.settings.data.SettingsModule
import dev.giste.roadbooknavigator.features.settings.domain.AppOrientation
import dev.giste.roadbooknavigator.features.settings.domain.AppSettings
import dev.giste.roadbooknavigator.features.settings.domain.AppSettingsRepository
import dev.giste.roadbooknavigator.features.settings.domain.input.InputSettings
import dev.giste.roadbooknavigator.features.settings.domain.input.InputSettingsRepository
import dev.giste.roadbooknavigator.features.settings.domain.location.LocationSettingsRepository
import dev.giste.roadbooknavigator.features.settings.domain.odometer.OdometerSettingsRepository
import dev.giste.roadbooknavigator.features.settings.domain.roadbook.RoadbookSettingsRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import dev.giste.location.LocationSettings as ModuleLocationSettings

@UninstallModules(SettingsModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @BindValue
    val appSettingsRepository: AppSettingsRepository = mockk(relaxed = true)

    @BindValue
    val inputSettingsRepository: InputSettingsRepository = mockk(relaxed = true)

    @BindValue
    val locationSettingsRepository: LocationSettingsRepository = mockk(relaxed = true)

    @BindValue
    val locationSettingsProvider: LocationSettingsProvider = locationSettingsRepository

    @BindValue
    val odometerSettingsRepository: OdometerSettingsRepository = mockk(relaxed = true)

    @BindValue
    val odometerSettingsProvider: OdometerSettingsProvider = odometerSettingsRepository

    @BindValue
    val roadbookSettingsRepository: RoadbookSettingsRepository = mockk(relaxed = true)

    @BindValue
    val roadbookSettingsProvider: RoadbookSettingsProvider = roadbookSettingsRepository

    private val settingsFlow = MutableStateFlow(AppSettings())
    private val moduleLocationSettingsFlow = MutableStateFlow(ModuleLocationSettings())

    @Before
    fun setup() {
        hiltRule.inject()
        every { appSettingsRepository.getSettings() } returns settingsFlow
        every { inputSettingsRepository.getInputSettings() } returns MutableStateFlow(InputSettings())
        every { locationSettingsRepository.settings } returns moduleLocationSettingsFlow
        every { odometerSettingsRepository.getSettings() } returns MutableStateFlow(OdometerSettings())
        every { roadbookSettingsRepository.getSettings() } returns MutableStateFlow(RoadbookSettings())
    }

    @Test
    fun whenOrientationSettingIsVERTICAL_activityRequestedOrientationIsPORTRAIT() {
        settingsFlow.value = AppSettings(orientation = AppOrientation.VERTICAL)

        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, activity.requestedOrientation)
            }
        }
    }

    @Test
    fun whenOrientationSettingIsHORIZONTAL_activityRequestedOrientationIsLANDSCAPE() {
        settingsFlow.value = AppSettings(orientation = AppOrientation.HORIZONTAL)

        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, activity.requestedOrientation)
            }
        }
    }

    @Test
    fun whenOrientationSettingIsFOLLOW_SYSTEM_activityRequestedOrientationIsUNSPECIFIED() {
        settingsFlow.value = AppSettings(orientation = AppOrientation.FOLLOW_SYSTEM)

        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, activity.requestedOrientation)
            }
        }
    }
}
