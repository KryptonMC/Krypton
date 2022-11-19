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

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import io.mockk.mockk
import net.kyori.adventure.text.Component
import org.junit.jupiter.api.Test
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.KryptonBrigadierCommand
import org.kryptonmc.krypton.command.KryptonCommandMeta
import org.kryptonmc.krypton.command.argument
import org.kryptonmc.krypton.command.literal
import org.kryptonmc.krypton.command.registrar.BrigadierCommandRegistrar
import org.kryptonmc.krypton.command.runs
import org.kryptonmc.krypton.util.Vec3dImpl
import java.util.concurrent.locks.ReentrantLock
import kotlin.test.assertEquals

class CommandTests {

    @Test
    fun `test api command nodes work with implementation types`() {
        val dispatcher = CommandDispatcher<CommandSourceStack>()
        val node = literal("test") {
            runs { println("Hello World!") }
        }.build()
        dispatcher.root.addChild(node)
        val source = CommandSourceStack(mockk(), Vec3dImpl.ZERO, 0F, 0F, mockk(), "", Component.empty(), mockk(), null)
        dispatcher.execute("test", source)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `test brigadier command registrar`() {
        val root = RootCommandNode<CommandSourceStack>()
        val node = literal("test") {
            runs { println("Hello World!") }
            argument("hello", IntegerArgumentType.integer()) {
                argument("world", StringArgumentType.string()) {}
            }
        }.build() as LiteralCommandNode<CommandExecutionContext>
        BrigadierCommandRegistrar(ReentrantLock()).register(root, KryptonBrigadierCommand(node), KryptonCommandMeta.Builder("test").build())
        assertEquals(node, root.getChild("test") as CommandNode<CommandExecutionContext>)
    }
}
