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

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import com.mojang.brigadier.tree.RootCommandNode
import org.kryptonmc.api.command.Command
import org.kryptonmc.api.command.CommandMeta
import org.kryptonmc.krypton.command.CommandSourceStack
import java.util.concurrent.locks.Lock
import kotlin.concurrent.withLock

abstract class AbstractCommandRegistrar<C : Command>(private val lock: Lock) {

    abstract fun register(root: RootCommandNode<CommandSourceStack>, command: C, meta: CommandMeta)

    protected fun register(root: RootCommandNode<CommandSourceStack>, node: LiteralCommandNode<CommandSourceStack>) {
        lock.withLock {
            root.removeChildByName(node.name)
            root.addChild(node)
        }
    }

    protected fun register(root: RootCommandNode<CommandSourceStack>, node: LiteralCommandNode<CommandSourceStack>, alias: String) {
        register(root, copyRedirect(node, alias))
    }

    companion object {

        @JvmStatic
        private fun copyRedirect(source: LiteralCommandNode<CommandSourceStack>, redirectAlias: String): LiteralCommandNode<CommandSourceStack> {
            val builder = LiteralArgumentBuilder.literal<CommandSourceStack>(redirectAlias)
                .requires(source.requirement)
                .requiresWithContext(source.contextRequirement)
                .forward(source.redirect, source.redirectModifier, source.isFork)
                .executes(source.command)
            source.children.forEach { builder.then(it) }
            return builder.build()
        }
    }
}
