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
package org.kryptonmc.krypton.api

import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * An extension that scans for initializers and runs them before and after
 * test set runs.
 */
class InitializerExtension : BeforeAllCallback, AfterAllCallback {

    override fun beforeAll(context: ExtensionContext) {
        beforeOrAfterAll(context, Initializer::initialize)
    }

    override fun afterAll(context: ExtensionContext) {
        beforeOrAfterAll(context, Initializer::tearDown)
    }

    private inline fun beforeOrAfterAll(context: ExtensionContext, action: (Initializer) -> Unit) {
        val testClass = context.testClass.orElseThrow { IllegalStateException("Could not find test class for context $context!") }
        val annotation = requireNotNull(testClass.getAnnotation(Initializers::class.java)) {
            "Could not resolve required initializers annotation for initializer extension!"
        }
        annotation.initializers.forEach {
            val instance = requireNotNull(it.objectInstance) { "Initializer must be an object!" }
            action(instance)
        }
    }
}
