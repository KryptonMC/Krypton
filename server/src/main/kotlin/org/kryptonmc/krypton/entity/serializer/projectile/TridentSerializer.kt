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
package org.kryptonmc.krypton.entity.serializer.projectile

import org.kryptonmc.krypton.entity.projectile.KryptonTrident
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.nbt.CompoundTag

object TridentSerializer : EntitySerializer<KryptonTrident> {

    private const val DEALT_DAMAGE_TAG = "DealtDamage"
    private const val TRIDENT_TAG = "Trident"

    override fun load(entity: KryptonTrident, data: CompoundTag) {
        ArrowLikeSerializer.load(entity, data)
        entity.dealtDamage = data.getBoolean(DEALT_DAMAGE_TAG)
        if (data.contains(TRIDENT_TAG, CompoundTag.ID)) entity.item = KryptonItemStack.from(data.getCompound(TRIDENT_TAG))
    }

    override fun save(entity: KryptonTrident): CompoundTag.Builder = ArrowLikeSerializer.save(entity).apply {
        putBoolean(DEALT_DAMAGE_TAG, entity.dealtDamage)
        put(TRIDENT_TAG, entity.item.save())
    }
}
