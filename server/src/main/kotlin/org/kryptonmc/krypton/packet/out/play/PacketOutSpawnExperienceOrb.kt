package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.KryptonExperienceOrb
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeVarInt

class PacketOutSpawnExperienceOrb(private val orb: KryptonExperienceOrb) : PlayPacket(0x01) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(orb.id)
        buf.writeDouble(orb.location.x)
        buf.writeDouble(orb.location.y)
        buf.writeDouble(orb.location.z)
        buf.writeShort(orb.count)
    }
}
