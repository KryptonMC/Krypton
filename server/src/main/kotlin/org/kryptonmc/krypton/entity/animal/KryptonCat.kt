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
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.animal.Animal
import org.kryptonmc.api.entity.animal.Cat
import org.kryptonmc.api.entity.animal.type.CatType
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.api.item.data.DyeColors
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonCat(world: KryptonWorld) : KryptonTamable(world, EntityTypes.CAT, ATTRIBUTES), Cat {

    override var catType: CatType
        get() = CAT_TYPES[data[MetadataKeys.CAT.TYPE]]
        set(value) = data.set(MetadataKeys.CAT.TYPE, value.ordinal)
    override var isLying: Boolean
        get() = data[MetadataKeys.CAT.LYING]
        set(value) = data.set(MetadataKeys.CAT.LYING, value)
    override var isRelaxed: Boolean
        get() = data[MetadataKeys.CAT.RELAXED]
        set(value) = data.set(MetadataKeys.CAT.RELAXED, value)
    override var collarColor: DyeColor
        get() = Registries.DYE_COLORS[data[MetadataKeys.CAT.COLLAR_COLOR]] ?: DyeColors.WHITE
        set(value) = data.set(MetadataKeys.CAT.COLLAR_COLOR, Registries.DYE_COLORS.idOf(value))

    init {
        data.add(MetadataKeys.CAT.TYPE)
        data.add(MetadataKeys.CAT.LYING)
        data.add(MetadataKeys.CAT.RELAXED)
        data.add(MetadataKeys.CAT.COLLAR_COLOR)
    }

    override fun hiss() {
        playSound(Sound.sound(SoundEvents.CAT_HISS, soundSource, soundVolume, voicePitch), location.x(), location.y(), location.z())
    }

    override fun canMate(target: Animal): Boolean {
        if (!isTame) return false
        if (target !is Cat) return false
        return target.isTame && super.canMate(target)
    }

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.COD || item.type === ItemTypes.SALMON

    companion object {

        private val CAT_TYPES = CatType.values()
        private val ATTRIBUTES = attributes()
            .add(AttributeTypes.MAX_HEALTH, 10.0)
            .add(AttributeTypes.MOVEMENT_SPEED, 0.3)
            .add(AttributeTypes.ATTACK_DAMAGE, 3.0)
            .build()
    }
}
