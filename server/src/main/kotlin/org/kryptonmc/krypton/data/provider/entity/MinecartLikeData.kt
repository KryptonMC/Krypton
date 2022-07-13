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
package org.kryptonmc.krypton.data.provider.entity

import org.kryptonmc.api.data.Keys
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.vehicle.KryptonMinecartLike
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.krypton.world.block.downcast

object MinecartLikeData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerMutable(Keys.MINECART_TYPE, KryptonMinecartLike::minecartType)
        registrar.registerMetadata<KryptonMinecartLike, _>(Keys.SHOW_CUSTOM_BLOCK, MetadataKeys.MINECART_LIKE.SHOW_CUSTOM_BLOCK)
        registrar.registerMetadata<KryptonMinecartLike, _, _>(
            Keys.CUSTOM_BLOCK,
            MetadataKeys.MINECART_LIKE.CUSTOM_BLOCK_ID,
            BlockLoader::fromState
        ) { it.downcast().stateId }
        registrar.registerMetadata<KryptonMinecartLike, _>(Keys.CUSTOM_BLOCK_OFFSET, MetadataKeys.MINECART_LIKE.CUSTOM_BLOCK_OFFSET)
    }
}
