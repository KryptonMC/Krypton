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
package org.kryptonmc.krypton.entity.player

import org.kryptonmc.krypton.util.nbt.hasNumber
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

class Abilities {

    var invulnerable: Boolean = false
    var flying: Boolean = false
    var canFly: Boolean = false
    var canInstantlyBuild: Boolean = false
    var canBuild: Boolean = true
    var flyingSpeed: Float = 0.05F // Default flying speed from vanilla
    var walkingSpeed: Float = 0.1F // Default walking speed from vanilla

    fun load(data: CompoundTag) {
        if (!data.contains(ABILITIES_TAG, CompoundTag.ID)) return
        val abilities = data.getCompound(ABILITIES_TAG)

        invulnerable = abilities.getBoolean(INVULNERABLE_TAG)
        flying = abilities.getBoolean(FLYING_TAG)
        canFly = abilities.getBoolean(MAY_FLY_TAG)
        canInstantlyBuild = abilities.getBoolean(INSTABUILD_TAG)
        if (abilities.contains(MAY_BUILD_TAG, ByteTag.ID)) canBuild = abilities.getBoolean(MAY_BUILD_TAG)

        if (abilities.hasNumber(FLY_SPEED_TAG)) {
            flyingSpeed = abilities.getFloat(FLY_SPEED_TAG)
            walkingSpeed = abilities.getFloat(WALK_SPEED_TAG)
        }
    }

    fun save(builder: CompoundTag.Builder): CompoundTag.Builder = builder.compound(ABILITIES_TAG) {
        putBoolean(INVULNERABLE_TAG, invulnerable)
        putBoolean(FLYING_TAG, flying)
        putBoolean(MAY_FLY_TAG, canFly)
        putBoolean(INSTABUILD_TAG, canInstantlyBuild)
        putBoolean(MAY_BUILD_TAG, canBuild)
        putFloat(FLY_SPEED_TAG, flyingSpeed)
        putFloat(WALK_SPEED_TAG, walkingSpeed)
    }

    companion object {

        private const val ABILITIES_TAG = "abilities"
        private const val INVULNERABLE_TAG = "invulnerable"
        private const val FLYING_TAG = "flying"
        private const val MAY_FLY_TAG = "mayfly"
        private const val INSTABUILD_TAG = "instabuild"
        private const val MAY_BUILD_TAG = "mayBuild"
        private const val FLY_SPEED_TAG = "flySpeed"
        private const val WALK_SPEED_TAG = "walkSpeed"
    }
}
