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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.list
import java.util.UUID

@Suppress("UNCHECKED_CAST")
class AttributeMap(private val supplier: AttributeSupplier) {

    private val attributes = mutableMapOf<AttributeType, KryptonAttribute>()
    private val dirty = mutableSetOf<KryptonAttribute>()
    val syncable: Collection<KryptonAttribute>
        get() = attributes.values.filter { it.type.sendToClient }

    operator fun get(type: AttributeType): KryptonAttribute = attributes.computeIfAbsent(type) { supplier.create(type, ::onModify) }

    fun value(type: AttributeType): Double = attributes[type]?.value ?: supplier.value(type)

    fun baseValue(type: AttributeType): Double = attributes[type]?.baseValue ?: supplier.baseValue(type)

    fun modifierValue(type: AttributeType, uuid: UUID): Double = attributes[type]?.modifier(uuid)?.amount ?: supplier.modifierValue(type, uuid)

    fun hasAttribute(type: AttributeType): Boolean = attributes[type] != null || supplier.hasAttribute(type)

    fun hasModifier(type: AttributeType, uuid: UUID): Boolean = attributes[type]?.modifier(uuid) != null || supplier.hasModifier(type, uuid)

    fun load(list: ListTag) {
        for (i in 0 until list.size) {
            val tag = list.getCompound(i)
            val name = tag.getString("Name")
            val key = Key.key(name)
            val type = Registries.ATTRIBUTE[key]?.apply { get(this).load(tag) }
            if (type == null) LOGGER.warn("Ignoring unknown attribute $key.")
        }
    }

    fun save(): ListTag = list {
        attributes.values.forEach { add(it.save()) }
    }

    private fun onModify(attribute: KryptonAttribute) {
        if (attribute.type.sendToClient) dirty.add(attribute)
    }

    companion object {

        private val LOGGER = logger<AttributeMap>()
    }
}
