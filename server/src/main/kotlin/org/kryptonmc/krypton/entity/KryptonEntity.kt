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
package org.kryptonmc.krypton.entity

import net.kyori.adventure.identity.Identity
import net.kyori.adventure.permission.PermissionChecker
import net.kyori.adventure.pointer.Pointers
import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.world.damage.type.DamageTypes
import org.kryptonmc.krypton.entity.components.BaseEntity
import org.kryptonmc.krypton.entity.components.SerializableEntity
import org.kryptonmc.krypton.entity.system.EntityVehicleSystem
import org.kryptonmc.krypton.entity.metadata.MetadataHolder
import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.system.EntityViewingSystem
import org.kryptonmc.krypton.entity.system.EntityWaterPhysicsSystem
import org.kryptonmc.krypton.network.chat.ChatSender
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityVelocity
import org.kryptonmc.krypton.util.Maths
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource
import org.spongepowered.math.vector.Vector2f
import org.spongepowered.math.vector.Vector3d
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

@Suppress("LeakingThis")
abstract class KryptonEntity(final override var world: KryptonWorld) : BaseEntity, SerializableEntity {

    override val serializer: EntitySerializer<out KryptonEntity>
        get() = BaseEntitySerializer

    final override val id: Int = NEXT_ENTITY_ID.incrementAndGet()
    override var uuid: UUID = Maths.createInsecureUUID(Random)

    final override val data: MetadataHolder = MetadataHolder(this)
    final override val vehicleSystem: EntityVehicleSystem = EntityVehicleSystem(this)
    final override val waterPhysicsSystem: EntityWaterPhysicsSystem = EntityWaterPhysicsSystem(this)
    final override val viewingSystem: EntityViewingSystem<KryptonEntity> = EntityViewingSystem.create(this)

    var eyeHeight: Float = 0F
        private set
    final override var isRemoved: Boolean = false
        private set
    private var wasDamaged = false
    final override var location: Vector3d = Vector3d.ZERO
    final override var rotation: Vector2f = Vector2f.ZERO
    final override var velocity: Vector3d = Vector3d.ZERO
    final override var boundingBox: BoundingBox = BoundingBox.zero()
    final override var isOnGround: Boolean = true
    final override var ticksExisted: Int = 0
    final override var fireTicks: Short = 0
    final override var isInvulnerable: Boolean = false
    final override var fallDistance: Float = 0F

    protected var cachedPointers: Pointers? = null

    init {
        defineData()
    }

    fun playSound(event: SoundEvent, volume: Float, pitch: Float) {
        if (isSilent) return
        world.playSound(location.x(), location.y(), location.z(), event, soundSource(), volume, pitch)
    }

    open fun tick() {
        waterPhysicsSystem.tick()
        if (data.isDirty) viewingSystem.sendToViewers(PacketOutSetEntityMetadata(id, data.collectDirty()))
        if (wasDamaged) {
            viewingSystem.sendToViewers(PacketOutSetEntityVelocity(this))
            wasDamaged = false
        }
    }

    // TODO: Separate interaction logic
    //open fun interact(player: KryptonPlayer, hand: Hand): InteractionResult = InteractionResult.PASS

    override fun isInvulnerableTo(source: KryptonDamageSource): Boolean =
        isRemoved || isInvulnerable && source.type !== DamageTypes.VOID && !source.isCreativePlayer

    override fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        if (isInvulnerableTo(source)) return false
        markDamaged()
        return false
    }

    protected fun markDamaged() {
        wasDamaged = true
    }

    override fun remove() {
        if (isRemoved) return
        isRemoved = true
        vehicleSystem.ejectVehicle()
        vehicleSystem.ejectPassengers()
        world.removeEntity(this)
    }

    override fun pointers(): Pointers {
        if (cachedPointers == null) {
            cachedPointers = Pointers.builder()
                .withDynamic(Identity.DISPLAY_NAME, ::name)
                .withDynamic(Identity.UUID, ::uuid)
                .withStatic(PermissionChecker.POINTER, PermissionChecker(::getPermissionValue))
                .build()
        }
        return cachedPointers!!
    }

    open fun asChatSender(): ChatSender = ChatSender.SYSTEM

    override fun soundSource(): Sound.Source = Sound.Source.NEUTRAL

    companion object {

        private val NEXT_ENTITY_ID = AtomicInteger(0)
        const val BREATHING_DISTANCE_BELOW_EYES: Float = 0.11111111F
    }
}
