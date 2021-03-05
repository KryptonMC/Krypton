package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.auth.GameProfile
import org.kryptonmc.krypton.entity.Gamemode
import org.kryptonmc.krypton.extension.writeChat
import org.kryptonmc.krypton.extension.writeString
import org.kryptonmc.krypton.extension.writeUUID
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * @author AlexL
 * @author Callum Jay Seabrook (BomBardyGamer)
 */
class PacketOutPlayerInfo(
    private val action: PlayerAction,
    private val players: List<PlayerInfo> = emptyList()
) : PlayPacket(0x32) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(action.id)
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

                    buf.writeVarInt(update.gamemode.id)
                    buf.writeVarInt(update.latency)
                    buf.writeBoolean(true)
                    buf.writeChat(update.displayName)
                }
                PlayerAction.UPDATE_GAMEMODE -> buf.writeVarInt(update.gamemode.id)
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

    enum class PlayerAction(val id: Int) {

        ADD_PLAYER(0),
        UPDATE_GAMEMODE(1),
        UPDATE_LATENCY(2),
        UPDATE_DISPLAY_NAME(3),
        REMOVE_PLAYER(4)
    }
}