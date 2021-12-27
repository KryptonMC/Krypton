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

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.ObjectArraySet
import org.kryptonmc.api.entity.attribute.Attribute
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.compound
import java.util.Collections
import java.util.UUID

data class KryptonAttribute(
    override val type: AttributeType,
    private val callback: (KryptonAttribute) -> Unit
) : Attribute {

    val modifiersByOperation = mutableMapOf<ModifierOperation, MutableSet<AttributeModifier>>()
    private val modifiersById = Object2ObjectArrayMap<UUID, AttributeModifier>()
    private val permanentModifiers = ObjectArraySet<AttributeModifier>()
    override val modifiers: Collection<AttributeModifier> = Collections.unmodifiableCollection(modifiersById.values)

    private var dirty = true
    private var cachedValue = 0.0
    override var baseValue = type.defaultBase
        set(value) {
            if (field == value) return
            field = value
            makeDirty()
        }
    override val value: Double
        get() {
            if (dirty) {
                cachedValue = recalculate()
                dirty = false
            }
            return cachedValue
        }

    fun load(tag: CompoundTag) {
        baseValue = tag.getDouble("Base")
        if (tag.contains("Modifiers", ListTag.ID)) tag.getList("Modifiers", CompoundTag.ID).forEachCompound {
            val operation = Registries.MODIFIER_OPERATIONS[it.getInt("Operation")] ?: return@forEachCompound
            val modifier = KryptonAttributeModifier(
                it.getString("Name"),
                it.getUUID("UUID") ?: return@forEachCompound,
                it.getDouble("Amount")
            )
            modifiersById[modifier.uuid] = modifier
            modifiers(operation).add(modifier)
            permanentModifiers.add(modifier)
        }
        makeDirty()
    }

    fun save(): CompoundTag = compound {
        string("Name", type.key().asString())
        double("Base", baseValue)
        put("Modifiers", modifiersByOperation.save())
    }

    fun hasModifier(modifier: AttributeModifier): Boolean = modifiersById[modifier.uuid] != null

    fun addPermanentModifier(operation: ModifierOperation, modifier: AttributeModifier) {
        addModifier(operation, modifier)
        permanentModifiers.add(modifier)
    }

    fun replaceFrom(other: KryptonAttribute) {
        baseValue = other.baseValue
        modifiersById.clear()
        modifiersById.putAll(other.modifiersById)
        permanentModifiers.clear()
        permanentModifiers.addAll(other.permanentModifiers)
        modifiersByOperation.clear()
        other.modifiersByOperation.forEach { modifiers(it.key).addAll(it.value) }
        makeDirty()
    }

    override fun modifier(uuid: UUID): AttributeModifier? = modifiersById[uuid]

    override fun modifiers(operation: ModifierOperation) = modifiersByOperation.getOrPut(operation) { mutableSetOf() }

    override fun addModifier(operation: ModifierOperation, modifier: AttributeModifier) {
        require(modifiersById.putIfAbsent(modifier.uuid, modifier) == null) { "The modifier is already applied to this attribute!" }
        modifiers(operation).add(modifier)
        makeDirty()
    }

    override fun removeModifier(operation: ModifierOperation, modifier: AttributeModifier) {
        modifiers(operation).remove(modifier)
        modifiersById[modifier.uuid] = modifier
        makeDirty()
    }

    override fun clearModifiers() {
        modifiersByOperation.clear()
        modifiersById.clear()
        permanentModifiers.clear()
        makeDirty()
    }

    private fun recalculate(): Double {
        var total = baseValue
        modifiersByOperation.forEach { (operation, modifiers) -> total = operation.apply(total, modifiers) }
        return total
    }

    private fun makeDirty() {
        dirty = true
        callback(this)
    }
}
