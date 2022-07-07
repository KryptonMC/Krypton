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
package org.kryptonmc.krypton.resource

import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.resource.ResourceKeys
import org.kryptonmc.krypton.entity.memory.MemoryKey
import org.kryptonmc.krypton.item.Instrument
import org.kryptonmc.krypton.util.provider.IntProviderType
import org.kryptonmc.krypton.world.event.GameEvent

object InternalResourceKeys {

    @JvmField
    val MEMORIES: ResourceKey<out Registry<MemoryKey<Any>>> = ResourceKeys.minecraft("memory_module_type")
    @JvmField
    val GAME_EVENT: ResourceKey<out Registry<GameEvent>> = ResourceKeys.minecraft("game_event")
    @JvmField
    val INT_PROVIDER_TYPES: ResourceKey<out Registry<IntProviderType<*>>> = ResourceKeys.minecraft("int_provider_type")
    @JvmField
    val INSTRUMENTS: ResourceKey<out Registry<Instrument>> = ResourceKeys.minecraft("instrument")
}
