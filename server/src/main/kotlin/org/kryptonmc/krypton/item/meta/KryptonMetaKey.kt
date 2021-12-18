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
package org.kryptonmc.krypton.item.meta

import net.kyori.adventure.key.Key
import org.kryptonmc.api.item.meta.MetaKey
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.MutableCompoundTag
import java.util.function.Predicate

@JvmRecord
data class KryptonMetaKey<V : Any>(
    private val key: Key,
    private val type: Class<V>,
    val reader: Reader<V>,
    val writer: Writer<V>,
    val predicate: Predicate<CompoundTag>
) : MetaKey<V> {

    override fun key(): Key = key

    override fun type(): Class<V> = type

    fun interface Reader<T> {

        fun read(tag: CompoundTag): T
    }

    fun interface Writer<T> {

        fun write(tag: CompoundTag, value: T): CompoundTag
    }
}
