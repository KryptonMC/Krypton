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
    var walkingSpeed: Float = 0.05F
    var flyingSpeed: Float = 0.1F

    fun load(data: CompoundTag) {
        val abilities = if (data.contains(ABILITIES_TAG, CompoundTag.ID)) data.getCompound(ABILITIES_TAG) else return
        invulnerable = abilities.getBoolean(INVULNERABLE_TAG)
        flying = abilities.getBoolean(FLYING_TAG)
        canFly = abilities.getBoolean(MAY_FLY_TAG)
        canInstantlyBuild = abilities.getBoolean(INSTABUILD_TAG)
        if (abilities.hasNumber(FLY_SPEED_TAG)) {
            flyingSpeed = abilities.getFloat(FLY_SPEED_TAG)
            walkingSpeed = abilities.getFloat(WALK_SPEED_TAG)
        }
        if (abilities.contains(MAY_BUILD_TAG, ByteTag.ID)) canBuild = abilities.getBoolean(MAY_BUILD_TAG)
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
