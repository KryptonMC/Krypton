package me.bristermitten.minekraft.entity.metadata

import io.netty.buffer.ByteBuf
import kotlinx.serialization.json.Json
import me.bardy.komponent.Component
import me.bristermitten.minekraft.extension.writeMetadata
import me.bristermitten.minekraft.extension.writeOptionalMetadata

open class EntityMetadata(
    open val movementFlags: MovementFlags = MovementFlags(),
    open val airTicks: Int = 300,
    open val customName: Component? = null,
    open val isCustomNameVisible: Boolean = false,
    open val isSilent: Boolean = false,
    open val hasNoGravity: Boolean = false,
    open val pose: Pose = Pose.STANDING
) {

    open fun write(buf: ByteBuf) {
        buf.writeMetadata(0u, movementFlags.toProtocol())
        buf.writeMetadata(1u, airTicks)
        buf.writeOptionalMetadata(2u, customName)
        buf.writeMetadata(3u, isCustomNameVisible)
        buf.writeMetadata(4u, isSilent)
        buf.writeMetadata(5u, hasNoGravity)
        buf.writeMetadata(6u, pose)
    }

    companion object {

        private val JSON = Json {}
    }
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