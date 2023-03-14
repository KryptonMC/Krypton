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
