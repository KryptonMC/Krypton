/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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

import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nextUUID
import org.kryptonmc.krypton.util.nbt.setUUID
import java.util.UUID
import kotlin.random.Random

class AttributeModifier(
    val id: UUID,
    private val nameGetter: () -> String,
    val amount: Double,
    val operation: Operation
) {

    constructor(name: String, amount: Double, operation: Operation) : this(Random.nextUUID(), { name }, amount, operation)

    constructor(id: UUID, name: String, amount: Double, operation: Operation) : this(id, { name }, amount, operation)

    fun save() = NBTCompound()
        .setString("Name", name)
        .setDouble("Amount", amount)
        .setInt("Operation", operation.ordinal)
        .setUUID("UUID", id)

    val name: String
        get() = nameGetter()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AttributeModifier
        return id == other.id
    }

    override fun hashCode() = id.hashCode()

    override fun toString() = "AttributeModifier(amount=$amount, operation=$operation, name='${nameGetter()}', id=$id)"

    enum class Operation {

        ADDITION,
        MULTIPLY_BASE,
        MULTIPLY_TOTAL;

        companion object {

            private val OPERATIONS = arrayOf(ADDITION, MULTIPLY_BASE, MULTIPLY_TOTAL)

            fun fromId(id: Int): Operation {
                require(id in 0..OPERATIONS.size) { "No operation with id $id!" }
                return OPERATIONS[id]
            }
        }
    }

    companion object {

        private val LOGGER = logger<AttributeModifier>()

        fun load(tag: NBTCompound): AttributeModifier? = try {
            val id = tag.getUUID("UUID")
            val operation = Operation.fromId(tag.getInt("Operation"))
            AttributeModifier(id, tag.getString("Name"), tag.getDouble("Amount"), operation)
        } catch (exception: Exception) {
            LOGGER.warn("Unable to create attribute: ${exception.message}")
            null
        }
    }
}
