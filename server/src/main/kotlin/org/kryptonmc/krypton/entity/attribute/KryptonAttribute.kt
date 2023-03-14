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

import com.google.common.collect.Multimaps
import com.google.common.collect.SetMultimap
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import it.unimi.dsi.fastutil.objects.ObjectArraySet
import it.unimi.dsi.fastutil.objects.ObjectCollections
import org.kryptonmc.api.entity.attribute.Attribute
import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.BasicModifierOperation
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.compound
import org.kryptonmc.nbt.list
import java.util.UUID
import java.util.function.Consumer

class KryptonAttribute(override val type: KryptonAttributeType, private val onDirty: Consumer<KryptonAttribute>) : Attribute {

    private val modifiersByOperation: SetMultimap<ModifierOperation, AttributeModifier> = Multimaps.newSetMultimap(HashMap()) { HashSet() }
    private val modifiersById = Object2ObjectArrayMap<UUID, AttributeModifier>()
    private val permanentModifiers = ObjectArraySet<AttributeModifier>()
    override val modifiers: Collection<AttributeModifier> = ObjectCollections.unmodifiable(modifiersById.values)

    private var dirty = true
    private var cachedValue = 0.0
    override var baseValue: Double = type.defaultValue
        set(value) {
            if (field == value) return
            field = value
            markDirty()
        }

    fun hasModifier(modifier: AttributeModifier): Boolean = modifiersById.get(modifier.uuid) != null

    override fun getModifier(uuid: UUID): AttributeModifier? = modifiersById.get(uuid)

    override fun getModifiers(operation: ModifierOperation): Set<AttributeModifier> = modifiersByOperation.get(operation)

    override fun addModifier(modifier: AttributeModifier) {
        addModifier0(modifier)
    }

    fun addPermanentModifier(modifier: AttributeModifier) {
        // This is because we will need to serialize permanent modifiers, and since this isn't part of the API, this is just a sanity
        // check to make sure that we don't accidentally use a custom operation.
        require(modifier.operation is BasicModifierOperation) { "Cannot add a permanent modifier with an operation that is not basic!" }
        addModifier0(modifier)
        permanentModifiers.add(modifier)
    }

    private fun addModifier0(modifier: AttributeModifier) {
        require(modifiersById.putIfAbsent(modifier.uuid, modifier) == null) { "The modifier $modifier is already applied to this attribute!" }
        modifiersByOperation.put(modifier.operation, modifier)
        markDirty()
    }

    override fun removeModifier(modifier: AttributeModifier) {
        modifiersByOperation.remove(modifier.operation, modifier)
        modifiersById.put(modifier.uuid, modifier)
        permanentModifiers.remove(modifier)
        markDirty()
    }

    fun removeModifier(id: UUID) {
        getModifier(id)?.let(::removeModifier)
    }

    fun removePermanentModifier(id: UUID): Boolean {
        val modifier = getModifier(id)
        if (modifier != null && permanentModifiers.contains(modifier)) {
            removeModifier(modifier)
            return true
        }
        return false
    }

    override fun clearModifiers() {
        modifiersByOperation.clear()
        modifiersById.clear()
        permanentModifiers.clear()
        markDirty()
    }

    override fun calculateValue(): Double {
        if (dirty) {
            cachedValue = recalculate()
            dirty = false
        }
        return cachedValue
    }

    private fun recalculate(): Double = modifiersByOperation.asMap().entries
        .fold(baseValue) { acc, (operation, modifiers) -> operation.apply(acc, modifiers) }
        .also(type::sanitizeValue)

    private fun markDirty() {
        dirty = true
        onDirty.accept(this)
    }

    fun replaceFrom(other: KryptonAttribute) {
        baseValue = other.baseValue
        modifiersById.clear()
        modifiersById.putAll(other.modifiersById)
        permanentModifiers.clear()
        permanentModifiers.addAll(other.permanentModifiers)
        modifiersByOperation.clear()
        other.modifiersByOperation.forEach { key, value -> modifiersByOperation.put(key, value) }
        markDirty()
    }

    fun load(data: CompoundTag) {
        baseValue = data.getDouble("Base")
        if (data.contains(MODIFIERS_TAG, ListTag.ID)) {
            data.getList(MODIFIERS_TAG, CompoundTag.ID).forEachCompound {
                val modifier = KryptonAttributeModifier.load(it) ?: return@forEachCompound
                modifiersById.put(modifier.uuid, modifier)
                modifiersByOperation.put(modifier.operation, modifier)
                permanentModifiers.add(modifier)
            }
        }
        markDirty()
    }

    fun save(): CompoundTag = compound {
        putString(NAME_TAG, type.key().asString())
        putDouble(BASE_TAG, baseValue)
        if (permanentModifiers.isNotEmpty()) {
            list(MODIFIERS_TAG) { permanentModifiers.forEach { add(saveModifier(it)) } }
        }
    }

    companion object {

        private const val NAME_TAG = "Name"
        private const val BASE_TAG = "Base"
        private const val MODIFIERS_TAG = "Modifiers"
        private const val UUID_TAG = "UUID"
        private const val AMOUNT_TAG = "Amount"
        private const val OPERATION_TAG = "Operation"

        @JvmStatic
        private fun saveModifier(modifier: AttributeModifier): CompoundTag = compound {
            putString(NAME_TAG, modifier.name)
            putUUID(UUID_TAG, modifier.uuid)
            putDouble(AMOUNT_TAG, modifier.amount)
            // Enforced by the invariant in addPermanentModifier. If this throws a CCE, it's a bug.
            putInt(OPERATION_TAG, (modifier.operation as BasicModifierOperation).ordinal)
        }
    }
}
