package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.extension.writeMetadata
import org.kryptonmc.krypton.extension.writeOptionalMetadata

open class EntityMetadata(
    private val movementFlags: MovementFlags? = null,
    private val airTicks: Int? = null,
    private val customName: Optional<Component>? = null,
    private val isCustomNameVisible: Boolean? = null,
    private val isSilent: Boolean? = null,
    private val hasNoGravity: Boolean? = null,
    private val pose: Pose? = null
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

enum class MetadataType {

    BYTE,
    VARINT,
    FLOAT,
    STRING,
    CHAT,
    OPTIONAL_CHAT,
    SLOT,
    BOOLEAN,
    ROTATION,
    POSITION,
    OPTIONAL_POSITION,
    DIRECTION,
    OPTIONAL_UUID,
    OPTIONAL_BLOCK_ID,
    NBT,
    PARTICLE,
    VILLAGER_DATA,
    OPTIONAL_VARINT,
    POSE
}

data class Optional<T>(val value: T?)