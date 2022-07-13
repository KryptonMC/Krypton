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

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.vehicle.MinecartLike
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.krypton.world.block.downcast
import org.kryptonmc.krypton.world.damage.KryptonDamageSource
import org.kryptonmc.krypton.world.damage.KryptonEntityDamageSource

abstract class KryptonMinecartLike(world: KryptonWorld, type: EntityType<out MinecartLike>) : KryptonEntity(world, type), MinecartLike {

    override var damageTaken: Float
        get() = data[MetadataKeys.MINECART_LIKE.DAMAGE]
        set(value) = data.set(MetadataKeys.MINECART_LIKE.DAMAGE, value)
    override var showCustomBlock: Boolean
        get() = data[MetadataKeys.MINECART_LIKE.SHOW_CUSTOM_BLOCK]
        set(value) = data.set(MetadataKeys.MINECART_LIKE.SHOW_CUSTOM_BLOCK, value)
    override var customBlock: Block
        get() = if (!showCustomBlock) defaultCustomBlock else BlockLoader.fromState(data[MetadataKeys.MINECART_LIKE.CUSTOM_BLOCK_ID])
        set(value) {
            data[MetadataKeys.MINECART_LIKE.CUSTOM_BLOCK_ID] = value.downcast().stateId
            showCustomBlock = value !== defaultCustomBlock
        }
    override var customBlockOffset: Int
        get() = data[MetadataKeys.MINECART_LIKE.CUSTOM_BLOCK_OFFSET]
        set(value) {
            data[MetadataKeys.MINECART_LIKE.CUSTOM_BLOCK_OFFSET] = value
            showCustomBlock = value != defaultCustomBlockOffset
        }
    override var damageTimer: Int
        get() = data[MetadataKeys.MINECART_LIKE.HURT_TIMER]
        set(value) = data.set(MetadataKeys.MINECART_LIKE.HURT_TIMER, value)
    private var hurtDirection: Int
        get() = data[MetadataKeys.MINECART_LIKE.HURT_DIRECTION]
        set(value) = data.set(MetadataKeys.MINECART_LIKE.HURT_DIRECTION, value)

    protected open val defaultCustomBlock: Block
        get() = Blocks.AIR
    protected open val defaultCustomBlockOffset: Int
        get() = 0

    init {
        data.add(MetadataKeys.MINECART_LIKE.HURT_TIMER, 0)
        data.add(MetadataKeys.MINECART_LIKE.HURT_DIRECTION, 1)
        data.add(MetadataKeys.MINECART_LIKE.DAMAGE, 0F)
        data.add(MetadataKeys.MINECART_LIKE.CUSTOM_BLOCK_ID, Blocks.AIR.downcast().id)
        data.add(MetadataKeys.MINECART_LIKE.CUSTOM_BLOCK_OFFSET, 6)
        data.add(MetadataKeys.MINECART_LIKE.SHOW_CUSTOM_BLOCK, false)
    }

    override fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        if (isRemoved) return true
        if (isInvulnerableTo(source)) return false
        hurtDirection = -hurtDirection
        damageTimer = 10
        markDamaged()
        damageTaken += damage * 10F
        val canInstantlyBuild = source is KryptonEntityDamageSource && source.entity is KryptonPlayer && source.entity.canInstantlyBuild
        if (canInstantlyBuild || damageTaken > 40F) {
            ejectPassengers()
            remove()
            //if (canInstantlyBuild && customName == null) remove()
        }
        return true
    }
}
