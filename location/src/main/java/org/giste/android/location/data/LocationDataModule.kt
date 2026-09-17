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

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.giste.android.location.LocationLogger
import org.giste.android.location.domain.LocationRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocationDataModule {

    @Binds
    @Singleton
    internal abstract fun bindLocationRepository(
        impl: GpsLocationRepository
    ): LocationRepository

    companion object {
        @Provides
        @Singleton
        internal fun provideGpsLocationRepository(
            @ApplicationContext context: Context,
            logger: LocationLogger
        ): GpsLocationRepository = GpsLocationRepository(
            context = context,
            logger = logger
        )
    }
}
