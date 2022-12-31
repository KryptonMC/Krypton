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

    private fun onModified(attribute: KryptonAttribute) {
        if (attribute.type.sendToClient) dirty.add(attribute)
    }

    companion object {

        private val LOGGER = LogManager.getLogger()
    }
}
