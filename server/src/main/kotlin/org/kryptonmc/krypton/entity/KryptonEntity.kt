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
import org.kryptonmc.krypton.coordinate.SectionPos
import org.kryptonmc.krypton.entity.components.BaseEntity
import org.kryptonmc.krypton.entity.components.SerializableEntity
import org.kryptonmc.krypton.entity.system.EntityVehicleSystem
import org.kryptonmc.krypton.entity.metadata.MetadataHolder
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.serializer.BaseEntitySerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.system.EntityWaterPhysicsSystem
import org.kryptonmc.krypton.entity.tracking.EntityTracker
import org.kryptonmc.krypton.entity.tracking.EntityTypeTarget
import org.kryptonmc.krypton.entity.tracking.EntityViewCallback
import org.kryptonmc.krypton.entity.tracking.EntityViewingEngine
import org.kryptonmc.krypton.event.player.KryptonEntityEnterViewEvent
import org.kryptonmc.krypton.event.player.KryptonEntityExitViewEvent
import org.kryptonmc.krypton.network.PacketGrouping
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutRemoveEntities
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityMetadata
import org.kryptonmc.krypton.packet.out.play.PacketOutSetEntityVelocity
import org.kryptonmc.krypton.packet.out.play.PacketOutSetHeadRotation
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnEntity
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

    val random: RandomSource = RandomSource.create()
    final override val id: Int = NEXT_ENTITY_ID.incrementAndGet()
    override var uuid: UUID = Maths.createInsecureUUID(random)

    final override val data: MetadataHolder = MetadataHolder(this)
    final override val vehicleSystem: EntityVehicleSystem = EntityVehicleSystem(this)
    final override val waterPhysicsSystem: EntityWaterPhysicsSystem = EntityWaterPhysicsSystem(this)

    val viewingEngine: EntityViewingEngine = EntityViewingEngine(this)
    val trackingTarget: EntityTypeTarget<KryptonEntity> = if (this is KryptonPlayer) EntityTypeTarget.PLAYERS else EntityTypeTarget.ENTITIES
    val trackingViewCallback: EntityViewCallback<KryptonEntity> = object : EntityViewCallback<KryptonEntity> {
        override fun add(entity: KryptonEntity) {
            viewingEngine.handleEnterView(entity)
        }

        override fun remove(entity: KryptonEntity) {
            viewingEngine.handleExitView(entity)
        }

        override fun referenceUpdate(position: Position, tracker: EntityTracker) {
            viewingEngine.updateTracker(world, position)
        }
    }

    var eyeHeight: Float = 0F
        private set
    private var removed = false
    final override var position: Position = Position.ZERO
    final override var velocity: Vec3d = Vec3d.ZERO
        set(value) {
            field = value
            velocityNeedsUpdate = true
        }
    private var velocityNeedsUpdate = false
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
        // Tick the scheduler here so that we can schedule things for the next tick within the tick.
        scheduler.process()

        // Run the main tick
        tick()

        // Do things that we need to do after the tick
        postTick()
    }

    open fun tick() {
        ticksExisted++
        waterPhysicsSystem.tick()
    }

    // This is for certain things that need to always be sent after the tick is finished, like update packets.
    protected open fun postTick() {
        if (data.isDirty()) sendPacketToViewersAndSelf(PacketOutSetEntityMetadata(id, data.collectDirty()))
        if (velocityNeedsUpdate) {
            sendPacketToViewers(PacketOutSetEntityVelocity.fromEntity(this))
            velocityNeedsUpdate = false
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
            // The update packets can only handle a maximum of 8 blocks in any direction due to the way they
            // are designed, and so, if an entity moves more than 8 blocks, we need to teleport them.
            sendPositionUpdate(PacketOutTeleportEntity.create(this), old, new)
            return
        } else if (positionChange && rotationChange) {
            sendPositionUpdate(PacketOutUpdateEntityPositionAndRotation.create(id, old, new, isOnGround), old, new)
            sendPacketToViewers(PacketOutSetHeadRotation(id, new.yaw))
        } else if (positionChange) {
            sendPositionUpdate(PacketOutUpdateEntityPosition.create(id, old, new, isOnGround), old, new)
        } else if (rotationChange) {
            sendPacketToViewers(PacketOutUpdateEntityRotation(id, new.yaw, new.pitch, isOnGround))
            sendPacketToViewers(PacketOutSetHeadRotation(id, new.yaw))
        }
    }

    protected open fun sendPositionUpdate(packet: Packet, old: Position, new: Position) {
        sendPacketToViewers(packet)
        world.entityTracker.onMove(this, new, trackingTarget, trackingViewCallback)

        val oldChunkX = SectionPos.blockToSection(old.x)
        val oldChunkZ = SectionPos.blockToSection(old.z)
        val newChunkX = SectionPos.blockToSection(new.x)
        val newChunkZ = SectionPos.blockToSection(new.z)
        if (oldChunkX != newChunkX || oldChunkZ != newChunkZ) {
            if (this is KryptonPlayer) updateChunks()
            world.chunkManager.updateEntityPosition(this, ChunkPos(newChunkX, newChunkZ))
        }
    }

    // TODO: Separate interaction logic
    //open fun interact(player: KryptonPlayer, hand: Hand): InteractionResult = InteractionResult.PASS

    override fun isInvulnerableTo(source: KryptonDamageSource): Boolean {
        return isRemoved() || isInvulnerable && source.type !== DamageTypes.VOID && !source.isCreativePlayer()
    }

    override fun damage(source: KryptonDamageSource, damage: Float): Boolean {
        if (isInvulnerableTo(source)) return false
        markDamaged()
        return false
    }

    protected fun markDamaged() {
        velocityNeedsUpdate = true
    }

    override fun remove() {
        if (removed) return
        removed = true
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

    open fun showToViewer(viewer: KryptonPlayer) {
        server.eventNode.fire(KryptonEntityEnterViewEvent(viewer, this))

        viewer.connection.send(getSpawnPacket())
        if (velocity.lengthSquared() > 0.001) viewer.connection.send(PacketOutSetEntityVelocity.fromEntity(this))
        viewer.connection.send(PacketOutSetEntityMetadata(id, data.collectAll()))
    }

    open fun hideFromViewer(viewer: KryptonPlayer) {
        server.eventNode.fire(KryptonEntityExitViewEvent(viewer, this))
        viewer.connection.send(PacketOutRemoveEntities.removeSingle(this))
    }

    fun sendPacketToViewers(packet: Packet) {
        PacketGrouping.sendGroupedPacket(viewingEngine.viewers(), packet)
    }

    protected open fun sendPacketToViewersAndSelf(packet: Packet) {
        sendPacketToViewers(packet)
    }

    protected open fun getSpawnPacket(): Packet = PacketOutSpawnEntity.create(this)

    override fun isRemoved(): Boolean = removed

    companion object {

        private val NEXT_ENTITY_ID = AtomicInteger(0)
        const val BREATHING_DISTANCE_BELOW_EYES: Float = 0.11111111F
    }
}
