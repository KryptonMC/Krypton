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
import net.kyori.adventure.text.Component
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.util.BoundingBox
import org.kryptonmc.api.util.Position
import org.kryptonmc.api.util.Vec3d
import org.kryptonmc.api.world.damage.type.DamageTypes
import org.kryptonmc.krypton.command.CommandSourceStack
import org.kryptonmc.krypton.command.KryptonSender
import org.kryptonmc.krypton.coordinate.ChunkPos
import org.kryptonmc.krypton.coordinate.Positioning
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.entity.components.BaseEntity
import org.kryptonmc.krypton.entity.components.SerializableEntity
import org.kryptonmc.krypton.entity.system.EntityVehicleSystem
import org.kryptonmc.krypton.entity.metadata.MetadataHolder
import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.system.EntityViewingSystem
import org.kryptonmc.krypton.entity.system.EntityWaterPhysicsSystem
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityVelocity
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeadRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutTeleportEntity
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPosition
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityPositionAndRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutUpdateEntityRotation
import org.kryptonmc.krypton.util.math.Maths
import org.kryptonmc.krypton.scheduling.KryptonScheduler
import org.kryptonmc.krypton.ticking.Tickable
import org.kryptonmc.krypton.util.random.RandomSource
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.damage.KryptonDamageSource
import org.kryptonmc.krypton.world.rule.GameRuleKeys
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

@Suppress("LeakingThis")
abstract class KryptonEntity(final override var world: KryptonWorld) : BaseEntity, SerializableEntity, KryptonSender, Tickable {

    override val serializer: EntitySerializer<out KryptonEntity>
        get() = BaseEntitySerializer

    protected val random = RandomSource.create()
    final override val id: Int = NEXT_ENTITY_ID.incrementAndGet()
    override var uuid: UUID = Maths.createInsecureUUID(random)

    final override val data: MetadataHolder = MetadataHolder(this)
    final override val vehicleSystem: EntityVehicleSystem = EntityVehicleSystem(this)
    final override val waterPhysicsSystem: EntityWaterPhysicsSystem = EntityWaterPhysicsSystem(this)
    final override val viewingSystem: EntityViewingSystem<KryptonEntity> = EntityViewingSystem.create(this)

    var eyeHeight: Float = 0F
        private set
    final override var isRemoved: Boolean = false
        private set
    private var wasDamaged = false
    final override var position: Position = Position.ZERO
    final override var velocity: Vec3d = Vec3d.ZERO
    final override var boundingBox: BoundingBox = BoundingBox.ZERO
    final override var isOnGround: Boolean = true
    final override var ticksExisted: Int = 0
    final override var remainingFireTicks: Int = 0
    final override var isInvulnerable: Boolean = false
    final override var fallDistance: Float = 0F

    protected var cachedPointers: Pointers? = null

    override val scheduler: KryptonScheduler = KryptonScheduler()

    init {
        defineData()
    }

    fun playSound(event: SoundEvent, volume: Float, pitch: Float) {
        if (isSilent) return
        world.playSound(position.x, position.y, position.z, event, soundSource(), volume, pitch)
    }

    override fun tick(time: Long) {
        // We don't need the time for any of the entity ticking logic for now.
        tick()
    }

    open fun tick() {
        ticksExisted++
        waterPhysicsSystem.tick()
        scheduler.process()
        if (data.isDirty()) viewingSystem.sendToViewers(PacketOutSetEntityMetadata(id, data.collectDirty()))
        if (wasDamaged) {
            viewingSystem.sendToViewers(PacketOutSetEntityVelocity.fromEntity(this))
            wasDamaged = false
        }
    }

    final override fun teleport(position: Position) {
        val old = this.position
        this.position = position
        updatePosition(old, position)
    }

    private fun updatePosition(old: Position, new: Position) {
        val dx = abs(new.x - old.x)
        val dy = abs(new.y - old.y)
        val dz = abs(new.z - old.z)
        val positionChange = dx != 0.0 || dy != 0.0 || dz != 0.0
        val rotationChange = new.yaw != old.yaw || new.pitch != old.pitch

        if (dx > 8 || dy > 8 || dz > 8) {
            sendPositionUpdate(PacketOutTeleportEntity.create(this), old, new)
            return
        } else if (positionChange && rotationChange) {
            sendPositionUpdate(PacketOutUpdateEntityPositionAndRotation.create(id, old, new, isOnGround), old, new)
            viewingSystem.sendToViewers(PacketOutSetHeadRotation(id, new.yaw))
        } else if (positionChange) {
            sendPositionUpdate(PacketOutUpdateEntityPosition.create(id, old, new, isOnGround), old, new)
        } else if (rotationChange) {
            viewingSystem.sendToViewers(PacketOutUpdateEntityRotation(id, new.yaw, new.pitch, isOnGround))
            viewingSystem.sendToViewers(PacketOutSetHeadRotation(id, new.yaw))
        }
    }

    protected open fun sendPositionUpdate(packet: Packet, old: Position, new: Position) {
        viewingSystem.sendToViewers(packet)

        val oldChunkX = SectionPos.blockToSection(old.x)
        val oldChunkZ = SectionPos.blockToSection(old.z)
        val newChunkX = SectionPos.blockToSection(new.x)
        val newChunkZ = SectionPos.blockToSection(new.z)
        if (oldChunkX != newChunkX || oldChunkZ != newChunkZ) {
            world.chunkManager.updateEntityPosition(this, ChunkPos(newChunkX, newChunkZ))
        }
    }

    // TODO: Separate interaction logic
    //open fun interact(player: KryptonPlayer, hand: Hand): InteractionResult = InteractionResult.PASS

    override fun isInvulnerableTo(source: KryptonDamageSource): Boolean =
        isRemoved || isInvulnerable && source.type !== DamageTypes.VOID && !source.isCreativePlayer()

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
                .withDynamic(Identity.DISPLAY_NAME) { nameOrDescription() }
                .withDynamic(Identity.UUID) { uuid }
                .withStatic(PermissionChecker.POINTER, PermissionChecker { getPermissionValue(it) })
                .build()
        }
        return cachedPointers!!
    }

    override fun soundSource(): Sound.Source = Sound.Source.NEUTRAL

    override fun sendSystemMessage(message: Component) {
        // Do nothing by default
    }

    override fun acceptsSuccess(): Boolean = world.gameRules().getBoolean(GameRuleKeys.SEND_COMMAND_FEEDBACK)

    override fun acceptsFailure(): Boolean = true

    override fun shouldInformAdmins(): Boolean = true

    final override fun createCommandSourceStack(): CommandSourceStack {
        return CommandSourceStack(this, position, world, name, displayName, server, this)
    }

    open fun headYaw(): Float = 0F

    companion object {

        private val NEXT_ENTITY_ID = AtomicInteger(0)
        const val BREATHING_DISTANCE_BELOW_EYES: Float = 0.11111111F
    }
}
