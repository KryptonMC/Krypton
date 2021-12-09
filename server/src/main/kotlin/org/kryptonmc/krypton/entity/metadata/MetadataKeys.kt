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
import org.kryptonmc.api.entity.animal.type.AxolotlVariant
import org.kryptonmc.api.entity.animal.type.CatType
import org.kryptonmc.api.entity.animal.type.FoxType
import org.kryptonmc.api.entity.animal.type.MooshroomType
import org.kryptonmc.api.item.meta.DyeColors
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.nbt.MutableCompoundTag
import org.spongepowered.math.vector.Vector3f
import org.spongepowered.math.vector.Vector3i
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
    @JvmField val AGEABLE = AgeableKeys
    @JvmField val TAMABLE = TamableKeys
    @JvmField val AXOLOTL = AxolotlKeys
    @JvmField val BEE = BeeKeys
    @JvmField val CAT = CatKeys
    @JvmField val ITEM = ItemKeys
    @JvmField val FOX = FoxKeys
    @JvmField val GOAT = GoatKeys
    @JvmField val MOOSHROOM = MooshroomKeys
    @JvmField val OCELOT = OcelotKeys
    @JvmField val PANDA = PandaKeys
    @JvmField val PIG = PigKeys
    @JvmField val POLAR_BEAR = PolarBearKeys
    @JvmField val RABBIT = RabbitKeys
    @JvmField val SHEEP = SheepKeys
    @JvmField val TURTLE = TurtleKeys
    @JvmField val WOLF = WolfKeys

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

    object AgeableKeys {

        @JvmField val BABY = create(16, MetadataSerializers.BOOLEAN, false)
    }

    object TamableKeys {

        @JvmField val FLAGS = create(17, MetadataSerializers.BYTE, 0)
        @JvmField val OWNER = create(18, MetadataSerializers.OPTIONAL_UUID, Optional.empty())
    }

    object AxolotlKeys {

        @JvmField val VARIANT = create(17, MetadataSerializers.VAR_INT, AxolotlVariant.LUCY.ordinal)
        @JvmField val PLAYING_DEAD = create(18, MetadataSerializers.BOOLEAN, false)
        @JvmField val FROM_BUCKET = create(19, MetadataSerializers.BOOLEAN, false)
    }

    object BeeKeys {

        @JvmField val FLAGS = create(17, MetadataSerializers.BYTE, 0)
        @JvmField val ANGER_TIME = create(18, MetadataSerializers.VAR_INT, 0)
    }

    object CatKeys {

        @JvmField val TYPE = create(19, MetadataSerializers.VAR_INT, CatType.BLACK.ordinal)
        @JvmField val LYING = create(20, MetadataSerializers.BOOLEAN, false)
        @JvmField val RELAXED = create(21, MetadataSerializers.BOOLEAN, false)
        @JvmField val COLLAR_COLOR = create(22, MetadataSerializers.VAR_INT, Registries.DYE_COLORS.idOf(DyeColors.RED))
    }

    object ItemKeys {

        @JvmField val ITEM = create(8, MetadataSerializers.SLOT, KryptonItemStack.Factory.empty())
    }

    object FoxKeys {

        @JvmField val TYPE = create(17, MetadataSerializers.VAR_INT, FoxType.RED.ordinal)
        @JvmField val FLAGS = create(18, MetadataSerializers.BYTE, 0)
        @JvmField val FIRST_TRUSTED = create(19, MetadataSerializers.OPTIONAL_UUID, Optional.empty())
        @JvmField val SECOND_TRUSTED = create(20, MetadataSerializers.OPTIONAL_UUID, Optional.empty())
    }

    object GoatKeys {

        @JvmField val SCREAMING = create(17, MetadataSerializers.BOOLEAN, false)
    }

    object MooshroomKeys {

        @JvmField val TYPE = create(17, MetadataSerializers.STRING, MooshroomType.RED.serialized)
    }

    object OcelotKeys {

        @JvmField val TRUSTING = create(17, MetadataSerializers.BOOLEAN, false)
    }

    object PandaKeys {

        @JvmField val UNHAPPY_TIMER = create(17, MetadataSerializers.VAR_INT, 0)
        @JvmField val SNEEZE_TIMER = create(18, MetadataSerializers.VAR_INT, 0)
        @JvmField val EATING_TIMER = create(19, MetadataSerializers.VAR_INT, 0)
        @JvmField val MAIN_GENE = create(20, MetadataSerializers.BYTE, 0)
        @JvmField val HIDDEN_GENE = create(21, MetadataSerializers.BYTE, 0)
        @JvmField val FLAGS = create(22, MetadataSerializers.BYTE, 0)
    }

    object PigKeys {

        @JvmField val SADDLE = create(17, MetadataSerializers.BOOLEAN, false)
        @JvmField val BOOST_TIME = create(18, MetadataSerializers.VAR_INT, 0)
    }

    object PolarBearKeys {

        @JvmField val STANDING = create(17, MetadataSerializers.BOOLEAN, false)
    }

    object RabbitKeys {

        @JvmField val TYPE = create(17, MetadataSerializers.VAR_INT, 0)
    }

    object SheepKeys {

        @JvmField val FLAGS = create(17, MetadataSerializers.BYTE, 0)
    }

    object TurtleKeys {

        @JvmField val HOME = create(17, MetadataSerializers.POSITION, Vector3i.ZERO)
        @JvmField val HAS_EGG = create(18, MetadataSerializers.BOOLEAN, false)
        @JvmField val LAYING_EGG = create(19, MetadataSerializers.BOOLEAN, false)
        @JvmField val DESTINATION = create(20, MetadataSerializers.POSITION, Vector3i.ZERO)
        @JvmField val GOING_HOME = create(21, MetadataSerializers.BOOLEAN, false)
        @JvmField val TRAVELLING = create(22, MetadataSerializers.BOOLEAN, false)
    }

    object WolfKeys {

        @JvmField val BEGGING = create(19, MetadataSerializers.BOOLEAN, false)
        @JvmField val COLLAR_COLOR = create(20, MetadataSerializers.VAR_INT, Registries.DYE_COLORS.idOf(DyeColors.RED))
        @JvmField val ANGER_TIME = create(21, MetadataSerializers.VAR_INT, 0)
    }

    @JvmStatic
    private fun <T> create(
        id: Int,
        serializer: MetadataSerializer<T>,
        default: T
    ): MetadataKey<T> = MetadataKey(id, serializer, default)
}
