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
package org.kryptonmc.krypton.entity.animal

import org.kryptonmc.api.entity.animal.Animal
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.krypton.entity.KryptonAgeable
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.AnimalSerializer
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID

abstract class KryptonAnimal(world: KryptonWorld) : KryptonAgeable(world), Animal {

    override val serializer: EntitySerializer<out KryptonAnimal>
        get() = AnimalSerializer

    final override var loveCause: UUID? = null
    final override var inLoveTime: Int = 0

    override fun isInLove(): Boolean = inLoveTime > 0

    override fun canFallInLove(): Boolean = inLoveTime <= 0

    fun loveCause(): KryptonPlayer? {
        val cause = loveCause ?: return null
        return world.entityManager.getByUUID(cause) as? KryptonPlayer
    }

    fun setLoveCause(cause: KryptonPlayer?) {
        inLoveTime = DEFAULT_IN_LOVE_TIME
        if (cause != null) loveCause = cause.uuid
    }

    override fun canMate(target: Animal): Boolean {
        if (target === this) return false
        if (target.javaClass != javaClass) return false
        return isInLove() && target.isInLove()
    }

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.WHEAT

    companion object {

        private const val DEFAULT_IN_LOVE_TIME = 30 * 20 // 30 seconds in ticks
    }
}
