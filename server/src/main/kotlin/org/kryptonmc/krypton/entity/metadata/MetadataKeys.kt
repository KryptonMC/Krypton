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

import net.kyori.adventure.key.Key
import org.jglrxavpok.hephaistos.nbt.NBTCompound
import org.kryptonmc.api.effect.particle.ParticleType
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.registry.InternalRegistries
import java.util.Optional

object MetadataKeys {

    val LIVING = LivingEntityKeys
    val MOB = MobKeys
    val ZOMBIE = ZombieKeys
    val PLAYER = PlayerKeys
    val AREA_EFFECT_CLOUD = AreaEffectCloudKeys

    val FLAGS = register("flags", 0, MetadataSerializers.BYTE, 0)
    val AIR_TICKS = register("air_ticks", 1, MetadataSerializers.VAR_INT, 300)
    val DISPLAY_NAME = register("display_name", 2, MetadataSerializers.OPTIONAL_COMPONENT, Optional.empty())
    val DISPLAY_NAME_VISIBILITY = register("display_name_visibility", 3, MetadataSerializers.BOOLEAN, false)
    val SILENT = register("silent", 4, MetadataSerializers.BOOLEAN, false)
    val NO_GRAVITY = register("no_gravity", 5, MetadataSerializers.BOOLEAN, false)
    val POSE = register("pose", 6, MetadataSerializers.POSE, Pose.STANDING)
    val FROZEN_TICKS = register("frozen_ticks", 7, MetadataSerializers.VAR_INT, 0)

    object LivingEntityKeys {

        val FLAGS = register("living/flags", 8, MetadataSerializers.BYTE, 0)
        val HEALTH = register("living/health", 9, MetadataSerializers.FLOAT, 1F)
        val POTION_EFFECT_COLOR = register("living/potion_effect_color", 10, MetadataSerializers.VAR_INT, 0)
        val POTION_EFFECT_AMBIENCE = register("living/potion_effect_ambience", 11, MetadataSerializers.BOOLEAN, false)
        val ARROWS = register("living/arrows", 12, MetadataSerializers.VAR_INT, 0)
        val STINGERS = register("living/stingers", 13, MetadataSerializers.VAR_INT, 0)
        val BED_LOCATION = register("living/bed_location", 14, MetadataSerializers.OPTIONAL_POSITION, Optional.empty())
    }

    object MobKeys {

        val FLAGS = register("mob/flags", 15, MetadataSerializers.BYTE, 0)
    }

    object ZombieKeys {

        val BABY = register("zombie/baby", 16, MetadataSerializers.BOOLEAN, false)
        val CONVERTING = register("zombie/converting", 18, MetadataSerializers.BOOLEAN, false)
    }

    object PlayerKeys {

        val ADDITIONAL_HEARTS = register("player/additional_hearts", 15, MetadataSerializers.FLOAT, 0F)
        val SCORE = register("player/score", 16, MetadataSerializers.VAR_INT, 0)
        val SKIN_FLAGS = register("player/skin_flags", 17, MetadataSerializers.BYTE, 0)
        val MAIN_HAND = register("player/main_hand", 18, MetadataSerializers.BYTE, 1)
        val LEFT_SHOULDER = register("player/left_shoulder", 19, MetadataSerializers.NBT, NBTCompound())
        val RIGHT_SHOULDER = register("player/right_shoulder", 20, MetadataSerializers.NBT, NBTCompound())
    }

    object AreaEffectCloudKeys {

        val RADIUS = register("area_effect_cloud/radius", 8, MetadataSerializers.FLOAT, 0.5F)
        val COLOR = register("area_effect_cloud/color", 9, MetadataSerializers.VAR_INT, 0)
        val IGNORE_RADIUS = register("area_effect_cloud/ignore_radius", 10, MetadataSerializers.BOOLEAN, false)
        val PARTICLE = register("area_effect_cloud/particle", 11, MetadataSerializers.PARTICLE, ParticleType.EFFECT)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> register(key: String, id: Int, serializer: MetadataSerializer<T>, default: T): MetadataKey<T> = Registries.register(
        InternalRegistries.METADATA,
        Key.key("krypton", "metadata/$key"),
        MetadataKey(id, serializer, default)
    ) as MetadataKey<T>
}
