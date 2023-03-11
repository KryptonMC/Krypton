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
package org.kryptonmc.krypton.entity.tracking

import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.util.ImmutableLists

/**
 * A target for entities. This is used to separate entities in to groups in tracking, so
 * they can be retrieved as a group, which is faster than manually filtering a list
 * every time.
 */
interface EntityTypeTarget<out E : KryptonEntity> {

    val type: Class<out E>
    val ordinal: Int

    companion object {

        @JvmField
        val ENTITIES: EntityTypeTarget<KryptonEntity> = create(KryptonEntity::class.java)
        @JvmField
        val PLAYERS: EntityTypeTarget<KryptonPlayer> = create(KryptonPlayer::class.java)

        @JvmField
        val VALUES: List<EntityTypeTarget<KryptonEntity>> = ImmutableLists.of(ENTITIES, PLAYERS)

        private fun <E : KryptonEntity> create(type: Class<E>): EntityTypeTarget<E> {
            val ordinal = DefaultEntityTracker.TARGET_COUNTER.getAndIncrement()
            return object : EntityTypeTarget<E> {

                override val type = type
                override val ordinal = ordinal
            }
        }
    }
}
