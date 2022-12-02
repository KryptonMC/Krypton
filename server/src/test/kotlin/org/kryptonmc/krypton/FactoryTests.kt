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
package org.kryptonmc.krypton

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.kryptonmc.internal.annotations.TypeFactory
import org.kryptonmc.krypton.util.Bootstrap
import org.kryptonmc.krypton.util.KryptonFactoryProvider
import org.reflections.Reflections
import org.reflections.scanners.Scanners
import org.reflections.util.ConfigurationBuilder
import kotlin.test.Test
import kotlin.test.assertEquals

class FactoryTests {

    @Test
    fun `ensure all factories are registered`() {
        REFLECTIONS.getTypesAnnotatedWith(TypeFactory::class.java).forEach {
            if (!it.isInterface) return // Skip subtypes that are not directly annotated
            val implementations = REFLECTIONS.getSubTypesOf(it)
            assertEquals(1, implementations.size, "Type ${it.canonicalName} has ${implementations.size} implementations!")
            assertDoesNotThrow { KryptonFactoryProvider.provide(it) }
        }
    }

    companion object {

        private val CONFIG = ConfigurationBuilder().forPackages("org.kryptonmc.api", "org.kryptonmc.krypton")
            .addScanners(Scanners.TypesAnnotated, Scanners.SubTypes)
        private val REFLECTIONS = Reflections(CONFIG)

        @JvmStatic
        @BeforeAll
        fun `bootstrap factory provider`() {
            Bootstrap.preload()
        }
    }
}
