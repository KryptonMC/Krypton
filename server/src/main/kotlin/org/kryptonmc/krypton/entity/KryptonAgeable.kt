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
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.Ageable
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.world.KryptonWorld

abstract class KryptonAgeable(world: KryptonWorld) : KryptonMob(world), Ageable {

    override val serializer: EntitySerializer<out KryptonAgeable>
        get() = AgeableSerializer

    final override var age: Int = 0
        set(value) {
            val old = field
            field = value
            if (old < 0 && value >= 0 || old >= 0 && value < 0) {
                data.set(MetadataKeys.Ageable.BABY, value < 0)
                onAgeTransformation()
            }
        }
    final override var isBaby: Boolean
        get() = age < 0
        set(value) {
            age = if (value) BABY_AGE else 0
        }

    internal var forcedAge = 0
    private var forcedAgeTimer = 0
    override val canBreedNaturally: Boolean
        get() = false

    init {
        data.define(MetadataKeys.Ageable.BABY, false)
    }

    protected open fun onAgeTransformation() {
        // nothing to do by default
    }

    private fun age(amount: Int, forced: Boolean) {
        val old = age
        var newAge = old + amount * 20
        if (newAge > 0) newAge = 0
        val difference = newAge - old
        age = newAge

        if (forced) {
            forcedAge += difference
            if (forcedAgeTimer == 0) forcedAgeTimer = FORCED_AGE_TIME
        }
        if (age == 0) age = forcedAge
    }

    override fun age(amount: Int) {
        age(amount, false)
    }

    companion object {

        private const val BABY_AGE = -24000
        private const val FORCED_AGE_TIME = 40
    }
}
