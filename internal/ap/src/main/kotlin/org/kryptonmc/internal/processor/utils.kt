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

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import kotlin.reflect.KClass

fun <T : Annotation> KSAnnotated.getKSAnnotation(annotationType: KClass<T>): KSAnnotation = annotations
    .filter { it.shortName.getShortName() == annotationType.simpleName }
    .filter { it.annotationType.resolve().declaration.qualifiedName?.asString() == annotationType.qualifiedName }
    .singleOrNull()
    ?: error("Cannot find ${annotationType.simpleName} on type $this")

fun KSAnnotation.getClassArgumentByName(name: String): KSClassDeclaration {
    val argument = arguments.firstOrNull { it.name?.asString() == name }?.value
        ?: throw AssertionError("No argument with name $name! Was $shortName updated?")
    if (argument !is KSType) throw AssertionError("Value of argument $name was not a type! Was $shortName updated?")
    return argument.declaration as? KSClassDeclaration
        ?: throw AssertionError("Type declaration for argument $name was not a class! Was $shortName updated?")
}
