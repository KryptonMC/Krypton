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
import org.kryptonmc.api.world.scoreboard.ScoreboardPosition
import org.kryptonmc.api.world.scoreboard.Team
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard

class PacketOutDisplayScoreboard(
    private val scoreboard: KryptonScoreboard,
    private val team: Team? = null // used for team specific positioning
) : PlayPacket(0x4C) {

    override fun write(buf: ByteBuf) {
        when (scoreboard.position) {
            ScoreboardPosition.TEAM_SPECIFIC -> {
                requireNotNull(team) { "Team must be supplied if position is team specific!" }
                buf.writeByte(3 + team.color.ordinal)
            }
            else -> buf.writeByte(scoreboard.position.id)
        }
        buf.writeString(scoreboard.name, 16)
    }
}
