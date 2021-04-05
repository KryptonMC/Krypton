package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.Slot
import org.kryptonmc.krypton.extension.readNBTCompound
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

class PacketInCreativeInventoryAction : PlayPacket(0x28) {

    var slot: Short = 0
        private set

    lateinit var clickedItem: Slot
        private set

    override fun read(buf: ByteBuf) {
        slot = buf.readShort()

        if (!buf.readBoolean()) {
            clickedItem = Slot(false)
            return
        }
        clickedItem = Slot(true, buf.readVarInt(), buf.readByte(), buf.readNBTCompound())
    }
}