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
package org.kryptonmc.krypton.data

import io.leangen.geantyref.GenericTypeReflector
import io.leangen.geantyref.TypeToken
import java.util.function.Supplier
import net.kyori.adventure.key.Key as NamespacedKey
import org.kryptonmc.api.data.Key
import org.kryptonmc.krypton.data.provider.EmptyDataProvider

class KryptonKey<V>(private val key: NamespacedKey, override val type: TypeToken<V>, val defaultValueSupplier: Supplier<V>?) : Key<V> {

    val emptyDataProvider: EmptyDataProvider<V> = EmptyDataProvider(this)

    override fun key(): NamespacedKey = key

    object Factory : Key.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <V> of(key: NamespacedKey, type: TypeToken<V>): Key<V> {
            val rawType = GenericTypeReflector.erase(type.type)
            val defaultValueSupplier: Supplier<V>? = when {
                List::class.java.isAssignableFrom(rawType) -> Supplier { ArrayList<Any>() as V }
                Set::class.java.isAssignableFrom(rawType) -> Supplier { HashSet<Any>() as V }
                Map::class.java.isAssignableFrom(rawType) -> Supplier { HashMap<Any, Any>() as V }
                else -> null
            }
            return KryptonKey(key, type, defaultValueSupplier)
        }
    }
}
