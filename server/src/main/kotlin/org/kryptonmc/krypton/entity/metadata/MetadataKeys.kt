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

    val AREA_EFFECT_CLOUD = AreaEffectCloudKeys
    val LIVING = LivingEntityKeys
    val BAT = BatKeys
    val ARMOR_STAND = ArmorStandKeys
    val ARROW_LIKE = ArrowLikeKeys
    val ARROW = ArrowKeys
    val TRIDENT = TridentKeys
    val FIREBALL = FireballKeys
    val FIREWORK_ROCKET = FireworkRocketKeys
    val FISHING_HOOK = FishingHookKeys
    val THROWABLE_PROJECTILE = ThrowableProjectileKeys
    val WITHER_SKULL = WitherSkullKeys
    val MOB = MobKeys
    val CREEPER = CreeperKeys
    val ZOMBIE = ZombieKeys
    val PLAYER = PlayerKeys

    val FLAGS = register(0, MetadataSerializers.BYTE, 0)
    val AIR_TICKS = register(1, MetadataSerializers.VAR_INT, 300)
    val DISPLAY_NAME = register(2, MetadataSerializers.OPTIONAL_COMPONENT, Optional.empty())
    val DISPLAY_NAME_VISIBILITY = register(3, MetadataSerializers.BOOLEAN, false)
    val SILENT = register(4, MetadataSerializers.BOOLEAN, false)
    val NO_GRAVITY = register(5, MetadataSerializers.BOOLEAN, false)
    val POSE = register(6, MetadataSerializers.POSE, Pose.STANDING)
    val FROZEN_TICKS = register(7, MetadataSerializers.VAR_INT, 0)

    object LivingEntityKeys {

        val FLAGS = register(8, MetadataSerializers.BYTE, 0)
        val HEALTH = register(9, MetadataSerializers.FLOAT, 1F)
        val POTION_EFFECT_COLOR = register(10, MetadataSerializers.VAR_INT, 0)
        val POTION_EFFECT_AMBIENCE = register(11, MetadataSerializers.BOOLEAN, false)
        val ARROWS = register(12, MetadataSerializers.VAR_INT, 0)
        val STINGERS = register(13, MetadataSerializers.VAR_INT, 0)
        val BED_LOCATION = register(14, MetadataSerializers.OPTIONAL_POSITION, Optional.empty())
    }

    object ArmorStandKeys {

        val FLAGS = register(15, MetadataSerializers.BYTE, 0)
        val HEAD_ROTATION = register(16, MetadataSerializers.ROTATION, Vector3f(0F, 0F, 0F))
        val BODY_ROTATION = register(17, MetadataSerializers.ROTATION, Vector3f(0F, 0F, 0F))
        val LEFT_ARM_ROTATION = register(18, MetadataSerializers.ROTATION, Vector3f(-10F, 0F, -10F))
        val RIGHT_ARM_ROTATION = register(19, MetadataSerializers.ROTATION, Vector3f(-15F, 0F, 10F))
        val LEFT_LEG_ROTATION = register(20, MetadataSerializers.ROTATION, Vector3f(-1F, 0F, -1F))
        val RIGHT_LEG_ROTATION = register(21, MetadataSerializers.ROTATION, Vector3f(1F, 0F, 1F))
    }

    object ArrowLikeKeys {

        val FLAGS = register(8, MetadataSerializers.BYTE, 0)
        val PIERCING_LEVEL = register(9, MetadataSerializers.BYTE, 0)
    }

    object ArrowKeys {

        val COLOR = register(10, MetadataSerializers.VAR_INT, -1)
    }

    object TridentKeys {

        val LOYALTY_LEVEL = register(10, MetadataSerializers.VAR_INT, 0)
        val ENCHANTED = register(11, MetadataSerializers.BOOLEAN, false)
    }

    object FireballKeys {

        val ITEM = register(8, MetadataSerializers.SLOT, KryptonItemStack.Factory.empty())
    }

    object FireworkRocketKeys {

        val ITEM = register(8, MetadataSerializers.SLOT, KryptonItemStack.Factory.empty())
        val ATTACHED = register(9, MetadataSerializers.OPTIONAL_VAR_INT, OptionalInt.empty())
        val SHOT_AT_ANGLE = register(10, MetadataSerializers.BOOLEAN, false)
    }

    object FishingHookKeys {

        val HOOKED = register(8, MetadataSerializers.VAR_INT, 0)
        val BITING = register(9, MetadataSerializers.BOOLEAN, false)
    }

    object ThrowableProjectileKeys {

        val ITEM = register(8, MetadataSerializers.SLOT, KryptonItemStack.Factory.empty())
    }

    object WitherSkullKeys {

        val DANGEROUS = register(8, MetadataSerializers.BOOLEAN, false)
    }

    object MobKeys {

        val FLAGS = register(15, MetadataSerializers.BYTE, 0)
    }

    object CreeperKeys {

        val STATE = register(16, MetadataSerializers.VAR_INT, -1)
        val CHARGED = register(17, MetadataSerializers.BOOLEAN, false)
        val IGNITED = register(18, MetadataSerializers.BOOLEAN, false)
    }

    object ZombieKeys {

        val BABY = register(16, MetadataSerializers.BOOLEAN, false)
        val CONVERTING = register(18, MetadataSerializers.BOOLEAN, false)
    }

    object PlayerKeys {

        val ADDITIONAL_HEARTS = register(15, MetadataSerializers.FLOAT, 0F)
        val SCORE = register(16, MetadataSerializers.VAR_INT, 0)
        val SKIN_FLAGS = register(17, MetadataSerializers.BYTE, 0)
        val MAIN_HAND = register(18, MetadataSerializers.BYTE, 1)
        val LEFT_SHOULDER = register(19, MetadataSerializers.NBT, MutableCompoundTag())
        val RIGHT_SHOULDER = register(20, MetadataSerializers.NBT, MutableCompoundTag())
    }

    object AreaEffectCloudKeys {

        val RADIUS = register(8, MetadataSerializers.FLOAT, 0.5F)
        val COLOR = register(9, MetadataSerializers.VAR_INT, 0)
        val IGNORE_RADIUS = register(10, MetadataSerializers.BOOLEAN, false)
        val PARTICLE = register(11, MetadataSerializers.PARTICLE, particleEffect(ParticleTypes.EFFECT))
    }

    object BatKeys {

        val FLAGS = register(16, MetadataSerializers.BYTE, 0)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> register(
        id: Int,
        serializer: MetadataSerializer<T>,
        default: T
    ): MetadataKey<T> = MetadataKey(id, serializer, default)
}
