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
package org.kryptonmc.krypton.world.block

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.PersistentMap
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.PushReaction
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.data.KryptonImmutableDataHolder
import org.kryptonmc.krypton.util.normalizePath
import org.kryptonmc.krypton.world.block.RenderShape
import org.kryptonmc.krypton.world.block.property.KryptonPropertyHolder

@JvmRecord
data class KryptonBlock(
    private val key: Key,
    val id: Int,
    val stateId: Int,
    override val hardness: Double,
    override val explosionResistance: Double,
    override val friction: Double,
    val speedFactor: Double,
    val jumpFactor: Double,
    override val isAir: Boolean,
    override val isSolid: Boolean,
    override val isLiquid: Boolean,
    val isSolidBlocking: Boolean,
    override val hasBlockEntity: Boolean,
    val lightEmission: Int,
    override val isOpaque: Boolean,
    override val blocksMotion: Boolean,
    override val isFlammable: Boolean,
    override val hasGravity: Boolean,
    override val translation: TranslatableComponent,
    override val isReplaceable: Boolean,
    val hasDynamicShape: Boolean,
    val useShapeForOcclusion: Boolean,
    val propagatesSkylightDown: Boolean,
    val lightBlock: Int,
    val isConditionallyFullyOpaque: Boolean,
    val isSolidRender: Boolean,
    val opacity: Int,
    val hasLargeCollisionShape: Boolean,
    val isCollisionShapeFullBlock: Boolean,
    override val canRespawnIn: Boolean,
    val requiresCorrectTool: Boolean,
    val renderShape: RenderShape,
    override val pushReaction: PushReaction,
    val itemKey: Key?,
    val fluidKey: Key,
    override val availableProperties: ImmutableSet<Property<*>>,
    override val properties: PersistentMap<String, String>
) : KryptonPropertyHolder<Block>, Block, KryptonImmutableDataHolder<Block> {

    override fun copy(key: String, value: String): Block {
        val newProperties = properties.put(key, value)
        return requireNotNull(BlockLoader.properties(key().asString(), newProperties)) { "Invalid property $key:$value for block ${key()}!" }
    }

    override fun copy(newValues: Map<String, String>): KryptonBlock {
        val newProperties = properties.putAll(newValues)
        return requireNotNull(BlockLoader.properties(key().asString(), newProperties)) { "Invalid properties $newValues for block ${key()}!" }
    }

    override fun asItem(): ItemType? = if (itemKey != null) Registries.ITEM[itemKey] else null

    override fun asFluid(): Fluid = Registries.FLUID[fluidKey]

    override fun key(): Key = key
}
