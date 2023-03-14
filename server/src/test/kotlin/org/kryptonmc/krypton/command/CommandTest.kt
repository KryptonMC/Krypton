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
package org.kryptonmc.krypton.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.CommandNode
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import io.mockk.mockk
import net.kyori.adventure.text.Component
import org.junit.jupiter.api.Test
import org.kryptonmc.api.command.CommandExecutionContext
import org.kryptonmc.api.command.argument
import org.kryptonmc.api.command.literalCommand
import org.kryptonmc.api.util.Position
import org.kryptonmc.krypton.command.registrar.BrigadierCommandRegistrar
import org.kryptonmc.krypton.commands.runs
import java.util.concurrent.locks.ReentrantLock
import kotlin.test.assertEquals

class CommandTest {

    @Test
    fun `test api command nodes work with implementation types`() {
        val dispatcher = CommandDispatcher<CommandSourceStack>()
        val node = LiteralArgumentBuilder.literal<CommandSourceStack>("test").runs { println("Hello World!") }.build()
        dispatcher.root.addChild(node)
        val source = CommandSourceStack(mockk(), Position.ZERO, mockk(), "", Component.empty(), mockk(), null)
        dispatcher.execute("test", source)
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `test brigadier command registrar`() {
        val root = RootCommandNode<CommandSourceStack>()
        val node = literalCommand<CommandSourceStack>("test") {
            runs { println("Hello World!") }
            argument("hello", IntegerArgumentType.integer()) {
                argument("world", StringArgumentType.string()) {}
            }
        }.build() as LiteralCommandNode<CommandExecutionContext>
        BrigadierCommandRegistrar(ReentrantLock()).register(root, KryptonBrigadierCommand(node), KryptonCommandMeta.Builder("test").build())
        assertEquals(node, root.getChild("test") as CommandNode<CommandExecutionContext>)
    }
}
