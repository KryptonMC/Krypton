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
        get() = name
    override val facing: Direction
        get() = Directions.ofPitch(pitch.toDouble())

    override fun getPermissionValue(permission: String): TriState = permissionFunction.getPermissionValue(permission)
}
