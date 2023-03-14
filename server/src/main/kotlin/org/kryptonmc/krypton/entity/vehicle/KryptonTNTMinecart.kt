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

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.vehicle.MinecartVariant
import org.kryptonmc.api.entity.vehicle.TNTMinecart
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.vehicle.TNTMinecartSerializer
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.KryptonBlocks
import org.kryptonmc.krypton.world.block.state.KryptonBlockState
import org.kryptonmc.krypton.world.damage.KryptonDamageSource

class KryptonTNTMinecart(world: KryptonWorld) : KryptonMinecartLike(world), TNTMinecart {

    override val type: KryptonEntityType<KryptonTNTMinecart>
        get() = KryptonEntityTypes.TNT_MINECART
    override val serializer: EntitySerializer<KryptonTNTMinecart>
        get() = TNTMinecartSerializer

    override val variant: MinecartVariant
        get() = MinecartVariant.TNT
    override var fuse: Int = -1

    override fun isPrimed(): Boolean = fuse > -1

    override fun prime() {
        fuse = PRIMED_FUSE
        if (!isSilent) world.playSound(this, SoundEvents.TNT_PRIMED.get(), Sound.Source.BLOCK, 1F, 1F)
    }

    @Suppress("ExpressionBodySyntax") // There will be logic here
    override fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        /* TODO: Explode the minecart if shot with a flaming arrow
        val entity = if (source is KryptonIndirectEntityDamageSource) source.entity else null
        if (entity is KryptonArrowLike && entity.isOnFire) {
        }
         */
        return super.damage(source, damage)
    }

    override fun defaultCustomBlock(): KryptonBlockState = KryptonBlocks.TNT.defaultState

    companion object {

        private const val PRIMED_FUSE = 80
    }
}
