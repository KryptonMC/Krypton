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

import org.kryptonmc.api.world.Difficulty
import org.spongepowered.configurate.serialize.ScalarSerializer
import org.spongepowered.configurate.serialize.SerializationException
import java.lang.reflect.Type
import java.util.function.Predicate

object DifficultyTypeSerializer : ScalarSerializer<Difficulty>(Difficulty::class.java) {

    override fun serialize(item: Difficulty, typeSupported: Predicate<Class<*>>) = item.name.lowercase()

    override fun deserialize(type: Type, source: Any): Difficulty = when (source) {
        is Int -> Difficulty.fromId(source)
        is String -> {
            try {
                Difficulty.valueOf(source.uppercase())
            } catch (exception: IllegalArgumentException) {
                throw SerializationException("Unknown difficulty $source!")
            }
        }
        else -> throw SerializationException("Expected either an integer or a string for difficulties, got ${source::class.simpleName}")
    }
}
