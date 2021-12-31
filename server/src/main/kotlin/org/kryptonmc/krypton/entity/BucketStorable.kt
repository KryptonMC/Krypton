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
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.item.meta.AbstractItemMeta
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.nbt.CompoundTag

interface BucketStorable {

    var spawnedFromBucket: Boolean
    val bucketItem: KryptonItemStack
    val pickupSound: SoundEvent

    fun loadFromBucket(tag: CompoundTag)

    fun loadDefaultsFromBucket(mob: KryptonMob, tag: CompoundTag) {
        if (tag.contains("NoAI")) mob.hasAI = !tag.getBoolean("NoAI")
        if (tag.contains("Silent")) mob.isSilent = tag.getBoolean("Silent")
        if (tag.contains("NoGravity")) mob.hasGravity = !tag.getBoolean("NoGravity")
        if (tag.contains("Glowing")) mob.isGlowing = tag.getBoolean("Glowing")
        if (tag.contains("Invulnerable")) mob.isInvulnerable = tag.getBoolean("Invulnerable")
        if (tag.contains("Health", 99)) mob.health = tag.getFloat("Health")
    }

    fun saveDefaultsToBucket(mob: KryptonMob): AbstractItemMeta<*> {
        // FIXME
        /*
        val nbt = item.meta.nbt
        if (mob.customName != null)
        if (mob.customName != null) item.meta[MetaKeys.NAME] = mob.customName!!
        if (!mob.hasAI) nbt.putBoolean("NoAI", !mob.hasAI)
        if (mob.isSilent) nbt.putBoolean("Silent", mob.isSilent)
        if (!mob.hasGravity) nbt.putBoolean("NoGravity", !mob.hasGravity)
        if (mob.isGlowing) nbt.putBoolean("Glowing", mob.isGlowing)
        if (mob.isInvulnerable) nbt.putBoolean("Invulnerable", mob.isInvulnerable)
        nbt.putFloat("Health", mob.health)
         */
        return KryptonItemMeta.DEFAULT
    }
}
