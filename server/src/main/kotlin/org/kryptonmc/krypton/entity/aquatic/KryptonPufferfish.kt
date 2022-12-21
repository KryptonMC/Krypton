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
import org.kryptonmc.api.entity.aquatic.Pufferfish
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.aquatic.PufferfishSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonPufferfish(world: KryptonWorld) : KryptonFish(world), Pufferfish {

    override val type: KryptonEntityType<KryptonPufferfish>
        get() = KryptonEntityTypes.PUFFERFISH
    override val serializer: EntitySerializer<KryptonPufferfish>
        get() = PufferfishSerializer
    override val bucketType: ItemType
        get() = ItemTypes.PUFFERFISH_BUCKET

    // This gets ticked up when the puff goal sets it to 1, and reset when the puff goal is stopped.
    private var inflateCounter = 0
    // This gets ticked up when the puff goal is cancelled, and reset when the puff goal is started.
    private var deflateTimer = 0

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Pufferfish.PUFF_STATE, 0)
    }

    override fun tick() {
        handleInflation()
        super.tick()
    }

    private fun handleInflation() {
        if (!isAlive() || !hasAI) return
        if (inflateCounter >= START_INFLATION_THRESHOLD) {
            // If this is > 0, the pufferfish has detected a scary mob within 2x its bounding box.
            if (getPuffState() == STATE_SMALL) {
                // The pufferfish is the very small, un-inflated version.
                // This will run the first time the puff goal is triggered.
                playSound(SoundEvents.PUFFER_FISH_BLOW_UP, soundVolume(), voicePitch())
                setPuffState(STATE_MEDIUM) // Set it to the medium size, in between
            } else if (inflateCounter > INFLATE_TO_LARGE_THRESHOLD && getPuffState() == STATE_MEDIUM) {
                // The inflation counter has reached 40 ticks, it takes 2 seconds for the pufferfish to go from medium to large.
                // This will run on the 40th tick.
                playSound(SoundEvents.PUFFER_FISH_BLOW_UP, soundVolume(), voicePitch())
                setPuffState(STATE_LARGE) // Set it to the large size
            }
            inflateCounter++ // Tick this up
        } else if (getPuffState() != STATE_SMALL) {
            // This will be the case when the pufferfish has reached large puff state but the puff goal has been stopped, resetting
            // the inflate counter to 0.
            if (deflateTimer > DEFLATE_TO_MEDIUM_THRESHOLD && getPuffState() == STATE_LARGE) {
                // After the puff goal has been cancelled, it takes 3 seconds before a large pufferfish deflates to medium
                playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, soundVolume(), voicePitch())
                setPuffState(STATE_MEDIUM) // Set it to the medium size
            } else if (deflateTimer > DEFLATE_TO_SMALL_THRESHOLD && getPuffState() == STATE_MEDIUM) {
                // After the pufferfish has deflated to medium, it takes 2 more seconds before it deflates back down to small.
                playSound(SoundEvents.PUFFER_FISH_BLOW_OUT, soundVolume(), voicePitch())
                setPuffState(STATE_SMALL) // Set it to the small size
            }
            deflateTimer++ // Tick this up
        }
    }

    private fun getPuffState(): Int = data.get(MetadataKeys.Pufferfish.PUFF_STATE)

    private fun setPuffState(state: Int) {
        data.set(MetadataKeys.Pufferfish.PUFF_STATE, state)
    }

    companion object {

        private const val STATE_SMALL = 0
        private const val STATE_MEDIUM = 1
        private const val STATE_LARGE = 2
        private const val START_INFLATION_THRESHOLD = 1
        private const val INFLATE_TO_LARGE_THRESHOLD = 2 * 20 // 2 seconds in ticks
        private const val DEFLATE_TO_MEDIUM_THRESHOLD = 3 * 20 // 3 seconds in ticks
        private const val DEFLATE_TO_SMALL_THRESHOLD = 5 * 20 // 5 seconds in ticks
    }
}
