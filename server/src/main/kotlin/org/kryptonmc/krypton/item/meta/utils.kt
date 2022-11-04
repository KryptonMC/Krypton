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
package org.kryptonmc.krypton.item.meta

import com.google.common.collect.ImmutableCollection
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.Tag

@JvmSynthetic
internal inline fun <R> CompoundTag.mapToList(key: String, type: Int, mapper: (Tag) -> R?): IL<R> = mapTo(key, type, mapper) { IL.builder() }

@JvmSynthetic
internal inline fun <R> CompoundTag.mapToSet(key: String, type: Int, mapper: (Tag) -> R?): IS<R> = mapTo(key, type, mapper) { IS.builder() }

@JvmSynthetic
@Suppress("UNCHECKED_CAST")
private inline fun <C : IC<R>, B : ICB<R>, R> CompoundTag.mapTo(key: String, type: Int, mapper: (Tag) -> R?, builder: () -> B): C {
    val result = builder()
    getList(key, type).forEach { mapper(it)?.let(result::add) }
    return result.build() as C
}

private typealias IC<E> = ImmutableCollection<E>
private typealias ICB<E> = ImmutableCollection.Builder<E>
private typealias IL<E> = ImmutableList<E>
private typealias IS<E> = ImmutableSet<E>
