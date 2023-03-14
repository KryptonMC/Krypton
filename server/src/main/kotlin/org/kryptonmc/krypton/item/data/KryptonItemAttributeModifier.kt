/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.item.data

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.BasicModifierOperation
import org.kryptonmc.api.item.ItemAttributeModifier
import org.kryptonmc.krypton.entity.util.EquipmentSlots
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeModifier
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound
import java.util.UUID
import java.util.function.Supplier

class KryptonItemAttributeModifier(
    override val type: AttributeType,
    override val slot: EquipmentSlot,
    uuid: UUID,
    nameGetter: Supplier<String>,
    amount: Double,
    override val operation: BasicModifierOperation
) : KryptonAttributeModifier(uuid, nameGetter, amount, operation), ItemAttributeModifier {

    constructor(type: AttributeType, slot: EquipmentSlot, uuid: UUID, name: String, amount: Double,
                operation: BasicModifierOperation) : this(type, slot, uuid, { name }, amount, operation)

    override fun toString(): String = "ItemAttributeModifier(type=$type, slot=$slot, uuid=$uuid, name=$name, amount=$amount, operation=$operation)"

    object Factory : ItemAttributeModifier.Factory {

        override fun of(type: AttributeType, slot: EquipmentSlot, uuid: UUID, name: String, amount: Double,
                        operation: BasicModifierOperation): ItemAttributeModifier {
            return KryptonItemAttributeModifier(type, slot, uuid, name, amount, operation)
        }
    }

    companion object {

        private const val ATTRIBUTE_NAME_TAG = "AttributeName"
        private const val SLOT_TAG = "Slot"

        @JvmStatic
        fun load(data: CompoundTag): KryptonItemAttributeModifier? {
            val type = KryptonRegistries.ATTRIBUTE.get(Key.key(data.getString(ATTRIBUTE_NAME_TAG))) ?: return null
            val slot = EquipmentSlots.fromName(data.getString(SLOT_TAG)) ?: return null
            return try {
                KryptonItemAttributeModifier(type, slot, data.getUUID(UUID_TAG), data.getString(NAME_TAG), data.getDouble(AMOUNT_TAG),
                    getOperation(data))
            } catch (exception: Exception) {
                LOGGER.warn("Unable to create attribute!", exception)
                null
            }
        }

        @JvmStatic
        fun save(modifier: ItemAttributeModifier): CompoundTag = compound {
            putString(ATTRIBUTE_NAME_TAG, modifier.type.key().asString())
            putString(SLOT_TAG, modifier.slot.name)
            putUUID(UUID_TAG, modifier.uuid)
            putString(NAME_TAG, modifier.name)
            putDouble(AMOUNT_TAG, modifier.amount)
            putInt(OPERATION_TAG, modifier.operation.ordinal)
        }
    }
}
