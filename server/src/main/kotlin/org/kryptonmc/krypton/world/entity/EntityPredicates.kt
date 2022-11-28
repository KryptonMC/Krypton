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
package org.kryptonmc.krypton.world.entity

import org.kryptonmc.api.world.GameMode
import org.kryptonmc.krypton.entity.KryptonEntity
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import java.util.function.Predicate

object EntityPredicates {

    @JvmField
    val LIVING_ENTITY_STILL_ALIVE: Predicate<KryptonEntity> = Predicate { it.isAlive && it is KryptonLivingEntity }
    @JvmField
    val NO_CREATIVE_OR_SPECTATOR: Predicate<KryptonEntity> = Predicate {
        it !is KryptonPlayer || it.gameMode != GameMode.CREATIVE && it.gameMode != GameMode.SPECTATOR
    }
    @JvmField
    val NO_SPECTATORS: Predicate<KryptonEntity> = Predicate { it !is KryptonPlayer || it.gameMode != GameMode.SPECTATOR }
}
