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
package org.kryptonmc.krypton.registry

import org.kryptonmc.api.resource.ResourceKey
import org.kryptonmc.krypton.registry.holder.Holder
import org.kryptonmc.krypton.registry.holder.HolderGetter

/**
 * A registry that may be written to. This subclass exists to limit the scope of registry writing, as registries must be downcasted
 * to this type in order to access the register methods in this class.
 */
interface WritableRegistry<T> : KryptonRegistry<T> {

    fun register(id: Int, key: ResourceKey<T>, value: T): Holder.Reference<T>

    fun register(key: ResourceKey<T>, value: T): Holder.Reference<T>

    fun isEmpty(): Boolean

    fun createRegistrationLookup(): HolderGetter<T>
}
