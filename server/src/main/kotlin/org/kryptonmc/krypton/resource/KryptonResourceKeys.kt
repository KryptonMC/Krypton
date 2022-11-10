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

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryRoots
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.memory.MemoryKey
import org.kryptonmc.krypton.item.Instrument
import org.kryptonmc.krypton.item.KryptonItemType
import org.kryptonmc.krypton.network.chat.ChatType
import org.kryptonmc.krypton.registry.WritableRegistry
import org.kryptonmc.krypton.util.provider.IntProviderType
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.event.GameEvent
import org.kryptonmc.krypton.world.fluid.KryptonFluid

object KryptonResourceKeys {

    @JvmField
    val PARENT: ResourceKey<out Registry<WritableRegistry<*>>> = minecraft("root")
    @JvmField
    val GAME_EVENT: ResourceKey<out Registry<GameEvent>> = minecraft("game_event")
    @JvmField
    val FLUID: ResourceKey<out Registry<KryptonFluid>> = minecraft("fluid")
    @JvmField
    val BLOCK: ResourceKey<out Registry<KryptonBlock>> = minecraft("block")
    @JvmField
    val ENTITY_TYPE: ResourceKey<out Registry<KryptonEntityType<*>>> = minecraft("entity_type")
    @JvmField
    val ITEM: ResourceKey<out Registry<KryptonItemType>> = minecraft("item")
    @JvmField
    val MEMORIES: ResourceKey<out Registry<MemoryKey<*>>> = minecraft("memory_module_type")
    @JvmField
    val INT_PROVIDER_TYPES: ResourceKey<out Registry<IntProviderType<*>>> = minecraft("int_provider_type")
    @JvmField
    val INSTRUMENTS: ResourceKey<out Registry<Instrument>> = minecraft("instrument")
    @JvmField
    val CHAT_TYPE: ResourceKey<out Registry<ChatType>> = minecraft("chat_type")

    @JvmStatic
    private fun <T> minecraft(key: String): ResourceKey<out Registry<T>> = KryptonResourceKey.of(RegistryRoots.MINECRAFT, Key.key(key))
}
