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
package org.kryptonmc.krypton.registry

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.entity.memory.MemoryKey
import org.kryptonmc.krypton.item.Instrument
import org.kryptonmc.krypton.network.chat.ChatType
import org.kryptonmc.krypton.resource.InternalResourceKeys
import org.kryptonmc.krypton.util.provider.IntProviderType
import org.kryptonmc.krypton.world.event.GameEvent

object InternalRegistries {

    @JvmField
    val MEMORIES: KryptonRegistry<MemoryKey<Any>> = create(InternalResourceKeys.MEMORIES)
    @JvmField
    val GAME_EVENT: KryptonRegistry<GameEvent> = create(InternalResourceKeys.GAME_EVENT)
    @JvmField
    val INT_PROVIDER_TYPES: KryptonRegistry<IntProviderType<*>> = create(InternalResourceKeys.INT_PROVIDER_TYPES)
    @JvmField
    val INSTRUMENTS: KryptonRegistry<Instrument> = create(InternalResourceKeys.INSTRUMENTS)
    @JvmField
    val CHAT_TYPE: KryptonRegistry<ChatType> = create(InternalResourceKeys.CHAT_TYPE)

    @JvmStatic
    private fun <T : Any> create(key: ResourceKey<out Registry<T>>): KryptonRegistry<T> = KryptonRegistryManager.create(key)
}
