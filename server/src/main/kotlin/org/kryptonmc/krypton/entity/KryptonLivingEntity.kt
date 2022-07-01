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

import org.kryptonmc.api.entity.ArmorSlot
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.EquipmentSlot
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
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.packet.Packet
import org.kryptonmc.krypton.packet.out.play.PacketOutAttributes
import org.kryptonmc.krypton.packet.out.play.PacketOutSpawnLivingEntity
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3i
import kotlin.random.Random

abstract class KryptonLivingEntity(
    world: KryptonWorld,
    type: EntityType<out LivingEntity>,
    attributeSupplier: AttributeSupplier
) : KryptonEntity(world, type), LivingEntity, KryptonEquipable {

    final override val maxHealth: Float
        get() = attributes.value(AttributeTypes.MAX_HEALTH).toFloat()
    override var absorption: Float = 0F
    final override val isAlive: Boolean
        get() = super.isAlive && health > 0F
    final override var isDead: Boolean = false
    final override var deathTime: Short = 0
    final override var hurtTime: Short = 0
    final override var lastHurtTimestamp: Int = 0
    override var isBaby: Boolean = false
    private var tickCount = 0
    val attributes: AttributeMap = AttributeMap(attributeSupplier)
    open val brain: Brain<*> = Brain<KryptonLivingEntity>()

    val killer: KryptonLivingEntity?
        get() {
            // TODO: Check combat tracker here
            if (lastHurtByPlayer != null) return lastHurtByPlayer
            if (lastHurtByMob != null) return lastHurtByMob
            return null
        }
    @Suppress("MemberVisibilityCanBePrivate")
    var lastHurtByMob: KryptonLivingEntity? = null
        set(value) {
            field = value
            lastHurtByMobTime = tickCount
        }
    private var lastHurtByMobTime = 0
    @Suppress("MemberVisibilityCanBePrivate")
    var lastHurtByPlayer: KryptonPlayer? = null
        set(value) {
            field = value
            lastHurtByPlayerTime = tickCount
        }
    private var lastHurtByPlayerTime = 0

    abstract override val armorSlots: Iterable<KryptonItemStack>
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

    final override var isUsingItem: Boolean
        get() = getLivingFlag(0)
        set(value) = setLivingFlag(0, value)
    final override var hand: Hand
        get() = if (getLivingFlag(1)) Hand.OFF else Hand.MAIN
        set(value) = setLivingFlag(1, value == Hand.OFF)
    final override var isInRiptideSpinAttack: Boolean
        get() = getLivingFlag(2)
        set(value) = setLivingFlag(2, value)
    override var health: Float
        get() = data[MetadataKeys.LIVING.HEALTH]
        set(value) = data.set(MetadataKeys.LIVING.HEALTH, value)
    private var potionEffectColor: Int
        get() = data[MetadataKeys.LIVING.POTION_EFFECT_COLOR]
        set(value) = data.set(MetadataKeys.LIVING.POTION_EFFECT_COLOR, value)
    private var isPotionEffectAmbient: Boolean
        get() = data[MetadataKeys.LIVING.POTION_EFFECT_AMBIENCE]
        set(value) = data.set(MetadataKeys.LIVING.POTION_EFFECT_AMBIENCE, value)
    var arrowCount: Int
        get() = data[MetadataKeys.LIVING.ARROWS]
        set(value) = data.set(MetadataKeys.LIVING.ARROWS, value)
    var stingerCount: Int
        get() = data[MetadataKeys.LIVING.STINGERS]
        set(value) = data.set(MetadataKeys.LIVING.STINGERS, value)
    final override var sleepingPosition: Vector3i?
        get() = data[MetadataKeys.LIVING.BED_LOCATION]
        set(value) = data.set(MetadataKeys.LIVING.BED_LOCATION, value)

    init {
        data.add(MetadataKeys.LIVING.FLAGS)
        data.add(MetadataKeys.LIVING.HEALTH)
        data.add(MetadataKeys.LIVING.POTION_EFFECT_COLOR)
        data.add(MetadataKeys.LIVING.POTION_EFFECT_AMBIENCE)
        data.add(MetadataKeys.LIVING.ARROWS)
        data.add(MetadataKeys.LIVING.STINGERS)
        data.add(MetadataKeys.LIVING.BED_LOCATION)
        data[MetadataKeys.LIVING.HEALTH] = maxHealth
    }

    abstract override fun equipment(slot: EquipmentSlot): KryptonItemStack

    override fun heldItem(hand: Hand): KryptonItemStack {
        if (hand == Hand.MAIN) return equipment(EquipmentSlot.MAIN_HAND)
        if (hand == Hand.OFF) return equipment(EquipmentSlot.OFF_HAND)
        error("Tried to get held item for hand $hand that should not exist! Please sure that no plugins are injecting entries in to enums!")
    }

    override fun setHeldItem(hand: Hand, item: KryptonItemStack) {
        when (hand) {
            Hand.MAIN -> setEquipment(EquipmentSlot.MAIN_HAND, item)
            Hand.OFF -> setEquipment(EquipmentSlot.OFF_HAND, item)
            else -> error("Tried to set held item for hand $hand that should not exist! Please sure that no plugins are injecting " +
                    "entries in to enums!")
        }
    }

    override fun armor(slot: ArmorSlot): KryptonItemStack = equipment(EquipmentSlots.fromArmorSlot(slot))

    override fun setArmor(slot: ArmorSlot, item: KryptonItemStack) {
        setEquipment(EquipmentSlots.fromArmorSlot(slot), item)
    }

    fun canAttack(target: KryptonLivingEntity): Boolean {
        if (target is KryptonPlayer && world.difficulty === Difficulty.PEACEFUL) return false
        return target.canBeSeenAsEnemy
    }

    override fun attribute(type: AttributeType): Attribute? = attributes[type]

    override fun addViewer(player: KryptonPlayer): Boolean {
        if (!super.addViewer(player)) return false
        player.session.send(PacketOutAttributes(id, attributes.syncable))
        return true
    }

    override fun getSpawnPacket(): Packet = PacketOutSpawnLivingEntity(this)

    private fun getLivingFlag(flag: Int): Boolean = getFlag(MetadataKeys.LIVING.FLAGS, flag)

    private fun setLivingFlag(flag: Int, state: Boolean) {
        setFlag(MetadataKeys.LIVING.FLAGS, flag, state)
    }

    protected fun removeEffectParticles() {
        isPotionEffectAmbient = false
        potionEffectColor = 0
    }

    override fun tryRide(entity: Entity) {
        if (!isAlive) return
        super.tryRide(entity)
    }

    companion object {

        @JvmField
        val ATTRIBUTES: AttributeSupplier = attributes().build()

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = AttributeSupplier.builder()
            .add(AttributeTypes.MAX_HEALTH)
            .add(AttributeTypes.KNOCKBACK_RESISTANCE)
            .add(AttributeTypes.MOVEMENT_SPEED)
            .add(AttributeTypes.ARMOR)
            .add(AttributeTypes.ARMOR_TOUGHNESS)
    }
}
