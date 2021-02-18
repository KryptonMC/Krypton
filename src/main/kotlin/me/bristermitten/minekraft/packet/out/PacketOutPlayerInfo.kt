package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.bardy.komponent.Component
import me.bristermitten.minekraft.auth.GameProfile
import me.bristermitten.minekraft.entity.Gamemode
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.packet.state.PlayPacket

/**
 * @author AlexL
 * @author Callum Jay Seabrook (BomBardyGamer)
 */
class PacketOutPlayerInfo(
    val action: PlayerAction,
    val players: List<PlayerInfo>
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
                    buf.writeString(JSON.encodeToString(update.displayName))
                }
                PlayerAction.UPDATE_GAMEMODE -> buf.writeVarInt(update.gamemode.id)
                PlayerAction.UPDATE_LATENCY -> buf.writeVarInt(update.latency)
                PlayerAction.UPDATE_DISPLAY_NAME -> {
                    buf.writeBoolean(true)
                    buf.writeString(JSON.encodeToString(update.displayName))
                }
                PlayerAction.REMOVE_PLAYER -> Unit
            }
        }
    }

    data class PlayerInfo(
        val latency: Int,
        val gamemode: Gamemode,
        val profile: GameProfile,
        val displayName: Component
    )

    enum class PlayerAction(val id: Int) {

        ADD_PLAYER(0),
        UPDATE_GAMEMODE(1),
        UPDATE_LATENCY(2),
        UPDATE_DISPLAY_NAME(3),
        REMOVE_PLAYER(4)
    }

    companion object {

        private val JSON = Json {}
    }
}