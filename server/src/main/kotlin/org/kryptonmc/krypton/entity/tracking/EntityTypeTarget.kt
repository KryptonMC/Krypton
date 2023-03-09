/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021-2022 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
