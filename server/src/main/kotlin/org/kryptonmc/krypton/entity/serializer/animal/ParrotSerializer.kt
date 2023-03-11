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
package org.kryptonmc.krypton.entity.serializer.animal

import org.kryptonmc.api.entity.animal.type.ParrotVariant
import org.kryptonmc.krypton.entity.animal.KryptonParrot
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.nbt.CompoundTag

object ParrotSerializer : EntitySerializer<KryptonParrot> {

    private val TYPES = ParrotVariant.values()
    private const val VARIANT_TAG = "Variant"

    override fun load(entity: KryptonParrot, data: CompoundTag) {
        TamableSerializer.load(entity, data)
        entity.variant = TYPES.getOrNull(data.getInt(VARIANT_TAG)) ?: ParrotVariant.RED_AND_BLUE
    }

    override fun save(entity: KryptonParrot): CompoundTag.Builder = TamableSerializer.save(entity).apply {
        putInt(VARIANT_TAG, entity.variant.ordinal)
    }
}
