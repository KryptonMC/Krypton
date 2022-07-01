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
package org.kryptonmc.krypton.util.serialization

import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.Tag
import org.kryptonmc.nbt.compound

class MapCodec<K, V>(private val keyCodec: StringCodec<K>, private val valueCodec: Codec<Tag, V>) : CompoundCodec<Map<K, V>> {

    override fun encode(value: Map<K, V>): CompoundTag = compound {
        value.forEach { put(keyCodec.encodeString(it.key), valueCodec.encode(it.value)) }
    }

    override fun decode(tag: CompoundTag): Map<K, V> {
        val map = mutableMapOf<K, V>()
        tag.forEach { map[keyCodec.decodeString(it.key)] = valueCodec.decode(it.value) }
        return map
    }
}