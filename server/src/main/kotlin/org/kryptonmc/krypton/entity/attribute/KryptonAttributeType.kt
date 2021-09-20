package org.kryptonmc.krypton.entity.attribute

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.TranslatableComponent
import org.kryptonmc.api.entity.attribute.AttributeType

@JvmRecord
data class KryptonAttributeType(
    private val key: Key,
    override val defaultBase: Double,
    override val minimum: Double,
    override val maximum: Double,
    override val sendToClient: Boolean,
    override val translation: TranslatableComponent
) : AttributeType {

    override fun key(): Key = key

    object Factory : AttributeType.Factory {

        override fun of(
            key: Key,
            defaultBase: Double,
            minimum: Double,
            maximum: Double,
            sendToClient: Boolean,
            translation: TranslatableComponent
        ): AttributeType = KryptonAttributeType(key, defaultBase, minimum, maximum, sendToClient, translation)
    }
}
