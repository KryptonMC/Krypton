package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.Slot
import org.kryptonmc.krypton.extension.readNBTCompound
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.packet.state.PlayPacket

/**
 * Sent by the client to indicate that they have performed an action in the creative inventory.
 *
 * The creative inventory works very different to the survival inventory, mainly that in vanilla,
 * the Notchian server deletes an item from a player's inventory, and when you place an item back
 * into your inventory, the server recreates the item.
 *
 * @author Callum Seabrook
 */
class PacketInCreativeInventoryAction : PlayPacket(0x28) {

    /**
     * The inventory slot that the player clicked
     */
    var slot: Short = 0; private set

    /**
     * The item that was clicked
     */
    lateinit var clickedItem: Slot private set

    override fun read(buf: ByteBuf) {
        slot = buf.readShort()

        if (!buf.readBoolean()) {
            clickedItem = Slot(false)
            return
        }
        clickedItem = Slot(true, buf.readVarInt(), buf.readByte(), buf.readNBTCompound())
    }
}