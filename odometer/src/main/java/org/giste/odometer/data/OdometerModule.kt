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

package org.giste.odometer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.giste.odometer.domain.OdometerRepository
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class OdometerIoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class OdometerApplicationScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class OdometerDataStoreQualifier

private val Context.odometerDataStore: DataStore<Preferences> by preferencesDataStore(name = "odometer_state")

@Module
@InstallIn(SingletonComponent::class)
internal abstract class OdometerModule {

    @Binds
    @Singleton
    internal abstract fun bindOdometerRepository(
        impl: DataStoreOdometerRepository
    ): OdometerRepository

    @Binds
    @Singleton
    internal abstract fun bindOdometerTimeProvider(
        impl: RealOdometerTimeProvider
    ): OdometerTimeProvider

    internal companion object {
        @Provides
        @Singleton
        @OdometerIoDispatcher
        internal fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Singleton
        @OdometerApplicationScope
        internal fun provideApplicationScope(
            @OdometerIoDispatcher ioDispatcher: CoroutineDispatcher
        ): CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)

        @Provides
        @Singleton
        @OdometerDataStoreQualifier
        internal fun provideOdometerDataStore(
            @ApplicationContext context: Context
        ): DataStore<Preferences> = context.odometerDataStore
    }
}
