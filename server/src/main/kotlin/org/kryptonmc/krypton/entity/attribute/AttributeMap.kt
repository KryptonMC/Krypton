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

import com.google.common.collect.Multimap
import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.map.nullableComputeIfAbsent
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.list
import java.util.UUID

class AttributeMap(private val supplier: AttributeSupplier) {

    private val attributes = HashMap<KryptonAttributeType, KryptonAttribute>()
    private val dirty = HashSet<KryptonAttribute>()

    fun syncable(): Collection<KryptonAttribute> = attributes.values.filter { it.type.sendToClient }

    fun hasAttribute(type: KryptonAttributeType): Boolean = attributes.get(type) != null || supplier.hasAttribute(type)

    fun hasModifier(type: KryptonAttributeType, modifierId: UUID): Boolean {
        val attribute = attributes.get(type) ?: return supplier.hasModifier(type, modifierId)
        return attribute.getModifier(modifierId) != null
    }

    // Would use a method reference for onModified, but for whatever reason, using a method reference generates a kotlin.Function
    // implementation and then creates a Consumer that delegates to that.
    fun getAttribute(type: KryptonAttributeType): KryptonAttribute? =
        attributes.nullableComputeIfAbsent(type) { supplier.create(type) { onModified(it) } }

    fun getValue(type: KryptonAttributeType): Double = attributes.get(type)?.calculateValue() ?: supplier.getValue(type)

    fun getBaseValue(type: KryptonAttributeType): Double = attributes.get(type)?.baseValue ?: supplier.getBaseValue(type)

    fun getModifierValue(type: KryptonAttributeType, modifierId: UUID): Double {
        val attribute = attributes.get(type) ?: return supplier.getModifierValue(type, modifierId)
        return requireNotNull(attribute.getModifier(modifierId)) { "Modifier $modifierId could not be found for attribute ${type.key()}!" }.amount
    }

    fun removeModifiers(modifiers: Multimap<KryptonAttributeType, AttributeModifier>) {
        modifiers.asMap().forEach { (type, modifiers) ->
            val attribute = attributes.get(type)
            if (attribute != null) modifiers.forEach(attribute::removeModifier)
        }
    }

    fun addModifiers(modifiers: Multimap<KryptonAttributeType, AttributeModifier>) {
        modifiers.forEach { type, modifier ->
            val attribute = getAttribute(type)
            if (attribute != null) {
                attribute.removeModifier(modifier)
                attribute.addModifier(modifier)
            }
        }
    }

    fun replaceFrom(other: AttributeMap) {
        other.attributes.values.forEach { getAttribute(it.type)?.replaceFrom(it) }
    }

    fun load(list: ListTag) {
        list.forEachCompound {
            val key = Key.key(it.getString("Name"))
            val type = KryptonRegistries.ATTRIBUTE.get(key)
            if (type == null) {
                LOGGER.warn("Ignoring unknown attribute $key.")
                return@forEachCompound
            }
            getAttribute(type.downcast())?.load(it)
        }
    }

    fun save(): ListTag = list { attributes.values.forEach { add(it.save()) } }

    fun isDirty(): Boolean = dirty.isNotEmpty()

    fun dirty(): List<KryptonAttribute> {
        val copy = ArrayList(dirty)
        dirty.clear()
        return copy
    }

    private fun onModified(attribute: KryptonAttribute) {
        if (attribute.type.sendToClient) dirty.add(attribute)
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
