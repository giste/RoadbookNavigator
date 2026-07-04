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

package org.giste.roadbooknavigator.features.roadbook.domain.repository

import kotlinx.coroutines.flow.Flow
import org.giste.roadbooknavigator.features.roadbook.domain.model.Route
import java.io.InputStream

/**
 * Interface defining the contract for roadbook data operations.
 * Following Clean Architecture, this sits in the Domain layer.
 */
interface RoadbookRepository {

    /**
     * A stream of the currently active roadbook.
     * Emits null if no roadbook is loaded.
     */
    val activeRoadbook: Flow<Route?>

    /**
     * Parses a raw rn2 stream, saves the processed model to internal storage,
     * and emits it through [activeRoadbook].
     *
     * @param inputStream The source stream (e.g., from an .rn2 file).
     * @return A [Result] containing the parsed and processed [Route].
     */
    suspend fun processNewRoadbook(inputStream: InputStream): Result<Route>
}
