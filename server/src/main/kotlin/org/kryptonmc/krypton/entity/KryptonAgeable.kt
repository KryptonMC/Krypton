/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.Ageable
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

abstract class KryptonAgeable(
    world: KryptonWorld,
    type: EntityType<out Ageable>,
    attributeSupplier: AttributeSupplier
) : KryptonMob(world, type, attributeSupplier), Ageable {

    final override var age = 0
        set(value) {
            val old = field
            field = value
            if (old < 0 && value >= 0 || old >= 0 && value < 0) {
                data[MetadataKeys.AGEABLE.BABY] = value < 0
                onAgeTransformation()
            }
        }
    private var forcedAge = 0
    private var forcedAgeTimer = 0
    override val canBreedNaturally: Boolean
        get() = false
    override var isBaby: Boolean
        get() = age < 0
        set(value) {
            age = if (value) -24000 else 0
        }

    protected open fun onAgeTransformation() = Unit

    fun age(amount: Int, forced: Boolean) {
        val old = age
        var newAge = old + amount * 20
        if (newAge > 0) newAge = 0
        val difference = newAge - old
        age = newAge

        if (forced) {
            forcedAge += difference
            if (forcedAgeTimer == 0) forcedAgeTimer = 40
        }
        if (age == 0) age = forcedAge
    }

    override fun age(amount: Int) {
        age(amount, false)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        age = tag.getInt("Age")
        forcedAge = tag.getInt("ForcedAge")
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        int("Age", age)
        int("ForcedAge", forcedAge)
    }
}
