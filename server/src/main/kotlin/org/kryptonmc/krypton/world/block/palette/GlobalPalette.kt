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
package org.kryptonmc.krypton.world.block.palette

import com.google.gson.reflect.TypeToken
import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.GSON
import org.kryptonmc.krypton.registry.json.RegistryBlock
import java.io.IOException

private val BLOCKS_TEXT = (Thread.currentThread().contextClassLoader.getResourceAsStream("registries/blocks.json")
    ?: throw IOException("registries/blocks.json not in classpath! Something has gone horribly wrong!"))
    .reader(Charsets.UTF_8)
    .readText()

private val PALETTE: Map<Key, RegistryBlock> = GSON.fromJson(BLOCKS_TEXT, object : TypeToken<Map<Key, RegistryBlock>>() {}.type)

/**
 * The global block palette, backed by the block state registry
 */
object GlobalPalette : Map<Key, RegistryBlock> by PALETTE {

    override fun get(key: Key) = PALETTE.getValue(key)
}
