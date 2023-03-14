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
        super.tick()
        tickIgnite()
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
