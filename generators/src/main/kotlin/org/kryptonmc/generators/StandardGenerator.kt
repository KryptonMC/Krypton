/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
import net.minecraft.resources.ResourceLocation
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.writeText

class StandardGenerator(private val output: Path) {

    @Suppress("UNCHECKED_CAST")
    fun run(
        source: Class<*>,
        registry: Registry<*>,
        name: ClassName,
        sourceType: Class<*>,
        returnType: ClassName,
        registryName: String,
        keyGetter: (Field) -> ResourceLocation = { (registry as Registry<Any>).getKey(it.get(null))!! }
    ) {
        val file = FileSpec.builder(name.packageName, name.simpleName)
            .indent("    ")
            .addImport("net.kyori.adventure.key", "Key")
            .addImport("org.kryptonmc.api.registry", "Registries")
        val outputClass = TypeSpec.objectBuilder(name)
            .addKdoc("This file is auto-generated. Do not edit this manually!")
            .addAnnotation(AnnotationSpec.builder(ClassName("org.kryptonmc.api.util", "Catalogue"))
                .addMember("${returnType.simpleName}::class")
                .build())
            .addFunction(FunSpec.builder("get")
                .returns(returnType)
                .addParameter("key", ClassName("kotlin", "String"))
                .addAnnotation(JvmStatic::class)
                .addModifiers(KModifier.PRIVATE)
                .addCode("return Registries.$registryName[Key.key(key)]!!")
                .build())
        source.declaredFields.asSequence()
            .filter { Modifier.isStatic(it.modifiers) }
            .filter { sourceType.isAssignableFrom(it.type) }
            .forEach {
                outputClass.addProperty(PropertySpec.builder(it.name, returnType)
                    .addAnnotation(JvmField::class)
                    .initializer("get(\"${keyGetter(it).path}\")")
                    .build())
            }
        val stringBuilder = StringBuilder()
        file.addType(outputClass.build())
            .build()
            .writeTo(stringBuilder)
        val outputFile = output.resolve(name.packageName.replace('.', '/'))
            .tryCreateDirectories()
            .resolve("${name.simpleName}.kt")
        if (outputFile.exists()) return
        outputFile.tryCreateFile().writeText(stringBuilder.toString().performReplacements(returnType.simpleName, name.simpleName))
    }
}
