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

import com.google.common.collect.ImmutableMap
import org.kryptonmc.krypton.util.notSupported
import java.util.UUID

class AttributeSupplier(instances: Map<Attribute, AttributeInstance>) {

    private val instances = ImmutableMap.copyOf(instances)

    fun getValue(attribute: Attribute) = getAttributeInstance(attribute).value

    fun getBase(attribute: Attribute) = getAttributeInstance(attribute).baseValue

    fun getModifierValue(attribute: Attribute, modifierId: UUID): Double {
        val modifier = getAttributeInstance(attribute).getModifier(modifierId)
        requireNotNull(modifier) { "Can't find modifier $modifierId on attribute $attribute" }
        return modifier.amount
    }

    fun create(onDirty: (AttributeInstance) -> Unit, attribute: Attribute): AttributeInstance? {
        val old = instances[attribute] ?: return null
        val instance = AttributeInstance(attribute, onDirty)
        instance.replaceFrom(old)
        return instance
    }

    fun hasAttribute(attribute: Attribute) = instances.containsKey(attribute)

    fun hasModifier(attribute: Attribute, modifierId: UUID) = instances[attribute]?.getModifier(modifierId) != null

    private fun getAttributeInstance(attribute: Attribute): AttributeInstance {
        val instance = instances[attribute]
        requireNotNull(instance) { "Can't find attribute $attribute!" }
        return instance
    }

    class Builder {

        private val builder = mutableMapOf<Attribute, AttributeInstance>()
        private var instanceFrozen = false

        fun add(attribute: Attribute) = apply { create(attribute) }

        fun add(attribute: Attribute, baseValue: Double) = apply {
            create(attribute).baseValue = baseValue
        }

        fun build(): AttributeSupplier {
            instanceFrozen = true
            return AttributeSupplier(builder)
        }

        private fun create(attribute: Attribute): AttributeInstance {
            val instance = AttributeInstance(attribute) {
                if (instanceFrozen) notSupported("Tried to change value for default attribute instance: $attribute!")
            }
            builder[attribute] = instance
            return instance
        }
    }

    companion object {

        fun builder() = Builder()
    }
}
