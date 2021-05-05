/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.packet.out.play.scoreboard

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.world.scoreboard.Score
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Tells the client to perform an action to the score of some entity
 */
class PacketOutScoreboardScore(
    private val score: Score,
    private val action: ScoreAction
) : PlayPacket(0x4D) {

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
