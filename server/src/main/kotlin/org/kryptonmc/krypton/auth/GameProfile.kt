package org.kryptonmc.krypton.auth

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

/**
 * Represents a Mojang game profile received from the session server on successful
 * authentication of a specific user
 *
 * @param uuid the universally unique identifier for this user
 * @param name the user's username
 * @param properties optional list of properties related to this profile (e.g. skins)
 */
@Serializable
data class GameProfile(
    @SerialName("id") @Serializable(with = MojangUUIDSerializer::class) val uuid: UUID,
    val name: String,
    val properties: List<ProfileProperty>
)

/**
 * Used to (de)serialize the UUID format that Yggdrasil uses (has no dashes in it)
 */
object MojangUUIDSerializer : KSerializer<UUID> {

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
