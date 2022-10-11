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
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.animal.Animal
import org.kryptonmc.api.entity.animal.Cat
import org.kryptonmc.api.entity.animal.type.CatVariant
import org.kryptonmc.api.entity.animal.type.CatVariants
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.item.data.DyeColors
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.CatSerializer
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonCat(world: KryptonWorld) : KryptonTamable(world), Cat {

    override val type: KryptonEntityType<Cat>
        get() = KryptonEntityTypes.CAT
    override val serializer: EntitySerializer<KryptonCat>
        get() = CatSerializer

    override var variant: CatVariant
        get() = KryptonRegistries.CAT_VARIANT.get(data.get(MetadataKeys.Cat.VARIANT))!!
        set(value) = data.set(MetadataKeys.Cat.VARIANT, KryptonRegistries.CAT_VARIANT.idOf(value))
    override var isLying: Boolean
        get() = data.get(MetadataKeys.Cat.LYING)
        set(value) = data.set(MetadataKeys.Cat.LYING, value)
    override var isRelaxed: Boolean
        get() = data.get(MetadataKeys.Cat.RELAXED)
        set(value) = data.set(MetadataKeys.Cat.RELAXED, value)
    override var collarColor: DyeColor
        get() = KryptonRegistries.DYE_COLORS.get(data.get(MetadataKeys.Cat.COLLAR_COLOR)) ?: DyeColors.WHITE
        set(value) = data.set(MetadataKeys.Cat.COLLAR_COLOR, KryptonRegistries.DYE_COLORS.idOf(value))

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Cat.VARIANT, KryptonRegistries.CAT_VARIANT.idOf(CatVariants.BLACK))
        data.define(MetadataKeys.Cat.LYING, false)
        data.define(MetadataKeys.Cat.RELAXED, false)
        data.define(MetadataKeys.Cat.COLLAR_COLOR, KryptonRegistries.DYE_COLORS.idOf(DyeColors.RED))
    }

    override fun hiss() {
        playSound(Sound.sound(SoundEvents.CAT_HISS, soundSource(), soundVolume, voicePitch), location.x(), location.y(), location.z())
    }

    override fun canMate(target: Animal): Boolean {
        if (!isTamed) return false
        if (target !is Cat) return false
        return target.isTamed && super.canMate(target)
    }

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.COD || item.type === ItemTypes.SALMON

    companion object {

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.3)
            .add(AttributeTypes.ATTACK_DAMAGE, 3.0)
    }
}
