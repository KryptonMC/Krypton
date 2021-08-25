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

import org.kryptonmc.api.entity.attribute.AttributeModifier
import org.kryptonmc.api.entity.attribute.ModifierOperation
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.compound

fun AttributeModifier.save(operation: ModifierOperation) = compound {
    string("Name", name)
    uuid("UUID", uuid)
    double("Amount", amount)
    int("Operation", Registries.MODIFIER_OPERATIONS.idOf(operation))
}

fun Map<ModifierOperation, List<AttributeModifier>>.save() = ListTag(elementType = CompoundTag.ID).apply {
    this@save.forEach { (operation, modifiers) -> modifiers.forEach { add(it.save(operation)) } }
}
