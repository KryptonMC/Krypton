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
package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt

/**
 * Contrary to its name, this is only for players, as it is only sent by clients.
 * This is sent when the player performs one of the actions listed in the [EntityAction] enum.
 */
class PacketInEntityAction : PlayPacket(0x1C) {

    /**
     * The action taken by the entity
     */
    lateinit var action: EntityAction
        private set

    /**
     * This is a bit magic. It's the jump boost modifier, only used with the
     * [start jump with horse][EntityAction.START_JUMP_WITH_HORSE] action.
     */
    var data = -1
        private set

    override fun read(buf: ByteBuf) {
        buf.readVarInt() // we already know the entity ID because we know where this came from, smh Mojang.
        action = buf.readEnum()
        data = buf.readVarInt()
    }
}

/**
 * Represents possible entity actions for the client
 */
enum class EntityAction {

    START_SNEAKING,
    STOP_SNEAKING,
    LEAVE_BED,
    START_SPRINTING,
    STOP_SPRINTING,
    START_JUMP_WITH_HORSE,
    STOP_JUMP_WITH_HORSE,
    OPEN_HORSE_INVENTORY,
    START_FLYING_WITH_ELYTRA
}
