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
package org.kryptonmc.krypton.entity.components

import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.entity.player.Abilities

/**
 * A delegate that moves the API implementations for abilities out of the main
 * KryptonPlayer class to make it less cluttered.
 *
 * Note that, due to the way invulnerability works, it does not have an
 * overriding delegate here to abilities.
 */
interface AbilitiesDelegate : Player {

    val abilities: Abilities

    override var isFlying: Boolean
        get() = abilities.flying
        set(value) {
            abilities.flying = value
            onAbilitiesUpdate()
        }
    override var canFly: Boolean
        get() = abilities.canFly
        set(value) {
            abilities.canFly = value
            onAbilitiesUpdate()
        }
    override var canInstantlyBuild: Boolean
        get() = abilities.canInstantlyBuild
        set(value) {
            abilities.canInstantlyBuild = value
            onAbilitiesUpdate()
        }
    override var canBuild: Boolean
        get() = abilities.canBuild
        set(value) {
            abilities.canBuild = value
            onAbilitiesUpdate()
        }
    override var walkingSpeed: Float
        get() = abilities.walkingSpeed
        set(value) {
            abilities.walkingSpeed = value
            onAbilitiesUpdate()
        }
    override var flyingSpeed: Float
        get() = abilities.flyingSpeed
        set(value) {
            abilities.flyingSpeed = value
            onAbilitiesUpdate()
        }

    fun onAbilitiesUpdate()
}
