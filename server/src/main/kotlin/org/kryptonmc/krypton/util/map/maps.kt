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
package org.kryptonmc.krypton.util.map

import java.util.function.Function

// We use the platform class here to clean up the generated code, because we don't need to check for KMappedMarker,
// and we ideally want to call the JVM implementation directly, as that's the one with our desired semantics.
@Suppress("UNCHECKED_CAST", "PLATFORM_CLASS_MAPPED_TO_KOTLIN", "NOTHING_TO_INLINE")
inline fun <K, V> MutableMap<K, V>.nullableComputeIfAbsent(key: K, mappingFunction: Function<in K, out V?>): V? =
    (this as java.util.Map<K, V>).computeIfAbsent(key, mappingFunction)
