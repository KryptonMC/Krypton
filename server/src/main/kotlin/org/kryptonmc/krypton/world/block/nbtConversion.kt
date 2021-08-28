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
package org.kryptonmc.krypton.world.block

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.krypton.util.transform
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.kryptonmc.nbt.compound

fun CompoundTag.toBlock(): Block {
    if (!contains("Name", StringTag.ID)) return Blocks.AIR
    var block = BlockLoader.fromKey(Key.key(getString("Name"))) ?: error("No block found with key ${getString("Name")}!")
    if (contains("Properties", CompoundTag.ID)) {
        block = block.copy(getCompound("Properties").transform { it.key to (it.value as StringTag).value })
    }
    return block
}

fun Block.toNBT() = compound {
    string("Name", key.asString())
    if (properties.isNotEmpty()) compound("Properties") { properties.forEach { string(it.key, it.value) } }
}
