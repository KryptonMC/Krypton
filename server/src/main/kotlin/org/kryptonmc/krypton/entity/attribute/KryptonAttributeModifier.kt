package org.kryptonmc.krypton.entity.attribute

import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.nbt.compound
import java.util.UUID

@JvmRecord
data class KryptonAttributeModifier(
    override val name: String,
    override val uuid: UUID,
    override val amount: Double
) : AttributeModifier {

    object Factory : AttributeModifier.Factory {

        override fun of(name: String, uuid: UUID, amount: Double): AttributeModifier = KryptonAttributeModifier(name, uuid, amount)
    }
}
