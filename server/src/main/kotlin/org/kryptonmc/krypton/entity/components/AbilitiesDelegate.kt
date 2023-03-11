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

import org.kryptonmc.api.entity.player.Player
import org.kryptonmc.krypton.entity.player.Abilities
import org.kryptonmc.krypton.packet.out.play.PacketOutAbilities

/**
 * A delegate that moves the API implementations for abilities out of the main
 * KryptonPlayer class to make it less cluttered.
 *
 * Note that, due to the way invulnerability works, it does not have an
 * overriding delegate here to abilities.
 */
interface AbilitiesDelegate : NetworkPlayer, Player {

    val abilities: Abilities

    override var isFlying: Boolean
        get() = abilities.flying
        set(value) {
            abilities.flying = value
            updateAbilities()
        }
    override var canFly: Boolean
        get() = abilities.canFly
        set(value) {
            abilities.canFly = value
            updateAbilities()
        }
    override var canInstantlyBuild: Boolean
        get() = abilities.canInstantlyBuild
        set(value) {
            abilities.canInstantlyBuild = value
            updateAbilities()
        }
    override var canBuild: Boolean
        get() = abilities.canBuild
        set(value) {
            abilities.canBuild = value
            updateAbilities()
        }
    override var walkingSpeed: Float
        get() = abilities.walkingSpeed
        set(value) {
            abilities.walkingSpeed = value
            updateAbilities()
        }
    override var flyingSpeed: Float
        get() = abilities.flyingSpeed
        set(value) {
            abilities.flyingSpeed = value
            updateAbilities()
        }

    private fun updateAbilities() {
        connection.send(PacketOutAbilities.create(abilities))
    }
}
