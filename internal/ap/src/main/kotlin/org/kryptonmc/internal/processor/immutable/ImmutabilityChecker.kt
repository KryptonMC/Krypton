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
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import org.kryptonmc.internal.annotations.ImmutableTypeIgnore
import org.kryptonmc.internal.processor.util.ContextualVisitor
import org.kryptonmc.internal.processor.util.VisitorContext

@OptIn(KspExperimental::class)
object ImmutabilityChecker : ContextualVisitor() {

    private const val LOG_IGNORES = false

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: VisitorContext) {
        val validator = when (classDeclaration.classKind) {
            ClassKind.CLASS -> ClassImmutabilityValidator
            ClassKind.INTERFACE -> InterfaceImmutabilityValidator
            else -> return
        }
        validator.validateClass(classDeclaration, data.resolver)
        classDeclaration.getDeclaredProperties().map { visitProperty(it, classDeclaration, data, validator) }
    }

    private fun visitProperty(property: KSPropertyDeclaration, type: KSClassDeclaration, context: VisitorContext, validator: ImmutabilityValidator) {
        if (property.isMutable) {
            error("Property ${property.simpleName.asString()} in immutable type ${type.simpleName.asString()} is mutable!")
        }
        if (property.isAnnotationPresent(ImmutableTypeIgnore::class)) {
            if (LOG_IGNORES) {
                context.logger.info("Ignoring property ${property.simpleName.asString()} in immutable type ${type.simpleName.asString()}")
            }
            return
        }
        validator.validateProperty(property, type, context.resolver)
    }
}
