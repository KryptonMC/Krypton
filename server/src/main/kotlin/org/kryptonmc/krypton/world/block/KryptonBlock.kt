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

import net.kyori.adventure.text.Component
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.RenderShape
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.block.BlockData
import org.kryptonmc.krypton.world.block.property.KryptonPropertyHolder

class KryptonBlock(
    data: BlockData,
    override val properties: Map<String, String>
) : KryptonPropertyHolder<Block>(), Block {

    override val key = data.key
    override val id = data.id
    override val stateId = data.stateId
    override val hardness = data.hardness
    override val explosionResistance = data.resistance
    override val friction = data.friction
    override val speedFactor = data.speedFactor
    override val jumpFactor = data.jumpFactor
    override val isAir = data.air
    override val isSolid = data.solid
    override val isLiquid = data.liquid
    override val hasBlockEntity = data.blockEntity
    override val blocksMotion = data.blocksMotion
    override val hasGravity = data.gravity
    override val isFlammable = data.flammable
    override val lightEmission = data.lightEmission
    override val occludes = data.occludes
    override val canRespawnIn = data.canRespawnIn
    override val isReplaceable = data.replaceable
    override val hasDynamicShape = data.dynamicShape
    override val useShapeForOcclusion = data.useShapeForOcclusion
    override val propagatesSkylightDown = data.propagatesLightDown
    override val hasLargeCollisionShape = data.largeCollisionShape
    override val isConditionallyFullyOpaque = data.conditionallyFullyOpaque
    override val isSolidRender = data.solidRender
    override val lightBlock = data.lightBlock
    override val opacity = data.opacity
    override val requiresCorrectTool = data.toolRequired
    override val translation = Component.translatable(data.translationKey)
    override val renderShape = data.renderShape?.let { RenderShape.valueOf(it) } ?: RenderShape.MODEL
    private val itemKey = data.itemKey

    override fun copy(key: String, value: String): Block {
        val newProperties = properties + (key to value)
        return requireNotNull(BlockLoader.properties(this.key.asString(), newProperties)) { "Invalid property $key:$value for block ${this.key}!" }
    }

    override fun copy(newValues: Map<String, String>): KryptonBlock {
        val newProperties = properties + newValues
        return requireNotNull(BlockLoader.properties(key.asString(), newProperties)) { "Invalid properties $properties for block $key!" }
    }

    override fun asItem() = itemKey?.let { InternalRegistries.ITEM[it] }

    override fun compareTo(other: Block) = id.compareTo(other.id)
}
