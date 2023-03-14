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
package org.kryptonmc.internal.processor.catalogue

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getVisibility
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.google.devtools.ksp.symbol.Nullability
import com.google.devtools.ksp.symbol.Visibility
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.annotations.IgnoreNotCataloguedBy
import org.kryptonmc.internal.processor.getClassArgumentByName
import org.kryptonmc.internal.processor.getKSAnnotation

@OptIn(KspExperimental::class)
object CatalogueTypeValidator : CatalogueValidator {

    override fun validateClass(type: KSClassDeclaration) {
        if (type.isAnnotationPresent(IgnoreNotCataloguedBy::class)) return
        val cataloguedBy = getCataloguedType(type).getKSAnnotation(CataloguedBy::class)
        check(cataloguedBy.getClassArgumentByName("catalogue").qualifiedName?.asString() == type.qualifiedName!!.asString()) {
            "Catalogue mismatch for ${type.simpleName.asString()}!"
        }
    }

    override fun validateProperty(property: KSPropertyDeclaration, type: KSClassDeclaration) {
        if (property.getVisibility() != Visibility.PUBLIC) return // Only need to check public properties for compliance
        if (!property.isAnnotationPresent(JvmField::class)) fail(property, type, "must all be @JvmField")
        if (property.isMutable) fail(property, type, "must all be values")
        val propertyType = property.type.resolve()
        if (propertyType.nullability != Nullability.NOT_NULL) fail(property, type, "must all be non-null")

        val cataloguedType = getCataloguedType(type)
        if (!propertyType.isAssignableFrom(cataloguedType.asStarProjectedType())) {
            fail(property, type, "must all be of type ${cataloguedType.qualifiedName!!.asString()}")
        }
    }

    @JvmStatic
    private fun getCataloguedType(type: KSClassDeclaration): KSClassDeclaration =
        type.getKSAnnotation(Catalogue::class).getClassArgumentByName("type")

    @JvmStatic
    private fun fail(property: KSPropertyDeclaration, type: KSClassDeclaration, message: String) {
        error("Public properties in catalogue type ${type.simpleName.asString()} $message! ${property.simpleName.asString()} was not.")
    }
}
