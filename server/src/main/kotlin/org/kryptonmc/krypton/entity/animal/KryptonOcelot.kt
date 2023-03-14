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

import org.kryptonmc.api.entity.animal.Ocelot
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.OcelotSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonOcelot(world: KryptonWorld) : KryptonAnimal(world), Ocelot {

    override val type: KryptonEntityType<KryptonOcelot>
        get() = KryptonEntityTypes.OCELOT
    override val serializer: EntitySerializer<KryptonOcelot>
        get() = OcelotSerializer

    override var isTrusting: Boolean
        get() = data.get(MetadataKeys.Ocelot.TRUSTING)
        set(value) = data.set(MetadataKeys.Ocelot.TRUSTING, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Ocelot.TRUSTING, false)
    }

    override fun isFood(item: ItemStack): Boolean = TEMPT_INGREDIENTS.contains(item.type)

    companion object {

        private val TEMPT_INGREDIENTS = setOf(ItemTypes.COD.get(), ItemTypes.SALMON.get())
        private const val DEFAULT_MAX_HEALTH = 10.0
        private const val DEFAULT_MOVEMENT_SPEED = 0.3
        private const val DEFAULT_ATTACK_DAMAGE = 3.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)
    }
}
