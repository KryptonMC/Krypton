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

import org.kryptonmc.krypton.entity.projectile.KryptonProjectile
import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.util.nbt.getUUID
import org.kryptonmc.krypton.util.nbt.hasUUID
import org.kryptonmc.krypton.util.nbt.putNullable
import org.kryptonmc.krypton.util.nbt.putUUID
import org.kryptonmc.nbt.CompoundTag

object ProjectileSerializer : EntitySerializer<KryptonProjectile> {

    private const val OWNER_TAG = "Owner"
    private const val LEFT_OWNER_TAG = "LeftOwner"
    private const val HAS_BEEN_SHOT_TAG = "HasBeenShot"

    override fun load(entity: KryptonProjectile, data: CompoundTag) {
        BaseEntitySerializer.load(entity, data)
        if (data.hasUUID(OWNER_TAG)) entity.setOwnerId(data.getUUID(OWNER_TAG))
        entity.setLeftOwner(data.getBoolean(LEFT_OWNER_TAG))
        entity.setBeenShot(data.getBoolean(HAS_BEEN_SHOT_TAG))
    }

    override fun save(entity: KryptonProjectile): CompoundTag.Builder = BaseEntitySerializer.save(entity).apply {
        putNullable(OWNER_TAG, entity.ownerId(), CompoundTag.Builder::putUUID)
        if (entity.hasLeftOwner()) putBoolean(LEFT_OWNER_TAG, true)
        putBoolean(HAS_BEEN_SHOT_TAG, entity.hasBeenShot())
    }
}
