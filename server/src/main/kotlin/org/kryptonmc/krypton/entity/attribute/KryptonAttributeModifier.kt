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
package org.kryptonmc.krypton.entity.attribute

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.BasicModifierOperation
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.nbt.CompoundTag
import java.util.UUID
import java.util.function.Supplier

open class KryptonAttributeModifier(
    final override val uuid: UUID,
    private val nameGetter: Supplier<String>,
    final override val amount: Double,
    override val operation: ModifierOperation
) : AttributeModifier {

    final override val name: String
        get() = nameGetter.get()

    constructor(uuid: UUID, name: String, amount: Double, operation: ModifierOperation) : this(uuid, { name }, amount, operation)

    final override fun equals(other: Any?): Boolean = this === other || other is KryptonAttributeModifier && uuid == other.uuid

    final override fun hashCode(): Int = uuid.hashCode()

    override fun toString(): String = "AttributeModifier(uuid=$uuid, name=$name, amount=$amount, operation=$operation)"

    object Factory : AttributeModifier.Factory {

        override fun of(uuid: UUID, name: String, amount: Double, operation: ModifierOperation): AttributeModifier =
            KryptonAttributeModifier(uuid, name, amount, operation)
    }

    @Suppress("JVM_STATIC_ON_CONST_OR_JVM_FIELD")
    companion object {

        @JvmStatic
        protected const val UUID_TAG = "UUID"
        @JvmStatic
        protected const val NAME_TAG = "Name"
        @JvmStatic
        protected const val AMOUNT_TAG = "Amount"
        @JvmStatic
        protected const val OPERATION_TAG = "Operation"

        @JvmStatic
        protected val LOGGER: Logger = LogManager.getLogger()
        private val BASIC_OPERATIONS = BasicModifierOperation.values()

        @JvmStatic
        fun load(data: CompoundTag): KryptonAttributeModifier? {
            return try {
                KryptonAttributeModifier(data.getUUID(UUID_TAG), data.getString(NAME_TAG), data.getDouble(AMOUNT_TAG), getOperation(data))
            } catch (exception: IllegalArgumentException) {
                LOGGER.warn("Unable to create attribute!", exception)
                null
            }
        }

        @JvmStatic
        protected fun getOperation(data: CompoundTag): BasicModifierOperation = getOperationById(data.getInt(OPERATION_TAG))

        @JvmStatic
        fun getOperationById(id: Int): BasicModifierOperation {
            require(id >= 0 && id < BASIC_OPERATIONS.size) { "No operation could be found with ID $id!" }
            return BASIC_OPERATIONS[id]
        }
    }
}
