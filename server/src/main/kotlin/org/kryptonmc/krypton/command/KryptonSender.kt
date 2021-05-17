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

import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.api.command.Sender
import org.kryptonmc.api.event.play.PermissionCheckEvent

abstract class KryptonSender(private val server: KryptonServer) : Sender {

    override val permissions = mutableMapOf<String, Boolean>()

    override fun hasPermission(permission: String): Boolean {
        val event = PermissionCheckEvent(this, permission, permission in permissions)
        server.eventBus.call(event)
        return event.result.value
    }

    override fun grant(permission: String) {
        permissions[permission] = true
    }

    override fun grant(vararg permissions: String) = permissions.forEach { grant(it) }

    override fun grant(permissions: Iterable<String>) = permissions.forEach { grant(it) }

    override fun revoke(permission: String) {
        permissions[permission] = false
    }

    override fun revoke(vararg permissions: String) = permissions.forEach { revoke(it) }

    override fun revoke(permissions: Iterable<String>) = permissions.forEach { revoke(it) }
}
