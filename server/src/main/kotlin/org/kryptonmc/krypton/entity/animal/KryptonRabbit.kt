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
package org.kryptonmc.krypton.entity.animal

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.entity.animal.Rabbit
import org.kryptonmc.api.entity.animal.type.RabbitVariant
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.RabbitSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonRabbit(world: KryptonWorld) : KryptonAnimal(world), Rabbit {

    override val type: KryptonEntityType<KryptonRabbit>
        get() = KryptonEntityTypes.RABBIT
    override val serializer: EntitySerializer<KryptonRabbit>
        get() = RabbitSerializer

    internal var moreCarrotTicks = 0
    override var variant: RabbitVariant
        get() {
            val id = data.get(MetadataKeys.Rabbit.TYPE)
            // It'll do you a treat mate!
            // Oh yeah?
            // Manky scot's git!
            // I'm warning you!
            // What's he do? Nibble ya bum?
            if (id == KILLER_TYPE) return RabbitVariant.KILLER
            return TYPES.getOrNull(data.get(MetadataKeys.Rabbit.TYPE)) ?: RabbitVariant.BROWN
        }
        set(value) {
            if (value == RabbitVariant.KILLER) {
                data.set(MetadataKeys.Rabbit.TYPE, KILLER_TYPE)
                return
            }
            data.set(MetadataKeys.Rabbit.TYPE, value.ordinal)
        }

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Rabbit.TYPE, 0)
    }

    override fun isFood(item: ItemStack): Boolean = TEMPTING_ITEMS.contains(item.type)

    override fun soundSource(): Sound.Source = if (variant == RabbitVariant.KILLER) Sound.Source.HOSTILE else Sound.Source.NEUTRAL

    companion object {

        private const val KILLER_TYPE = 99
        private val TYPES = RabbitVariant.values()
        private val TEMPTING_ITEMS = setOf(ItemTypes.CARROT.get(), ItemTypes.GOLDEN_CARROT.get(), ItemTypes.DANDELION.get())

        private const val DEFAULT_MAX_HEALTH = 3.0
        private const val DEFAULT_MOVEMENT_SPEED = 0.3

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
    }
}
