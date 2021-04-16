package org.kryptonmc.krypton.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

/**
 * A hacky serialiser that bridges the Gson component serialiser and kotlinx.serialization, allowing us to (de)serialise
 * using this serialiser rather than treating everything as a string and parsing that
 *
 * @author Callum Seabrook
 */
object ComponentSerializer : KSerializer<Component> {

    override val descriptor = PrimitiveSerialDescriptor("net.kyori.adventure.text.Component", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeSerializableValue(JsonElement.serializer(), Json.parseToJsonElement(GsonComponentSerializer.gson().serialize(value)))
    }

    override fun deserialize(decoder: Decoder): Component {
        return GsonComponentSerializer.gson().deserialize(decoder.decodeSerializableValue(JsonObject.serializer()).toString())
    }
}