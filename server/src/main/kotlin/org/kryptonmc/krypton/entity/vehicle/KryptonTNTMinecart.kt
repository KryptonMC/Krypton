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
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.vehicle.MinecartType
import org.kryptonmc.api.entity.vehicle.TNTMinecart
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource

class KryptonTNTMinecart(world: KryptonWorld) : KryptonMinecartLike(world, EntityTypes.TNT_MINECART), TNTMinecart {

    override val minecartType: MinecartType
        get() = MinecartType.TNT
    override val isPrimed: Boolean
        get() = fuse > -1
    override var fuse: Int = -1

    override val defaultCustomBlock: Block
        get() = Blocks.TNT

    override fun prime() {
        fuse = 80
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
}
