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
package org.kryptonmc.krypton.entity.player

import org.kryptonmc.api.entity.player.Abilities
import org.kryptonmc.nbt.ByteTag
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

fun Abilities.load(tag: CompoundTag) {
    isInvulnerable = tag.getBoolean("invulnerable")
    canFly = tag.getBoolean("mayFly")
    isFlying = tag.getBoolean("flying")
    canBuild = tag.getBoolean("mayBuild")
    if (tag.contains("mayBuild", ByteTag.ID)) canInstantlyBuild = tag.getBoolean("instabuild")
    if (tag.contains("flySpeed", 99)) {
        walkSpeed = tag.getFloat("walkSpeed")
        flyingSpeed = tag.getFloat("flySpeed")
    }
}

fun Abilities.save() = compound {
    boolean("invulnerable", isInvulnerable)
    boolean("mayfly", canFly)
    boolean("flying", isFlying)
    boolean("mayBuild", canBuild)
    boolean("instabuild", canInstantlyBuild)
    float("walkSpeed", walkSpeed)
    float("flySpeed", flyingSpeed)
}
