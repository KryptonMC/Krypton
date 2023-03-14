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
package org.kryptonmc.internal.processor.immutable

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference

object InterfaceImmutabilityValidator : ImmutabilityValidator {

    private val DEFAULT_BOOLEAN_REGEX = "is[A-Z].*".toRegex()

    override fun validateClass(type: KSClassDeclaration, resolver: Resolver) {
        if (type.classKind != ClassKind.INTERFACE) error("Expected interface for interface validation strategy, given $type!")
    }

    @OptIn(KspExperimental::class)
    override fun validateProperty(property: KSPropertyDeclaration, declaringType: KSClassDeclaration, resolver: Resolver) {
        val getter = property.getter ?: return
        val returnType = getter.returnType ?: return
        if (isBoolean(resolver, returnType) && property.simpleName.asString().matches(DEFAULT_BOOLEAN_REGEX)) return
        if (resolver.getJvmName(getter) != property.simpleName.asString()) {
            error("Expected JvmName for getter on property ${property.simpleName} in immutable type ${declaringType.simpleName} to be the same" +
                    " as the property name!")
        }
    }

    @JvmStatic
    private fun isBoolean(resolver: Resolver, type: KSTypeReference): Boolean = type.resolve().isAssignableFrom(resolver.builtIns.booleanType)
}
