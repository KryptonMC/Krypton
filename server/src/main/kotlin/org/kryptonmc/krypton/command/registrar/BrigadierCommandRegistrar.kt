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
package org.kryptonmc.krypton.command.registrar

import com.mojang.brigadier.tree.RootCommandNode
import org.kryptonmc.api.command.BrigadierCommand
import org.kryptonmc.api.command.CommandMeta
import org.kryptonmc.api.command.Sender
import java.util.concurrent.locks.Lock

/**
 * Registers Brigadier commands to a root node. Brigadier commands are really
 * easy to register, as they are already backed by nodes that we are able to
 * add to the tree.
 */
class BrigadierCommandRegistrar(lock: Lock) : KryptonCommandRegistrar<BrigadierCommand>(lock) {

    override fun register(root: RootCommandNode<Sender>, command: BrigadierCommand, meta: CommandMeta) {
        val literal = command.node
        val name = literal.name
        if (name == name.lowercase()) register(root, literal)
        meta.aliases.forEach {
            if (name == it) return@forEach
            register(root, literal, it)
        }
    }
}
