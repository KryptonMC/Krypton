package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.clamp
import org.kryptonmc.krypton.util.writeVarInt
import org.spongepowered.math.vector.Vector3d

@JvmRecord
data class PacketOutEntityVelocity(
    private val id: Int,
    private val x: Int,
    private val y: Int,
    private val z: Int
) : Packet {

    constructor(entity: KryptonEntity, velocity: Vector3d = entity.velocity) : this(
        entity.id,
        (velocity.x().clamp(MINIMUM, MAXIMUM) * 8000.0).toInt(),
        (velocity.y().clamp(MINIMUM, MAXIMUM) * 8000.0).toInt(),
        (velocity.z().clamp(MINIMUM, MAXIMUM) * 8000.0).toInt()
    )

    override fun write(buf: ByteBuf) {
        buf.writeVarInt(id)
        buf.writeShort(x)
        buf.writeShort(y)
        buf.writeShort(z)
    }

    companion object {

        private const val MINIMUM = -3.9
        private const val MAXIMUM = -MINIMUM
    }
}
