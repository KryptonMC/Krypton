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
package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeUByte

/**
 * Changes the state of something in the game, such as when it starts raining or when you "win" the game.
 *
 * Krypton currently only makes use of the states [GameState.BEGIN_RAINING] and [GameState.END_RAINING]
 *
 * @param reason the reason for the update (the state that's being updated, just what it's named in the protocol)
 * @param value the value of the updated state. Not used for most states.
 */
class PacketOutChangeGameState(
    private val reason: GameState,
    private val value: Float = 0.0F
) : PlayPacket(0x1D) {

    override fun write(buf: ByteBuf) {
        buf.writeUByte(reason.ordinal.toUByte())
        buf.writeFloat(value)
    }
}

enum class GameState {

    NO_RESPAWN_BLOCK_AVAILABLE,
    END_RAINING,
    BEGIN_RAINING,
    CHANGE_GAMEMODE,
    WIN_GAME,
    DEMO_EVENT,
    ARROW_HIT_PLAYER,
    RAIN_LEVEL_CHANGE,
    THUNDER_LEVEL_CHANGE,
    PLAY_PUFFERFISH_STING_SOUND,
    PLAY_ELDER_GUARDIAN_MOB_APPEARANCE,
    ENABLE_RESPAWN_SCREEN
}
