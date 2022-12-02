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
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.ImmutableTypeIgnore

object ImmutabilityChecker : KSEmptyVisitor<ImmutabilityCheckerContext, Unit>() {

    private const val LOG_IGNORES = false

    override fun defaultHandler(node: KSNode, data: ImmutabilityCheckerContext) {
        // Do nothing
    }

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: ImmutabilityCheckerContext) {
        if (!classDeclaration.isAnnotationPresent(ImmutableType::class)) return
        data.validator.validateType(classDeclaration)
        classDeclaration.getDeclaredProperties().map { it.accept(this, data) }
    }

    @OptIn(KspExperimental::class)
    override fun visitPropertyDeclaration(property: KSPropertyDeclaration, data: ImmutabilityCheckerContext) {
        val declaringType = property.parentDeclaration as? KSClassDeclaration ?: return
        if (property.isMutable) {
            data.logger.error("Property ${property.simpleName.asString()} in immutable type ${declaringType.simpleName.asString()} is mutable!")
            return
        }
        if (property.isAnnotationPresent(ImmutableTypeIgnore::class)) {
            if (LOG_IGNORES) {
                data.logger.info("Ignoring property ${property.simpleName.asString()} in immutable type ${declaringType.simpleName.asString()}")
            }
            return
        }
        data.validator.validateProperty(property, declaringType)
    }
}
