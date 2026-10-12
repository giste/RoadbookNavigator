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

import org.giste.odometer.OdometerLogger
import org.giste.roadbooknavigator.core.util.Logger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Bridge implementation of [OdometerLogger] that redirects to the app's [Logger].
 */
@Singleton
internal class OdometerLoggerBridge @Inject constructor(
    private val logger: Logger
) : OdometerLogger {

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

    override fun withTag(tag: String): OdometerLogger {
        return OdometerLoggerBridge(logger.withTag(tag))
    }
}
