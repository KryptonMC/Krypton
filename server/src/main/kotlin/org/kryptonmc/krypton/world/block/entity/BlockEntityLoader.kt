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

import com.google.common.collect.ImmutableSet
import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.util.KryptonDataLoader

object BlockEntityLoader : KryptonDataLoader("block_entities") {

    override fun load(data: JsonObject) {
        data.entrySet().forEach { (key, value) ->
            val namespacedKey = Key.key(key)
            value as JsonObject

            val blocks = ImmutableSet.copyOf(value["blocks"].asJsonArray.map {
                val id = Key.key(it.asJsonObject["id"].asString)
                Registries.BLOCK[id]!!
            })

            if (InternalRegistries.BLOCK_ENTITY_TYPE.contains(namespacedKey)) return@forEach
            KryptonRegistryManager.register(
                InternalRegistries.BLOCK_ENTITY_TYPE,
                namespacedKey,
                KryptonBlockEntityType(namespacedKey, blocks)
            )
        }
    }
}
