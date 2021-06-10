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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeUByte
import org.kryptonmc.krypton.util.writeVarInt

/**
 * Sent when some form of animation should be played by the client for a specific entity it knows about.
 * Possible values are in [EntityAnimation].
 *
 * @param entityId the ID of the entity who we want the client to play an animation for
 * @param animation the animation we want played
 */
class PacketOutEntityAnimation(
    private val entityId: Int,
    private val animation: EntityAnimation
) : PlayPacket(0x06) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(entityId)
        buf.writeUByte(animation.ordinal.toUByte())
    }
}

enum class EntityAnimation {

    SWING_MAIN_ARM,
    TAKE_DAMAGE,
    LEAVE_BED,
    SWING_OFFHAND,
    CRITICAL_EFFECT,
    MAGIC_CRITICAL_EFFECT
}
