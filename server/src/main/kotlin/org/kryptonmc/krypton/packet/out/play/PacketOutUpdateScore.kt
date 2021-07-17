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
import org.kryptonmc.api.world.scoreboard.Score
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutUpdateScore(
    private val score: Score,
    private val action: ScoreAction
) : PlayPacket(0x56) {

    override fun write(buf: ByteBuf) {
        buf.writeString(score.player.name, 40)
        buf.writeByte(action.ordinal)
        buf.writeString(score.objective.name, 16)
        if (action != ScoreAction.REMOVE) buf.writeVarInt(score.score)
    }
}

enum class ScoreAction {

    CREATE_OR_UPDATE,
    REMOVE
}
