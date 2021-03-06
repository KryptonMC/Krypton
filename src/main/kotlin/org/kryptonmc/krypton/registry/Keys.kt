package org.kryptonmc.krypton.registry

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
    val components = split(":")
    if (components.size != 2) throw IllegalArgumentException("Not a namespaced key!")

    val (namespace, key) = components
    if (NAMESPACE_REGEX matches namespace && NAMESPACE_REGEX matches key) {
        return NamespacedKey(namespace, key)
    }
    throw IllegalArgumentException("Invalid characters found in string \"$this\"! Must match $NAMESPACE_REGEX!")
}

object NamespacedKeySerialiser : KSerializer<NamespacedKey> {

    override val descriptor = PrimitiveSerialDescriptor("NamespacedKey", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: NamespacedKey) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): NamespacedKey = decoder.decodeString().toNamespacedKey()
}

val NAMESPACE_REGEX = "[a-z0-9/._-]+".toRegex()