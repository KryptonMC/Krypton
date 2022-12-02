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
package org.kryptonmc.internal.processor

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import org.kryptonmc.internal.processor.factory.FactoryChecker
import org.kryptonmc.internal.processor.factory.FactoryCheckerContext
import org.kryptonmc.internal.processor.immutable.ImmutabilityChecker
import org.kryptonmc.internal.processor.immutable.ImmutabilityCheckerContext
import org.kryptonmc.internal.processor.immutable.ImmutabilityValidator

class KryptonSymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getAllFiles().mapNotNull { it.declarations.singleOrNull() as? KSClassDeclaration }.forEach {
            it.accept(ImmutabilityChecker, ImmutabilityCheckerContext(resolver, environment.logger) { ImmutabilityValidator.select(resolver, it) })
            it.accept(FactoryChecker, FactoryCheckerContext(resolver, environment.logger))
        }
        return emptyList()
    }
}
