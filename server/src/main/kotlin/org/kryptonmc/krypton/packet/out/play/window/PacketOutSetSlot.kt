package org.kryptonmc.krypton.packet.out.play.window

import io.netty.buffer.ByteBuf
import net.kyori.adventure.nbt.CompoundBinaryTag
import org.kryptonmc.krypton.api.inventory.item.ItemStack
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.writeItem

class PacketOutSetSlot(
    private val id: Int,
    private val slot: Int,
    private val item: ItemStack,
    private val nbt: CompoundBinaryTag? = null
) : PlayPacket(0x15) {

    override fun write(buf: ByteBuf) {
        buf.writeByte(id)
        buf.writeShort(slot)
        buf.writeItem(item, nbt)
    }
}
