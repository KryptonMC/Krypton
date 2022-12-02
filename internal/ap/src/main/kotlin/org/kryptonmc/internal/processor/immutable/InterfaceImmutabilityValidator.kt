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
package org.kryptonmc.internal.processor.immutable

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference

class InterfaceImmutabilityValidator(private val resolver: Resolver) : ImmutabilityValidator {

    override fun validateType(type: KSClassDeclaration) {
        if (type.classKind != ClassKind.INTERFACE) error("Expected interface for interface validation strategy, given $type!")
    }

    @OptIn(KspExperimental::class)
    override fun validateProperty(property: KSPropertyDeclaration, declaringType: KSClassDeclaration) {
        val getter = property.getter ?: return
        val returnType = getter.returnType ?: return
        if (isBoolean(resolver, returnType) && property.simpleName.asString().matches(DEFAULT_BOOLEAN_REGEX)) return
        val jvmName = resolver.getJvmName(getter)
        if (jvmName != property.simpleName.asString()) {
            error("Expected JvmName for getter on property ${property.simpleName} in immutable type ${declaringType.simpleName} to be the same" +
                    " as the property name!")
        }
    }

    companion object {

        private val DEFAULT_BOOLEAN_REGEX = "is[A-Z].*".toRegex()

        @JvmStatic
        private fun isBoolean(resolver: Resolver, type: KSTypeReference): Boolean = type.resolve().isAssignableFrom(resolver.builtIns.booleanType)
    }
}
