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

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.block.BlockState
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.vehicle.MinecartVariant
import org.kryptonmc.api.entity.vehicle.TNTMinecart
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.vehicle.TNTMinecartSerializer
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource

class KryptonTNTMinecart(world: KryptonWorld) : KryptonMinecartLike(world), TNTMinecart {

    override val type: KryptonEntityType<KryptonTNTMinecart>
        get() = KryptonEntityTypes.TNT_MINECART
    override val serializer: EntitySerializer<KryptonTNTMinecart>
        get() = TNTMinecartSerializer

    override val variant: MinecartVariant
        get() = MinecartVariant.TNT
    override val isPrimed: Boolean
        get() = fuse > -1
    override var fuse: Int = -1

    override val defaultCustomBlock: BlockState
        get() = Blocks.TNT.defaultState

    override fun prime() {
        fuse = PRIMED_FUSE
        if (!isSilent) world.playSound(location, SoundEvents.TNT_PRIMED, Sound.Source.BLOCK, 1F, 1F)
    }

    override fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        /* TODO: Explode the minecart if shot with a flaming arrow
        val entity = if (source is KryptonIndirectEntityDamageSource) source.entity else null
        if (entity is KryptonArrowLike && entity.isOnFire) {
        }
         */
        return super.damage(source, damage)
    }

    companion object {

        private const val PRIMED_FUSE = 80
    }
}
