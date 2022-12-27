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
package org.kryptonmc.krypton.pack

import org.kryptonmc.krypton.pack.metadata.MetadataSectionSerializer
import org.kryptonmc.krypton.util.ImmutableMaps

class BuiltInMetadata private constructor(private val values: Map<MetadataSectionSerializer<*>, *>) {

    @Suppress("UNCHECKED_CAST")
    fun <T> get(serializer: MetadataSectionSerializer<T>): T = values.get(serializer) as T

    companion object {

        private val EMPTY = BuiltInMetadata(ImmutableMaps.of<_, Any>())

        @JvmStatic
        fun of(): BuiltInMetadata = EMPTY

        @JvmStatic
        fun <T> of(serializer: MetadataSectionSerializer<T>, value: T): BuiltInMetadata = BuiltInMetadata(ImmutableMaps.of(serializer, value))

        @JvmStatic
        fun <T1, T2> of(s1: MetadataSectionSerializer<T1>, v1: T1, s2: MetadataSectionSerializer<T2>, v2: T2): BuiltInMetadata =
            BuiltInMetadata(ImmutableMaps.of(s1, v1, s2, v2))
    }
}
