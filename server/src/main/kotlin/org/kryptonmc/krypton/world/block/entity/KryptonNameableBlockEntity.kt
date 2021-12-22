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

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.block.entity.NameableBlockEntity
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag
import org.spongepowered.math.vector.Vector3i

abstract class KryptonNameableBlockEntity(
    type: BlockEntityType,
    world: KryptonWorld,
    block: Block,
    position: Vector3i,
    override val translation: TranslatableComponent
) : KryptonBlockEntity(type, world, block, position), NameableBlockEntity {

    private var name: Component? = null
    override var displayName: Component
        get() = name ?: translation
        set(value) {
            name = value
        }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        if (tag.contains("CustomName", StringTag.ID)) name = GsonComponentSerializer.gson().deserialize(tag.getString("CustomName"))
    }

    override fun saveAdditional(tag: CompoundTag.Builder): CompoundTag.Builder = super.saveAdditional(tag).apply {
        if (name != null) string("CustomName", GsonComponentSerializer.gson().serialize(name!!))
    }
}
