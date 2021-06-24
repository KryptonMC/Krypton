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

import net.kyori.adventure.identity.Identity
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.HoverEvent.ShowEntity
import net.kyori.adventure.text.event.HoverEvent.showEntity
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.space.Vector
import org.kryptonmc.api.world.Location
import org.kryptonmc.krypton.KryptonServer
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.entity.metadata.EntityDataSerializers
import org.kryptonmc.krypton.entity.metadata.EntityData
import org.kryptonmc.krypton.entity.metadata.EntityDataAccessor
import java.util.Optional
import java.util.UUID
import java.util.function.UnaryOperator

@Suppress("LeakingThis")
abstract class KryptonEntity(
    val id: Int,
    server: KryptonServer,
    override val uuid: UUID,
    override val type: EntityType<out Entity>
) : KryptonSender(server), Entity {

    val data = EntityData(this)

    override var location = Location.ZERO
    override var velocity = Vector.ZERO
    override var isOnGround = true
    override var isDead = false
    override var isPersistent = false
    override var ticksLived = 0
    override val name: String
        get() = LegacyComponentSerializer.legacySection().serialize(displayName)

    open val maxAirTicks = 300

    init {
        data.define(DATA_SHARED_FLAGS_ID, 0)
        data.define(DATA_AIR_SUPPLY_ID, maxAirTicks)
        data.define(DATA_CUSTOM_NAME_VISIBLE, false)
        data.define(DATA_CUSTOM_NAME, Optional.empty())
        data.define(DATA_SILENT, false)
        data.define(DATA_NO_GRAVITY, false)
        data.define(DATA_POSE, Pose.STANDING)
        data.define(DATA_TICKS_FROZEN, 0)
        defineExtraData()
    }

    protected abstract fun defineExtraData()

    open fun onDataUpdate(accessor: EntityDataAccessor<*>) {
        // TODO: Data updating
    }

    private fun getSharedFlag(flag: Int) = data[DATA_SHARED_FLAGS_ID].toInt() and (1 shl flag) != 0

    private fun setSharedFlag(flag: Int, state: Boolean) {
        val flags = data[DATA_SHARED_FLAGS_ID]
        data[DATA_SHARED_FLAGS_ID] = if (state) {
            flags.toInt() or (1 shl flag)
        } else {
            flags.toInt() and (1 shl flag).inv()
        }.toByte()
    }

    override fun remove() = Unit // TODO: Make this do something

    override fun identity() = Identity.identity(uuid)

    override fun asHoverEvent(op: UnaryOperator<ShowEntity>) = showEntity(ShowEntity.of(type.key, uuid, displayName.takeIf { it !== Component.empty() }))

    override var isOnFire: Boolean
        get() = getSharedFlag(0)
        set(value) = setSharedFlag(0, value)

    override var isCrouching: Boolean
        get() = getSharedFlag(1)
        set(value) = setSharedFlag(1, value)

    override var isSprinting: Boolean
        get() = getSharedFlag(3)
        set(value) = setSharedFlag(3, value)

    override var isSwimming: Boolean
        get() = getSharedFlag(4)
        set(value) = setSharedFlag(4, value)

    override var isInvisible: Boolean
        get() = getSharedFlag(5)
        set(value) = setSharedFlag(5, value)

    override var isGlowing: Boolean
        get() = getSharedFlag(6)
        set(value) = setSharedFlag(6, value)

    override var isFlying: Boolean
        get() = getSharedFlag(7)
        set(value) = setSharedFlag(7, value)

    override var airSupply: Int
        get() = data[DATA_AIR_SUPPLY_ID]
        set(value) = data.set(DATA_AIR_SUPPLY_ID, value)

    override var displayName: Component
        get() = data[DATA_CUSTOM_NAME].orElse(Component.empty())
        set(value) = data.set(DATA_CUSTOM_NAME, Optional.ofNullable(value.takeIf { it != Component.empty() }))

    override var isDisplayNameVisible: Boolean
        get() = data[DATA_CUSTOM_NAME_VISIBLE]
        set(value) = data.set(DATA_CUSTOM_NAME_VISIBLE, value)

    override var isSilent: Boolean
        get() = data[DATA_SILENT]
        set(value) = data.set(DATA_SILENT, value)

    override var hasGravity: Boolean
        get() = !data[DATA_NO_GRAVITY]
        set(value) = data.set(DATA_NO_GRAVITY, !value)

    var pose: Pose
        get() = data[DATA_POSE]
        set(value) = data.set(DATA_POSE, value)

    companion object {

        @JvmStatic protected val DATA_SHARED_FLAGS_ID = EntityData.define(KryptonEntity::class.java, EntityDataSerializers.BYTE)
        private val DATA_AIR_SUPPLY_ID = EntityData.define(KryptonEntity::class.java, EntityDataSerializers.INT)
        private val DATA_CUSTOM_NAME = EntityData.define(KryptonEntity::class.java, EntityDataSerializers.OPTIONAL_COMPONENT)
        private val DATA_CUSTOM_NAME_VISIBLE = EntityData.define(KryptonEntity::class.java, EntityDataSerializers.BOOLEAN)
        private val DATA_SILENT = EntityData.define(KryptonEntity::class.java, EntityDataSerializers.BOOLEAN)
        private val DATA_NO_GRAVITY = EntityData.define(KryptonEntity::class.java, EntityDataSerializers.BOOLEAN)
        @JvmStatic protected val DATA_POSE = EntityData.define(KryptonEntity::class.java, EntityDataSerializers.POSE)
        private val DATA_TICKS_FROZEN = EntityData.define(KryptonEntity::class.java, EntityDataSerializers.INT)
    }
}
