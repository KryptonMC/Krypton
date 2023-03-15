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
package org.kryptonmc.krypton.packet.out.play

import org.kryptonmc.krypton.entity.player.Abilities
import org.kryptonmc.krypton.network.buffer.BinaryReader
import org.kryptonmc.krypton.network.buffer.BinaryWriter
import org.kryptonmc.krypton.packet.Packet

@JvmRecord
data class PacketOutAbilities(
    val isInvulnerable: Boolean,
    val isFlying: Boolean,
    val canFly: Boolean,
    val canInstantlyBuild: Boolean,
    val flyingSpeed: Float,
    val walkingSpeed: Float
) : Packet {

    constructor(reader: BinaryReader) : this(reader.readByte().toInt(), reader.readFloat(), reader.readFloat())

    private constructor(flags: Int, flyingSpeed: Float, walkingSpeed: Float) : this(flags and FLAG_INVULNERABLE != 0, flags and FLAG_FLYING != 0,
        flags and FLAG_CAN_FLY != 0, flags and FLAG_CAN_INSTANTLY_BUILD != 0, flyingSpeed, walkingSpeed)

    override fun write(writer: BinaryWriter) {
        var flags = 0
        if (isInvulnerable) flags = flags or FLAG_INVULNERABLE
        if (isFlying) flags = flags or FLAG_FLYING
        if (canFly) flags = flags or FLAG_CAN_FLY
        if (canInstantlyBuild) flags = flags or FLAG_CAN_INSTANTLY_BUILD
        writer.writeByte(flags.toByte())
        writer.writeFloat(flyingSpeed)
        writer.writeFloat(walkingSpeed)
    }

    companion object {

        private const val FLAG_INVULNERABLE = 0x01
        private const val FLAG_FLYING = 0x02
        private const val FLAG_CAN_FLY = 0x04
        private const val FLAG_CAN_INSTANTLY_BUILD = 0x08

        @JvmStatic
        fun create(abilities: Abilities): PacketOutAbilities {
            return PacketOutAbilities(abilities.invulnerable, abilities.flying, abilities.canFly, abilities.canInstantlyBuild,
                abilities.flyingSpeed, abilities.walkingSpeed)
        }
    }
}
