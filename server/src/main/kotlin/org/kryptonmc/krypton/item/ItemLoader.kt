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

import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.item.ItemRarity
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.registry.KryptonRegistry
import org.kryptonmc.krypton.util.KryptonDataLoader

class ItemLoader(registry: KryptonRegistry<KryptonItemType>) : KryptonDataLoader<KryptonItemType>("items", registry) {

    override fun create(key: Key, value: JsonObject): KryptonItemType {
        val rarityName = value.get("rarity")?.asString ?: "COMMON"
        val rarity = ItemRarity.valueOf(rarityName)
        val maxStackSize = value.get("maxStackSize").asInt
        val maxDamage = value.get("maxDamage").asInt
        val edible = value.get("edible").asBoolean
        val fireResistant = value.get("fireResistant").asBoolean
        val eatingSound = KryptonRegistries.SOUND_EVENT.get(Key.key(value.get("eatingSound").asString)) ?: SoundEvents.GENERIC_EAT.get()
        val drinkingSound = KryptonRegistries.SOUND_EVENT.get(Key.key(value.get("drinkingSound").asString)) ?: SoundEvents.GENERIC_DRINK.get()
        return KryptonItemType(rarity, maxStackSize, maxDamage, edible, fireResistant, eatingSound, drinkingSound)
    }
}
