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

import org.kryptonmc.api.entity.aquatic.GlowSquid
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.aquatic.GlowSquidSerializer
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource

class KryptonGlowSquid(world: KryptonWorld) : KryptonSquid(world), GlowSquid {

    override val type: KryptonEntityType<GlowSquid>
        get() = KryptonEntityTypes.GLOW_SQUID
    override val serializer: EntitySerializer<KryptonGlowSquid>
        get() = GlowSquidSerializer

    override var remainingDarkTicks: Int
        get() = data.get(MetadataKeys.GlowSquid.REMAINING_DARK_TICKS)
        set(value) = data.set(MetadataKeys.GlowSquid.REMAINING_DARK_TICKS, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.GlowSquid.REMAINING_DARK_TICKS, 0)
    }

    override fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        val wasDamaged = super.damage(source, damage)
        // Glow squids will stop glowing for 5 seconds when they are damaged.
        if (wasDamaged) remainingDarkTicks = DAMAGED_DARK_TICKS
        return wasDamaged
    }

    companion object {

        private const val DAMAGED_DARK_TICKS = 5 * 20 // 5 seconds in ticks
    }
}
