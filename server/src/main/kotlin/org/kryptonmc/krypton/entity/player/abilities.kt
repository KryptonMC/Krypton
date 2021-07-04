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

import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.kryptonmc.api.entity.player.Abilities
import org.kryptonmc.krypton.util.nbt.contains
import org.kryptonmc.krypton.util.nbt.getBoolean
import org.kryptonmc.krypton.util.nbt.getFloat
import org.kryptonmc.krypton.util.nbt.setBoolean

fun Abilities.load(tag: NBTCompound) {
    isInvulnerable = tag.getBoolean("invulnerable", false)
    canFly = tag.getBoolean("mayFly", false)
    isFlying = tag.getBoolean("flying", false)
    canBuild = tag.getBoolean("mayBuild", false)
    if (tag.contains("mayBuild", NBTTypes.TAG_Byte)) canInstantlyBuild = tag.getBoolean("instabuild", false)
    if (tag.contains("flySpeed", 99)) {
        walkSpeed = tag.getFloat("walkSpeed", 0F)
        flyingSpeed = tag.getFloat("flySpeed", 0F)
    }
}

fun Abilities.save(): NBTCompound = NBTCompound()
    .setBoolean("invulnerable", isInvulnerable)
    .setBoolean("mayfly", canFly)
    .setBoolean("flying", isFlying)
    .setBoolean("mayBuild", canBuild)
    .setBoolean("instabuild", canInstantlyBuild)
    .setFloat("walkSpeed", walkSpeed)
    .setFloat("flySpeed", flyingSpeed)
