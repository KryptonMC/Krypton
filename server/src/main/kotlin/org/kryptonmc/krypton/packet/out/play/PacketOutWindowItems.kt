package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.inventory.PlayerInventory
import org.kryptonmc.krypton.api.inventory.item.ItemStack
import org.kryptonmc.krypton.extension.writeVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.registry.Registries

/**
 * Set the items for an inventory with an ID. Currently only supports player inventories.
 *
 * @param inventory the inventory to get the items to send from
 *
 * @author Callum Seabrook
 */
class PacketOutWindowItems(private val inventory: PlayerInventory) : PlayPacket(0x13) {

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(0)

        buf.writeShort(46)
        inventory.items.forEach { buf.writeItem(it) }
    }

    private fun ByteBuf.writeItem(item: ItemStack?) {
        if (item == null) {
            writeBoolean(false)
            return
        }
        writeBoolean(true)
        writeVarInt(Registries.BLOCKS.idOf(item.type.key))
        writeByte(item.amount)
        writeByte(0) // TAG_End
    }
}