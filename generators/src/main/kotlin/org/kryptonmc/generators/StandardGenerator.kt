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
