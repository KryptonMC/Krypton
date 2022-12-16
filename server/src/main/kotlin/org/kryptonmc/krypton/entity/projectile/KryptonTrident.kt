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

    override val defaultHitGroundSound: SoundEvent
        get() = SoundEvents.TRIDENT_HIT_GROUND

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Trident.LOYALTY_LEVEL, 0)
        data.define(MetadataKeys.Trident.ENCHANTED, false)
    }

    companion object {

        private val DEFAULT_ITEM = KryptonItemStack(ItemTypes.TRIDENT)
    }
}
