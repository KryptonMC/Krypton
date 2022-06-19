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
package org.kryptonmc.krypton.item.handler

import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.util.InteractionResult
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.InteractionContext
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.util.findRelative
import org.kryptonmc.krypton.world.KryptonWorld

object DebugStickHandler : ItemHandler {

    override fun canAttackBlock(player: KryptonPlayer, world: KryptonWorld, block: Block, x: Int, y: Int, z: Int): Boolean {
        handleInteraction(player, world, block, x, y, z, false, player.heldItem(Hand.MAIN))
        return false
    }

    override fun interact(context: InteractionContext): InteractionResult {
        val player = context.player
        val item = context.heldItem
        val world = context.world
        val position = context.position
        if (!handleInteraction(player, world, world.getBlock(position), position.x(), position.y(), position.z(), true, item)) {
            return InteractionResult.FAIL
        }
        return InteractionResult.CONSUME
    }

    // TODO: We need to get information about where the item was so we can replace it with a copy that has the modified metadata,
    //  as all item stacks are immutable, so we can't just simply modify the data on the item like vanilla does.
    @Suppress("UNCHECKED_CAST") // Screw you too generics, no wonder you have no friends
    @JvmStatic
    private fun handleInteraction(
        player: KryptonPlayer,
        world: KryptonWorld,
        block: Block,
        x: Int,
        y: Int,
        z: Int,
        isUse: Boolean,
        item: KryptonItemStack
    ): Boolean {
        if (!player.canUseGameMasterBlocks) return false
        val properties = block.availableProperties
        val key = block.key().asString()
        if (properties.isEmpty()) {
            player.sendActionBar(Component.translatable("${ItemTypes.DEBUG_STICK.translation.key()}.empty", Component.text(key)))
            return false
        }

        var tag = item.meta.data.getCompound("DebugProperty")
        val propertyKey = tag.getString(key)
        var property = properties.firstOrNull { tag.getString(propertyKey) == it.name } as? Property<Comparable<Any>>

        if (isUse) {
            if (property == null) property = properties.first() as Property<Comparable<Any>>
            val cycled = block.cycle(property, player.isSneaking)
            world.setBlock(x, y, z, cycled)
            val cycledText = Component.text(property.toString(cycled[property]!!))
            player.sendMessage(Component.translatable("${ItemTypes.DEBUG_STICK.translation.key()}.update", Component.text(property.name), cycledText))
        } else {
            property = properties.findRelative(property, player.isSneaking) as Property<Comparable<Any>>
            val name = property.name
            tag = tag.putString(key, name)
            val propertyText = Component.text(property.toString(block[property]!!))
            player.sendMessage(Component.translatable("${ItemTypes.DEBUG_STICK.translation.key()}.select", Component.text(name), propertyText))
        }
        return true
    }

    @JvmStatic
    private fun <T : Comparable<T>> Block.cycle(property: Property<T>, reversed: Boolean): Block =
        set(property, property.values.findRelative(get(property), reversed)!!)
}
