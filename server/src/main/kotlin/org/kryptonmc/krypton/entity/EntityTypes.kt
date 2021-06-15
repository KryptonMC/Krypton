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
package org.kryptonmc.krypton.entity

import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.ServerStorage
import org.kryptonmc.krypton.entity.monster.KryptonZombie
import java.util.UUID

object EntityTypes {

    private val TYPE_MAP = mapOf<EntityType, (KryptonServer, UUID) -> KryptonEntity>(
        EntityType.ZOMBIE to { server, uuid -> KryptonZombie(ServerStorage.NEXT_ENTITY_ID.getAndIncrement(), server, uuid) }
    )

    fun create(type: EntityType, server: KryptonServer, uuid: UUID): KryptonEntity? = TYPE_MAP[type]?.invoke(server, uuid)
}
