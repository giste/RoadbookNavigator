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

package org.giste.roadbooknavigator.features.roadbook.data.rn2

import io.mockk.mockk
import org.giste.roadbooknavigator.core.util.Logger
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Icon
import org.giste.roadbooknavigator.features.roadbook.data.rn2.dto.Rn2Waypoint
import org.giste.roadbooknavigator.features.roadbook.domain.model.Icon
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class Rn2ElementMapperTest {

    private lateinit var mapper: Rn2ElementMapper
    private val logger: Logger = mockk(relaxed = true)

    @Before
    fun setup() {
        val geometryCalculator = RoadbookGeometryCalculator(logger)
        mapper = Rn2ElementMapper(geometryCalculator, logger)
    }

    @Test
    fun `mapElements should map every Rn2Icon subclass to a valid domain IconType`() {
        // This test ensures that every icon DTO defined in the data layer
        // is correctly handled by the mapper and not just defaulted to "Unknown".
        
        val subclasses = Rn2Icon::class.sealedSubclasses
        val currentWaypoint = mockk<Rn2Waypoint>(relaxed = true)

        subclasses.forEach { subclass ->
            // Skip "Unknown" subclass as it's expected to map to Unknown
            if (subclass == Rn2Icon.Unknown::class) return@forEach

            // Instantiate the subclass (they are all data classes with at least one constructor)
            val iconDto = createInstance(subclass)
            
            // When
            val elements = mapper.mapElements(listOf(iconDto), currentWaypoint = currentWaypoint)
            val domainIcon = elements.first() as Icon

            // Then
            assertNotEquals(
                "Icon DTO ${subclass.simpleName} was mapped to IconType.Unknown. " +
                        "Did you forget to add a branch in Rn2ElementMapper?",
                Icon.IconType.Unknown,
                domainIcon.type
            )
        }
    }

    private fun <T : Any> createInstance(kClass: KClass<T>): T {
        val constructor = kClass.primaryConstructor 
            ?: throw IllegalStateException("No primary constructor for ${kClass.simpleName}")
        
        val args = constructor.parameters.associateWith { param ->
            when (param.name) {
                "id" -> "test-id"
                else -> null // Most other fields in Rn2Icon are nullable Double?
            }
        }
        return constructor.callBy(args)
    }
}
