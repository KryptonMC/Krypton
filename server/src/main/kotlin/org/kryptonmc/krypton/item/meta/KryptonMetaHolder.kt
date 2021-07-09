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

import net.kyori.adventure.pointer.Pointer
import net.kyori.adventure.pointer.Pointers
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.kryptonmc.api.item.meta.MetaHolder
import org.kryptonmc.api.item.meta.MetaKey
import java.util.Optional

open class KryptonMetaHolder(var nbt: NBTCompound = NBTCompound()) : MetaHolder {

    override fun <V : Any> get(key: MetaKey<V>): V? = MetaFactory.get(key, nbt)

    override fun <V : Any> set(key: MetaKey<V>, value: V) = MetaFactory.set(key, nbt, value)

    override fun <V : Any> contains(key: MetaKey<V>) = MetaFactory.contains(key, nbt)

    override fun copy() = KryptonMetaHolder(nbt.deepClone())

    override fun <T : Any> get(pointer: Pointer<T>): Optional<T> {
        if (pointer !is MetaKey<T>) return Optional.empty()
        return Optional.ofNullable(get(pointer))
    }

    override fun <T : Any> getOrDefault(pointer: Pointer<T>, defaultValue: T?): T? {
        if (pointer !is MetaKey<T>) return defaultValue
        return get(pointer) ?: defaultValue
    }

    override fun <T : Any> supports(pointer: Pointer<T>): Boolean {
        if (pointer !is MetaKey<T>) return false
        return contains(pointer)
    }

    override fun toBuilder(): Pointers.Builder = Pointers.builder()
}
