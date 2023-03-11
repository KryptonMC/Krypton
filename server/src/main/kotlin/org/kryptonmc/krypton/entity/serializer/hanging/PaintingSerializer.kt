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
package org.kryptonmc.krypton.entity.serializer.hanging

import net.kyori.adventure.key.Key
import org.kryptonmc.krypton.entity.hanging.KryptonPainting
import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.util.enumhelper.Directions
import org.kryptonmc.krypton.util.nbt.putNullable
import org.kryptonmc.krypton.util.nbt.putKeyed
import org.kryptonmc.nbt.CompoundTag

object PaintingSerializer : EntitySerializer<KryptonPainting> {

    private const val VARIANT_TAG = "variant"
    private const val FACING_TAG = "facing"

    override fun load(entity: KryptonPainting, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        entity.variant = KryptonRegistries.PAINTING_VARIANT.get(Key.key(data.getString(VARIANT_TAG)))
        entity.direction = Directions.of2D(data.getByte(FACING_TAG).toInt())
    }

    override fun save(entity: KryptonPainting): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putNullable(VARIANT_TAG, entity.variant, CompoundTag.Builder::putKeyed)
        putByte(FACING_TAG, Directions.data2D(entity.direction).toByte())
    }
}
