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
package org.kryptonmc.krypton.item.handler

import net.kyori.adventure.text.Component
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.state.property.KryptonProperty
import org.kryptonmc.krypton.util.collection.Iterables
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.chunk.flag.SetBlockFlag
import java.util.function.Consumer

object DebugStickHandler : ItemHandler {

    private val translation by lazy { ItemTypes.DEBUG_STICK.get().translationKey() }

    override fun canAttackBlock(player: KryptonPlayer, world: KryptonWorld, block: KryptonBlockState, pos: Vec3i): Boolean {
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
    private fun handleInteraction(player: KryptonPlayer, world: KryptonWorld, state: KryptonBlockState, pos: Vec3i, isUse: Boolean,
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
