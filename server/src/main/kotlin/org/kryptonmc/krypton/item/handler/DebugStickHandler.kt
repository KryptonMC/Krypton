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
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.util.BlockPos
import org.kryptonmc.krypton.util.Iterables
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.SetBlockFlag
import java.util.function.Consumer

object DebugStickHandler : ItemHandler {

    private val translation by lazy { ItemTypes.DEBUG_STICK.get().translationKey() }

    override fun canAttackBlock(player: KryptonPlayer, world: KryptonWorld, block: KryptonBlockState, pos: BlockPos): Boolean {
        handleInteraction(player, world, block, pos, false, player.getHeldItem(Hand.MAIN)) { player.setHeldItem(Hand.MAIN, it) }
        return false
    }

    /* FIXME fix this when we sort out interactions
    override fun interact(context: InteractionContext): InteractionResult {
        val player = context.player
        val item = context.heldItem
        val world = context.world
        val position = context.position
        if (!handleInteraction(player, world, world.getBlock(position), position, true, item)) {
            return InteractionResult.FAIL
        }
        return InteractionResult.CONSUME
    }
     */

    // TODO: We need to get information about where the item was so we can replace it with a copy that has the modified metadata,
    //  as all item stacks are immutable, so we can't just simply modify the data on the item like vanilla does.
    @JvmStatic
    private fun handleInteraction(player: KryptonPlayer, world: KryptonWorld, state: KryptonBlockState, pos: BlockPos, isUse: Boolean,
                                  item: KryptonItemStack, setter: Consumer<KryptonItemStack>): Boolean {
        if (!player.canUseGameMasterBlocks()) return false
        val block = state.block
        val definition = block.stateDefinition
        val properties = definition.properties()
        val key = KryptonRegistries.BLOCK.getKey(block).asString()
        if (properties.isEmpty()) {
            player.sendActionBar(Component.translatable("$translation.empty", Component.text(key)))
            return false
        }

        var debugProperty = item.meta.data.getCompound("DebugProperty")
        val propertyName = debugProperty.getString(key)
        var property = definition.getProperty(propertyName)

        if (isUse) {
            if (property == null) property = properties.first()
            val cycled = cycleState(state, property, player.isSneaking)
            world.setBlock(pos, cycled, SetBlockFlag.UPDATE_NEIGHBOUR_SHAPES or SetBlockFlag.NOTIFY_CLIENTS)
            player.sendActionBar(Component.translatable("$translation.update", Component.text(property.name), toString(state, property)))
        } else {
            property = Iterables.findRelative(properties, property, player.isSneaking)!!
            debugProperty = debugProperty.putString(key, property.name)
            player.sendActionBar(Component.translatable("$translation.select", Component.text(property.name), toString(state, property)))
        }
        setter.accept(item.withMeta(item.meta.copy(item.meta.data.put("DebugProperty", debugProperty))))
        return true
    }

    @JvmStatic
    private fun <T : Comparable<T>> cycleState(state: KryptonBlockState, property: KryptonProperty<T>, reversed: Boolean): KryptonBlockState =
        state.setProperty(property, Iterables.findRelative(property.values, state.requireProperty(property), reversed)!!)

    @JvmStatic
    private fun <T : Comparable<T>> toString(state: KryptonBlockState, property: KryptonProperty<T>): Component =
        Component.text(property.toString(state.requireProperty(property)))
}
