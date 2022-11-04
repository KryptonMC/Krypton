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

import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.util.TriState
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.function.UnaryOperator

interface BaseEntity : BaseDataHolder, NameableTeamMember, Rideable, Viewable, WaterPushable, Damageable, Interactable {

    val isRemoved: Boolean
    val maxAirTicks: Int
        get() = DEFAULT_MAX_AIR
    val isAlive: Boolean
        get() = !isRemoved

    override val world: KryptonWorld
    override val server: KryptonServer
        get() = world.server

    override fun defineData() {
        data.define(MetadataKeys.Entity.FLAGS, 0)
        data.define(MetadataKeys.Entity.AIR_TICKS, maxAirTicks)
        data.define(MetadataKeys.Entity.CUSTOM_NAME, null)
        data.define(MetadataKeys.Entity.CUSTOM_NAME_VISIBILITY, false)
        data.define(MetadataKeys.Entity.SILENT, false)
        data.define(MetadataKeys.Entity.NO_GRAVITY, false)
        data.define(MetadataKeys.Entity.POSE, Pose.STANDING)
        data.define(MetadataKeys.Entity.FROZEN_TICKS, 0)
    }

    override fun identity(): Identity = Identity.identity(uuid)

    override fun getPermissionValue(permission: String): TriState = TriState.FALSE

    override fun asHoverEvent(op: UnaryOperator<HoverEvent.ShowEntity>): HoverEvent<HoverEvent.ShowEntity> =
        HoverEvent.showEntity(op.apply(HoverEvent.ShowEntity.of(type.key(), uuid, displayName)))

    companion object {

        private const val DEFAULT_MAX_AIR = 15 * 20 // 15 seconds in ticks
    }
}
