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
