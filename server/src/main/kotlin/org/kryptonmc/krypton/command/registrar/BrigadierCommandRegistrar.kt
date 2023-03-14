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
package org.kryptonmc.krypton.command.registrar

import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.CommandMeta
import org.kryptonmc.krypton.command.CommandSourceStack
import java.util.concurrent.locks.Lock

/**
 * Registers Brigadier commands to a root node. Brigadier commands are really
 * easy to register, as they are already backed by nodes that we are able to
 * add to the tree.
 */
class BrigadierCommandRegistrar(lock: Lock) : AbstractCommandRegistrar<BrigadierCommand>(lock) {

    @Suppress("UNCHECKED_CAST")
    override fun register(root: RootCommandNode<CommandSourceStack>, command: BrigadierCommand, meta: CommandMeta) {
        val literal = command.node
        val name = literal.name
        if (name == name.lowercase()) register(root, literal as LiteralCommandNode<CommandSourceStack>)
        meta.aliases.forEach { if (name != it) register(root, literal as LiteralCommandNode<CommandSourceStack>, it) }
    }
}
