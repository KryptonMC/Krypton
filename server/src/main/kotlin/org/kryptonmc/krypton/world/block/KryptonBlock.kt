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

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.PushReaction
import org.kryptonmc.api.block.RenderShape
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.util.IntHashBiMap
import org.kryptonmc.krypton.world.block.property.KryptonPropertyHolder

@JvmRecord
data class KryptonBlock(
    private val key: Key,
    override val id: Int,
    override val stateId: Int,
    override val hardness: Double,
    override val explosionResistance: Double,
    override val friction: Double,
    override val speedFactor: Double,
    override val jumpFactor: Double,
    override val isAir: Boolean,
    override val isSolid: Boolean,
    override val isLiquid: Boolean,
    override val isSolidBlocking: Boolean,
    override val hasBlockEntity: Boolean,
    override val lightEmission: Int,
    override val occludes: Boolean,
    override val blocksMotion: Boolean,
    override val isFlammable: Boolean,
    override val hasGravity: Boolean,
    override val translation: TranslatableComponent,
    override val isReplaceable: Boolean,
    override val hasDynamicShape: Boolean,
    override val useShapeForOcclusion: Boolean,
    override val propagatesSkylightDown: Boolean,
    override val lightBlock: Int,
    override val isConditionallyFullyOpaque: Boolean,
    override val isSolidRender: Boolean,
    override val opacity: Int,
    override val hasLargeCollisionShape: Boolean,
    override val isCollisionShapeFullBlock: Boolean,
    override val canRespawnIn: Boolean,
    override val requiresCorrectTool: Boolean,
    override val renderShape: RenderShape,
    override val pushReaction: PushReaction,
    val itemKey: Key?,
    val fluidKey: Key,
    override val availableProperties: Set<Property<*>>,
    override val properties: Map<String, String>
) : KryptonPropertyHolder<Block>, Block {

    override fun copy(key: String, value: String): Block {
        val newProperties = properties + (key to value)
        return requireNotNull(BlockLoader.properties(key().asString(), newProperties)) { "Invalid property $key:$value for block ${key()}!" }
    }

    override fun copy(newValues: Map<String, String>): KryptonBlock {
        val newProperties = properties + newValues
        return requireNotNull(BlockLoader.properties(key().asString(), newProperties)) { "Invalid properties $newValues for block ${key()}!" }
    }

    override fun asItem() = itemKey?.let { InternalRegistries.ITEM[it] }

    override fun asFluid() = InternalRegistries.FLUID[fluidKey]

    override fun key(): Key = key

    override fun compareTo(other: Block) = id.compareTo(other.id)

    companion object {

        val CODEC: Codec<Block> = RecordCodecBuilder.create {
            it.group(
                Codecs.KEY.fieldOf("Name").forGetter(Block::key),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("Properties").forGetter(Block::properties)
            ).apply(it) { key, properties -> BlockLoader.fromKey(key)!!.copy(properties) }
        }
        val STATES = IntHashBiMap<Block>()
    }
}
