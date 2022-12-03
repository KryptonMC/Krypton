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

import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.entity.vehicle.MinecartLike
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.vehicle.MinecartLikeSerializer
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.block.state.downcast
import org.kryptonmc.krypton.world.damage.KryptonDamageSource
import org.kryptonmc.krypton.world.damage.KryptonEntityDamageSource

abstract class KryptonMinecartLike(world: KryptonWorld) : KryptonEntity(world), MinecartLike {

    override val serializer: EntitySerializer<out KryptonMinecartLike>
        get() = MinecartLikeSerializer

    override var damageTaken: Float
        get() = data.get(MetadataKeys.MinecartLike.DAMAGE)
        set(value) = data.set(MetadataKeys.MinecartLike.DAMAGE, value)
    override var damageTimer: Int
        get() = data.get(MetadataKeys.MinecartLike.HURT_TIMER)
        set(value) = data.set(MetadataKeys.MinecartLike.HURT_TIMER, value)
    override var showCustomBlock: Boolean
        get() = data.get(MetadataKeys.MinecartLike.SHOW_CUSTOM_BLOCK)
        set(value) = data.set(MetadataKeys.MinecartLike.SHOW_CUSTOM_BLOCK, value)
    override var customBlock: BlockState
        get() = internalCustomBlock
        set(value) {
            internalCustomBlock = value.downcast()
        }
    var internalCustomBlock: KryptonBlockState
        get() = if (!showCustomBlock) defaultCustomBlock else KryptonBlock.stateFromId(data.get(MetadataKeys.MinecartLike.CUSTOM_BLOCK_ID))
        set(value) {
            data.set(MetadataKeys.MinecartLike.CUSTOM_BLOCK_ID, KryptonBlock.idOf(value))
            showCustomBlock = value !== defaultCustomBlock
        }
    override var customBlockOffset: Int
        get() = data.get(MetadataKeys.MinecartLike.CUSTOM_BLOCK_OFFSET)
        set(value) {
            data.set(MetadataKeys.MinecartLike.CUSTOM_BLOCK_OFFSET, value)
            showCustomBlock = value != defaultCustomBlockOffset
        }
    private var hurtDirection: Int
        get() = data.get(MetadataKeys.MinecartLike.HURT_DIRECTION)
        set(value) = data.set(MetadataKeys.MinecartLike.HURT_DIRECTION, value)

    protected open val defaultCustomBlock: KryptonBlockState
        get() = KryptonBlocks.AIR.defaultState
    protected open val defaultCustomBlockOffset: Int
        get() = 0

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.MinecartLike.HURT_TIMER, 0)
        data.define(MetadataKeys.MinecartLike.HURT_DIRECTION, 1)
        data.define(MetadataKeys.MinecartLike.DAMAGE, 0F)
        data.define(MetadataKeys.MinecartLike.CUSTOM_BLOCK_ID, KryptonRegistries.BLOCK.getId(KryptonBlocks.AIR))
        data.define(MetadataKeys.MinecartLike.CUSTOM_BLOCK_OFFSET, DEFAULT_CUSTOM_BLOCK_OFFSET)
        data.define(MetadataKeys.MinecartLike.SHOW_CUSTOM_BLOCK, false)
    }

    override fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        if (isRemoved) return true
        if (isInvulnerableTo(source)) return false
        hurtDirection = -hurtDirection
        damageTimer = DEFAULT_DAMAGE_TIMER
        markDamaged()
        damageTaken += damage * DAMAGE_INCREASE_MULTIPLIER
        val canInstantlyBuild = source is KryptonEntityDamageSource && source.entity is KryptonPlayer && source.entity.abilities.canInstantlyBuild
        if (canInstantlyBuild || damageTaken > MAX_DAMAGE) {
            ejectPassengers()
            remove()
            //if (canInstantlyBuild && customName == null) remove()
        }
        return true
    }

    companion object {

        private const val DEFAULT_DAMAGE_TIMER = 10
        private const val DAMAGE_INCREASE_MULTIPLIER = 10F
        private const val MAX_DAMAGE = 40F
        private const val DEFAULT_CUSTOM_BLOCK_OFFSET = 6
    }
}
