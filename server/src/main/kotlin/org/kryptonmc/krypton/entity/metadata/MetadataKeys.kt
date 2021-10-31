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
package org.kryptonmc.krypton.entity.metadata

import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.effect.particle.particleEffect
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.nbt.MutableCompoundTag
import org.spongepowered.math.vector.Vector3f
import java.util.Optional
import java.util.OptionalInt

@Catalogue(MetadataKey::class)
object MetadataKeys {

    @JvmField val AREA_EFFECT_CLOUD = AreaEffectCloudKeys
    @JvmField val LIVING = LivingEntityKeys
    @JvmField val BAT = BatKeys
    @JvmField val ARMOR_STAND = ArmorStandKeys
    @JvmField val ARROW_LIKE = ArrowLikeKeys
    @JvmField val ARROW = ArrowKeys
    @JvmField val TRIDENT = TridentKeys
    @JvmField val FIREBALL = FireballKeys
    @JvmField val FIREWORK_ROCKET = FireworkRocketKeys
    @JvmField val FISHING_HOOK = FishingHookKeys
    @JvmField val THROWABLE_PROJECTILE = ThrowableProjectileKeys
    @JvmField val WITHER_SKULL = WitherSkullKeys
    @JvmField val MOB = MobKeys
    @JvmField val CREEPER = CreeperKeys
    @JvmField val ZOMBIE = ZombieKeys
    @JvmField val PLAYER = PlayerKeys

    @JvmField val FLAGS = create(0, MetadataSerializers.BYTE, 0)
    @JvmField val AIR_TICKS = create(1, MetadataSerializers.VAR_INT, 300)
    @JvmField val CUSTOM_NAME = create(2, MetadataSerializers.OPTIONAL_COMPONENT, Optional.empty())
    @JvmField val CUSTOM_NAME_VISIBILITY = create(3, MetadataSerializers.BOOLEAN, false)
    @JvmField val SILENT = create(4, MetadataSerializers.BOOLEAN, false)
    @JvmField val NO_GRAVITY = create(5, MetadataSerializers.BOOLEAN, false)
    @JvmField val POSE = create(6, MetadataSerializers.POSE, Pose.STANDING)
    @JvmField val FROZEN_TICKS = create(7, MetadataSerializers.VAR_INT, 0)

    object LivingEntityKeys {

        @JvmField val FLAGS = create(8, MetadataSerializers.BYTE, 0)
        @JvmField val HEALTH = create(9, MetadataSerializers.FLOAT, 1F)
        @JvmField val POTION_EFFECT_COLOR = create(10, MetadataSerializers.VAR_INT, 0)
        @JvmField val POTION_EFFECT_AMBIENCE = create(11, MetadataSerializers.BOOLEAN, false)
        @JvmField val ARROWS = create(12, MetadataSerializers.VAR_INT, 0)
        @JvmField val STINGERS = create(13, MetadataSerializers.VAR_INT, 0)
        @JvmField val BED_LOCATION = create(14, MetadataSerializers.OPTIONAL_POSITION, Optional.empty())
    }

    object ArmorStandKeys {

        @JvmField val FLAGS = create(15, MetadataSerializers.BYTE, 0)
        @JvmField val HEAD_ROTATION = create(16, MetadataSerializers.ROTATION, Vector3f(0F, 0F, 0F))
        @JvmField val BODY_ROTATION = create(17, MetadataSerializers.ROTATION, Vector3f(0F, 0F, 0F))
        @JvmField val LEFT_ARM_ROTATION = create(18, MetadataSerializers.ROTATION, Vector3f(-10F, 0F, -10F))
        @JvmField val RIGHT_ARM_ROTATION = create(19, MetadataSerializers.ROTATION, Vector3f(-15F, 0F, 10F))
        @JvmField val LEFT_LEG_ROTATION = create(20, MetadataSerializers.ROTATION, Vector3f(-1F, 0F, -1F))
        @JvmField val RIGHT_LEG_ROTATION = create(21, MetadataSerializers.ROTATION, Vector3f(1F, 0F, 1F))
    }

