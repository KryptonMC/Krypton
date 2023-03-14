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
