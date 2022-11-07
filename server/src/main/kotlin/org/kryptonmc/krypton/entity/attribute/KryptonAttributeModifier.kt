/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity.attribute

import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.BasicModifierOperation
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.nbt.CompoundTag
import java.util.Objects
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return Objects.equals(uuid, (other as KryptonAttributeModifier).uuid)
    }

    override fun hashCode(): Int = uuid.hashCode()

    override fun toString(): String = "KryptonAttributeModifier(amount=$amount, operation=$operation, name=${nameGetter.get()}, uuid=$uuid)"

    object Factory : AttributeModifier.Factory {

        override fun of(uuid: UUID, name: String, amount: Double, operation: ModifierOperation): AttributeModifier =
            KryptonAttributeModifier(uuid, name, amount, operation)
    }

    companion object {

        @JvmStatic
        protected val LOGGER = logger<KryptonAttributeModifier>()
        private val BASIC_OPERATIONS = BasicModifierOperation.values()

        @JvmStatic
        fun from(data: CompoundTag): KryptonAttributeModifier? {
            return try {
                KryptonAttributeModifier(getId(data), data.getString("Name"), data.getDouble("Amount"), getOperation(data))
            } catch (exception: IllegalArgumentException) {
                LOGGER.warn("Unable to create attribute!", exception)
                null
            }
        }

        @JvmStatic
        protected fun getId(data: CompoundTag): UUID = requireNotNull(data.getUUID("UUID")) { "Could not deserialize UUID for attribute modifier!" }

        @JvmStatic
        protected fun getOperation(data: CompoundTag): BasicModifierOperation = getOperationById(data.getInt("Operation"))

        @JvmStatic
        fun getOperationById(id: Int): BasicModifierOperation {
            require(id >= 0 && id < BASIC_OPERATIONS.size) { "No operation could be found with ID $id!" }
            return BASIC_OPERATIONS[id]
        }
    }
}