    object ArrowLikeKeys {

        @JvmField val FLAGS = create(8, MetadataSerializers.BYTE, 0)
        @JvmField val PIERCING_LEVEL = create(9, MetadataSerializers.BYTE, 0)
    }

    object ArrowKeys {

        @JvmField val COLOR = create(10, MetadataSerializers.VAR_INT, -1)
    }

    object TridentKeys {

        @JvmField val LOYALTY_LEVEL = create(10, MetadataSerializers.VAR_INT, 0)
        @JvmField val ENCHANTED = create(11, MetadataSerializers.BOOLEAN, false)
    }

    object FireballKeys {

        @JvmField val ITEM = create(8, MetadataSerializers.SLOT, KryptonItemStack.Factory.empty())
    }

    object FireworkRocketKeys {

        @JvmField val ITEM = create(8, MetadataSerializers.SLOT, KryptonItemStack.Factory.empty())
        @JvmField val ATTACHED = create(9, MetadataSerializers.OPTIONAL_VAR_INT, OptionalInt.empty())
        @JvmField val SHOT_AT_ANGLE = create(10, MetadataSerializers.BOOLEAN, false)
    }

    object FishingHookKeys {

        @JvmField val HOOKED = create(8, MetadataSerializers.VAR_INT, 0)
        @JvmField val BITING = create(9, MetadataSerializers.BOOLEAN, false)
    }

    object ThrowableProjectileKeys {

        @JvmField val ITEM = create(8, MetadataSerializers.SLOT, KryptonItemStack.Factory.empty())
    }

    object WitherSkullKeys {

        @JvmField val DANGEROUS = create(8, MetadataSerializers.BOOLEAN, false)
    }

    object MobKeys {

        @JvmField val FLAGS = create(15, MetadataSerializers.BYTE, 0)
    }

    object CreeperKeys {

        @JvmField val STATE = create(16, MetadataSerializers.VAR_INT, -1)
        @JvmField val CHARGED = create(17, MetadataSerializers.BOOLEAN, false)
        @JvmField val IGNITED = create(18, MetadataSerializers.BOOLEAN, false)
    }

    object ZombieKeys {

        @JvmField val BABY = create(16, MetadataSerializers.BOOLEAN, false)
        @JvmField val CONVERTING = create(18, MetadataSerializers.BOOLEAN, false)
    }

    object PlayerKeys {

        @JvmField val ADDITIONAL_HEARTS = create(15, MetadataSerializers.FLOAT, 0F)
        @JvmField val SCORE = create(16, MetadataSerializers.VAR_INT, 0)
        @JvmField val SKIN_FLAGS = create(17, MetadataSerializers.BYTE, 0)
        @JvmField val MAIN_HAND = create(18, MetadataSerializers.BYTE, 1)
        @JvmField val LEFT_SHOULDER = create(19, MetadataSerializers.NBT, MutableCompoundTag())
        @JvmField val RIGHT_SHOULDER = create(20, MetadataSerializers.NBT, MutableCompoundTag())
    }

    object AreaEffectCloudKeys {

        @JvmField val RADIUS = create(8, MetadataSerializers.FLOAT, 0.5F)
        @JvmField val COLOR = create(9, MetadataSerializers.VAR_INT, 0)
        @JvmField val IGNORE_RADIUS = create(10, MetadataSerializers.BOOLEAN, false)
        @JvmField val PARTICLE = create(11, MetadataSerializers.PARTICLE, particleEffect(ParticleTypes.EFFECT))
    }

    object BatKeys {

        @JvmField val FLAGS = create(16, MetadataSerializers.BYTE, 0)
    }

    @JvmStatic
    private fun <T> create(
        id: Int,
        serializer: MetadataSerializer<T>,
        default: T
    ): MetadataKey<T> = MetadataKey(id, serializer, default)
}
