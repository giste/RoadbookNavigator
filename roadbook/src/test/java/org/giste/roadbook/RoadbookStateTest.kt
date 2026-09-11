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
 * along with this program.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  See the
 * GNU General Public License for more details.
 */

package org.giste.roadbook

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.giste.roadbook.domain.model.RoadbookPosition
import org.giste.roadbook.ui.RoadbookUiState
import org.giste.roadbook.ui.RoadbookViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RoadbookStateTest {

    private val viewModel: RoadbookViewModel = mockk(relaxed = true)

    @Before
    fun setup() {
        // Mock properties before creating the state to ensure they are captured correctly in the constructor
        every { viewModel.routeName } returns MutableStateFlow(null)
        every { viewModel.events } returns MutableSharedFlow()
        every { viewModel.roadbookState } returns MutableStateFlow(RoadbookUiState.Empty)
        every { viewModel.initialScrollPosition } returns MutableStateFlow(RoadbookPosition())
    }

    @Test
    fun `routeName should delegate to viewModel`() {
        val routeNameFlow = MutableStateFlow<String?>("Test Route")
        every { viewModel.routeName } returns routeNameFlow
        
        val state = RoadbookState(viewModel)

        assertEquals(routeNameFlow, state.routeName)
    }

    @Test
    fun `events should delegate to viewModel`() {
        val eventsFlow = MutableSharedFlow<RoadbookEvent>()
        every { viewModel.events } returns eventsFlow

        val state = RoadbookState(viewModel)

        assertEquals(eventsFlow, state.events)
    }

    @Test
    fun `scrollUp should delegate to viewModel`() {
        val state = RoadbookState(viewModel)
        state.scrollUp()
        verify { viewModel.scrollUp() }
    }

    @Test
    fun `scrollDown should delegate to viewModel`() {
        val state = RoadbookState(viewModel)
        state.scrollDown()
        verify { viewModel.scrollDown() }
    }
}
