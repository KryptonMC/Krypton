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

import org.kryptonmc.api.adventure.toPlainText
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.entity.attribute.Attribute
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.entity.attribute.AttributeTypes
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.krypton.entity.attribute.AttributeMap
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.memory.Brain
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutAttributes
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnLivingEntity
import org.kryptonmc.krypton.util.getIfPresent
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.kryptonmc.nbt.StringTag
import org.spongepowered.math.vector.Vector3i
import java.util.Optional
import kotlin.random.Random

abstract class KryptonLivingEntity(
    world: KryptonWorld,
    type: EntityType<out LivingEntity>,
    attributeSupplier: AttributeSupplier
) : KryptonEntity(world, type), LivingEntity {

    override var absorption = 0F
    final override val isAlive: Boolean
        get() = super.isAlive && health > 0F
    final override var isDead = false
    final override var deathTime: Short = 0
    final override var hurtTime: Short = 0
    final override var isFallFlying = false
    final override var lastHurtTimestamp = 0
    override var isBaby = false
    private var tickCount = 0
    val attributes = AttributeMap(attributeSupplier)
    open val brain: Brain<*> = Brain<KryptonLivingEntity>()

    var lastHurtByMob: KryptonLivingEntity? = null
        set(value) {
            field = value
            lastHurtByMobTime = tickCount
        }
    private var lastHurtByMobTime = 0
    var lastHurtByPlayer: KryptonPlayer? = null
        set(value) {
            field = value
            lastHurtByPlayerTime = tickCount
        }
    private var lastHurtByPlayerTime = 0

    open val canBeSeenAsEnemy: Boolean
        get() = !isInvulnerable && canBeSeenByAnyone
    open val canBeSeenByAnyone: Boolean
        get() = !isSpectator && isAlive
    open val soundVolume: Float
        get() = 1F
    open val voicePitch: Float
        get() {
            val babyFactor = if (isBaby) 1.5F else 1F
            return (Random.nextFloat() - Random.nextFloat()) * 0.2F + babyFactor
        }

    init {
        data.add(MetadataKeys.LIVING.FLAGS)
        data.add(MetadataKeys.LIVING.HEALTH)
        data.add(MetadataKeys.LIVING.POTION_EFFECT_COLOR)
        data.add(MetadataKeys.LIVING.POTION_EFFECT_AMBIENCE)
        data.add(MetadataKeys.LIVING.ARROWS)
        data.add(MetadataKeys.LIVING.STINGERS)
        data.add(MetadataKeys.LIVING.BED_LOCATION)
    }

    fun canAttack(target: KryptonLivingEntity): Boolean {
        if (target is KryptonPlayer && world.difficulty === Difficulty.PEACEFUL) return false
        return target.canBeSeenAsEnemy
    }

    override fun attribute(type: AttributeType): Attribute? = attributes[type]

    override fun load(tag: CompoundTag) {
        super.load(tag)
        // AI stuff
        if (tag.contains("Attributes", ListTag.ID)) attributes.load(tag.getList("Attributes", CompoundTag.ID))
        if (tag.contains("Brain", CompoundTag.ID)) brain.load(tag.getCompound("Brain"))

        // Values
        absorption = tag.getFloat("AbsorptionAmount")
        deathTime = tag.getShort("DeathTime")
        isFallFlying = tag.getBoolean("FallFlying")
        health = tag.getFloat("Health")
        lastHurtTimestamp = tag.getInt("HurtByTimestamp")
        hurtTime = tag.getShort("HurtTime")

        // Scoreboard stuff
        if (tag.contains("Team", StringTag.ID)) {
            val teamName = tag.getString("Team")
            val team = world.scoreboard.team(teamName)
            val wasAdded = team != null && world.scoreboard.addMemberToTeam(teamRepresentation, team)
            if (!wasAdded) LOGGER.warn("Unable to add living entity ${name.toPlainText()} to team ${teamName}. This team may not exist.")
        }

        // Sleeping coordinates
        if (
            tag.contains("SleepingX", 99) &&
            tag.contains("SleepingY", 99) &&
            tag.contains("SleepingZ", 99)
        ) {
            sleepingPosition = Vector3i(
                tag.getInt("SleepingX"),
                tag.getInt("SleepingY"),
                tag.getInt("SleepingZ")
            )
            pose = Pose.SLEEPING
        }
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        float("AbsorptionAmount", absorption)
        put("Attributes", attributes.save())
        put("Brain", brain.save())
        short("DeathTime", deathTime)
        boolean("FallFlying", isFallFlying)
        float("Health", health)
        int("HurtByTimestamp", lastHurtTimestamp)
        short("HurtTime", hurtTime)
        sleepingPosition?.let {
            int("SleepingX", it.x())
            int("SleepingY", it.y())
            int("SleepingZ", it.z())
        }
    }

    override fun addViewer(player: KryptonPlayer): Boolean {
        if (!super.addViewer(player)) return false
        player.session.send(PacketOutAttributes(id, attributes.syncable))
        return true
    }

    override fun getSpawnPacket(): Packet = PacketOutSpawnLivingEntity(this)

    private fun setLivingFlag(flag: Int, state: Boolean) {
        val flags = data[MetadataKeys.LIVING.FLAGS].toInt()
        data[MetadataKeys.LIVING.FLAGS] = (if (state) flags or flag else flags and flag.inv()).toByte()
    }

    protected fun removeEffectParticles() {
        isPotionEffectAmbient = false
        potionEffectColor = 0
    }

    override fun tryRide(entity: Entity) {
        if (!isAlive) return
        super.tryRide(entity)
    }

    final override var isUsingItem: Boolean
        get() = data[MetadataKeys.LIVING.FLAGS].toInt() and 1 > 0
        set(value) = setLivingFlag(1, value)
    final override var hand: Hand
        get() = if (data[MetadataKeys.LIVING.FLAGS].toInt() and 2 > 0) Hand.OFF else Hand.MAIN
        set(value) = setLivingFlag(2, value == Hand.OFF)
    final override var isInRiptideSpinAttack: Boolean
        get() = data[MetadataKeys.LIVING.FLAGS].toInt() and 4 > 0
        set(value) = setLivingFlag(4, value)
    override var health: Float
        get() = data[MetadataKeys.LIVING.HEALTH]
        set(value) = data.set(MetadataKeys.LIVING.HEALTH, value)
    var potionEffectColor: Int
        get() = data[MetadataKeys.LIVING.POTION_EFFECT_COLOR]
        set(value) = data.set(MetadataKeys.LIVING.POTION_EFFECT_COLOR, value)
    var isPotionEffectAmbient: Boolean
        get() = data[MetadataKeys.LIVING.POTION_EFFECT_AMBIENCE]
        set(value) = data.set(MetadataKeys.LIVING.POTION_EFFECT_AMBIENCE, value)
    var arrowCount: Int
        get() = data[MetadataKeys.LIVING.ARROWS]
        set(value) = data.set(MetadataKeys.LIVING.ARROWS, value)
    var stingerCount: Int
        get() = data[MetadataKeys.LIVING.STINGERS]
        set(value) = data.set(MetadataKeys.LIVING.STINGERS, value)
    final override var sleepingPosition: Vector3i?
        get() = data[MetadataKeys.LIVING.BED_LOCATION].getIfPresent()
        set(value) = data.set(MetadataKeys.LIVING.BED_LOCATION, Optional.ofNullable(value))

    companion object {

        @JvmField
        val ATTRIBUTES = attributes().build()

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = AttributeSupplier.builder()
            .add(AttributeTypes.MAX_HEALTH)
            .add(AttributeTypes.KNOCKBACK_RESISTANCE)
            .add(AttributeTypes.MOVEMENT_SPEED)
            .add(AttributeTypes.ARMOR)
            .add(AttributeTypes.ARMOR_TOUGHNESS)
    }
}
