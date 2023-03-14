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
