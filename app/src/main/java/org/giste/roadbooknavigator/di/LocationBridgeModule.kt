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

package org.giste.roadbooknavigator.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.location.data.AppLocationLogger
import org.giste.roadbooknavigator.features.location.domain.LocationLogger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bridges the :location module's logging to the app's main Timber logger.
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocationBridgeModule {

    @Binds
    @Singleton
    @AppLocationLogger
    internal abstract fun bindLocationLogger(
        locationTimberBridge: LocationTimberBridge
    ): LocationLogger
}

internal class LocationTimberBridge @Inject constructor(
    private val logger: Logger
) : LocationLogger {

    override fun v(message: String, vararg args: Any?) {
        logger.v(message, *args)
    }

    override fun d(message: String, vararg args: Any?) {
        logger.d(message, *args)
    }

    override fun i(message: String, vararg args: Any?) {
        logger.i(message, *args)
    }

    override fun w(message: String, vararg args: Any?) {
        logger.w(message, *args)
    }

    override fun e(message: String, vararg args: Any?) {
        logger.e(message, *args)
    }

    override fun e(t: Throwable, message: String, vararg args: Any?) {
        logger.e(t, message, *args)
    }

    override fun withTag(tag: String): LocationLogger {
        return LocationTimberBridge(logger.withTag(tag))
    }
}
