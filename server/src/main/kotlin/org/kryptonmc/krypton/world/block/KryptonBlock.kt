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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.block.RenderShape
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.data.BlockData
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.util.IntHashBiMap
import org.kryptonmc.krypton.world.WorldAccessor
import org.kryptonmc.krypton.world.block.property.KryptonPropertyHolder
import org.spongepowered.math.vector.Vector3i

@Suppress("INAPPLICABLE_JVM_NAME")
data class KryptonBlock(
    private val data: BlockData,
    override val availableProperties: Set<Property<*>>,
    override val properties: Map<String, String>
) : KryptonPropertyHolder<Block>(), Block {

    override val id: Int
        @JvmName("id") get() = data.id
    override val stateId: Int
        @JvmName("stateId") get() = data.stateId
    override val hardness: Double
        @JvmName("hardness") get() = data.hardness
    override val explosionResistance: Double
        @JvmName("explosionResistance") get() = data.resistance
    override val friction: Double
        @JvmName("friction") get() = data.friction
    override val speedFactor: Double
        @JvmName("speedFactor") get() = data.speedFactor
    override val jumpFactor: Double
        @JvmName("jumpFactor") get() = data.jumpFactor
    override val isAir: Boolean
        get() = data.air
    override val isSolid: Boolean
        get() = data.solid
    override val isLiquid: Boolean
        get() = data.liquid
    override val hasBlockEntity: Boolean
        @JvmName("hasBlockEntity") get() = data.blockEntity
    override val blocksMotion: Boolean
        @JvmName("blocksMotion") get() = data.blocksMotion
    override val hasGravity: Boolean
        @JvmName("hasGravity") get() = data.gravity
    override val isFlammable: Boolean
        get() = data.flammable
    override val lightEmission: Int
        @JvmName("lightEmission") get() = data.lightEmission
    override val occludes: Boolean
        @JvmName("occludes") get() = data.occludes
    override val canRespawnIn: Boolean
        @JvmName("canRespawnIn") get() = data.canRespawnIn
    override val isReplaceable: Boolean
        get() = data.replaceable
    override val hasDynamicShape: Boolean
        @JvmName("hasDynamicShape") get() = data.dynamicShape
    override val useShapeForOcclusion: Boolean
        @JvmName("useShapeForOcclusion") get() = data.useShapeForOcclusion
    override val propagatesSkylightDown: Boolean
        @JvmName("propagatesSkylightDown") get() = data.propagatesLightDown
    override val hasLargeCollisionShape: Boolean
        @JvmName("hasLargeCollisionShape") get() = data.largeCollisionShape
    override val isConditionallyFullyOpaque: Boolean
        get() = data.conditionallyFullyOpaque
    override val isSolidRender: Boolean
        get() = data.solidRender
    override val lightBlock: Int
        @JvmName("lightBlock") get() = data.lightBlock
    override val opacity: Int
        @JvmName("opacity") get() = data.opacity
    override val requiresCorrectTool: Boolean
        @JvmName("requiresCorrectTool") get() = data.toolRequired
    @get:JvmName("translation")
    override val translation = Component.translatable(data.translationKey)
    override val renderShape = data.renderShape?.let {
        if (it == "ENTITYBLOCK_ANIMATED") RenderShape.ANIMATED_ENTITY_BLOCK else RenderShape.valueOf(it)
    } ?: RenderShape.MODEL

    override fun copy(key: String, value: String): Block {
        val newProperties = properties + (key to value)
        return requireNotNull(BlockLoader.properties(key().asString(), newProperties)) { "Invalid property $key:$value for block ${key()}!" }
    }

    override fun copy(newValues: Map<String, String>): KryptonBlock {
        val newProperties = properties + newValues
        return requireNotNull(BlockLoader.properties(key().asString(), newProperties)) { "Invalid properties $newValues for block ${key()}!" }
    }

    override fun asItem() = data.itemKey?.let { InternalRegistries.ITEM[it] }

    override fun asFluid() = InternalRegistries.FLUID[data.fluidKey]

    override fun key(): Key = data.key

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
