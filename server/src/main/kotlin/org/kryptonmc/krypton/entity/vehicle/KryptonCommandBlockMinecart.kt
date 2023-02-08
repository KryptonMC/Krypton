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
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.kryptonmc.api.entity.vehicle.CommandBlockMinecart
import org.kryptonmc.api.entity.vehicle.MinecartVariant
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKey
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.vehicle.CommandBlockMinecartSerializer
import org.kryptonmc.krypton.world.command.CommandBlockHandler
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState

class KryptonCommandBlockMinecart(world: KryptonWorld) : KryptonMinecartLike(world), CommandBlockMinecart {

    override val type: KryptonEntityType<KryptonCommandBlockMinecart>
        get() = KryptonEntityTypes.COMMAND_BLOCK_MINECART
    override val serializer: EntitySerializer<KryptonCommandBlockMinecart>
        get() = CommandBlockMinecartSerializer

    internal val commandBlock = Handler()
    override val variant: MinecartVariant
        get() = MinecartVariant.COMMAND_BLOCK
    override var command: String
        get() = data.get(MetadataKeys.CommandBlockMinecart.COMMAND)
        set(value) = data.set(MetadataKeys.CommandBlockMinecart.COMMAND, value)
    override var lastOutput: Component
        get() = data.get(MetadataKeys.CommandBlockMinecart.LAST_OUTPUT)
        set(value) = data.set(MetadataKeys.CommandBlockMinecart.LAST_OUTPUT, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.CommandBlockMinecart.COMMAND, "")
        data.define(MetadataKeys.CommandBlockMinecart.LAST_OUTPUT, Component.empty())
    }

    override fun onDataUpdate(key: MetadataKey<*>) {
        super.onDataUpdate(key)
        when (key) {
            MetadataKeys.CommandBlockMinecart.LAST_OUTPUT -> commandBlock.lastOutput = lastOutput
            MetadataKeys.CommandBlockMinecart.COMMAND -> commandBlock.command = command
        }
    }

    override fun defaultCustomBlock(): KryptonBlockState = KryptonBlocks.COMMAND_BLOCK.defaultState

    inner class Handler : CommandBlockHandler() {

        override fun world(): KryptonWorld = this@KryptonCommandBlockMinecart.world

        override fun createCommandSourceStack(): CommandSourceStack = CommandSourceStack(this, position, world(),
            PlainTextComponentSerializer.plainText().serialize(name), displayName, world().server, this@KryptonCommandBlockMinecart)

        override fun onUpdated() {
            this@KryptonCommandBlockMinecart.command = command
            this@KryptonCommandBlockMinecart.lastOutput = lastOutput ?: Component.empty()
        }
    }
}
