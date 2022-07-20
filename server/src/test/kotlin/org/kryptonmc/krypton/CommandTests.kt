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
package org.kryptonmc.krypton

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.RootCommandNode
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.command.meta.CommandMeta
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.registrar.BrigadierCommandRegistrar
import org.kryptonmc.krypton.util.Bootstrap
import java.util.concurrent.locks.ReentrantLock
import kotlin.test.assertEquals

class CommandTests {

    @Test
    fun `test brigadier command registrar`() {
        val root = RootCommandNode<Sender>()
        val node = literal("test") {
            argument("hello", IntegerArgumentType.integer()) {
                argument("world", StringArgumentType.string()) {}
            }
        }.build()
        BrigadierCommandRegistrar(ReentrantLock()).register(root, BrigadierCommand.of(node), CommandMeta.builder("test").build())
        assertEquals(node, root.getChild("test"))
    }

    companion object {

        @JvmStatic
        @BeforeAll
        fun `preload and inject`() {
            Bootstrap.preload()
        }
    }
}
