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
