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
package org.kryptonmc.krypton.world.block.palette

import io.netty.buffer.ByteBuf
import org.kryptonmc.krypton.util.ByteBufExtras
import org.kryptonmc.krypton.util.map.IntBiMap

class GlobalPalette<T>(private val registry: IntBiMap<T>) : Palette<T> {

    override fun size(): Int = registry.size()

    override fun getId(value: T): Int {
        val id = registry.getId(value)
        return if (id == -1) 0 else id
    }

    override fun get(id: Int): T = registry.get(id) ?: throw MissingPaletteEntryException(id)

    override fun write(buf: ByteBuf) {
        // The global palette has nothing to write because the client assumes that
        // we are using the global palette if we send the right bits per entry
    }

    override fun calculateSerializedSize(): Int = ByteBufExtras.getVarIntBytes(0)

    override fun copy(): Palette<T> = this

    object Factory : Palette.Factory {

        override fun <T> create(bits: Int, registry: IntBiMap<T>, resizer: PaletteResizer<T>, entries: List<T>): Palette<T> = GlobalPalette(registry)
    }
}
