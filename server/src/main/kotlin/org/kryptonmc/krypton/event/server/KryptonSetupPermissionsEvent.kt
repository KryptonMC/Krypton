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
package org.kryptonmc.krypton.event.server

import org.kryptonmc.api.event.server.SetupPermissionsEvent
import org.kryptonmc.api.permission.PermissionFunction
import org.kryptonmc.api.permission.PermissionProvider
import org.kryptonmc.api.permission.Subject

class KryptonSetupPermissionsEvent(override val subject: Subject, private val defaultProvider: PermissionProvider) : SetupPermissionsEvent {

    override var provider: PermissionProvider = defaultProvider

    override fun resetProvider() {
        provider = defaultProvider
    }

    override fun createFunction(subject: Subject): PermissionFunction = provider.createFunction(subject)
}
