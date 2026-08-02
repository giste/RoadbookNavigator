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

import org.giste.roadbooknavigator.core.util.Logger
import org.giste.odometer.domain.OdometerLogger
import javax.inject.Inject

/**
 * Bridge implementation of [OdometerLogger] that redirects to the app's [Logger].
 */
class OdometerLoggerBridge @Inject constructor(
    private val logger: Logger
) : OdometerLogger {
    private val taggedLogger = logger.withTag("Odometer")

    override fun v(message: String, vararg args: Any?) {
        taggedLogger.v(message, *args)
    }

    override fun d(message: String, vararg args: Any?) {
        taggedLogger.d(message, *args)
    }

    override fun i(message: String, vararg args: Any?) {
        taggedLogger.i(message, *args)
    }

    override fun w(message: String, vararg args: Any?) {
        taggedLogger.w(message, *args)
    }

    override fun e(message: String, vararg args: Any?, throwable: Throwable?) {
        if (throwable != null) {
            taggedLogger.e(throwable, message, *args)
        } else {
            taggedLogger.e(message, *args)
        }
    }
}
