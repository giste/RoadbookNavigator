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

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.giste.odometer.OdometerLogger
import org.giste.odometer.domain.Odometer
import org.giste.odometer.domain.OdometerRepository
import org.giste.roadbooknavigator.core.di.ApplicationScope
import org.giste.roadbooknavigator.core.di.IoDispatcher

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Buffered implementation of [OdometerRepository] using DataStore for persistence.
 *
 * It maintains an in-memory state for low-latency updates by combining persisted data
 * with a buffer of pending changes, persisting to disk only when thresholds are reached.
 */
@Singleton
internal class DataStoreOdometerRepository @Inject constructor(
    @param:OdometerDataStoreQualifier private val dataStore: DataStore<Preferences>,
    private val logger: OdometerLogger,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
    private val timeProvider: OdometerTimeProvider
) : OdometerRepository {

    private companion object {
        private val TOTAL_DISTANCE_KEY = doublePreferencesKey("total_distance")
        private val PARTIAL_DISTANCE_KEY = doublePreferencesKey("partial_distance")

        private const val PERSISTENCE_DISTANCE_THRESHOLD = 0.5 // km (500m)
        private const val PERSISTENCE_TIME_THRESHOLD = 60000L // ms (1 min)
    }

    private val _pendingDeltas = MutableStateFlow(Odometer(0.0, 0.0))
    private var lastSaveTime = timeProvider.currentTimeMillis()

    private val persistedOdometer: Flow<Odometer> = dataStore.data
        .map { prefs ->
            Odometer(
                total = prefs[TOTAL_DISTANCE_KEY] ?: 0.0,
                partial = prefs[PARTIAL_DISTANCE_KEY] ?: 0.0
            )
        }

    override val odometer: StateFlow<Odometer> = combine(
        persistedOdometer,
        _pendingDeltas
    ) { persisted, pending ->
        Odometer(
            total = persisted.total + pending.total,
            partial = persisted.partial + pending.partial
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = Odometer(0.0, 0.0)
    )

    override suspend fun updateDistance(delta: Double) {
        withContext(ioDispatcher) {
            _pendingDeltas.update { current ->
                current.copy(total = current.total + delta, partial = current.partial + delta)
            }

            val pending = _pendingDeltas.value
            val timeSinceSave = timeProvider.currentTimeMillis() - lastSaveTime

            if (pending.total >= PERSISTENCE_DISTANCE_THRESHOLD || timeSinceSave >= PERSISTENCE_TIME_THRESHOLD) {
                persist()
            }
        }
    }

    override suspend fun updatePartialDistance(delta: Double) {
        withContext(ioDispatcher) {
            logger.v("DataStoreOdometerRepository: Updating partial with delta: %f", delta)
            _pendingDeltas.update { current ->
                current.copy(partial = current.partial + delta)
            }
            persist()
        }
    }

    override suspend fun resetPartialDistance() {
        withContext(ioDispatcher) {
            logger.i("DataStoreOdometerRepository: Resetting partial distance")
            // We want the final partial to be 0.
            // odometer.value.partial = persisted.partial + pending.partial
            // To make it 0, we set pending.partial = -persisted.partial
            // However, it's easier to just flush a hardcoded 0 to disk.
            persist(targetPartial = 0.0)
        }
    }

    override suspend fun resetAllDistances() {
        withContext(ioDispatcher) {
            logger.i("DataStoreOdometerRepository: Resetting all distances")
            persist(targetTotal = 0.0, targetPartial = 0.0)
        }
    }

    override suspend fun setPartialDistance(distance: Double) {
        withContext(ioDispatcher) {
            logger.i("DataStoreOdometerRepository: Setting partial distance to: %f", distance)
            persist(targetPartial = distance)
        }
    }

    private suspend fun persist(
        targetTotal: Double? = null,
        targetPartial: Double? = null
    ) {
        // We MUST read the latest persisted values and current pending buffer to correctly subtract
        val currentPersisted = persistedOdometer.first()
        val currentPending = _pendingDeltas.value

        val toSaveTotal = targetTotal ?: (currentPersisted.total + currentPending.total)
        val toSavePartial = targetPartial ?: (currentPersisted.partial + currentPending.partial)

        logger.v("DataStoreOdometerRepository: Persisting to DataStore: total=%f, partial=%f", toSaveTotal, toSavePartial)
        
        try {
            dataStore.edit { prefs ->
                prefs[TOTAL_DISTANCE_KEY] = toSaveTotal
                prefs[PARTIAL_DISTANCE_KEY] = toSavePartial
            }
            
            // Adjust pending deltas: remove the amounts we just successfully wrote to disk
            _pendingDeltas.update { latestPending ->
                Odometer(
                    total = if (targetTotal != null) 0.0 else (latestPending.total - currentPending.total).coerceAtLeast(0.0),
                    partial = if (targetPartial != null) 0.0 else (latestPending.partial - currentPending.partial)
                )
            }
            lastSaveTime = timeProvider.currentTimeMillis()
        } catch (e: Exception) {
            logger.e("DataStoreOdometerRepository: Error persisting state", throwable = e)
        }
    }
}
