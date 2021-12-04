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

import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.BlockHandler
import org.kryptonmc.api.block.PushReaction
import org.kryptonmc.api.block.RenderShape
import org.kryptonmc.api.block.property.Property
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.Codecs
import org.kryptonmc.krypton.world.block.handler.DummyBlockHandler
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

    override val handler: BlockHandler
        get() = KryptonBlockManager.handler(this) ?: DummyBlockHandler

    override fun copy(key: String, value: String): Block {
        val newProperties = properties + (key to value)
        return requireNotNull(BlockLoader.properties(key().asString(), newProperties)) { "Invalid property $key:$value for block ${key()}!" }
    }

    override fun copy(newValues: Map<String, String>): KryptonBlock {
        val newProperties = properties + newValues
        return requireNotNull(BlockLoader.properties(key().asString(), newProperties)) { "Invalid properties $newValues for block ${key()}!" }
    }

    override fun asItem(): ItemType? = if (itemKey != null) InternalRegistries.ITEM[itemKey!!] else null

    override fun asFluid(): Fluid = InternalRegistries.FLUID[fluidKey]

    override fun key(): Key = key

    override fun toBuilder(): Builder = Builder(this)

    class Builder(
        private val key: Key,
        private var id: Int,
        private var stateId: Int
    ) : KryptonPropertyHolder.Builder<Block.Builder, Block>(), Block.Builder {

        private var hardness = 0.0
        private var explosionResistance = 0.0
        private var friction = 0.0
        private var speedFactor = 0.0
        private var jumpFactor = 0.0
        private var air = false
        private var solid = false
        private var liquid = false
        private var solidBlocking = false
        private var blockEntity = false
        private var lightEmission = 0
        private var occludes = false
        private var blocksMotion = false
        private var flammable = false
        private var gravity = false
        private var translation: TranslatableComponent? = null
        private var replaceable = false
        private var dynamicShape = false
        private var useShapeForOcclusion = false
        private var propagatesSkylightDown = false
        private var lightBlock = 0
        private var conditionallyFullyOpaque = false
        private var solidRender = false
        private var opacity = 0
        private var largeCollisionShape = false
        private var collisionShapeFullBlock = false
        private var canRespawnIn = false
        private var requiresCorrectTool = false
        private var renderShape = RenderShape.MODEL
        private var pushReaction = PushReaction.NORMAL
        private var item: Key? = null
        private var fluid: Key? = null

        constructor(block: Block) : this(block.key(), block.id, block.stateId) {
            hardness = block.hardness
            explosionResistance = block.explosionResistance
            friction = block.friction
            speedFactor = block.speedFactor
            jumpFactor = block.jumpFactor
            air = block.isAir
            solid = block.isSolid
            liquid = block.isLiquid
            solidBlocking = block.isSolidBlocking
            blockEntity = block.hasBlockEntity
            lightEmission = block.lightEmission
            occludes = block.occludes
            blocksMotion = block.blocksMotion
            flammable = block.isFlammable
            gravity = block.hasGravity
            translation = block.translation
            replaceable = block.isReplaceable
            dynamicShape = block.hasDynamicShape
            useShapeForOcclusion = block.useShapeForOcclusion
            propagatesSkylightDown = block.propagatesSkylightDown
            lightBlock = block.lightBlock
            conditionallyFullyOpaque = block.isConditionallyFullyOpaque
            opacity = block.opacity
            largeCollisionShape = block.hasLargeCollisionShape
            collisionShapeFullBlock = block.isCollisionShapeFullBlock
            canRespawnIn = block.canRespawnIn
            requiresCorrectTool = block.requiresCorrectTool
            renderShape = block.renderShape
            pushReaction = block.pushReaction
            item = block.asItem()?.key()
            fluid = block.asFluid().key()
        }

        override fun id(id: Int): Block.Builder = apply { this.id = id }

        override fun stateId(id: Int): Block.Builder = apply { stateId = id }

        override fun hardness(hardness: Double): Block.Builder = apply { this.hardness = hardness }

        override fun resistance(resistance: Double): Block.Builder = apply { explosionResistance = resistance }

        override fun friction(friction: Double): Block.Builder = apply { this.friction = friction }

        override fun speedFactor(factor: Double): Block.Builder = apply { speedFactor = factor }

        override fun jumpFactor(factor: Double): Block.Builder = apply { jumpFactor = factor }

        override fun air(value: Boolean): Block.Builder = apply { air = value }

        override fun solid(value: Boolean): Block.Builder = apply { solid = value }

        override fun liquid(value: Boolean): Block.Builder = apply { liquid = value }

        override fun solidBlocking(value: Boolean): Block.Builder = apply { solidBlocking = value }

        override fun flammable(value: Boolean): Block.Builder = apply { flammable = value }

        override fun replaceable(value: Boolean): Block.Builder = apply { replaceable = value }

        override fun hasBlockEntity(value: Boolean): Block.Builder = apply { blockEntity = value }

        override fun lightEmission(amount: Int): Block.Builder = apply {
            require(amount in 0..15) { "The light value must be between 0 and 15!" }
            lightEmission = amount
        }

        override fun occludes(value: Boolean): Block.Builder = apply { occludes = value }

        override fun dynamicShape(value: Boolean): Block.Builder = apply { dynamicShape = value }

        override fun useShapeForOcclusion(value: Boolean): Block.Builder = apply { useShapeForOcclusion = value }

        override fun propagatesSkylightDown(value: Boolean): Block.Builder = apply { propagatesSkylightDown = value }

        override fun lightBlock(value: Int): Block.Builder = apply {
            require(value in 0..15) { "The light value must be between 0 and 15!" }
            lightBlock = value
        }

        override fun conditionallyFullyOpaque(value: Boolean): Block.Builder = apply { conditionallyFullyOpaque = value }

        override fun solidRender(value: Boolean): Block.Builder = apply { solidRender = value }

        override fun opacity(value: Int): Block.Builder = apply {
            require(value in 0..15) { "The light value must be between 0 and 15!" }
            opacity = value
        }

        override fun blocksMotion(value: Boolean): Block.Builder = apply { blocksMotion = value }

        override fun gravity(value: Boolean): Block.Builder = apply { gravity = value }

        override fun translation(translation: TranslatableComponent): Block.Builder = apply {
            this.translation = translation
        }

        override fun canRespawnIn(value: Boolean): Block.Builder = apply { canRespawnIn = value }

        override fun largeCollisionShape(value: Boolean): Block.Builder = apply { largeCollisionShape = value }

        override fun collisionShapeFullBlock(value: Boolean): Block.Builder = apply { collisionShapeFullBlock = value }

        override fun requiresCorrectTool(value: Boolean): Block.Builder = apply { requiresCorrectTool = value }

        override fun renderShape(shape: RenderShape): Block.Builder = apply { renderShape = shape }

        override fun pushReaction(reaction: PushReaction): Block.Builder = apply { pushReaction = reaction }

        override fun item(key: Key): Block.Builder = apply { item = key }

        override fun fluid(key: Key): Block.Builder = apply { fluid = key }

        override fun build(): Block = KryptonBlock(
            key,
            id,
            stateId,
            hardness,
            explosionResistance,
            friction,
            speedFactor,
            jumpFactor,
            air,
            solid,
            liquid,
            solidBlocking,
            blockEntity,
            lightEmission,
            occludes,
            blocksMotion,
            flammable,
            gravity,
            translation ?: Component.translatable("block.${key.namespace()}.${key.value()}"),
            replaceable,
            dynamicShape,
            useShapeForOcclusion,
            propagatesSkylightDown,
            lightBlock,
            conditionallyFullyOpaque,
            solidRender,
            opacity,
            largeCollisionShape,
            collisionShapeFullBlock,
            canRespawnIn,
            requiresCorrectTool,
            renderShape,
            pushReaction,
            item,
            fluid ?: EMPTY_KEY,
            ImmutableSet.copyOf(availableProperties),
            ImmutableMap.copyOf(properties)
        )
    }

    object Factory : Block.Factory {

        override fun builder(key: Key, id: Int, stateId: Int): Builder = Builder(key, id, stateId)

        override fun fromId(id: Int): Block? = Registries.BLOCK[id]

        override fun fromStateId(id: Int): Block? = BlockLoader.STATES[id]
    }

    companion object {

        private val EMPTY_KEY = Key.key("empty")

        @JvmField
        val CODEC: Codec<Block> = RecordCodecBuilder.create {
            it.group(
                Codecs.KEY.fieldOf("Name").forGetter(Block::key),
                Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("Properties").forGetter(Block::properties)
            ).apply(it) { key, properties -> BlockLoader.fromKey(key)!!.copy(properties) }
        }
    }
}
