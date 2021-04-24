package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.entity.Hand
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.util.readEnum
import org.kryptonmc.krypton.util.readVarInt
import org.kryptonmc.krypton.util.toVector
import org.kryptonmc.krypton.world.block.BlockFace

/**
 * Sent to indicate the player has placed a block.
 */
class PacketInPlayerBlockPlacement : PlayPacket(0x2E) {

    /**
     * The hand the player used to place the block
     */
    lateinit var hand: Hand private set

    /**
     * The location of the __block__ being placed
     */
    lateinit var location: Vector private set

    /**
     * The face of the block that has been placed that is facing the player
     */
    lateinit var face: BlockFace private set

    /**
     * The X, Y and Z positions of the crosshair on the block, from 0 to 1 increasing:
     * - West to east for X
     * - Bottom to top for Y
     * - North to south for Z
     */
    var cursorX = 0F; private set
    var cursorY = 0F; private set
    var cursorZ = 0F; private set

    /**
     * Whether the player's head is inside a block
     */
    var insideBlock = false; private set

    override fun read(buf: ByteBuf) {
        hand = buf.readEnum(Hand::class)
        location = buf.readLong().toVector()
        face = BlockFace.fromId(buf.readVarInt())
        cursorX = buf.readFloat()
        cursorY = buf.readFloat()
        cursorZ = buf.readFloat()
        insideBlock = buf.readBoolean()
    }
}
