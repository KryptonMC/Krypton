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

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.block.Blocks
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.world.biome.Biome
import org.kryptonmc.api.world.biome.Biomes
import org.kryptonmc.krypton.util.varIntBytes
import org.kryptonmc.krypton.util.writeVarInt
import org.kryptonmc.krypton.world.block.BlockLoader
import org.kryptonmc.nbt.ListTag
import java.util.function.ToIntFunction

sealed class SinglePalette<T>(
    private val resizer: PaletteResizer<T>,
    private val idProvider: ToIntFunction<T>,
    private val deserializer: (String) -> T
) : Palette<T> {

    private var value: T? = null
    override val size: Int = 1
    override val serializedSize: Int = idProvider.applyAsInt(checkNotNull(value) { "Attempted to use an uninitialised palette!" }).varIntBytes

    override fun get(id: Int): T? {
        check(value != null && id == 0) { "Missing palette entry for ID $id!" }
        return value
    }

    override fun get(value: T): Int {
        if (this.value != null && this.value != value) return resizer(1, value)
        this.value = value
        return 0
    }

    override fun write(buf: ByteBuf) {
        checkNotNull(value) { "Attempted to use an uninitialised palette!" }
        buf.writeVarInt(idProvider.applyAsInt(value))
    }

    override fun load(data: ListTag) {
        if (data.isEmpty()) return
        value = deserializer(data.getString(0))
    }

    class Blocks(resizer: PaletteResizer<Block>) : SinglePalette<Block>(
        resizer,
        { BlockLoader.STATES.idOf(it) },
        { BlockLoader.fromKey(it) ?: org.kryptonmc.api.block.Blocks.AIR }
    )

    class Biomes(resizer: PaletteResizer<Biome>) : SinglePalette<Biome>(
        resizer,
        { Registries.BIOME.idOf(it) },
        { Registries.BIOME[Key.key(it)] ?: org.kryptonmc.api.world.biome.Biomes.PLAINS }
    )
}
