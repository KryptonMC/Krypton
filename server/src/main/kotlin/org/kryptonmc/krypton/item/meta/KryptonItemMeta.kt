/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.item.meta

import org.kryptonmc.api.item.meta.ItemMeta
import org.kryptonmc.api.item.meta.ItemMetaBuilder
import org.kryptonmc.krypton.item.ItemFactory
import org.kryptonmc.nbt.CompoundTag

class KryptonItemMeta(data: CompoundTag) : AbstractItemMeta<KryptonItemMeta>(data), ItemMeta {

    override fun copy(data: CompoundTag): KryptonItemMeta = KryptonItemMeta(data)

    class Builder : KryptonItemMetaBuilder<ItemMeta.Builder, ItemMeta>(), ItemMeta.Builder {

        override fun build(): KryptonItemMeta = KryptonItemMeta(buildData().build())
    }

    object Factory : ItemMeta.Factory {

        override fun builder(): ItemMeta.Builder = Builder()

        override fun <B : ItemMetaBuilder<B, P>, P : ItemMetaBuilder.Provider<B>> builder(type: Class<P>): B = ItemFactory.builder(type)
    }

    companion object {

        @JvmField
        val DEFAULT: KryptonItemMeta = Builder().build()
    }
}
