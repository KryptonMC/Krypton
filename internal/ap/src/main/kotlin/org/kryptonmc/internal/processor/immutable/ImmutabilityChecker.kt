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
