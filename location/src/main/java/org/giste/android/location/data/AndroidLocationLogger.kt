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

import android.util.Log
import org.giste.android.location.domain.LocationLogger
import javax.inject.Inject

/**
 * Default implementation of [LocationLogger] using [android.util.Log].
 */
internal class AndroidLocationLogger(
    private val tag: String
) : LocationLogger {

    @Inject
    constructor() : this("Location")

    override fun v(message: String, vararg args: Any?) {
        Log.v(tag, format(message, *args))
    }

    override fun d(message: String, vararg args: Any?) {
        Log.d(tag, format(message, *args))
    }

    override fun i(message: String, vararg args: Any?) {
        Log.i(tag, format(message, *args))
    }

    override fun w(message: String, vararg args: Any?) {
        Log.w(tag, format(message, *args))
    }

    override fun e(message: String, vararg args: Any?) {
        Log.e(tag, format(message, *args))
    }

    override fun e(t: Throwable, message: String, vararg args: Any?) {
        Log.e(tag, format(message, *args), t)
    }

    override fun withTag(tag: String): LocationLogger {
        return AndroidLocationLogger("${this.tag}:$tag")
    }

    private fun format(message: String, vararg args: Any?): String {
        return if (args.isEmpty()) message else String.format(message, *args)
    }
}