package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import me.bardy.komponent.Component
import org.kryptonmc.krypton.extension.writeMetadata
import org.kryptonmc.krypton.extension.writeOptionalMetadata

open class EntityMetadata(
    open val movementFlags: MovementFlags? = null,
    open val airTicks: Int? = null,
    open val customName: Optional<Component>? = null,
    open val isCustomNameVisible: Boolean? = null,
    open val isSilent: Boolean? = null,
    open val hasNoGravity: Boolean? = null,
    open val pose: Pose? = null
) {

    open fun write(buf: ByteBuf) {
        if (movementFlags != null) buf.writeMetadata(0u, requireNotNull(movementFlags).toProtocol())
        if (airTicks != null) buf.writeMetadata(1u, requireNotNull(airTicks))
        if (customName != null) buf.writeOptionalMetadata(2u, requireNotNull(customName))
        if (isCustomNameVisible != null) buf.writeMetadata(3u, requireNotNull(isCustomNameVisible))
        if (isSilent != null) buf.writeMetadata(4u, requireNotNull(isSilent))
        if (hasNoGravity != null) buf.writeMetadata(5u, requireNotNull(hasNoGravity))
        if (pose != null) buf.writeMetadata(6u, requireNotNull(pose))
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