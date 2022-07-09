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
package org.kryptonmc.krypton.entity.aquatic

import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.aquatic.Pufferfish
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonPufferfish(world: KryptonWorld) : KryptonFish(world, EntityTypes.PUFFERFISH), Pufferfish {

    private var inflateCounter = 1
    private var deflateTimer = 0

    private var puffState: Int
        get() = data[MetadataKeys.PUFFERFISH.PUFF_STATE]
        set(value) = data.set(MetadataKeys.PUFFERFISH.PUFF_STATE, value)

    override val bucketType: ItemType = ItemTypes.PUFFERFISH_BUCKET

    init {
        data.add(MetadataKeys.PUFFERFISH.PUFF_STATE, 0)
    }

    override fun tick() {
        if (isAlive && hasAI) {
            if (inflateCounter > 0) {
                if (puffState == 0) {
                    playSound(SoundEvents.PUFFER_FISH_BLOW_UP, soundVolume, voicePitch)
                    puffState = 1
                } else if (inflateCounter > 40 && puffState == 1) {
                    playSound(SoundEvents.PUFFER_FISH_BLOW_UP, soundVolume, voicePitch)
                    puffState = 2
                }
                inflateCounter++
            } else if (puffState != 0) {
                if (deflateTimer > 60 && puffState == 2) {
                    playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, soundVolume, voicePitch)
                    puffState = 1
                } else if (deflateTimer > 100 && puffState == 1) {
                    playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, soundVolume, voicePitch)
                    puffState = 0
                }
                deflateTimer++
            }
        }
        super.tick()
    }

    companion object {

        private val BUCKET_ITEM = KryptonItemStack(ItemTypes.PUFFERFISH_BUCKET, 1, KryptonItemMeta.DEFAULT)
    }
}
