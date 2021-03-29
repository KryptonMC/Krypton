package org.kryptonmc.krypton.serializers

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.nbt.IntArrayBinaryTag
import java.util.*

object UUIDSerializer : KSerializer<UUID> {

    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
}

// this isn't related to the above, this turns a UUID into 4 integers sorted by significance
// descending (most significant first, least significant last)
fun UUID.serialize() = IntArrayBinaryTag.of(
    (mostSignificantBits shr 32).toInt(),
    (mostSignificantBits and Int.MAX_VALUE.toLong()).toInt(),
    (leastSignificantBits shr 32).toInt(),
    (leastSignificantBits and Int.MAX_VALUE.toLong()).toInt()
)