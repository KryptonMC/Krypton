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
import com.google.devtools.ksp.symbol.KSVisitor
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import org.kryptonmc.internal.processor.catalogue.CatalogueChecker
import org.kryptonmc.internal.processor.factory.FactoryChecker
import org.kryptonmc.internal.processor.immutable.ImmutabilityChecker
import org.kryptonmc.internal.processor.util.VisitorContext
import kotlin.reflect.KClass

class KryptonSymbolProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val context = VisitorContext(resolver, environment.logger)
        visitAllAnnotatedWith(ImmutableType::class, context, ImmutabilityChecker)
        visitAllAnnotatedWith(TypeFactory::class, context, FactoryChecker)
        visitAllAnnotatedWith(Catalogue::class, context, CatalogueChecker)
        visitAllAnnotatedWith(CataloguedBy::class, context, CatalogueChecker)
        return emptyList()
    }

    private fun visitAllAnnotatedWith(annotation: KClass<*>, context: VisitorContext, visitor: KSVisitor<VisitorContext, Unit>) {
        val symbols = context.resolver.getSymbolsWithAnnotation(annotation.qualifiedName!!)
        var empty = true
        symbols.forEach {
            empty = false
            it.accept(visitor, context)
        }
        if (empty) context.logger.warn("No classes annotated with ${annotation.qualifiedName} found.")
    }
}
