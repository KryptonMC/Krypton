package org.kryptonmc.krypton.entity.metadata

import io.netty.buffer.ByteBuf
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.extension.writeMetadata
import org.kryptonmc.krypton.extension.writeOptionalMetadata

/**
 * Represents metadata common to all entities.
 *
 * The ordering of this comes from [wiki.vg](https://wiki.vg/Entity_metadata#Entity_Metadata_Format)
 *
 * @author Callum Seabrook
 */
open class EntityMetadata(
    val movementFlags: MovementFlags? = null,
    val airTicks: Int? = null,
    val customName: Optional<Component>? = null,
    val isCustomNameVisible: Boolean? = null,
    val isSilent: Boolean? = null,
    val hasNoGravity: Boolean? = null,
    val pose: Pose? = null
) {

    /**
     * Write this metadata to a [ByteBuf] (for serialising to network)
     *
     * @param buf the buffer to write to
     */
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

/**
 * Flags for entity movement info. These are self-explanatory.
 *
 * @author Callum Seabrook
 */
data class MovementFlags(
    val isOnFire: Boolean = false,
    val isCrouching: Boolean = false,
    val isSprinting: Boolean = false,
    val isSwimming: Boolean = false,
    val isInvisible: Boolean = false,
    val hasGlowingEffect: Boolean = false,
    val isFlying: Boolean = false
) {

    /**
     * Convert this [MovementFlags] object to flags compatible with the protocol
     * (a single byte that contains all of the values)
     */
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

/**
 * The type of metadata being sent. This has to prefix the metadata value, no matter the index or if
 * it could be inferred by the client.
 *
 * @author Callum Seabrook
 */
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

/**
 * For optional metadata values. This allows us to use a tristate for metadata, a.k.a to separate
 * optional metadata from not present metadata (represented by null)
 *
 * @author Callum Seabrook
 */
data class Optional<T>(val value: T?)