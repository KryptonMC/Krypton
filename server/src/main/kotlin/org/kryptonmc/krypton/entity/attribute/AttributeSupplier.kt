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

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import org.kryptonmc.api.entity.attribute.AttributeType
import java.util.UUID

class AttributeSupplier(private val attributes: ImmutableMap<AttributeType, KryptonAttribute>) {

    fun value(type: AttributeType): Double = attribute(type).value

    fun baseValue(type: AttributeType): Double = attribute(type).baseValue

    fun modifierValue(type: AttributeType, uuid: UUID): Double = requireNotNull(attribute(type).modifier(uuid)?.amount) {
        "Cannot find modifier $uuid on attribute ${type.key()}!"
    }

    fun hasAttribute(type: AttributeType): Boolean = attributes.containsKey(type)

    fun hasModifier(type: AttributeType, uuid: UUID): Boolean = attributes[type]?.modifier(uuid) != null

    fun create(type: AttributeType, callback: (KryptonAttribute) -> Unit): KryptonAttribute {
        val attribute = attributes[type] ?: return KryptonAttribute(type, callback)
        val copy = KryptonAttribute(type, callback)
        copy.replaceFrom(attribute)
        return copy
    }

    private fun attribute(type: AttributeType): KryptonAttribute = requireNotNull(attributes[type]) { "Cannot find attribute ${type.key()}!" }

    class Builder {

        private val attributes = persistentMapOf<AttributeType, KryptonAttribute>().builder()
        private var frozen = false

        fun add(type: AttributeType): Builder = apply { create(type) }

        fun add(type: AttributeType, base: Double): Builder = apply { create(type).baseValue = base }

        fun build(): AttributeSupplier {
            frozen = true
            return AttributeSupplier(attributes.build())
        }

        private fun create(type: AttributeType): KryptonAttribute {
            val attribute = KryptonAttribute(type) {
                if (frozen) throw UnsupportedOperationException("Attempted to change value for default attribute ${type.key()}!")
            }
            attributes[type] = attribute
            return attribute
        }
    }

    companion object {

        @JvmStatic
        fun builder(): Builder = Builder()
    }
}
