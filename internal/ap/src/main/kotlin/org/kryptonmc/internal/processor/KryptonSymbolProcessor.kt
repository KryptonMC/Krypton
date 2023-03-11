/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
