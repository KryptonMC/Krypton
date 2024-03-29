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
package org.kryptonmc.api.item

import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.kryptonmc.api.Krypton
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.BasicModifierOperation
import org.kryptonmc.internal.annotations.ImmutableType
import org.kryptonmc.internal.annotations.TypeFactory
import java.util.UUID

/**
 * An attribute that can be applied to an item that modifies a specific
 * attribute on an entity that has the item this attribute is applied to
 * equipped.
 */
@Suppress("INAPPLICABLE_JVM_NAME")
@ImmutableType
public interface ItemAttributeModifier : AttributeModifier {

    /**
     * The type of the attribute.
     */
    @get:JvmName("type")
    public val type: AttributeType

    /**
     * The slot that the item has to be in for the attribute to apply.
     */
    @get:JvmName("slot")
    public val slot: EquipmentSlot

    override val operation: BasicModifierOperation

    @ApiStatus.Internal
    @TypeFactory
    public interface Factory {

        public fun of(type: AttributeType, slot: EquipmentSlot, uuid: UUID, name: String, amount: Double,
                      operation: BasicModifierOperation): ItemAttributeModifier
    }

    public companion object {

        /**
         * Creates a new item attribute modifier with the given [type], [slot],
         * [uuid], [name], [amount], [operation].
         *
         * @param type the type of the attribute
         * @param slot the equipment slot
         * @param uuid the UUID of the modifier
         * @param name the name of the modifier
         * @param amount the modifier amount
         * @param operation the modifier operation
         * @return a new item attribute
         */
        @JvmStatic
        @Contract("_, _, _ -> new", pure = true)
        public fun of(type: AttributeType, slot: EquipmentSlot, uuid: UUID, name: String, amount: Double,
                      operation: BasicModifierOperation): ItemAttributeModifier {
            return Krypton.factory<Factory>().of(type, slot, uuid, name, amount, operation)
        }

        /**
         * Creates a new item attribute modifier with the given [type], [slot],
         * and [modifier].
         *
         * @param type the type of the attribute
         * @param slot the equipment slot
         * @param modifier the modifier to take properties from
         * @return the resulting item attribute modifier
         * @throws IllegalArgumentException if the operation of the modifier is
         * not a [BasicModifierOperation].
         */
        @JvmStatic
        public fun of(type: AttributeType, slot: EquipmentSlot, modifier: AttributeModifier): ItemAttributeModifier {
            require(modifier.operation is BasicModifierOperation) { "Modifier operation must be one of the basic operations!" }
            return of(type, slot, modifier.uuid, modifier.name, modifier.amount, modifier.operation as BasicModifierOperation)
        }
    }
}
