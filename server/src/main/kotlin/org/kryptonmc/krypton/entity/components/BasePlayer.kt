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

import net.kyori.adventure.text.Component
import net.kyori.adventure.util.TriState
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.api.util.Direction
import org.kryptonmc.krypton.util.enumhelper.Directions
import org.kryptonmc.krypton.world.scoreboard.KryptonScoreboard

interface BasePlayer : BaseEntity, KryptonEquipable, HungerDelegate, AbilitiesDelegate, PlayerAudience, Glider, GameModePlayer {

    val permissionFunction: PermissionFunction

    override val scoreboard: KryptonScoreboard
        get() = world.scoreboard
    override val teamRepresentation: Component
        get() = Component.text(name)
    override val facing: Direction
        get() = Directions.ofPitch(position.pitch.toDouble())

    override fun getPermissionValue(permission: String): TriState = permissionFunction.getPermissionValue(permission)
}
