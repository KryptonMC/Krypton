package me.bristermitten.minekraft.serializers

import kotlinx.serialization.*
import java.util.*

@Serializer(forClass = UUID::class)
class UUIDSerializer : KSerializer<UUID> {

    override val descriptor: SerialDescriptor = SerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
}
