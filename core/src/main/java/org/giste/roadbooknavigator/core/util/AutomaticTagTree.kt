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

package org.giste.roadbooknavigator.core.util

import timber.log.Timber

/**
 * A custom Timber Tree that automatically finds the caller class,
 * skipping the logging infrastructure frames.
 */
class AutomaticTagTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? {
        // Iterate through stack trace to find the first class that is not part of the logging utility
        val stackTrace = Throwable().stackTrace
        for (i in stackTrace.indices) {
            val className = stackTrace[i].className
            if (!isLoggingInternal(className)) {
                // Return the tag for the first non-internal class found
                // We also filter out common Kotlin internal suffixes to keep the tag clean
                val tag = super.createStackElementTag(stackTrace[i])
                return tag?.split('$')?.firstOrNull() ?: tag
            }
        }
        return super.createStackElementTag(element)
    }

    private fun isLoggingInternal(className: String): Boolean {
        return className.contains(Timber::class.java.name) ||
                className.contains(Logger::class.java.name) ||
                className.contains(TimberLogger::class.java.name) ||
                className.contains(AutomaticTagTree::class.java.name) ||
                className.contains("DefaultImpls") || // Kotlin interface default implementation wrappers
                className.contains("java.lang.reflect") ||
                className.contains("sun.reflect")
    }
}
