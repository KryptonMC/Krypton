package org.kryptonmc.krypton.auth

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@Serializable
data class GameProfile(
    @SerialName("id") @Serializable(with = MojangUUIDSerialiser::class) val uuid: UUID,
    val name: String,
    val properties: List<ProfileProperty>
)

object MojangUUIDSerialiser : KSerializer<UUID> {

    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID = UUID.fromString(
        StringBuilder(decoder.decodeString())
            .insert(20, '-')
            .insert(16, '-')
            .insert(12, '-')
            .insert(8, '-')
            .toString())

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(StringBuilder(value.toString())
            .deleteCharAt(8)
            .deleteCharAt(12)
            .deleteCharAt(16)
            .deleteCharAt(20)
            .toString())
    }
}