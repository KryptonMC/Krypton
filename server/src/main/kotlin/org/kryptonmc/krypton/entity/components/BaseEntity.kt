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

import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.util.TriState
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.function.UnaryOperator

interface BaseEntity : BaseDataHolder, NameableTeamMember, Rideable, WaterPushable, Damageable, Interactable {

    override val world: KryptonWorld
    override val server: KryptonServer
        get() = world.server

    fun isRemoved(): Boolean

    fun maxAirTicks(): Int = DEFAULT_MAX_AIR

    fun isAlive(): Boolean = !isRemoved()

    override fun defineData() {
        data.define(MetadataKeys.Entity.FLAGS, 0)
        data.define(MetadataKeys.Entity.AIR_TICKS, maxAirTicks())
        data.define(MetadataKeys.Entity.CUSTOM_NAME, null)
        data.define(MetadataKeys.Entity.CUSTOM_NAME_VISIBILITY, false)
        data.define(MetadataKeys.Entity.SILENT, false)
        data.define(MetadataKeys.Entity.NO_GRAVITY, false)
        data.define(MetadataKeys.Entity.POSE, Pose.STANDING)
        data.define(MetadataKeys.Entity.FROZEN_TICKS, 0)
    }

    override fun identity(): Identity = Identity.identity(uuid)

    override fun getPermissionValue(permission: String): TriState = TriState.FALSE

    override fun asHoverEvent(op: UnaryOperator<HoverEvent.ShowEntity>): HoverEvent<HoverEvent.ShowEntity> {
        return HoverEvent.showEntity(op.apply(HoverEvent.ShowEntity.of(type.key(), uuid, nameOrDescription())))
    }

    companion object {

        private const val DEFAULT_MAX_AIR = 15 * 20 // 15 seconds in ticks
    }
}
