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

import com.google.common.collect.ImmutableSet
import com.google.common.collect.Multimaps
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.ObjectArraySet
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.nbt.ListBinaryTag
import org.kryptonmc.krypton.registry.InternalRegistries
import java.util.EnumMap
import java.util.UUID

class AttributeInstance(
    val attribute: Attribute,
    val onDirty: (AttributeInstance) -> Unit
) {

    private val modifiersByOperation = Multimaps.newSetMultimap<AttributeModifier.Operation, AttributeModifier>(
        EnumMap(AttributeModifier.Operation::class.java)
    ) { mutableSetOf() }
    private val modifiersById = Object2ObjectArrayMap<UUID, AttributeModifier>()
    private val permanentModifiers = ObjectArraySet<AttributeModifier>()

    var baseValue = attribute.default
        set(value) {
            if (value == field) return
            field = value
            makeDirty()
        }

    var isDirty = true
    var cachedValue = 0.0

    private fun getModifiers(operation: AttributeModifier.Operation): MutableSet<AttributeModifier> = modifiersByOperation[operation]

    fun getModifier(id: UUID) = modifiersById[id]

    operator fun contains(modifier: AttributeModifier) = modifiersById[modifier.id] != null

    private fun addModifier(modifier: AttributeModifier) {
        val old = modifiersById.putIfAbsent(modifier.id, modifier)
        require(old == null) { "Modifier is already applied on this attribute!" }
        getModifiers(modifier.operation) += modifier
        makeDirty()
    }

    fun addTransientModifier(modifier: AttributeModifier) = addModifier(modifier)

    fun addPermanentModifier(modifier: AttributeModifier) {
        addModifier(modifier)
        permanentModifiers += modifier
    }

    fun removeModifier(modifier: AttributeModifier) {
        getModifiers(modifier.operation).remove(modifier)
        modifiersById.remove(modifier.id)
        permanentModifiers.remove(modifier)
        makeDirty()
    }

    fun removeModifier(id: UUID) = getModifier(id)?.let { removeModifier(it) }

    fun removePermanentModifier(id: UUID): Boolean {
        val modifier = getModifier(id)
        if (modifier == null || modifier in permanentModifiers) return false
        removeModifier(modifier)
        return true
    }

    fun removeModifiers() = modifiers.forEach { removeModifier(it) }

    fun replaceFrom(other: AttributeInstance) {
        baseValue = other.baseValue
        modifiersById.clear()
        modifiersById.putAll(other.modifiersById)
        permanentModifiers.clear()
        permanentModifiers.addAll(other.permanentModifiers)
        modifiersByOperation.clear()
        other.modifiersByOperation.asMap().forEach { (operation, modifiers) ->
            getModifiers(operation).addAll(modifiers)
        }
        makeDirty()
    }

    fun save() = CompoundBinaryTag.builder()
        .putString("Name", InternalRegistries.ATTRIBUTE.getKey(attribute)!!.toString())
        .putDouble("Base", baseValue)
        .apply {
            if (permanentModifiers.isEmpty()) return@apply
            put("Modifiers", ListBinaryTag.of(BinaryTagTypes.COMPOUND, permanentModifiers.map(AttributeModifier::save)))
        }.build()

    fun load(tag: CompoundBinaryTag) {
        baseValue = tag.getDouble("Base")

        val modifiers = tag.getList("Modifiers", BinaryTagTypes.COMPOUND)
        if (modifiers.size() == 0) {
            makeDirty()
            return
        }
        modifiers.asSequence().filterIsInstance<CompoundBinaryTag>().forEach {
            val modifier = AttributeModifier.load(it) ?: return@forEach
            modifiersById[modifier.id] = modifier
            getModifiers(modifier.operation) += modifier
            permanentModifiers += modifier
        }
        makeDirty()
    }

    private fun calculateValue(): Double {
        var base = baseValue
        modifiersByOperation.get(AttributeModifier.Operation.ADDITION).forEach { base += it.amount }

        var temp = base
        modifiersByOperation.get(AttributeModifier.Operation.MULTIPLY_BASE).forEach { temp += base * it.amount }
        modifiersByOperation.get(AttributeModifier.Operation.MULTIPLY_TOTAL).forEach { temp *= 1.0 + it.amount }
        return attribute.sanitize(temp)
    }

    private fun makeDirty() {
        isDirty = true
        onDirty(this)
    }

    val modifiers: Set<AttributeModifier>
        get() = ImmutableSet.copyOf(modifiersById.values)

    val value: Double
        get() {
            if (isDirty) {
                cachedValue = calculateValue()
                isDirty = false
            }
            return cachedValue
        }
}
