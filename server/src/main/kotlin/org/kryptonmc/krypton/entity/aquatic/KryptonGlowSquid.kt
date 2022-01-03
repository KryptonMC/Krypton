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

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.aquatic.GlowSquid
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource
import org.kryptonmc.nbt.CompoundTag

class KryptonGlowSquid(world: KryptonWorld) : KryptonSquid(world, EntityTypes.GLOW_SQUID), GlowSquid {

    override var remainingDarkTicks: Int
        get() = data[MetadataKeys.GLOW_SQUID.REMAINING_DARK_TICKS]
        set(value) = data.set(MetadataKeys.GLOW_SQUID.REMAINING_DARK_TICKS, value)

    init {
        data.add(MetadataKeys.GLOW_SQUID.REMAINING_DARK_TICKS)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        remainingDarkTicks = tag.getInt("DarkTicksRemaining")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        int("DarkTicksRemaining", remainingDarkTicks)
    }

    override fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        val wasDamaged = super.damage(source, damage)
        if (wasDamaged) remainingDarkTicks = 100
        return wasDamaged
    }
}
