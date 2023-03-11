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
package org.kryptonmc.krypton.entity.vehicle

import net.kyori.adventure.text.Component
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

        override fun createCommandSourceStack(): CommandSourceStack {
            return CommandSourceStack(this, position, world(), name, displayName, world().server, this@KryptonCommandBlockMinecart)
        }

        override fun onUpdated() {
            this@KryptonCommandBlockMinecart.command = command
            this@KryptonCommandBlockMinecart.lastOutput = lastOutput ?: Component.empty()
        }
    }
}
