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
package org.kryptonmc.krypton.world.block.entity

import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntity
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.krypton.packet.GenericPacket
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.compound
import org.spongepowered.math.vector.Vector3i

abstract class KryptonBlockEntity(
    override val type: BlockEntityType,
    override val world: KryptonWorld,
    override var block: Block,
    override val position: Vector3i
) : BlockEntity {

    open val restrictModification: Boolean
        get() = false
    open val updatePacket: GenericPacket?
        get() = null
    open val updateTag: CompoundTag
        get() = CompoundTag.empty()

    open fun load(tag: CompoundTag) {
        // nothing to do for the base type
    }

    fun save(full: Boolean): CompoundTag = compound {
        saveAdditional(this)
        if (full) saveMetadata(this)
    }

    protected open fun saveAdditional(tag: CompoundTag.Builder): CompoundTag.Builder = tag

    private fun saveMetadata(tag: CompoundTag.Builder): CompoundTag.Builder = tag.apply {
        val key = type.key()
        string("id", key.asString())
        int("x", position.x())
        int("y", position.y())
        int("z", position.z())
    }
}
