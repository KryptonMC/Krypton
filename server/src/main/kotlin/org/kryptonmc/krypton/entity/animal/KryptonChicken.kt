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

import org.kryptonmc.api.entity.animal.Chicken
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.ChickenSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonChicken(world: KryptonWorld) : KryptonAnimal(world), Chicken {

    override val type: KryptonEntityType<KryptonChicken>
        get() = KryptonEntityTypes.CHICKEN
    override val serializer: EntitySerializer<KryptonChicken>
        get() = ChickenSerializer

    override var eggCooldownTime: Int = random.nextInt(FIVE_MINUTES_TICKS) + FIVE_MINUTES_TICKS
    override var isJockey: Boolean = false

    override fun isFood(item: ItemStack): Boolean = FOOD_ITEMS.contains(item.type)

    companion object {

        private const val FIVE_MINUTES_TICKS = 6000
        private val FOOD_ITEMS = setOf(
            ItemTypes.WHEAT_SEEDS.get(),
            ItemTypes.MELON_SEEDS.get(),
            ItemTypes.PUMPKIN_SEEDS.get(),
            ItemTypes.BEETROOT_SEEDS.get()
        )

        private const val DEFAULT_MAX_HEALTH = 4.0
        private const val DEFAULT_MOVEMENT_SPEED = 0.25

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
    }
}
