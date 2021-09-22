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
package org.kryptonmc.krypton.entity.attribute

import com.google.gson.JsonObject
import me.bardy.gsonkt.fromJson
import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.KryptonPlatform
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.registry.KryptonRegistryManager
import org.kryptonmc.krypton.util.KryptonDataLoader
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.block.BlockLoader

object AttributeLoader : KryptonDataLoader("attributes") {

    override fun load(data: JsonObject) {
        data.entrySet().forEach { (key, value) ->
            val namespacedKey = Key.key(key)
            value as JsonObject

            val translationKey = value["translationKey"].asString
            val defaultValue = value["defaultValue"].asDouble
            val clientSync = value["clientSync"].asBoolean
            val range = value["range"].asJsonObject
            val min = range["minValue"].asDouble
            val max = range["maxValue"].asDouble

            if (InternalRegistries.ATTRIBUTE.contains(namespacedKey)) return@forEach
            KryptonRegistryManager.register(
                InternalRegistries.ATTRIBUTE,
                namespacedKey,
                KryptonAttributeType(namespacedKey, defaultValue, min, max, clientSync, Component.translatable(translationKey))
            )
        }
    }
}
