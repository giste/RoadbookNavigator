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

package org.giste.roadbooknavigator.core.permissions.domain

/**
 * Represents the various states of a permission.
 */
enum class PermissionStatus {
    /** Permission has been granted by the user. */
    GRANTED,

    /** Permission is not granted, but can be requested. */
    DENIED,

    /** Permission is denied, and we should show a rationale before requesting again. */
    RATIONALE_REQUIRED,

    /** Permission is permanently denied; the user must enable it in system settings. */
    PERMANENTLY_DENIED
}
