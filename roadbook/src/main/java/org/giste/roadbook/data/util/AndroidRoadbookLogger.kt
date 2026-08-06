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

package org.giste.roadbook.data.util

import android.util.Log
import org.giste.roadbook.RoadbookLogger
import javax.inject.Inject

/**
 * Default implementation of [RoadbookLogger] using [android.util.Log].
 */
internal class AndroidRoadbookLogger private constructor(
    private val tag: String
) : RoadbookLogger {

    @Inject
    constructor() : this("Roadbook")

    override fun v(message: String, vararg args: Any?) {
        Log.v(tag, message.format(*args))
    }

    override fun d(message: String, vararg args: Any?) {
        Log.d(tag, message.format(*args))
    }

    override fun i(message: String, vararg args: Any?) {
        Log.i(tag, message.format(*args))
    }

    override fun w(message: String, vararg args: Any?) {
        Log.w(tag, message.format(*args))
    }

    override fun e(message: String, vararg args: Any?) {
        Log.e(tag, message.format(*args))
    }

    override fun e(t: Throwable, message: String, vararg args: Any?) {
        Log.e(tag, message.format(*args), t)
    }

    override fun withTag(tag: String): RoadbookLogger {
        return AndroidRoadbookLogger("${this.tag}:$tag")
    }
}