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
import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.internal.annotations.CataloguedBy
import org.kryptonmc.internal.processor.util.ContextualVisitor
import org.kryptonmc.internal.processor.util.VisitorContext

object CatalogueChecker : ContextualVisitor() {

    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: VisitorContext) {
        val validator = when {
            classDeclaration.isAnnotationPresent(Catalogue::class) -> CatalogueTypeValidator
            classDeclaration.isAnnotationPresent(CataloguedBy::class) -> CataloguedTypeValidator
            else -> return
        }
        validator.validateClass(classDeclaration)
        classDeclaration.getDeclaredProperties().map { validator.validateProperty(it, classDeclaration) }
    }
}
