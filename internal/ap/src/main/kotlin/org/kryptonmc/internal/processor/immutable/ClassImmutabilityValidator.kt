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
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.Modifier

object ClassImmutabilityValidator : ImmutabilityValidator {

    @OptIn(KspExperimental::class)
    override fun validateClass(type: KSClassDeclaration, resolver: Resolver) {
        if (type.classKind != ClassKind.CLASS) error("Expected class for record validation strategy, given $type!")
        if (!type.modifiers.contains(Modifier.DATA)) error("Expected type $type to be a data class!")
        if (!type.isAnnotationPresent(JvmRecord::class)) error("Expected JvmRecord annotation to be present for class type $type!")
    }

    override fun validateProperty(property: KSPropertyDeclaration, declaringType: KSClassDeclaration, resolver: Resolver) {
        // Nothing to do
    }
}
