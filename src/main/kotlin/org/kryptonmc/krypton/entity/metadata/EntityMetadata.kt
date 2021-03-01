package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import me.bardy.komponent.Component
import org.kryptonmc.krypton.extension.writeMetadata
import org.kryptonmc.krypton.extension.writeOptionalMetadata

open class EntityMetadata(
    val movementFlags: MovementFlags? = null,
    val airTicks: Int? = null,
    val customName: Optional<Component>? = null,
    val isCustomNameVisible: Boolean? = null,
    val isSilent: Boolean? = null,
    val hasNoGravity: Boolean? = null,
    val pose: Pose? = null
) {

    open fun write(buf: ByteBuf) {
        buf.writeMetadata(0u, movementFlags?.toProtocol())
        buf.writeMetadata(1u, airTicks)
        buf.writeOptionalMetadata(2u, customName)
        buf.writeMetadata(3u, isCustomNameVisible)
        buf.writeMetadata(4u, isSilent)
        buf.writeMetadata(5u, hasNoGravity)
        buf.writeMetadata(6u, pose)
    }

    companion object Default : EntityMetadata(
        MovementFlags(),
        300,
        Optional(null),
        false,
        false,
        false,
        Pose.STANDING
    )
}

data class MovementFlags(
    val isOnFire: Boolean = false,
    val isCrouching: Boolean = false,
    val isSprinting: Boolean = false,
    val isSwimming: Boolean = false,
    val isInvisible: Boolean = false,
    val hasGlowingEffect: Boolean = false,
    val isFlying: Boolean = false
) {

    fun toProtocol(): Byte {
        var byte = 0x0
        if (isOnFire) byte += 0x01
        if (isCrouching) byte += 0x02
        if (isSprinting) byte += 0x08
        if (isSwimming) byte += 0x10
        if (isInvisible) byte += 0x20
        if (hasGlowingEffect) byte += 0x40
        if (isFlying) byte += 0x80
        return byte.toByte()
    }
}

enum class MetadataType(val id: Int) {

    BYTE(0),
    VARINT(1),
    FLOAT(2),
    STRING(3),
    CHAT(4),
    OPTIONAL_CHAT(5),
    SLOT(6),
    BOOLEAN(7),
    ROTATION(8),
    POSITION(9),
    OPTIONAL_POSITION(10),
    DIRECTION(11),
    OPTIONAL_UUID(12),
    OPTIONAL_BLOCK_ID(13),
    NBT(14),
    PARTICLE(15),
    VILLAGER_DATA(16),
    OPTIONAL_VARINT(17),
    POSE(18)
}

data class Optional<T>(val value: T?)