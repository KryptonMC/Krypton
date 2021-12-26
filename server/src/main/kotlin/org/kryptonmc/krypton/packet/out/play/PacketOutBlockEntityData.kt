package org.kryptonmc.krypton.packet.out.play

import io.netty.buffer.ByteBuf
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.util.writeNBT
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.util.writeVector
import org.kryptonmc.krypton.world.block.entity.KryptonBlockEntity
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i

@JvmRecord
data class PacketOutBlockEntityData(
    val x: Int,
    val y: Int,
    val z: Int,
    val type: Int,
    val nbt: CompoundTag?
) : Packet {

    constructor(
        position: Vector3i,
        type: BlockEntityType,
        nbt: CompoundTag?
    ) : this(position.x(), position.y(), position.z(), Registries.BLOCK_ENTITY_TYPE.idOf(type), nbt)

    constructor(entity: KryptonBlockEntity) : this(entity, entity::updateTag)

    constructor(entity: KryptonBlockEntity, nbtCreator: () -> CompoundTag?) : this(entity.position, entity.type, nbtCreator())

    override fun write(buf: ByteBuf) {
        buf.writeVector(x, y, z)
        buf.writeVarInt(type)
        buf.writeNBT(nbt)
    }
}
