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
package org.kryptonmc.krypton.entity.animal

import net.kyori.adventure.sound.Sound
import org.kryptonmc.api.effect.sound.SoundEvents
import org.kryptonmc.api.entity.animal.Animal
import org.kryptonmc.api.entity.animal.Cat
import org.kryptonmc.api.entity.animal.type.CatVariant
import org.kryptonmc.api.item.ItemStack
import org.kryptonmc.api.item.ItemTypes
import org.kryptonmc.api.item.data.DyeColor
import org.kryptonmc.krypton.entity.KryptonEntityType
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonMob
import org.kryptonmc.krypton.entity.attribute.AttributeSupplier
import org.kryptonmc.krypton.entity.attribute.KryptonAttributeTypes
import org.kryptonmc.krypton.entity.metadata.MetadataKeys
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.animal.CatSerializer
import org.kryptonmc.krypton.util.enumhelper.DyeColors
import org.kryptonmc.krypton.world.KryptonWorld

class KryptonCat(world: KryptonWorld) : KryptonTamable(world), Cat {

    override val type: KryptonEntityType<KryptonCat>
        get() = KryptonEntityTypes.CAT
    override val serializer: EntitySerializer<KryptonCat>
        get() = CatSerializer

    override var variant: CatVariant
        get() = VARIANTS[data.get(MetadataKeys.Cat.VARIANT)]
        set(value) = data.set(MetadataKeys.Cat.VARIANT, value.ordinal)
    override var isLying: Boolean
        get() = data.get(MetadataKeys.Cat.LYING)
        set(value) = data.set(MetadataKeys.Cat.LYING, value)
    override var isRelaxed: Boolean
        get() = data.get(MetadataKeys.Cat.RELAXED)
        set(value) = data.set(MetadataKeys.Cat.RELAXED, value)
    override var collarColor: DyeColor
        get() = DyeColors.fromId(data.get(MetadataKeys.Cat.COLLAR_COLOR))
        set(value) = data.set(MetadataKeys.Cat.COLLAR_COLOR, value.ordinal)

    override fun defineData() {
        super.defineData()
        data.define(MetadataKeys.Cat.VARIANT, CatVariant.BLACK.ordinal)
        data.define(MetadataKeys.Cat.LYING, false)
        data.define(MetadataKeys.Cat.RELAXED, false)
        data.define(MetadataKeys.Cat.COLLAR_COLOR, DyeColor.RED.ordinal)
    }

    override fun hiss() {
        playSound(Sound.sound(SoundEvents.CAT_HISS, soundSource(), soundVolume(), voicePitch()), position.x, position.y, position.z)
    }

    override fun canMate(target: Animal): Boolean {
        if (!isTamed) return false
        if (target !is Cat) return false
        return target.isTamed && super.canMate(target)
    }

    override fun isFood(item: ItemStack): Boolean = item.type === ItemTypes.COD || item.type === ItemTypes.SALMON

    companion object {

        private val VARIANTS = CatVariant.values()
        private const val DEFAULT_MAX_HEALTH = 10.0
        private const val DEFAULT_MOVEMENT_SPEED = 0.3
        private const val DEFAULT_ATTACK_DAMAGE = 3.0

        @JvmStatic
        fun attributes(): AttributeSupplier.Builder = KryptonMob.attributes()
            .add(KryptonAttributeTypes.MAX_HEALTH, DEFAULT_MAX_HEALTH)
            .add(KryptonAttributeTypes.MOVEMENT_SPEED, DEFAULT_MOVEMENT_SPEED)
            .add(KryptonAttributeTypes.ATTACK_DAMAGE, DEFAULT_ATTACK_DAMAGE)
    }
}
