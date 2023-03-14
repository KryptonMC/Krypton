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
