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

package org.giste.odometer.domain

/**
 * Logger interface for the odometer module to decouple it from specific logging implementations.
 */
public interface OdometerLogger {
    public fun v(message: String, vararg args: Any?)
    public fun d(message: String, vararg args: Any?)
    public fun i(message: String, vararg args: Any?)
    public fun w(message: String, vararg args: Any?)
    public fun e(message: String, vararg args: Any?, throwable: Throwable? = null)
}
