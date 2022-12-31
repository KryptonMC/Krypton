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
package org.kryptonmc.krypton.entity.animal

import org.kryptonmc.api.entity.animal.Bee
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.tags.ItemTags
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.components.Neutral
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.BeeSerializer
import org.kryptonmc.krypton.item.downcast
import org.kryptonmc.krypton.util.provider.UniformInt
import org.kryptonmc.krypton.world.KryptonWorld
import java.util.UUID

class KryptonBee(world: KryptonWorld) : KryptonAnimal(world), Bee, Neutral {

    override val type: KryptonEntityType<KryptonBee>
        get() = KryptonEntityTypes.BEE
    override val serializer: EntitySerializer<KryptonBee>
        get() = BeeSerializer

    override var cannotEnterHiveTicks: Int = 0
    override var hive: Vec3i? = null
    override var flower: Vec3i? = null
    override var angerTarget: UUID? = null
    internal var timeSincePollination = 0
    internal var cropsGrownSincePollination = 0

    override var isAngry: Boolean
        get() = data.getFlag(MetadataKeys.Bee.FLAGS, FLAG_ANGRY)
        set(value) = data.setFlag(MetadataKeys.Bee.FLAGS, FLAG_ANGRY, value)
    override var hasStung: Boolean
        get() = data.getFlag(MetadataKeys.Bee.FLAGS, FLAG_STUNG)
        set(value) = data.setFlag(MetadataKeys.Bee.FLAGS, FLAG_STUNG, value)
    override var hasNectar: Boolean
        get() = data.getFlag(MetadataKeys.Bee.FLAGS, FLAG_NECTAR)
        set(value) {
            if (value) timeSincePollination = 0
            data.setFlag(MetadataKeys.Bee.FLAGS, FLAG_NECTAR, value)
        }
    override var remainingAngerTime: Int
        get() = data.get(MetadataKeys.Bee.ANGER_TIME)
        set(value) = data.set(MetadataKeys.Bee.ANGER_TIME, value)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Bee.FLAGS, 0)
        data.define(MetadataKeys.Bee.ANGER_TIME, 0)
    }

    override fun startAngerTimer() {
        remainingAngerTime = PERSISTENT_ANGER_TIME.sample(random)
    }

    override fun isFood(item: ItemStack): Boolean = item.type.downcast().eq(ItemTags.FLOWERS)

    override fun soundVolume(): Float = 0.4F

    companion object {

        private const val FLAG_ANGRY = 1
        private const val FLAG_STUNG = 2
        private const val FLAG_NECTAR = 3
        private val PERSISTENT_ANGER_TIME = UniformInt(20 * 20, 39 * 20)

        private const val DEFAULT_MAX_HEALTH = 10.0
        private const val DEFAULT_FLYING_SPEED = 0.6
        private const val DEFAULT_MOVEMENT_SPEED = 0.3
        private const val DEFAULT_ATTACK_DAMAGE = 2.0
        private const val DEFAULT_FOLLOW_RANGE = 48.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.FLYING_SPEED, DEFAULT_FLYING_SPEED)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)
            .add(KryptonAttributeTypes.FOLLOW_RANGE, DEFAULT_FOLLOW_RANGE)
    }
}
