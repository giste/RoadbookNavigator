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

package org.giste.roadbooknavigator

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import org.giste.roadbooknavigator.core.permission.ui.PermissionGate
import org.giste.roadbooknavigator.core.ui.theme.RoadbookNavigatorTheme
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.settings.domain.AppOrientation
import org.giste.roadbooknavigator.features.settings.domain.AppSettings
import org.giste.roadbooknavigator.features.settings.domain.usecase.GetSettingsUseCase
import org.giste.roadbooknavigator.features.settings.ui.SettingsScreen
import org.giste.roadbooknavigator.ui.Screen
import org.giste.roadbooknavigator.ui.dashboard.DashboardScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getSettingsUseCase: GetSettingsUseCase

    @Inject
    lateinit var logger: Logger

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        logger.i("MainActivity: onCreate")
        enableEdgeToEdge()
        setContent {
            val settings by getSettingsUseCase().collectAsState(initial = AppSettings())

            LaunchedEffect(settings.orientation) {
                logger.d("MainActivity: Applying orientation setting: %s", settings.orientation)
                requestedOrientation = when (settings.orientation) {
                    AppOrientation.VERTICAL -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    AppOrientation.HORIZONTAL -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    AppOrientation.FOLLOW_SYSTEM -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
            }

            LaunchedEffect(settings.fullScreen) {
                logger.d("MainActivity: Applying full screen setting: %b", settings.fullScreen)
                val windowInsetsController =
                    WindowCompat.getInsetsController(window, window.decorView)
                if (settings.fullScreen) {
                    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                    windowInsetsController.systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                } else {
                    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                }
            }

            val windowSizeClass = calculateWindowSizeClass(this)
            val navController = rememberNavController()

            RoadbookNavigatorTheme(
                windowSizeClass = windowSizeClass,
                appTheme = settings.theme
            ) {
                PermissionGate {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Dashboard
                    ) {
                        composable<Screen.Dashboard> {
                            DashboardScreen(
                                windowSizeClass = windowSizeClass,
                                onSettingsClick = { navController.navigate(Screen.Settings) }
                            )
                        }
                        composable<Screen.Settings> {
                            SettingsScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
