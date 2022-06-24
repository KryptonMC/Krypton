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
package org.kryptonmc.krypton.entity.vehicle

import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.vehicle.CommandBlockMinecart
import org.kryptonmc.api.entity.vehicle.MinecartType
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.CommandBlockHandler
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonCommandBlockMinecart(world: KryptonWorld) : KryptonMinecartLike(world, EntityTypes.COMMAND_BLOCK_MINECART), CommandBlockMinecart {

    internal val commandBlock = Handler()
    override val minecartType: MinecartType
        get() = MinecartType.COMMAND_BLOCK
    override var command: String
        get() = data[MetadataKeys.COMMAND_BLOCK_MINECART.COMMAND]
        set(value) = data.set(MetadataKeys.COMMAND_BLOCK_MINECART.COMMAND, value)
    override var lastOutput: Component
        get() = data[MetadataKeys.COMMAND_BLOCK_MINECART.LAST_OUTPUT]
        set(value) = data.set(MetadataKeys.COMMAND_BLOCK_MINECART.LAST_OUTPUT, value)

    override val defaultCustomBlock: Block
        get() = Blocks.COMMAND_BLOCK

    init {
        data.add(MetadataKeys.COMMAND_BLOCK_MINECART.COMMAND)
        data.add(MetadataKeys.COMMAND_BLOCK_MINECART.LAST_OUTPUT)
    }

    override fun onDataUpdate(key: MetadataKey<*>) {
        super.onDataUpdate(key)
        when (key) {
            MetadataKeys.COMMAND_BLOCK_MINECART.LAST_OUTPUT -> {
                try {
                    commandBlock.lastOutput = lastOutput
                } catch (_: Exception) {}
            }
            MetadataKeys.COMMAND_BLOCK_MINECART.COMMAND -> commandBlock.command = command
        }
    }

    inner class Handler : CommandBlockHandler(server) {

        override fun onUpdated() {
            this@KryptonCommandBlockMinecart.command = command
            this@KryptonCommandBlockMinecart.lastOutput = lastOutput ?: Component.empty()
        }
    }
}
