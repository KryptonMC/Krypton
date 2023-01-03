/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.adventure

import net.kyori.adventure.text.Component
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.krypton.testutil.Bootstrapping
import kotlin.test.assertEquals

class TranslatableFlatteningTest {

    @Test
    fun `ensure translates language name using Minecraft translations`() {
        var called = false
        KryptonAdventure.FLATTENER.flatten(Component.translatable("language.name")) {
            called = true
            assertEquals("English", it)
        }
        // This just verifies that the assertion is called, and ensures the test fails when the assertion isn't called, as that means we have a
        // broken test.
        check(called) { "Flattener listener was not called! This is an error with the test!" }
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `load translations and factories`() {
            Bootstrapping.loadTranslations()
        }
    }
}
