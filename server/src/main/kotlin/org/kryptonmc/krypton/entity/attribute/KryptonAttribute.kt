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

import org.kryptonmc.api.entity.attribute.Attribute
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.compound

class KryptonAttribute(
    override val type: AttributeType,
    override val modifiers: MutableMap<ModifierOperation, MutableList<AttributeModifier>> = mutableMapOf()
) : Attribute {

    override val name = type.key.asString()
    override var baseValue = type.defaultBase
        set(value) {
            field = value
            recalculate()
        }
    override var value = 0.0
        private set

    fun load(tag: CompoundTag) {
        baseValue = tag.getDouble("Base")
        if (tag.contains("Modifiers", ListTag.ID)) tag.getList("Modifiers", CompoundTag.ID).forEachCompound {
            val operation = Registries.MODIFIER_OPERATIONS[it.getInt("Operation")] ?: return@forEachCompound
            modifiers.getOrPut(operation) { mutableListOf() } += AttributeModifier(
                it.getString("Name"),
                it.getUUID("UUID") ?: return@forEachCompound,
                it.getDouble("Amount")
            )
        }
    }

    fun save() = compound {
        string("Name", name)
        double("Base", baseValue)
        put("Modifiers", modifiers.save())
    }

    override fun getModifiers(operation: ModifierOperation) = modifiers.getOrPut(operation) { mutableListOf() }.apply { recalculate() }

    override fun addModifier(operation: ModifierOperation, modifier: AttributeModifier) {
        modifiers.getOrPut(operation) { mutableListOf() } += modifier
        recalculate()
    }

    override fun removeModifier(operation: ModifierOperation, modifier: AttributeModifier) {
        modifiers.getOrPut(operation) { mutableListOf() } -= modifier
        recalculate()
    }

    override fun removeModifiers(operation: ModifierOperation) {
        modifiers -= operation
        recalculate()
    }

    override fun recalculate() {
        var total = baseValue
        modifiers.forEach { (operation, modifiers) -> total = operation.apply(total, modifiers) }
        value = total
    }
}
