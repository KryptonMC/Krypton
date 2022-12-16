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

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.processor.getClassArgumentByName
import org.kryptonmc.internal.processor.getKSAnnotation

object CataloguedTypeValidator : CatalogueValidator {

    override fun validateClass(type: KSClassDeclaration) {
        val catalogue = type.getKSAnnotation(CataloguedBy::class).getClassArgumentByName("catalogue").getKSAnnotation(Catalogue::class)
        check(catalogue.getClassArgumentByName("type").qualifiedName?.asString() == type.qualifiedName!!.asString()) {
            "Catalogue mismatch for ${type.simpleName.asString()}!"
        }
    }

    override fun validateProperty(property: KSPropertyDeclaration, type: KSClassDeclaration) {
        // Do nothing for catalogued types
    }
}
