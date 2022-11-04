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

import com.google.common.collect.ImmutableMap
import java.util.UUID
import java.util.function.Consumer

class AttributeSupplier(attributes: Map<KryptonAttributeType, KryptonAttribute>) {

    private val attributes = ImmutableMap.copyOf(attributes)

    fun hasAttribute(type: KryptonAttributeType): Boolean = attributes.containsKey(type)

    fun hasModifier(type: KryptonAttributeType, modifierId: UUID): Boolean = attributes.get(type)?.getModifier(modifierId) != null

    fun getValue(type: KryptonAttributeType): Double = getAttribute(type).calculateValue()

    fun getBaseValue(type: KryptonAttributeType): Double = getAttribute(type).baseValue

    fun getModifierValue(type: KryptonAttributeType, modifierId: UUID): Double {
        val modifier = requireNotNull(getAttribute(type).getModifier(modifierId)) { "Cannot find modifier $modifierId on attribute ${type.key()}!" }
        return modifier.amount
    }

    fun create(type: KryptonAttributeType, callback: Consumer<KryptonAttribute>): KryptonAttribute? {
        val attribute = attributes.get(type) ?: return null
        val copy = KryptonAttribute(type, callback)
        copy.replaceFrom(attribute)
        return copy
    }

    private fun getAttribute(type: KryptonAttributeType): KryptonAttribute =
        requireNotNull(attributes.get(type)) { "Cannot find attribute ${type.key()}!" }

    class Builder {

        private val attributes = HashMap<KryptonAttributeType, KryptonAttribute>()
        private var frozen = false

        fun add(type: KryptonAttributeType): Builder = apply { create(type) }

        fun add(type: KryptonAttributeType, base: Double): Builder = apply { create(type).baseValue = base }

        fun build(): AttributeSupplier {
            frozen = true
            return AttributeSupplier(attributes)
        }

        private fun create(type: KryptonAttributeType): KryptonAttribute {
            val attribute = KryptonAttribute(type) {
                if (frozen) throw UnsupportedOperationException("Attempted to change value for default attribute ${type.key()}!")
            }
            attributes.put(type, attribute)
            return attribute
        }
    }

    companion object {

        @JvmStatic
        fun builder(): Builder = Builder()
    }
}
