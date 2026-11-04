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

package org.giste.roadbooknavigator.features.odometer

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.giste.odometer.OdometerController
import org.giste.odometer.OdometerLocationProvider
import org.giste.odometer.OdometerLogger
import org.giste.odometer.OdometerProvider
import org.giste.odometer.OdometerSettingsProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class OdometerBridgeModule {

    @Binds
    @Singleton
    internal abstract fun bindOdometerLogger(
        impl: OdometerLoggerBridge
    ): OdometerLogger

    @Binds
    @Singleton
    internal abstract fun bindOdometerLocationProvider(
        impl: OdometerLocationProviderBridge
    ): OdometerLocationProvider

    companion object {
        @Provides
        @Singleton
        internal fun provideOdometerProvider(
            @ApplicationContext context: Context,
            locationProvider: OdometerLocationProvider,
            settingsProvider: OdometerSettingsProvider,
            logger: OdometerLogger
        ): OdometerProvider = OdometerController(
            context = context,
            locationProvider = locationProvider,
            settingsProvider = settingsProvider,
            logger = logger
        )
    }
}
