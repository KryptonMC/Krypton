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
package org.kryptonmc.krypton.item

import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.item.ItemType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.util.KryptonDataLoader

object ItemLoader : KryptonDataLoader<ItemType>("items", Registries.ITEM) {

    override fun create(key: Key, value: JsonObject): ItemType {
        val rarityName = value["rarity"]?.asString ?: "COMMON"
        val rarity = Registries.ITEM_RARITIES[Key.key("krypton", rarityName.lowercase())]!!
        val translationKey = value["translationKey"]?.asString ?: "item.${key.namespace()}.${key.value().replace('/', '.')}"
        val depletes = value["depletes"].asBoolean
        val maxStackSize = value["maxStackSize"].asInt
        val maxDamage = value["maxDamage"].asInt
        val edible = value["edible"].asBoolean
        val fireResistant = value["fireResistant"].asBoolean
        val eatingSound = Registries.SOUND_EVENT[Key.key(value["eatingSound"].asString)] ?: SoundEvents.GENERIC_EAT
        val drinkingSound = Registries.SOUND_EVENT[Key.key(value["drinkingSound"].asString)] ?: SoundEvents.GENERIC_DRINK
        val translation = Component.translatable(translationKey)
        return KryptonItemType(key, rarity, maxStackSize, depletes, maxDamage, edible, fireResistant, eatingSound, drinkingSound, translation)
    }
}
