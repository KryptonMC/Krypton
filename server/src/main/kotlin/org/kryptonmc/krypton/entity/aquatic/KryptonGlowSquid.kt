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

    override val type: KryptonEntityType<KryptonGlowSquid>
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
