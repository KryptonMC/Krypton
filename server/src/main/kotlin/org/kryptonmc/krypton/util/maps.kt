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
package org.kryptonmc.krypton.util

fun <K, V, K1, V1> Map<K, V>.transform(transformer: (Map.Entry<K, V>) -> Pair<K1, V1>): Map<K1, V1> =
    transformTo(mutableMapOf(), transformer)

fun <C : MutableMap<K1, V1>, K, V, K1, V1> Map<K, V>.transformTo(destination: C, transformer: (Map.Entry<K, V>) -> Pair<K1, V1>): C {
    for (entry in this) {
        destination += transformer(entry)
    }
    return destination
}
