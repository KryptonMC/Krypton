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
package org.kryptonmc.krypton.entity.monster

import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.monster.Creeper
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.monster.CreeperSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonCreeper(world: KryptonWorld) : KryptonMonster(world), Creeper {

    override val type: KryptonEntityType<KryptonCreeper>
        get() = KryptonEntityTypes.CREEPER
    override val serializer: EntitySerializer<KryptonCreeper>
        get() = CreeperSerializer

    override var fuse: Int = 30
    override var explosionRadius: Int = 0

    override var isCharged: Boolean
        get() = data.get(MetadataKeys.Creeper.CHARGED)
        set(value) = data.set(MetadataKeys.Creeper.CHARGED, value)

    override val isIgnited: Boolean
        get() = data.get(MetadataKeys.Creeper.IGNITED)
    override var currentFuse: Int
        get() = data.get(MetadataKeys.Creeper.STATE)
        set(value) = data.set(MetadataKeys.Creeper.STATE, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Creeper.STATE, -1)
        data.define(MetadataKeys.Creeper.CHARGED, false)
        data.define(MetadataKeys.Creeper.IGNITED, false)
    }

    override fun tick() {
        tickIgnite()
        super.tick()
    }

    private fun tickIgnite() {
        if (!isAlive() || !isIgnited) return
        if (currentFuse > 0) currentFuse--
        if (currentFuse <= 0) {
            explode()
            currentFuse = -1
            setIgnited(false)
        }
    }

    override fun explode() {
        playSound(SoundEvents.GENERIC_EXPLODE.get(), 4F, (1F + (random.nextFloat() - random.nextFloat()) * 0.2F) * 0.7F)
        remove()
        // TODO: Creeper explosions
    }

    override fun ignite() {
        setIgnited(true)
        currentFuse = fuse
        playSound(SoundEvents.CREEPER_PRIMED.get(), 1F, 0.5F)
    }

    fun setIgnited(ignited: Boolean) {
        data.set(MetadataKeys.Creeper.IGNITED, ignited)
    }

    companion object {

        private const val DEFAULT_MOVEMENT_SPEED = 0.25

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMonster.attributes().add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
    }
}
