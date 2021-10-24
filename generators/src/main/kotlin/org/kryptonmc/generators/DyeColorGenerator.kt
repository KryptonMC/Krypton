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

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import net.minecraft.world.item.DyeColor
import java.awt.Color
import java.lang.reflect.Modifier
import java.nio.file.Path

class DyeColorGenerator(output: Path) : EnumRegistryGenerator(output) {

    override fun generate(file: FileSpec.Builder, clazz: TypeSpec.Builder) {
        file.addImport("java.awt", "Color")
        file.addImport("net.kyori.adventure.text.format", "TextColor")
        clazz.addFunction(FunSpec.builder("register")
            .returns(DYE_COLOR)
            .addParameter("name", ClassName("kotlin", "String"))
            .addParameter("color", COLOR)
            .addParameter("fireworkColor", COLOR)
            .addParameter("textColor", Int::class)
            .addAnnotation(JvmStatic::class)
            .addModifiers(KModifier.PRIVATE)
            .addCode("""
                val·key·=·Key.key(name)
                return·Registries.register(Registries.DYE_COLORS,·key,·DyeColor.of(key,·color,·fireworkColor,·TextColor.color(textColor)))
            """.trimIndent())
            .build())
        DyeColor::class.java.declaredFields.asSequence()
            .filter { Modifier.isStatic(it.modifiers) }
            .filter { it.isEnumConstant }
            .forEach {
                val value = it[null] as DyeColor
                clazz.addProperty(PropertySpec.builder(it.name, DYE_COLOR)
                    .addAnnotation(JvmField::class)
                    .initializer("register(\"${value.serializedName}\",·Color(${value.materialColor.col}),·Color(${value.fireworkColor}),·" +
                            "${value.textColor})")
                    .build())
            }
    }

    companion object {

        val DYE_COLOR = ClassName("org.kryptonmc.api.item.meta", "DyeColor")
        private val COLOR = Color::class.asClassName()
    }
}
