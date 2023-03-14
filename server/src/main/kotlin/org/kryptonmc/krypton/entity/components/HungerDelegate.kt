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
import org.kryptonmc.krypton.entity.system.PlayerHungerSystem
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHealth

interface HungerDelegate : Player, NetworkPlayer {

    val hungerSystem: PlayerHungerSystem

    override var foodLevel: Int
        get() = hungerSystem.foodLevel
        set(value) {
            hungerSystem.foodLevel = value
            connection.send(PacketOutSetHealth(health, value, foodSaturationLevel))
        }
    override var foodExhaustionLevel: Float
        get() = hungerSystem.exhaustionLevel
        set(value) {
            hungerSystem.exhaustionLevel = value
        }
    override var foodSaturationLevel: Float
        get() = hungerSystem.saturationLevel
        set(value) {
            hungerSystem.saturationLevel = value
            connection.send(PacketOutSetHealth(health, foodLevel, value))
        }
}
