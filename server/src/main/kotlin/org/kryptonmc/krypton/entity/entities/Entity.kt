/*
 * This file is part of the Krypton project, licensed under the GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2021 KryptonMC and the contributors of the Krypton project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.kryptonmc.krypton.entity.entities

import net.kyori.adventure.text.Component
import org.kryptonmc.krypton.api.space.Vector
import org.kryptonmc.krypton.api.world.Location
import java.util.UUID

// TODO: Actually implement some entities
abstract class Entity {

    abstract val uuid: UUID

    abstract val name: Component?

    abstract val isCustomNameVisible: Boolean

    abstract val location: Location

    abstract val isOnGround: Boolean

    abstract val motion: Vector

    abstract val tags: List<String>

    abstract val passenger: Entity?

    abstract val airTicks: Short

    abstract val fallDistance: Float

    abstract val fireTicks: Short

    abstract val isGlowing: Boolean

    abstract val isInvulnerable: Boolean

    abstract val hasNoGravity: Boolean

    abstract val isSilent: Boolean

    abstract val portalCooldown: Int
}
