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
        get() = customBlock()
        set(value) {
            setCustomBlock(value.downcast())
        }
    override var customBlockOffset: Int
        get() = data.get(MetadataKeys.MinecartLike.CUSTOM_BLOCK_OFFSET)
        set(value) {
            data.set(MetadataKeys.MinecartLike.CUSTOM_BLOCK_OFFSET, value)
            showCustomBlock = value != defaultCustomBlockOffset()
        }
    private var hurtDirection: Int
        get() = data.get(MetadataKeys.MinecartLike.HURT_DIRECTION)
        set(value) = data.set(MetadataKeys.MinecartLike.HURT_DIRECTION, value)

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
        if (isRemoved()) return true
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

    fun customBlock(): KryptonBlockState {
        if (!showCustomBlock) return defaultCustomBlock()
        return KryptonBlock.stateFromId(data.get(MetadataKeys.MinecartLike.CUSTOM_BLOCK_ID))
    }

    fun setCustomBlock(block: KryptonBlockState) {
        data.set(MetadataKeys.MinecartLike.CUSTOM_BLOCK_ID, KryptonBlock.idOf(block))
        showCustomBlock = block !== defaultCustomBlock()
    }

    protected open fun defaultCustomBlock(): KryptonBlockState = KryptonBlocks.AIR.defaultState

    protected open fun defaultCustomBlockOffset(): Int = 0

    companion object {

        private const val DEFAULT_DAMAGE_TIMER = 10
        private const val DAMAGE_INCREASE_MULTIPLIER = 10F
        private const val MAX_DAMAGE = 40F
        private const val DEFAULT_CUSTOM_BLOCK_OFFSET = 6
    }
}
