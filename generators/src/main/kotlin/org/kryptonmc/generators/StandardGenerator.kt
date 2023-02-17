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
package org.kryptonmc.generators

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import net.minecraft.core.Registry
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.writeText

open class StandardGenerator(private val output: Path) {

    protected open fun <S, T> collectFields(catalogueType: Class<S>, type: Class<T>): Sequence<CollectedField<T>> =
        catalogueType.collectFields(type)

    fun <S, T> run(
        catalogueType: Class<S>,
        type: Class<T>,
        name: ClassName,
        returnType: ClassName,
        registryName: String,
        keyGetter: KeyGetter<T>
    ) {
        val file = FileSpec.catalogueType(name)
        val outputClass = TypeSpec.catalogueType(name, returnType).registryOfMethod(returnType, registryName)
        collectFields(catalogueType, type).forEach { outputClass.catalogueField(it, returnType, keyGetter) }
        val out = StringBuilder()
        file.addType(outputClass.build()).build().writeTo(out)
        val outputFile = output.resolve(name.packageName.replace('.', '/')).tryCreateDirectories().resolve("${name.simpleName}.kt")
        if (outputFile.exists()) return
        outputFile.tryCreateFile().writeText(out.toString().performReplacements(returnType.simpleName, name.simpleName))
    }

    inline fun <reified S, reified T> run(registry: Registry<T>, name: String, returnType: String, registryName: String) {
        run(S::class.java, T::class.java, className(name), className(returnType), registryName) { registry.getKey(it.value)!! }
    }

    companion object {

        @JvmStatic
        @PublishedApi
        internal fun className(name: String): ClassName {
            val lastSeparator = name.lastIndexOf('.')
            require(lastSeparator != -1) { "Cannot create class name from name without '.' separator!" }
            return ClassName("$PACKAGE.${name.substring(0, lastSeparator)}", name.substring(lastSeparator + 1))
        }
    }
}
