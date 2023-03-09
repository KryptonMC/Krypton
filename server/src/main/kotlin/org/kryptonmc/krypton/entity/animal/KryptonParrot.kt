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
package org.kryptonmc.krypton.entity.animal

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.entity.animal.Animal
import org.kryptonmc.api.entity.animal.Parrot
import org.kryptonmc.api.entity.animal.type.ParrotVariant
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.ParrotSerializer
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonParrot(world: KryptonWorld) : KryptonTamable(world), Parrot {

    override val type: KryptonEntityType<KryptonParrot>
        get() = KryptonEntityTypes.PARROT
    override val serializer: EntitySerializer<KryptonParrot>
        get() = ParrotSerializer

    override var variant: ParrotVariant
        get() = TYPES.getOrNull(data.get(MetadataKeys.Parrot.TYPE)) ?: ParrotVariant.RED_AND_BLUE
        set(value) = data.set(MetadataKeys.Parrot.TYPE, value.ordinal)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Parrot.TYPE, ParrotVariant.RED_AND_BLUE.ordinal)
    }

    override fun isFood(item: ItemStack): Boolean = false

    override fun canMate(target: Animal): Boolean = false

    override fun soundSource(): Sound.Source = Sound.Source.NEUTRAL

    override fun voicePitch(): Float = (random.nextFloat() - random.nextFloat()) * 0.2F + 1F

    companion object {

        private val TYPES = ParrotVariant.values()

        private const val DEFAULT_MAX_HEALTH = 6.0
        private const val DEFAULT_FLYING_SPEED = 0.4
        private const val DEFAULT_MOVEMENT_SPEED = 0.2

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.FLYING_SPEED, DEFAULT_FLYING_SPEED)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
    }
}
