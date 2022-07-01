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
package org.kryptonmc.krypton.item

import org.kryptonmc.api.item.ItemAttribute
import org.kryptonmc.krypton.item.handler.ItemHandler
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.data.ItemFlag
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.item.handler.DummyItemHandler
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound

fun ItemType.handler(): ItemHandler = ItemManager.handler(this) ?: DummyItemHandler

fun KryptonItemStack.destroySpeed(block: KryptonBlock): Float = type.handler().destroySpeed(this, block)

fun ItemFlag.mask(): Int = 1 shl ordinal

fun ItemAttribute.save(): CompoundTag = compound {
    string("AttributeName", type.key().asString())
    string("Slot", slot.name.lowercase())
    string("Name", modifier.name)
    uuid("UUID", modifier.uuid)
    double("Amount", modifier.amount)
    int("Operation", Registries.MODIFIER_OPERATIONS.idOf(modifier.operation))
}
