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
package org.kryptonmc.krypton.entity.components

import org.kryptonmc.api.entity.Bucketable
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.item.meta.AbstractItemMeta
import org.kryptonmc.krypton.item.meta.KryptonItemMeta
import org.kryptonmc.nbt.CompoundTag

interface BucketStorable : Bucketable {

    fun loadFromBucket(tag: CompoundTag)

    fun setSpawnedFromBucket(value: Boolean)

    companion object {

        fun loadDefaultsFromBucket(mob: KryptonMob, tag: CompoundTag) {
            if (tag.contains("NoAI")) mob.hasAI = !tag.getBoolean("NoAI")
            if (tag.contains("Silent")) mob.isSilent = tag.getBoolean("Silent")
            if (tag.contains("NoGravity")) mob.hasGravity = !tag.getBoolean("NoGravity")
            if (tag.contains("Glowing")) mob.isGlowing = tag.getBoolean("Glowing")
            if (tag.contains("Invulnerable")) mob.isInvulnerable = tag.getBoolean("Invulnerable")
            if (tag.contains("Health", 99)) mob.health = tag.getFloat("Health")
        }

        @Suppress("ExpressionBodySyntax") // There's commented out code here
        fun saveDefaultsToBucket(mob: KryptonMob): AbstractItemMeta<*> {
            // FIXME
            /*
            val nbt = item.meta.nbt
            if (mob.customName != null)
            if (mob.customName != null) item.meta[MetaKeys.NAME] = mob.customName!!
            if (!mob.hasAI) nbt.putBoolean("NoAI", !mob.hasAI)
            if (mob.isSilent) nbt.putBoolean("Silent", mob.isSilent)
            if (!mob.hasGravity) nbt.putBoolean("NoGravity", !mob.hasGravity)
            if (mob.isGlowing) nbt.putBoolean("Glowing", mob.isGlowing)
            if (mob.isInvulnerable) nbt.putBoolean("Invulnerable", mob.isInvulnerable)
            nbt.putFloat("Health", mob.health)
             */
            return KryptonItemMeta.DEFAULT
        }
    }
}
