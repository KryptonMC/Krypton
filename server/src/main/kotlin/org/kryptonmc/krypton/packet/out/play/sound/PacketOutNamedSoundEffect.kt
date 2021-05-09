package org.kryptonmc.krypton.packet.out.play.sound

import io.netty.buffer.ByteBuf
import net.kyori.adventure.sound.Sound
import org.kryptonmc.krypton.api.registry.toNamespacedKey
import org.kryptonmc.krypton.api.space.Position
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutNamedSoundEffect(
    private val sound: Sound,
    private val location: Position
) : PlayPacket(0x18) {

    override fun write(buf: ByteBuf) {
        buf.writeKey(sound.name().toNamespacedKey())
        buf.writeVarInt(sound.source().ordinal)
        buf.writeInt((location.x * 8.0).toInt())
        buf.writeInt((location.y * 8.0).toInt())
        buf.writeInt((location.z * 8.0).toInt())
        buf.writeFloat(sound.pitch())
        buf.writeFloat(sound.volume())
    }
}
