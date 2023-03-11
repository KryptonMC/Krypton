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

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Ageable.BABY, false)
    }

    protected open fun onAgeTransformation() {
        // nothing to do by default
    }

    private fun increaseAge(amount: Int, forced: Boolean) {
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

    override fun increaseAge(amount: Int) {
        increaseAge(amount, false)
    }

    override fun canBreedNaturally(): Boolean = false

    companion object {

        private const val BABY_AGE = -24000
        private const val FORCED_AGE_TIME = 40
    }
}
