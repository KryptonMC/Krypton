package me.bristermitten.minekraft.packet.out

import io.netty.buffer.ByteBuf
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.json.Json
import me.bristermitten.minekraft.extension.writeString
import me.bristermitten.minekraft.extension.writeUUID
import me.bristermitten.minekraft.extension.writeVarInt
import me.bristermitten.minekraft.lang.Color
import me.bristermitten.minekraft.packet.data.Chat
import me.bristermitten.minekraft.packet.play.PlayPacket
import java.util.*

/**
 * @author AlexL
 */
class PacketOutPlayerInfo : PlayPacket(0x34) {

    private val json = Json { encodeDefaults = false }

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(0)
        buf.writeVarInt(1)
        buf.writeUUID(UUID.fromString("876ce46d-dc56-4a17-9644-0be67fe7c7f6"))
        buf.writeString("BristerMitten")

        buf.writeVarInt(1)

        buf.writeString("textures")
        buf.writeString("ewogICJ0aW1lc3RhbXAiIDogMTU5NDkyNzk1OTc2OSwKICAicHJvZmlsZUlkIiA6ICI4NzZjZTQ2ZGRjNTY0YTE3OTY0NDBiZTY3ZmU3YzdmNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCcmlzdGVyTWl0dGVuIiwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVjY2VjM2ZjMDZhMWI2YmY5NjJkMjMwNzA0YjY2MjNiYmZiNDMwNDNmMDY2Nzk2NmMyMGM4OWMzMGM0YTMzMCIKICAgIH0KICB9Cn0=")
        buf.writeBoolean(false)

        buf.writeVarInt(0)
        buf.writeVarInt(5)
        buf.writeBoolean(true)

        buf.writeString(
            json.encodeToString(
                SetSerializer(Chat.serializer()), "BristerMitten".toCharArray().map {
                    Chat(
                        it.toString(),
                        bold = true,
                        color = Color.values().random()
                    )
                }.toSet()
            )
        )
    }
}
