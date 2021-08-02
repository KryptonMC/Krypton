/*
 * This file is part of the Krypton project, licensed under the GNU General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
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
package org.kryptonmc.krypton.command

import net.kyori.adventure.util.TriState
import org.kryptonmc.api.command.PermissionLevel
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.event.play.PermissionCheckEvent
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.player.KryptonPlayer

abstract class KryptonSender(override val server: KryptonServer) : Sender {

    override val permissions = mutableMapOf<String, Boolean>()
    override val permissionLevel = PermissionLevel.LEVEL_1

    override fun hasPermission(permission: String): Boolean {
        val event = PermissionCheckEvent(this, permission, permission in permissions || permissionLevel.hasPermission(permission))
        return when (server.eventManager.fireSync(event).result) {
            TriState.TRUE -> true
            TriState.FALSE, TriState.NOT_SET -> false
        }
    }

    override fun grant(permission: String) {
        permissions[permission] = true
        if (this is KryptonPlayer) server.commandManager.sendCommands(this)
    }

    override fun revoke(permission: String) {
        permissions[permission] = false
        if (this is KryptonPlayer) server.commandManager.sendCommands(this)
    }
}
