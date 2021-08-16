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
package org.kryptonmc.krypton.entity.projectile

import net.kyori.adventure.key.Key
import org.kryptonmc.api.block.Block
import org.kryptonmc.api.effect.sound.SoundEvent
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.projectile.ArrowLike
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.krypton.world.block.toBlock
import org.kryptonmc.krypton.world.block.toNBT
import org.kryptonmc.nbt.CompoundTag
import org.kryptonmc.nbt.StringTag

abstract class KryptonArrowLike(
    world: KryptonWorld,
    type: EntityType<out ArrowLike>,
    private val defaultHitGroundSound: SoundEvent = SoundEvents.ARROW_HIT
) : KryptonProjectile(world, type), ArrowLike {

    override var damage = 2.0
    override var isInGround = false
    override var life = 0
    override var shakeTime = 0
    override var sound = defaultHitGroundSound
    override var stuckInBlock: Block? = null
    override var pickup = ArrowLike.Pickup.DISALLOWED

    override var isCritical: Boolean
        get() = data[MetadataKeys.ARROW_LIKE.FLAGS].toInt() and 1 != 0
        set(value) = setFlag(1, value)
    override var ignoresPhysics: Boolean
        get() = data[MetadataKeys.ARROW_LIKE.FLAGS].toInt() and 2 != 0
        set(value) {
            noPhysics = value
            setFlag(2, value)
        }
    override var wasShotFromCrossbow: Boolean
        get() = data[MetadataKeys.ARROW_LIKE.FLAGS].toInt() and 4 != 0
        set(value) = setFlag(4, value)
    override var piercingLevel: Int
        get() = data[MetadataKeys.ARROW_LIKE.PIERCING_LEVEL].toInt()
        set(value) = data.set(MetadataKeys.ARROW_LIKE.PIERCING_LEVEL, value.toByte())

    private fun setFlag(flag: Int, value: Boolean) {
        val old = data[MetadataKeys.ARROW_LIKE.FLAGS].toInt()
        data[MetadataKeys.ARROW_LIKE.FLAGS] = (if (value) old or flag else old and flag.inv()).toByte()
    }

    init {
        data.add(MetadataKeys.ARROW_LIKE.FLAGS)
        data.add(MetadataKeys.ARROW_LIKE.PIERCING_LEVEL)
    }

    override fun load(tag: CompoundTag) {
        super.load(tag)
        isCritical = tag.getBoolean("crit")
        if (tag.contains("damage", 99)) damage = tag.getDouble("damage")
        if (tag.contains("inBlockState", CompoundTag.ID)) stuckInBlock = tag.getCompound("inBlockState").toBlock()
        isInGround = tag.getBoolean("inGround")
        life = tag.getShort("life").toInt()

        val pickupOrdinal = tag.getInt("pickup")
        pickup = ArrowLike.Pickup.values()[if (pickupOrdinal in 0..ArrowLike.Pickup.values().size) pickupOrdinal else 0]
        piercingLevel = tag.getByte("PierceLevel").toInt()
        shakeTime = tag.getByte("shake").toInt() and 255
        wasShotFromCrossbow = tag.getBoolean("ShotFromCrossbow")

        if (tag.contains("SoundEvent", StringTag.ID)) {
            sound = InternalRegistries.SOUND_EVENT[Key.key(tag.getString("SoundEvent"))] ?: defaultHitGroundSound
        }
    }

    override fun save(): CompoundTag.Builder = super.save().apply {
        boolean("crit", isCritical)
        double("damage", damage)
        stuckInBlock?.let { put("inBlockState", it.toNBT()) }
        boolean("inGround", isInGround)
        short("life", life.toShort())
        byte("pickup", pickup.ordinal.toByte())
        byte("PierceLevel", piercingLevel.toByte())
        byte("shake", shakeTime.toByte())
        boolean("ShotFromCrossbow", wasShotFromCrossbow)
        string("SoundEvent", sound.key.asString())
    }
}
