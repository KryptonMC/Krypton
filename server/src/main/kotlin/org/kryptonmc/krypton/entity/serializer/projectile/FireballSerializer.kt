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

import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.projectile.KryptonFireball
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.nbt.CompoundTag

object FireballSerializer : EntitySerializer<KryptonFireball> {

    private const val ITEM_TAG = "Item"

    override fun load(entity: KryptonFireball, data: CompoundTag) {
        AcceleratingProjectileSerializer.load(entity, data)
        entity.data.set(MetadataKeys.Fireball.ITEM, KryptonItemStack.from(data.getCompound(ITEM_TAG)))
    }

    override fun save(entity: KryptonFireball): CompoundTag.Builder = AcceleratingProjectileSerializer.save(entity).apply {
        val item = entity.data.get(MetadataKeys.Fireball.ITEM)
        if (!item.isEmpty()) put(ITEM_TAG, item.save())
    }
}
