package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.extension.writeUByte
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Changes the state of something in the game, such as when it starts raining or when you "win" the game.
 *
 * Krypton currently only makes use of the states [GameState.BEGIN_RAINING] and [GameState.END_RAINING]
 *
 * @param reason the reason for the update (the state that's being updated, just what it's named in the protocol)
 * @param value the value of the updated state. Not used for most states.
 *
 * @author Callum Seabrook
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