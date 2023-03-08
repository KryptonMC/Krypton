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

import org.kryptonmc.api.entity.ExperienceOrb
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.ExperienceOrbSerializer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnExperienceOrb
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonExperienceOrb(world: KryptonWorld) : KryptonEntity(world), ExperienceOrb {

    override val type: KryptonEntityType<KryptonExperienceOrb>
        get() = KryptonEntityTypes.EXPERIENCE_ORB
    override val serializer: EntitySerializer<KryptonExperienceOrb>
        get() = ExperienceOrbSerializer

    var age: Int = 0
    override var count: Int = 1
    override var health: Int = 5
    override var experience: Int = 0
    override var following: KryptonPlayer? = null

    override fun getSpawnPacket(): Packet = PacketOutSpawnExperienceOrb.create(this)
}
