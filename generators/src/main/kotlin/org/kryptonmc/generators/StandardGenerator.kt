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

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import net.minecraft.core.Registry
import java.lang.reflect.Modifier
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.writeText

open class StandardGenerator(@PublishedApi internal val output: Path) {

    protected open fun <S, T> collectFields(catalogueType: Class<S>, type: Class<T>): Sequence<CollectedField> =
        catalogueType.declaredFields.asSequence()
            .filter { Modifier.isStatic(it.modifiers) }
            .filter { type.isAssignableFrom(it.type) }
            .map(::CollectedField)

    @Suppress("UNCHECKED_CAST")
    fun <S, T> run(
        catalogueType: Class<S>,
        type: Class<T>,
        registry: Registry<*>,
        name: String,
        returnType: String,
        registryName: String,
        keyGetter: KeyGetter = KeyGetter { (registry as Registry<Any>).getKey(it.value)!! }
    ) {
        val pkg = "org.kryptonmc.api"
        val className = className(name)
        val classReturnType = className(returnType)
        val file = FileSpec.builder(className.packageName, className.simpleName)
            .indent("    ")
            .addImport("net.kyori.adventure.key", "Key")
            .addImport("$pkg.registry", "Registries")
        val outputClass = TypeSpec.objectBuilder(className)
            .addKdoc("This file is auto-generated. Do not edit this manually!")
            .addAnnotation(AnnotationSpec.builder(ClassName("org.kryptonmc.api.util", "Catalogue"))
                .addMember("${classReturnType.simpleName}::class")
                .build())
            .addFunction(FunSpec.builder("get")
                .returns(classReturnType)
                .addParameter("key", ClassName("kotlin", "String"))
                .addAnnotation(JvmStatic::class)
                .addModifiers(KModifier.PRIVATE)
                .addCode("return Registries.$registryName[Key.key(key)]!!")
                .build())
        collectFields(catalogueType, type).forEach {
            outputClass.addProperty(PropertySpec.builder(it.name, classReturnType)
                .addAnnotation(JvmField::class)
                .initializer("get(\"${keyGetter.key(it).path}\")")
                .build())
        }
        val stringBuilder = StringBuilder()
        file.addType(outputClass.build())
            .build()
            .writeTo(stringBuilder)
        val outputFile = output.resolve(className.packageName.replace('.', '/'))
            .tryCreateDirectories()
            .resolve("${className.simpleName}.kt")
        if (outputFile.exists()) return
        outputFile.tryCreateFile().writeText(stringBuilder.toString().performReplacements(classReturnType.simpleName, className.simpleName))
    }

    companion object {

        @JvmStatic
        private fun className(name: String): ClassName {
            val parts = name.split("/")
            return ClassName("org.kryptonmc.api.${parts[0]}", parts[1])
        }
    }
}

inline fun <reified S, reified T> StandardGenerator.run(
    registry: Registry<*>,
    name: String,
    returnType: String,
    registryName: String,
    keyGetter: KeyGetter = KeyGetter { (registry as Registry<Any>).getKey(it.value)!! }
) {
    run(S::class.java, T::class.java, registry, name, returnType, registryName, keyGetter)
}
