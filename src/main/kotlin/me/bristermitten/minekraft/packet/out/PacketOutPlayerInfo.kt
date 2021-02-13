package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.Json
import me.bristermitten.minekraft.entity.Gamemode
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.lang.Color
import me.bristermitten.minekraft.packet.data.Chat
import me.bristermitten.minekraft.packet.state.PlayPacket
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import java.util.*

/**
 * @author AlexL
 */
class PacketOutPlayerInfo(
    val action: PlayerAction,
    val numberOfPlayers: Int,
    val uuid: UUID,
    // TEMPORARY - only supports adding players at the moment
    val name: String,
    val properties: List<Property>,
    val gamemode: Gamemode,
    val ping: Int,
    val hasDisplayName: Boolean,
    val displayName: Component
) : PlayPacket(0x32) {

    //private val json = Json {}

    override fun write(buf: ByteBuf) {
//        buf.writeVarInt(0)
//        buf.writeVarInt(1)
//        buf.writeUUID(UUID.fromString("876ce46d-dc56-4a17-9644-0be67fe7c7f6"))
//        buf.writeString("BristerMitten")
//
//        buf.writeVarInt(1)
//
//        buf.writeString("textures")
//        buf.writeString("ewogICJ0aW1lc3RhbXAiIDogMTU5NDkyNzk1OTc2OSwKICAicHJvZmlsZUlkIiA6ICI4NzZjZTQ2ZGRjNTY0YTE3OTY0NDBiZTY3ZmU3YzdmNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCcmlzdGVyTWl0dGVuIiwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVjY2VjM2ZjMDZhMWI2YmY5NjJkMjMwNzA0YjY2MjNiYmZiNDMwNDNmMDY2Nzk2NmMyMGM4OWMzMGM0YTMzMCIKICAgIH0KICB9Cn0=")
//        buf.writeBoolean(false)
//
//        buf.writeVarInt(0)
//        buf.writeVarInt(5)
//        buf.writeBoolean(true)
//        buf.writeString(GsonComponentSerializer.gson().serialize(Component.text("BristerMitten")))

        buf.writeVarInt(action.id)
        buf.writeVarInt(numberOfPlayers)
        buf.writeUUID(uuid)
        buf.writeString(name, 16)

        buf.writeVarInt(properties.size)
        for (property in properties) {
            buf.writeString(property.name)
            buf.writeString(property.value)
            buf.writeBoolean(property.isSigned)
            if (property.isSigned) buf.writeString(property.signature)
        }

        buf.writeVarInt(gamemode.id)
        buf.writeVarInt(ping)
        buf.writeBoolean(hasDisplayName)
        if (hasDisplayName) buf.writeString(GsonComponentSerializer.gson().serialize(displayName))

//        buf.writeString(
//            json.encodeToString(
//                SetSerializer(Chat.serializer()), "BristerMitten".toCharArray().map {
//                    Chat(
//                        it.toString(),
//                        bold = true,
//                        color = Color.values().random()
//                    )
//                }.toSet()
//            )
//        )
    }

    enum class PlayerAction(val id: Int) {

        ADD_PLAYER(0),
        UPDATE_GAMEMODE(1),
        UPDATE_LATENCY(2),
        UPDATE_DISPLAY_NAME(3),
        REMOVE_PLAYER(4)
    }
}

data class Property(
    val name: String,
    val value: String,
    val isSigned: Boolean = false,
    val signature: String = ""
)
