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
import org.kryptonmc.api.entity.EquipmentSlot
import org.kryptonmc.api.entity.Hand
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.api.entity.attribute.Attribute
import org.kryptonmc.api.entity.attribute.AttributeType
import org.kryptonmc.api.fluid.Fluid
import org.kryptonmc.api.world.Difficulty
import org.kryptonmc.krypton.entity.attribute.AttributeMap
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.DefaultAttributes
import org.kryptonmc.krypton.entity.attribute.KryptonAttribute
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeType
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.attribute.downcast
import org.kryptonmc.krypton.entity.memory.Brain
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.player.KryptonPlayer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.LivingEntitySerializer
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.krypton.world.KryptonWorld
import org.spongepowered.math.vector.Vector3i
import kotlin.random.Random

@Suppress("LeakingThis")
abstract class KryptonLivingEntity(world: KryptonWorld) : KryptonEntity(world), LivingEntity, KryptonEquipable {

    abstract override val type: KryptonEntityType<KryptonLivingEntity>
    override val serializer: EntitySerializer<out KryptonLivingEntity>
        get() = LivingEntitySerializer

    final override val maxHealth: Float
        get() = attributes.getValue(KryptonAttributeTypes.MAX_HEALTH).toFloat()
    override var absorption: Float = 0F
    final override val isAlive: Boolean
        get() = super.isAlive && health > 0F
    final override var isDead: Boolean = false
    final override var deathTime: Int = 0
    final override var hurtTime: Int = 0
    final override var lastHurtTimestamp: Int = 0
    private var tickCount = 0
    val attributes: AttributeMap = AttributeMap(DefaultAttributes.get(type))
    open val brain: Brain<*> = Brain<KryptonLivingEntity>()
    var headYaw: Float = rotation.x()

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

    open val isBaby: Boolean
        get() = false
    open val canBeSeenAsEnemy: Boolean
        get() = !isInvulnerable && canBeSeenByAnyone
    open val canBeSeenByAnyone: Boolean
        get() = isAlive
    open val soundVolume: Float
        get() = 1F
    open val voicePitch: Float
        get() {
            val babyFactor = if (isBaby) 1.5F else 1F
            return (Random.nextFloat() - Random.nextFloat()) * 0.2F + babyFactor
        }
    val killCredit: KryptonLivingEntity?
        get() {
            // TODO: Check combat tracker here
            if (lastHurtByPlayer != null) return lastHurtByPlayer
            if (lastHurtByMob != null) return lastHurtByMob
            return null
        }

    final override var isGliding: Boolean
        get() = data.getFlag(MetadataKeys.Entity.FLAGS, FLAG_ENTITY_GLIDING)
        set(value) = data.setFlag(MetadataKeys.Entity.FLAGS, FLAG_ENTITY_GLIDING, value)
    final override var isUsingItem: Boolean
        get() = data.getFlag(MetadataKeys.LivingEntity.FLAGS, FLAG_USING_ITEM)
        set(value) = data.setFlag(MetadataKeys.LivingEntity.FLAGS, FLAG_USING_ITEM, value)
    final override var hand: Hand
        get() = if (data.getFlag(MetadataKeys.LivingEntity.FLAGS, FLAG_OFFHAND)) Hand.OFF else Hand.MAIN
        set(value) = data.setFlag(MetadataKeys.LivingEntity.FLAGS, FLAG_OFFHAND, value == Hand.OFF)
    final override var isInRiptideSpinAttack: Boolean
        get() = data.getFlag(MetadataKeys.LivingEntity.FLAGS, FLAG_IN_RIPTIDE_SPIN_ATTACK)
        set(value) = data.setFlag(MetadataKeys.LivingEntity.FLAGS, FLAG_IN_RIPTIDE_SPIN_ATTACK, value)
    override var health: Float
        get() = data.get(MetadataKeys.LivingEntity.HEALTH)
        set(value) = data.set(MetadataKeys.LivingEntity.HEALTH, value)
    private var potionEffectColor: Int
        get() = data.get(MetadataKeys.LivingEntity.POTION_EFFECT_COLOR)
        set(value) = data.set(MetadataKeys.LivingEntity.POTION_EFFECT_COLOR, value)
    private var isPotionEffectAmbient: Boolean
        get() = data.get(MetadataKeys.LivingEntity.POTION_EFFECT_AMBIENCE)
        set(value) = data.set(MetadataKeys.LivingEntity.POTION_EFFECT_AMBIENCE, value)
    var arrowCount: Int
        get() = data.get(MetadataKeys.LivingEntity.ARROWS)
        set(value) = data.set(MetadataKeys.LivingEntity.ARROWS, value)
    var stingerCount: Int
        get() = data.get(MetadataKeys.LivingEntity.STINGERS)
        set(value) = data.set(MetadataKeys.LivingEntity.STINGERS, value)
    final override var sleepingPosition: Vector3i?
        get() = data.get(MetadataKeys.LivingEntity.BED_LOCATION)
        set(value) = data.set(MetadataKeys.LivingEntity.BED_LOCATION, value)

    init {
        data.set(MetadataKeys.LivingEntity.HEALTH, maxHealth)
    }

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.LivingEntity.FLAGS, 0)
        data.define(MetadataKeys.LivingEntity.HEALTH, 1F)
        data.define(MetadataKeys.LivingEntity.POTION_EFFECT_COLOR, 0)
        data.define(MetadataKeys.LivingEntity.POTION_EFFECT_AMBIENCE, false)
        data.define(MetadataKeys.LivingEntity.ARROWS, 0)
        data.define(MetadataKeys.LivingEntity.STINGERS, 0)
        data.define(MetadataKeys.LivingEntity.BED_LOCATION, null)
    }

    abstract override fun getEquipment(slot: EquipmentSlot): KryptonItemStack

    override fun getHeldItem(hand: Hand): KryptonItemStack {
        if (hand == Hand.MAIN) return getEquipment(EquipmentSlot.MAIN_HAND)
        if (hand == Hand.OFF) return getEquipment(EquipmentSlot.OFF_HAND)
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

    override fun getArmor(slot: ArmorSlot): KryptonItemStack = getEquipment(EquipmentSlots.fromArmorSlot(slot))

    override fun setArmor(slot: ArmorSlot, item: KryptonItemStack) {
        setEquipment(EquipmentSlots.fromArmorSlot(slot), item)
    }

    fun canAttack(target: KryptonLivingEntity): Boolean {
        if (target is KryptonPlayer && world.difficulty === Difficulty.PEACEFUL) return false
        return target.canBeSeenAsEnemy
    }

    @Suppress("FunctionOnlyReturningConstant", "UnusedPrivateMember") // This will have logic in the future
    fun canStandOnFluid(fluid: Fluid): Boolean = false

    protected fun getAttribute(type: KryptonAttributeType): KryptonAttribute? = attributes.get(type)

    override fun getAttribute(type: AttributeType): Attribute? = getAttribute(type.downcast())

    protected fun removeEffectParticles() {
        isPotionEffectAmbient = false
        potionEffectColor = 0
    }

    companion object {

        private const val FLAG_ENTITY_GLIDING = 7
        private const val FLAG_USING_ITEM = 0
        private const val FLAG_OFFHAND = 1
        private const val FLAG_IN_RIPTIDE_SPIN_ATTACK = 2

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = AttributeSupplier.builder()
            .add(KryptonAttributeTypes.MAX_HEALTH)
            .add(KryptonAttributeTypes.KNOCKBACK_RESISTANCE)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ARMOR)
            .add(KryptonAttributeTypes.ARMOR_TOUGHNESS)
    }
}
