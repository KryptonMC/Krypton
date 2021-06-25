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

import com.google.common.collect.Multimap
import net.kyori.adventure.key.Key
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.jglrxavpok.hephaistos.nbt.NBTList
import org.jglrxavpok.hephaistos.nbt.NBTTypes
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.util.nbt.getString
import java.util.UUID

class AttributeMap(private val supplier: AttributeSupplier) : Iterable<Map.Entry<Attribute, AttributeInstance?>> {

    private val attributes = mutableMapOf<Attribute, AttributeInstance?>()
    val dirtyAttributes = mutableSetOf<AttributeInstance>()

    operator fun get(attribute: Attribute) = attributes.getOrPut(attribute) { supplier.create(::onModify, attribute) }

    operator fun contains(attribute: Attribute) = attributes[attribute] != null || supplier.hasAttribute(attribute)

    fun hasModifier(attribute: Attribute, modifierId: UUID) =
        attributes[attribute]?.getModifier(modifierId) != null || supplier.hasModifier(attribute, modifierId)

    fun getValue(attribute: Attribute) = attributes[attribute]?.value ?: supplier.getValue(attribute)

    fun getBase(attribute: Attribute) = attributes[attribute]?.baseValue ?: supplier.getBase(attribute)

    fun getModifierValue(attribute: Attribute, modifierId: UUID) =
        attributes[attribute]?.getModifier(modifierId)?.amount ?: supplier.getModifierValue(attribute, modifierId)

    fun removeModifiers(modifiers: Multimap<Attribute, AttributeModifier>) = modifiers.asMap().forEach { (attribute, modifiers) ->
        val instance = attributes[attribute] ?: return@forEach
        modifiers.forEach(instance::removeModifier)
    }

    fun addTransientModifiers(modifiers: Multimap<Attribute, AttributeModifier>) = modifiers.forEach { attribute, modifier ->
        val instance = get(attribute) ?: return@forEach
        instance.removeModifier(modifier)
        instance.addTransientModifier(modifier)
    }

    fun assignValues(map: AttributeMap) = map.attributes.values.asSequence().filterNotNull().forEach {
        get(it.attribute)?.replaceFrom(it)
    }

    fun save() = NBTList<NBTCompound>(NBTTypes.TAG_Compound).apply {
        attributes.values.asSequence().filterNotNull().forEach { add(it.save()) }
    }

    fun load(tag: NBTList<NBTCompound>) = tag.forEach {
        val name = it.getString("Name", "")
        val attribute = InternalRegistries.ATTRIBUTE[Key.key(name)] ?: return@forEach LOGGER.warn("Ignoring unknown attribute $name")
        get(attribute)?.load(it)
    }

    private fun onModify(instance: AttributeInstance) {
        if (!instance.attribute.isSyncable) return
        dirtyAttributes += instance
    }

    override fun iterator() = attributes.iterator()

    val syncableAttributes: Collection<AttributeInstance>
        get() = attributes.values.asSequence().filterNotNull().filter { it.attribute.isSyncable }.toList()

    companion object {

        private val LOGGER = logger<AttributeMap>()
    }
}
