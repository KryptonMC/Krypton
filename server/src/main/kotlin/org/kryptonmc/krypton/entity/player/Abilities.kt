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

import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag

class Abilities {

    var invulnerable: Boolean = false
    var flying: Boolean = false
    var canFly: Boolean = false
    var canInstantlyBuild: Boolean = false
    var canBuild: Boolean = true
    var walkingSpeed: Float = 0.05F
    var flyingSpeed: Float = 0.1F

    fun load(data: CompoundTag) {
        val abilities = if (data.contains("abilities", CompoundTag.ID)) data.getCompound("abilities") else return
        invulnerable = abilities.getBoolean("invulnerable")
        flying = abilities.getBoolean("flying")
        canFly = abilities.getBoolean("mayfly")
        canInstantlyBuild = abilities.getBoolean("instabuild")
        if (abilities.contains("flySpeed", 99)) {
            flyingSpeed = abilities.getFloat("flySpeed")
            walkingSpeed = abilities.getFloat("walkSpeed")
        }
        if (abilities.contains("mayBuild", ByteTag.ID)) canBuild = abilities.getBoolean("mayBuild")
    }

    fun save(builder: CompoundTag.Builder): CompoundTag.Builder = builder.compound("abilities") {
        boolean("invulnerable", invulnerable)
        boolean("flying", flying)
        boolean("mayfly", canFly)
        boolean("instabuild", canInstantlyBuild)
        boolean("mayBuild", canBuild)
        float("flySpeed", flyingSpeed)
        float("walkSpeed", walkingSpeed)
    }
}
