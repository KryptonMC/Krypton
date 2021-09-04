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
package org.kryptonmc.krypton.config.serializer

import org.kryptonmc.api.world.Gamemode
import org.spongepowered.configurate.serialize.ScalarSerializer
import org.spongepowered.configurate.serialize.SerializationException
import java.lang.reflect.Type
import java.util.function.Predicate

object GamemodeTypeSerializer : ScalarSerializer<Gamemode>(Gamemode::class.java) {

    override fun serialize(item: Gamemode, typeSupported: Predicate<Class<*>>) = item.name.lowercase()

    override fun deserialize(type: Type, source: Any): Gamemode {
        return try {
            when (source) {
                is Int -> Gamemode.fromId(source) ?: throw SerializationException("$source is not a valid gamemode ID!")
                is String -> Gamemode.valueOf(source.uppercase())
                else -> throw SerializationException("Expected either an integer or a string for this gamemode, " +
                        "got ${source::class.simpleName}")
            }
        } catch (exception: IllegalArgumentException) {
            throw SerializationException("Unknown gamemode: $source")
        }
    }
}
