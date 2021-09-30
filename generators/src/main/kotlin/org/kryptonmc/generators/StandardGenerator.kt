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
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText

class StandardGenerator(private val output: Path) {

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
            .addAnnotation(AnnotationSpec.builder(Suppress::class)
                .addMember("\"UndocumentedPublicProperty\", \"LargeClass\"")
                .build())
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
        output.resolve(name.packageName.replace('.', '/'))
            .tryCreateDirectories()
            .resolve("${name.simpleName}.kt")
            .apply { deleteIfExists() }
            .tryCreateFile()
            .writeText(stringBuilder.toString()
                .replace(
                    "@JvmField\n {4}public val (.*): ${returnType.simpleName} =(\n {12})?(.*)(\n)?".toRegex(),
                    "@JvmField public val $1: ${returnType.simpleName} = $3"
                )
                .replace("=  ", "= ")
                .replace(
                    "public object ${name.simpleName} {\n",
                    "public object ${name.simpleName} {\n\n    // @formatter:off\n"
                )
                .replace("\n    @JvmStatic", "\n\n    // @formatter:on\n    @JvmStatic")
                .replace("`get`", "get"))
    }
}
