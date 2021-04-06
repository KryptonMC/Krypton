package org.kryptonmc.krypton.packet.`in`.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.api.entity.Hand
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.extension.readVarInt
import org.kryptonmc.krypton.extension.toVector
import org.kryptonmc.krypton.packet.state.PlayPacket
import org.kryptonmc.krypton.world.block.BlockFace

class PacketInPlayerBlockPlacement : PlayPacket(0x2E) {

    lateinit var hand: Hand
    lateinit var location: Vector
    lateinit var face: BlockFace

    var cursorX = 0.0F
    var cursorY = 0.0F
    var cursorZ = 0.0F

    var insideBlock = false

    override fun read(buf: ByteBuf) {
        hand = Hand.values()[buf.readVarInt()]
        location = buf.readLong().toVector()
        face = BlockFace.fromId(buf.readVarInt())
        cursorX = buf.readFloat()
        cursorY = buf.readFloat()
        cursorZ = buf.readFloat()
        insideBlock = buf.readBoolean()
    }
}