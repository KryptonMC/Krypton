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
package org.kryptonmc.krypton.util

import net.kyori.adventure.key.InvalidKeyException
import net.kyori.adventure.key.Key
import org.kryptonmc.serialization.DataResult

object Keys {

    @JvmStatic
    fun create(namespace: String, path: String): Key? {
        return try {
            Key.key(namespace, path)
        } catch (_: InvalidKeyException) {
            null
        }
    }

    @JvmStatic
    fun read(value: String): DataResult<Key> {
        return try {
            DataResult.success(Key.key(value))
        } catch (exception: InvalidKeyException) {
            DataResult.error("$value is not a valid resource location! ${exception.message}")
        }
    }

    @JvmStatic
    fun translation(type: String, key: Key?): String {
        if (key == null) return "$type.unregistered_sadface"
        return "$type.${key.namespace()}.${key.value().replace('/', '.')}"
    }
}
