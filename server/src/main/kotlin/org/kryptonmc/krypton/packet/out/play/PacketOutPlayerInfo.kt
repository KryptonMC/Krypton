package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.world.Gamemode
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.util.writeChat
import org.kryptonmc.krypton.util.writeString
import org.kryptonmc.krypton.util.writeUUID
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Updates information on the tab list (called the player list by vanilla).
 *
 * @param action the action to perform
 * @param players a list of players, can be empty if not required by the [action]
 */
class PacketOutPlayerInfo(
    private val action: PlayerAction,
    private val players: List<PlayerInfo> = emptyList()
) : PlayPacket(0x32) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.ordinal)
        buf.writeVarInt(players.size)

        players.forEach { update ->
            buf.writeUUID(update.profile.uuid)
            when (action) {
                PlayerAction.ADD_PLAYER -> {
                    buf.writeString(update.profile.name)
                    buf.writeVarInt(update.profile.properties.size)

                    update.profile.properties.forEach {
                        buf.writeString(it.name)
                        buf.writeString(it.value)
                        buf.writeBoolean(true)
                        buf.writeString(it.signature)
                    }

                    buf.writeVarInt(update.gamemode.ordinal)
                    buf.writeVarInt(update.latency)
                    buf.writeBoolean(true)
                    buf.writeChat(update.displayName)
                }
                PlayerAction.UPDATE_GAMEMODE -> buf.writeVarInt(update.gamemode.ordinal)
                PlayerAction.UPDATE_LATENCY -> buf.writeVarInt(update.latency)
                PlayerAction.UPDATE_DISPLAY_NAME -> {
                    buf.writeBoolean(true)
                    buf.writeChat(update.displayName)
                }
                PlayerAction.REMOVE_PLAYER -> Unit
            }
        }
    }

    data class PlayerInfo(
        val latency: Int = 0,
        val gamemode: Gamemode = Gamemode.SURVIVAL,
        val profile: GameProfile,
        val displayName: Component = Component.text("")
    )

    enum class PlayerAction {

        ADD_PLAYER,
        UPDATE_GAMEMODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER
    }
}
