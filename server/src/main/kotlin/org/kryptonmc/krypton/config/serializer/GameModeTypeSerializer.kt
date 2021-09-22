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

import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.world.KryptonGameMode
import org.spongepowered.configurate.serialize.ScalarSerializer
import org.spongepowered.configurate.serialize.SerializationException
import java.lang.reflect.Type
import java.util.function.Predicate

object GameModeTypeSerializer : ScalarSerializer<GameMode>(GameMode::class.java) {

    override fun serialize(item: GameMode, typeSupported: Predicate<Class<*>>) = item.name

    override fun deserialize(type: Type, source: Any): GameMode = when (source) {
        is Int -> Registries.GAME_MODES[source] ?: throw SerializationException("$source is not a valid game mode ID!")
        is String -> KryptonGameMode.fromName(source.lowercase()) ?: throw SerializationException("$source is not a valid game mode name!")
        else -> throw SerializationException("Expected either an integer or a string for this game mode, got ${source::class.simpleName}")
    }
}
