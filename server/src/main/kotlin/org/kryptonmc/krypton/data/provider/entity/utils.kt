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
package org.kryptonmc.krypton.data.provider.entity

import org.kryptonmc.api.data.Key
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.metadata.MetadataKey

inline fun <reified H : KryptonEntity, E> DataProviderRegistrar.registerMetadata(key: Key<E>, metadataKey: MetadataKey<E>) {
    registerMutable<H, E>(key, { it.data[metadataKey] }, { holder, value -> holder.data[metadataKey] = value })
}

inline fun <reified H : KryptonEntity, E, M> DataProviderRegistrar.registerMetadata(
    key: Key<E>,
    metadataKey: MetadataKey<M>,
    crossinline getterTransformer: (M) -> E?,
    crossinline setterTransformer: (E) -> M
) {
    registerMutable<H, E>(key) {
        get { getterTransformer(it.data[metadataKey]) }
        set { holder, value -> holder.data[metadataKey] = setterTransformer(value) }
    }
}

inline fun <reified H : KryptonEntity> DataProviderRegistrar.registerFlag(key: Key<Boolean>, metadataKey: MetadataKey<Byte>, flag: Int) {
    registerMutable<H, Boolean>(key, { it.data.getFlag(metadataKey, flag) }) { holder, value -> holder.data.setFlag(metadataKey, flag, value) }
}
