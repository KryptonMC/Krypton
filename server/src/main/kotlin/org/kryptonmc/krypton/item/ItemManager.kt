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
package org.kryptonmc.krypton.item

import org.kryptonmc.krypton.item.handler.ItemHandler
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.item.handler.DebugStickHandler
import org.kryptonmc.krypton.item.handler.FoodHandler
import org.kryptonmc.krypton.item.handler.SwordHandler
import org.kryptonmc.krypton.item.handler.TridentHandler

object ItemManager {

    private val handlers = HashMap<String, ItemHandler>()

    @JvmStatic
    fun bootstrap() {
        register(ItemTypes.WOODEN_SWORD.get(), SwordHandler)
        register(ItemTypes.STONE_SWORD.get(), SwordHandler)
        register(ItemTypes.GOLDEN_SWORD.get(), SwordHandler)
        register(ItemTypes.IRON_SWORD.get(), SwordHandler)
        register(ItemTypes.DIAMOND_SWORD.get(), SwordHandler)
        register(ItemTypes.NETHERITE_SWORD.get(), SwordHandler)
        register(ItemTypes.TRIDENT.get(), TridentHandler)
        register(ItemTypes.DEBUG_STICK.get(), DebugStickHandler)
        register(ItemTypes.COOKED_BEEF.get(), FoodHandler)
    }

    @JvmStatic
    fun handler(type: ItemType): ItemHandler? = handlers.get(type.key().asString())

    @JvmStatic
    fun register(type: ItemType, handler: ItemHandler) {
        handlers.put(type.key().asString(), handler)
    }
}
