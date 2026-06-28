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

package org.giste.roadbooknavigator.core.permission.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import org.giste.roadbooknavigator.core.util.Logger
import javax.inject.Inject

class ObserveAllPermissionsUseCase @Inject constructor(
    private val repository: PermissionRepository
) {
    operator fun invoke(): Flow<Map<AppPermission, PermissionState>> {
        Logger.d("Starting to observe all permissions")
        return repository.observeAllPermissions()
            .onStart { Logger.v("Permission flow collection started") }
    }
}
