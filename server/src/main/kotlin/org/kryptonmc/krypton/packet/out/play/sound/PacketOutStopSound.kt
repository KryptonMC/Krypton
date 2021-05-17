package org.kryptonmc.krypton.packet.out.play.sound

import io.netty.buffer.ByteBuf
import net.kyori.adventure.sound.SoundStop
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeKey
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutStopSound(private val soundStop: SoundStop) : PlayPacket(0x52) {

    override fun write(buf: ByteBuf) {
        val source = soundStop.source()
        val sound = soundStop.sound()
        if (source != null) {
            if (sound != null) {
                buf.writeByte(3)
                buf.writeVarInt(source.ordinal)
                buf.writeKey(sound)
                return
            }
            buf.writeByte(1)
            buf.writeVarInt(source.ordinal)
            return
        }
        if (sound != null) {
            buf.writeByte(2)
            buf.writeKey(sound)
            return
        }
        buf.writeByte(0)
    }
}
