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
package org.kryptonmc.krypton.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.adventure.KryptonAdventure

/**
 * Used as an easy way to create command exception types that return a translatable message from their arguments.
 */
object CommandExceptions {

    @JvmStatic
    fun simple(key: String): SimpleCommandExceptionType = SimpleCommandExceptionType(KryptonAdventure.asMessage(Component.translatable(key)))

    @JvmStatic
    fun dynamic(key: String): DynamicCommandExceptionType =
        DynamicCommandExceptionType { KryptonAdventure.asMessage(Component.translatable(key, Component.text(it.toString()))) }

    @JvmStatic
    fun dynamic2(key: String): Dynamic2CommandExceptionType = Dynamic2CommandExceptionType { a, b ->
        KryptonAdventure.asMessage(Component.translatable(key, Component.text(a.toString()), Component.text(b.toString())))
    }

    @JvmStatic
    fun resetAndThrow(reader: StringReader, position: Int, exception: CommandSyntaxException): Nothing {
        reader.cursor = position
        throw exception
    }
}
