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
package org.kryptonmc.krypton.command.arguments

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.adventure.KryptonAdventure

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
