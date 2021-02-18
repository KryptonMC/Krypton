package me.bristermitten.minekraft.registry

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = NamespacedKeySerialiser::class)
data class NamespacedKey(
    val namespace: String = "minecraft",
    val value: String
) {

    override fun toString() = "$namespace:$value"
}

fun String.toNamespacedKey(): NamespacedKey {
    val (namespace, key) = split(":")
    return NamespacedKey(namespace, key)
}

object NamespacedKeySerialiser : KSerializer<NamespacedKey> {

    override val descriptor = PrimitiveSerialDescriptor("NamespacedKey", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: NamespacedKey) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): NamespacedKey = decoder.decodeString().toNamespacedKey()
}