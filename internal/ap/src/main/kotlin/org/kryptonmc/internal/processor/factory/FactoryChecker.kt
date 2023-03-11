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
package org.kryptonmc.internal.processor.factory

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import org.jetbrains.annotations.ApiStatus
import org.kryptonmc.internal.processor.util.ContextualVisitor
import org.kryptonmc.internal.processor.util.VisitorContext

object FactoryChecker : ContextualVisitor() {

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: VisitorContext) {
        val parent = classDeclaration.parentDeclaration
        if (parent == null || parent !is KSClassDeclaration) fail(classDeclaration, "Must be a member type")
        if (classDeclaration.classKind != ClassKind.INTERFACE) fail(classDeclaration, "Must be an interface")
        if (!classDeclaration.isAnnotationPresent(ApiStatus.Internal::class)) fail(classDeclaration, "Must be marked with @ApiStatus.Internal")
        if (classDeclaration.getDeclaredProperties().count() != 0) fail(classDeclaration, "Must not have properties")
        if (classDeclaration.getDeclaredFunctions().count() == 0) fail(classDeclaration, "Must have at least one factory function")
    }

    private fun fail(type: KSClassDeclaration, message: String) {
        error("Type factory ${type.simpleName.asString()} is invalid! $message!")
    }
}
