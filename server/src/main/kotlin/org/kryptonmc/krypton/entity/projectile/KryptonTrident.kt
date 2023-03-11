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
package org.kryptonmc.krypton.entity.projectile

import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.projectile.Trident
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.projectile.TridentSerializer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonTrident(world: KryptonWorld) : KryptonArrowLike(world), Trident {

    override val type: KryptonEntityType<KryptonTrident>
        get() = KryptonEntityTypes.TRIDENT
    override val serializer: EntitySerializer<KryptonTrident>
        get() = TridentSerializer

    override var item: KryptonItemStack = DEFAULT_ITEM
    override var dealtDamage: Boolean = false

    override var loyaltyLevel: Int
        get() = data.get(MetadataKeys.Trident.LOYALTY_LEVEL)
        set(value) = data.set(MetadataKeys.Trident.LOYALTY_LEVEL, value)
    override var isEnchanted: Boolean
        get() = data.get(MetadataKeys.Trident.ENCHANTED)
        set(value) = data.set(MetadataKeys.Trident.ENCHANTED, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Trident.LOYALTY_LEVEL, 0)
        data.define(MetadataKeys.Trident.ENCHANTED, false)
    }

    override fun defaultHitGroundSound(): SoundEvent = SoundEvents.TRIDENT_HIT_GROUND.get()

    companion object {

        private val DEFAULT_ITEM = KryptonItemStack(ItemTypes.TRIDENT.get())
    }
}
