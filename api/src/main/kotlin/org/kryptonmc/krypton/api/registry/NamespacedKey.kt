@file:JvmName("KeyUtils")
package org.kryptonmc.krypton.api.registry

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A namespaced key is a key that contains both a namespace and a value.
 *
 * These are generally represented in the format "namespace:value".
 *
 * @author Callum Seabrook
 */
@Serializable(with = NamespacedKeySerializer::class)
data class NamespacedKey @JvmOverloads constructor(
    val namespace: String = "minecraft",
    val value: String
) {

    override fun toString() = "$namespace:$value"
}

/**
 * Convert a [String] to a [NamespacedKey]
 *
 * Required conditions:
 * - The provided string must be in the format namespace:key else this will throw an error!
 * - It must also be splittable by : only once (only contain one namespace and one key)
 * - Both the namespace and the key must only contain a-z, 0-9, /, ., _ or - characters (regex: [NAMESPACE_REGEX])
 *
 * If all of the above required checks pass, this
 *
 * @author Callum Seabrook
 */
@JvmName("fromString")
fun String.toNamespacedKey(): NamespacedKey {
    val components = split(":")
    require(components.size == 2) {
        "Not a namespaced key! Must only contain one namespace and one key in the format namespace:key, was $this"
    }

    val (namespace, key) = components
    require(NAMESPACE_REGEX matches namespace) {
        "Invalid characters found in string \"$this\"! Must match $NAMESPACE_REGEX!"
    }
    return NamespacedKey(namespace, key)
}

/**
 * Custom serializer for namespaced keys, to convert them to and from string format
 * (converts namespace:key to NamespacedKey and vice versa)
 *
 * @author Callum Seabrook
 */
object NamespacedKeySerializer : KSerializer<NamespacedKey> {

    override val descriptor = PrimitiveSerialDescriptor("NamespacedKey", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: NamespacedKey) = encoder.encodeString(value.toString())

    override fun deserialize(decoder: Decoder): NamespacedKey = decoder.decodeString().toNamespacedKey()
}

/**
 * The regex for namespaced keys. Both the namespace **and** the key must match this.
 */
@JvmField
val NAMESPACE_REGEX = "[a-z0-9/._-]+".toRegex()