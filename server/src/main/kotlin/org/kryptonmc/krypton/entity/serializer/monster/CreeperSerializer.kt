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
package org.kryptonmc.krypton.entity.serializer.monster

import org.kryptonmc.krypton.entity.monster.KryptonCreeper
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.MobSerializer
import org.kryptonmc.nbt.CompoundTag

object CreeperSerializer : EntitySerializer<KryptonCreeper> {

    private const val POWERED_TAG = "powered"
    private const val IGNITED_TAG = "ignited"
    private const val FUSE_TAG = "Fuse"
    private const val EXPLOSION_RADIUS_TAG = "ExplosionRadius"

    override fun load(entity: KryptonCreeper, data: CompoundTag) {
        MobSerializer.load(entity, data)
        entity.isCharged = data.getBoolean(POWERED_TAG)
        entity.setIgnited(data.getBoolean(IGNITED_TAG))
        entity.fuse = data.getShort(FUSE_TAG).toInt()
        entity.explosionRadius = data.getInt(EXPLOSION_RADIUS_TAG)
    }

    override fun save(entity: KryptonCreeper): CompoundTag.Builder = MobSerializer.save(entity).apply {
        putBoolean(POWERED_TAG, entity.isCharged)
        putBoolean(IGNITED_TAG, entity.isIgnited)
        putShort(FUSE_TAG, entity.fuse.toShort())
        putInt(EXPLOSION_RADIUS_TAG, entity.explosionRadius)
    }
}
