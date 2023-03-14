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

import org.kryptonmc.api.item.meta.LeatherArmorMeta
import org.kryptonmc.api.util.Color
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.IntTag

class KryptonLeatherArmorMeta(data: CompoundTag) : AbstractItemMeta<KryptonLeatherArmorMeta>(data), LeatherArmorMeta {

    override val color: Color? = getDisplay<IntTag, _>(data, COLOR_TAG, IntTag.ID, null) { Color(it.value()) }

    override fun copy(data: CompoundTag): KryptonLeatherArmorMeta = KryptonLeatherArmorMeta(data)

    override fun withColor(color: Color?): KryptonLeatherArmorMeta {
        val newData = if (color == null) data.remove(COLOR_TAG) else data.putInt(COLOR_TAG, color.encode())
        return KryptonLeatherArmorMeta(newData)
    }

    override fun toBuilder(): LeatherArmorMeta.Builder = Builder(this)

    class Builder : KryptonItemMetaBuilder<LeatherArmorMeta.Builder, LeatherArmorMeta>, LeatherArmorMeta.Builder {

        private var color: Color? = null

        constructor() : super()

        constructor(meta: KryptonLeatherArmorMeta) : super(meta) {
            color = meta.color
        }

        override fun color(color: Color?): LeatherArmorMeta.Builder = apply { this.color = color }

        override fun buildData(): CompoundTag.Builder = super.buildData().apply {
            if (color != null) putInt(COLOR_TAG, color!!.encode())
        }

        override fun build(): KryptonLeatherArmorMeta = KryptonLeatherArmorMeta(buildData().build())
    }

    companion object {

        private const val COLOR_TAG = "color"
    }
}
