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
package org.kryptonmc.krypton.item

import com.google.gson.JsonObject
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.util.KryptonDataLoader

object ItemLoader : KryptonDataLoader("items") {

    override fun load(data: JsonObject) {
        data.entrySet().forEach { (key, value) ->
            val namespacedKey = Key.key(key)
            value as JsonObject

            val rarity = value["rarity"]?.asString ?: "COMMON"
            val translationKey = value["translationKey"]?.asString
                ?: "item.${namespacedKey.namespace()}.${namespacedKey.value().replace('/', '.')}"
            val depletes = value["depletes"].asBoolean
            val maxStackSize = value["maxStackSize"].asInt
            val maxDamage = value["maxDamage"].asInt
            val edible = value["edible"].asBoolean
            val fireResistant = value["fireResistant"].asBoolean
            val eatingSound = value["eatingSound"].asString
            val drinkingSound = value["drinkingSound"].asString

            if (Registries.ITEM.contains(namespacedKey)) return@forEach
            Registries.ITEM.register(
                namespacedKey,
                KryptonItemType(
                    namespacedKey,
                    Registries.ITEM_RARITIES[Key.key("krypton", rarity.lowercase())]!!,
                    maxStackSize,
                    depletes,
                    maxDamage,
                    edible,
                    fireResistant,
                    Registries.SOUND_EVENT[Key.key(eatingSound)] ?: SoundEvents.GENERIC_EAT,
                    Registries.SOUND_EVENT[Key.key(drinkingSound)] ?: SoundEvents.GENERIC_DRINK,
                    Component.translatable(translationKey)
                )
            )
        }
    }
}
