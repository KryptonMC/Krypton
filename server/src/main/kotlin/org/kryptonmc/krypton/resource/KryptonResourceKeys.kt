/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.resource

import net.kyori.adventure.key.Key
import org.kryptonmc.api.registry.Registry
import org.kryptonmc.api.registry.RegistryRoots
import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.api.world.World
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.ai.memory.MemoryKey
import org.kryptonmc.krypton.item.data.Instrument
import org.kryptonmc.krypton.item.KryptonItemType
import org.kryptonmc.krypton.network.chat.RichChatType
import org.kryptonmc.krypton.registry.WritableRegistry
import org.kryptonmc.krypton.util.provider.IntProviderType
import org.kryptonmc.krypton.world.block.KryptonBlock
import org.kryptonmc.krypton.world.dimension.KryptonDimensionType
import org.kryptonmc.krypton.world.gameevent.GameEvent
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
    val CHAT_TYPE: ResourceKey<out Registry<RichChatType>> = minecraft("chat_type")
    @JvmField
    val WORLD: ResourceKey<out Registry<World>> = minecraft("dimension")
    @JvmField
    val DIMENSION_TYPE: ResourceKey<out Registry<KryptonDimensionType>> = minecraft("dimension_type")

    @JvmStatic
    private fun <T> minecraft(key: String): ResourceKey<out Registry<T>> = KryptonResourceKey.of(RegistryRoots.MINECRAFT, Key.key(key))
}
