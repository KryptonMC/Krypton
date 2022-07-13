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
package org.kryptonmc.krypton.data.provider.entity

import org.kryptonmc.api.data.Keys
import org.kryptonmc.krypton.data.provider.DataProviderRegistrar
import org.kryptonmc.krypton.entity.player.KryptonPlayer

object PlayerData {

    @JvmStatic
    fun register(registrar: DataProviderRegistrar) {
        registrar.registerMutable(Keys.FIRST_JOINED, KryptonPlayer::firstJoined)
        registrar.registerMutable(Keys.LAST_JOINED, KryptonPlayer::lastJoined)
        registrar.registerMutable(Keys.CAN_FLY, KryptonPlayer::canFly)
        registrar.registerMutable(Keys.CAN_BUILD, KryptonPlayer::canBuild)
        registrar.registerMutable(Keys.CAN_INSTANTLY_BUILD, KryptonPlayer::canInstantlyBuild)
        registrar.registerMutable(Keys.IS_FLYING, KryptonPlayer::isFlying)
        registrar.registerMutable(Keys.WALKING_SPEED, KryptonPlayer::walkingSpeed)
        registrar.registerMutable(Keys.FLYING_SPEED, KryptonPlayer::flyingSpeed)
        registrar.registerMutable(Keys.VIEW_DISTANCE, KryptonPlayer::viewDistance)
        registrar.registerMutable(Keys.CHAT_VISIBILITY, KryptonPlayer::chatVisibility)
        registrar.registerMutable(Keys.ALLOWS_LISTING, KryptonPlayer::allowsListing)
        registrar.registerMutable(Keys.IS_VANISHED, KryptonPlayer::isVanished)
        registrar.registerMutable(Keys.IS_AFK, KryptonPlayer::isAfk)
        registrar.registerMutable(Keys.GAME_MODE, KryptonPlayer::gameMode)
        registrar.registerMutable(Keys.LOCALE, KryptonPlayer::locale)
        registrar.registerMutable(Keys.FOOD_LEVEL, KryptonPlayer::foodLevel)
        registrar.registerMutable(Keys.EXHAUSTION, KryptonPlayer::exhaustion)
        registrar.registerMutable(Keys.SATURATION, KryptonPlayer::saturation)
    }
}
