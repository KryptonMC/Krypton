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
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.util.getIfPresent
import org.kryptonmc.krypton.entity.attribute.AttributeMap
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.Attributes
import org.kryptonmc.krypton.entity.attribute.attributeSupplier
import org.kryptonmc.krypton.entity.memory.Brain
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.packet.out.play.PacketOutAttributes
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnLivingEntity
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.ListTag
import org.spongepowered.math.vector.Vector3i
import java.util.Optional

abstract class KryptonLivingEntity(
    world: KryptonWorld,
    type: EntityType<out LivingEntity>
) : KryptonEntity(world, type), LivingEntity {

    override var absorption = 0F
    override var isDead = false
    override var deathTime: Short = 0
    override var hurtTime: Short = 0
    override var isFallFlying = false
    override var lastHurtTimestamp = 0
    val attributes = AttributeMap(type.attributeSupplier)
    private val brain = Brain(mutableListOf())

    init {
        data += MetadataKeys.LIVING.FLAGS
        data += MetadataKeys.LIVING.HEALTH
        data += MetadataKeys.LIVING.POTION_EFFECT_COLOR
        data += MetadataKeys.LIVING.POTION_EFFECT_AMBIENCE
        data += MetadataKeys.LIVING.ARROWS
        data += MetadataKeys.LIVING.STINGERS
        data += MetadataKeys.LIVING.BED_LOCATION
    }

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

        // Sleeping coordinates
        if (tag.contains("SleepingX", 99) && tag.contains("SleepingY", 99) && tag.contains("SleepingZ", 99)) {
            sleepingPosition = Vector3i(tag.getInt("SleepingX"), tag.getInt("SleepingY"), tag.getInt("SleepingZ"))
            pose = Pose.SLEEPING
        }
    }

    override fun save() = super.save().apply {
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
        player.session.sendPacket(PacketOutAttributes(id, attributes.syncable))
        return true
    }

    override fun getSpawnPacket() = PacketOutSpawnLivingEntity(this)

    private fun setLivingFlag(flag: Int, state: Boolean) {
        val flags = data[MetadataKeys.LIVING.FLAGS].toInt()
        data[MetadataKeys.LIVING.FLAGS] = (if (state) flags or flag else flags and flag.inv()).toByte()
    }

    protected fun removeEffectParticles() {
        isPotionEffectAmbient = false
        potionEffectColor = 0
    }

    override var isUsingItem: Boolean
        get() = data[MetadataKeys.LIVING.FLAGS].toInt() and 1 > 0
        set(value) = setLivingFlag(1, value)

    override var hand: Hand
        get() = if (data[MetadataKeys.LIVING.FLAGS].toInt() and 2 > 0) Hand.OFF else Hand.MAIN
        set(value) = setLivingFlag(2, value == Hand.OFF)

    override var isInRiptideSpinAttack: Boolean
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

    override var sleepingPosition: Vector3i?
        get() = data[MetadataKeys.LIVING.BED_LOCATION].getIfPresent()
        set(value) = data.set(MetadataKeys.LIVING.BED_LOCATION, Optional.ofNullable(value))

    companion object {

        fun createAttributes() = AttributeSupplier.builder()
            .add(Attributes.MAX_HEALTH)
            .add(Attributes.KNOCKBACK_RESISTANCE)
            .add(Attributes.MOVEMENT_SPEED)
            .add(Attributes.ARMOR)
            .add(Attributes.ARMOR_TOUGHNESS)
    }
}
