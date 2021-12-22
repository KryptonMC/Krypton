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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.entity.BlockEntity
import org.kryptonmc.api.block.entity.BlockEntityType
import org.kryptonmc.api.block.entity.BlockEntityTypes
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.entity.banner.bannerColor
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3i

object BlockEntityFactory {

    private val TYPE_MAP = mapOf<BlockEntityType, BlockEntityConstructor<out BlockEntity>>(
        BlockEntityTypes.BANNER to BlockEntityConstructor { world, block, position -> KryptonBanner(world, block, position, block.bannerColor()) }
    )

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <T : BlockEntity> create(type: BlockEntityType, world: KryptonWorld, block: Block, position: Vector3i): T? {
        val constructor = TYPE_MAP[type] as? BlockEntityConstructor<T> ?: return null
        return constructor.create(world, block, position)
    }

    @JvmStatic
    fun <T : BlockEntity> create(world: KryptonWorld, block: Block, tag: CompoundTag): T? {
        val type = Registries.BLOCK_ENTITY_TYPE[Key.key(tag.getString("id"))] ?: return null
        return create(type, world, block, Vector3i(tag.getInt("x"), tag.getInt("y"), tag.getInt("z")))
    }
}
