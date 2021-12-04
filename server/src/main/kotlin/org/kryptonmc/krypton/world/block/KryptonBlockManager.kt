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
package org.kryptonmc.krypton.world.block

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockHandler
import org.kryptonmc.api.block.BlockManager

@Suppress("INAPPLICABLE_JVM_NAME")
object KryptonBlockManager : BlockManager {

    @get:JvmName("handlers")
    override val handlers = mutableMapOf<String, BlockHandler>()

    override fun handler(key: String): BlockHandler? = handlers[key]

    override fun handler(key: Key): BlockHandler? = handlers[key.asString()]

    override fun handler(block: Block): BlockHandler? = handlers[block.key().asString()]

    override fun register(key: String, handler: BlockHandler) {
        handlers[key] = handler
    }

    override fun register(key: Key, handler: BlockHandler) {
        register(key.asString(), handler)
    }

    override fun register(block: Block, handler: BlockHandler) {
        register(block.key().asString(), handler)
    }
}
