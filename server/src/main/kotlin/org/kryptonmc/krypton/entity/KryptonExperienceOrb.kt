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

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.entity.ExperienceOrb
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnExperienceOrb
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

class KryptonExperienceOrb(world: KryptonWorld) : KryptonEntity(world, EntityTypes.EXPERIENCE_ORB), ExperienceOrb {

    override var age: Int = 0
    override var count: Int = 1
    override var health: Int = 5
    override var value: Int = 0
    override var following: KryptonPlayer? = null

    override fun load(tag: CompoundTag) {
        super.load(tag)
        age = tag.getShort("Age").toInt()
        count = tag.getInt("Count")
        health = tag.getShort("Health").toInt()
        value = tag.getShort("Value").toInt()
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        short("Age", age.toShort())
        int("Count", count)
        short("Health", health.toShort())
        short("Value", value.toShort())
    }

    override fun getSpawnPacket(): Packet = PacketOutSpawnExperienceOrb(this)
}
