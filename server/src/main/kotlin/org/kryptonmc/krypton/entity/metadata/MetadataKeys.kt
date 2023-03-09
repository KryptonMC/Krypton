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
package org.kryptonmc.krypton.entity.metadata

import net.kyori.adventure.text.Component
import org.kryptonmc.api.util.Rotation
import org.kryptonmc.api.util.Vec3i
import org.kryptonmc.internal.annotations.Catalogue
import org.kryptonmc.krypton.effect.particle.ParticleOptions
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.nbt.CompoundTag
import java.util.OptionalInt
import java.util.UUID

@Catalogue(MetadataKey::class)
object MetadataKeys {

    object Entity {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(0, MetadataSerializers.BYTE)
        @JvmField
        val AIR_TICKS: MetadataKey<Int> = MetadataKey(1, MetadataSerializers.INT)
        @JvmField
        val CUSTOM_NAME: MetadataKey<Component?> = MetadataKey(2, MetadataSerializers.OPTIONAL_COMPONENT)
        @JvmField
        val CUSTOM_NAME_VISIBILITY: MetadataKey<Boolean> = MetadataKey(3, MetadataSerializers.BOOLEAN)
        @JvmField
        val SILENT: MetadataKey<Boolean> = MetadataKey(4, MetadataSerializers.BOOLEAN)
        @JvmField
        val NO_GRAVITY: MetadataKey<Boolean> = MetadataKey(5, MetadataSerializers.BOOLEAN)
        @JvmField
        val POSE: MetadataKey<Pose> = MetadataKey(6, MetadataSerializers.POSE)
        @JvmField
        val FROZEN_TICKS: MetadataKey<Int> = MetadataKey(7, MetadataSerializers.INT)
    }

    object LivingEntity {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(8, MetadataSerializers.BYTE)
        @JvmField
        val HEALTH: MetadataKey<Float> = MetadataKey(9, MetadataSerializers.FLOAT)
        @JvmField
        val POTION_EFFECT_COLOR: MetadataKey<Int> = MetadataKey(10, MetadataSerializers.INT)
        @JvmField
        val POTION_EFFECT_AMBIENCE: MetadataKey<Boolean> = MetadataKey(11, MetadataSerializers.BOOLEAN)
        @JvmField
        val ARROWS: MetadataKey<Int> = MetadataKey(12, MetadataSerializers.INT)
        @JvmField
        val STINGERS: MetadataKey<Int> = MetadataKey(13, MetadataSerializers.INT)
        @JvmField
        val BED_LOCATION: MetadataKey<Vec3i?> = MetadataKey(14, MetadataSerializers.OPTIONAL_BLOCK_POS)
    }

    object ArmorStand {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(15, MetadataSerializers.BYTE)
        @JvmField
        val HEAD_ROTATION: MetadataKey<Rotation> = MetadataKey(16, MetadataSerializers.ROTATION)
        @JvmField
        val BODY_ROTATION: MetadataKey<Rotation> = MetadataKey(17, MetadataSerializers.ROTATION)
        @JvmField
        val LEFT_ARM_ROTATION: MetadataKey<Rotation> = MetadataKey(18, MetadataSerializers.ROTATION)
        @JvmField
        val RIGHT_ARM_ROTATION: MetadataKey<Rotation> = MetadataKey(19, MetadataSerializers.ROTATION)
        @JvmField
        val LEFT_LEG_ROTATION: MetadataKey<Rotation> = MetadataKey(20, MetadataSerializers.ROTATION)
        @JvmField
        val RIGHT_LEG_ROTATION: MetadataKey<Rotation> = MetadataKey(21, MetadataSerializers.ROTATION)
    }

    object ArrowLike {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(8, MetadataSerializers.BYTE)
        @JvmField
        val PIERCING_LEVEL: MetadataKey<Byte> = MetadataKey(9, MetadataSerializers.BYTE)
    }

    object Arrow {

        @JvmField
        val COLOR: MetadataKey<Int> = MetadataKey(10, MetadataSerializers.INT)
    }

    object Trident {

        @JvmField
        val LOYALTY_LEVEL: MetadataKey<Int> = MetadataKey(10, MetadataSerializers.INT)
        @JvmField
        val ENCHANTED: MetadataKey<Boolean> = MetadataKey(11, MetadataSerializers.BOOLEAN)
    }

    object Fireball {

        @JvmField
        val ITEM: MetadataKey<KryptonItemStack> = MetadataKey(8, MetadataSerializers.ITEM_STACK)
    }

    object FireworkRocket {

        @JvmField
        val ITEM: MetadataKey<KryptonItemStack> = MetadataKey(8, MetadataSerializers.ITEM_STACK)
        @JvmField
        val ATTACHED: MetadataKey<OptionalInt> = MetadataKey(9, MetadataSerializers.OPTIONAL_UNSIGNED_INT)
        @JvmField
        val SHOT_AT_ANGLE: MetadataKey<Boolean> = MetadataKey(10, MetadataSerializers.BOOLEAN)
    }

    object FishingHook {

        @JvmField
        val HOOKED: MetadataKey<Int> = MetadataKey(8, MetadataSerializers.INT)
        @JvmField
        val BITING: MetadataKey<Boolean> = MetadataKey(9, MetadataSerializers.BOOLEAN)
    }

    object ThrowableProjectile {

        @JvmField
        val ITEM: MetadataKey<KryptonItemStack> = MetadataKey(8, MetadataSerializers.ITEM_STACK)
    }

    object WitherSkull {

        @JvmField
        val DANGEROUS: MetadataKey<Boolean> = MetadataKey(8, MetadataSerializers.BOOLEAN)
    }

    object MinecartLike {

        @JvmField
        val HURT_TIMER: MetadataKey<Int> = MetadataKey(8, MetadataSerializers.INT)
        @JvmField
        val HURT_DIRECTION: MetadataKey<Int> = MetadataKey(9, MetadataSerializers.INT)
        @JvmField
        val DAMAGE: MetadataKey<Float> = MetadataKey(10, MetadataSerializers.FLOAT)
        @JvmField
        val CUSTOM_BLOCK_ID: MetadataKey<Int> = MetadataKey(11, MetadataSerializers.INT)
        @JvmField
        val CUSTOM_BLOCK_OFFSET: MetadataKey<Int> = MetadataKey(12, MetadataSerializers.INT)
        @JvmField
        val SHOW_CUSTOM_BLOCK: MetadataKey<Boolean> = MetadataKey(13, MetadataSerializers.BOOLEAN)
    }

    object CommandBlockMinecart {

        @JvmField
        val COMMAND: MetadataKey<String> = MetadataKey(14, MetadataSerializers.STRING)
        @JvmField
        val LAST_OUTPUT: MetadataKey<Component> = MetadataKey(15, MetadataSerializers.COMPONENT)
    }

    object FurnaceMinecart {

        @JvmField
        val HAS_FUEL: MetadataKey<Boolean> = MetadataKey(14, MetadataSerializers.BOOLEAN)
    }

    object Mob {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(15, MetadataSerializers.BYTE)
    }

    object Blaze {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(16, MetadataSerializers.BYTE)
    }

    object Creeper {

        @JvmField
        val STATE: MetadataKey<Int> = MetadataKey(16, MetadataSerializers.INT)
        @JvmField
        val CHARGED: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
        @JvmField
        val IGNITED: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN)
    }

    object Guardian {

        @JvmField
        val MOVING: MetadataKey<Boolean> = MetadataKey(16, MetadataSerializers.BOOLEAN)
        @JvmField
        val TARGET_ID: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
    }

    object Spider {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(16, MetadataSerializers.BYTE)
    }

    object Zombie {

        @JvmField
        val BABY: MetadataKey<Boolean> = MetadataKey(16, MetadataSerializers.BOOLEAN)
        @JvmField
        val CONVERTING: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN)
    }

    object Player {

        @JvmField
        val ADDITIONAL_HEARTS: MetadataKey<Float> = MetadataKey(15, MetadataSerializers.FLOAT)
        @JvmField
        val SCORE: MetadataKey<Int> = MetadataKey(16, MetadataSerializers.INT)
        @JvmField
        val SKIN_FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE)
        @JvmField
        val MAIN_HAND: MetadataKey<Byte> = MetadataKey(18, MetadataSerializers.BYTE)
        @JvmField
        val LEFT_SHOULDER: MetadataKey<CompoundTag> = MetadataKey(19, MetadataSerializers.COMPOUND_TAG)
        @JvmField
        val RIGHT_SHOULDER: MetadataKey<CompoundTag> = MetadataKey(20, MetadataSerializers.COMPOUND_TAG)
    }

    object AreaEffectCloud {

        @JvmField
        val RADIUS: MetadataKey<Float> = MetadataKey(8, MetadataSerializers.FLOAT)
        @JvmField
        val COLOR: MetadataKey<Int> = MetadataKey(9, MetadataSerializers.INT)
        @JvmField
        val IGNORE_RADIUS: MetadataKey<Boolean> = MetadataKey(10, MetadataSerializers.BOOLEAN)
        @JvmField
        val PARTICLE: MetadataKey<ParticleOptions> = MetadataKey(11, MetadataSerializers.PARTICLE)
    }

    object Bat {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(16, MetadataSerializers.BYTE)
    }

    object Boat {

        @JvmField
        val HURT_TIMER: MetadataKey<Int> = MetadataKey(8, MetadataSerializers.INT)
        @JvmField
        val HURT_DIRECTION: MetadataKey<Int> = MetadataKey(9, MetadataSerializers.INT)
        @JvmField
        val DAMAGE: MetadataKey<Float> = MetadataKey(10, MetadataSerializers.FLOAT)
        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(11, MetadataSerializers.INT)
        @JvmField
        val LEFT_PADDLE_TURNING: MetadataKey<Boolean> = MetadataKey(12, MetadataSerializers.BOOLEAN)
        @JvmField
        val RIGHT_PADDLE_TURNING: MetadataKey<Boolean> = MetadataKey(13, MetadataSerializers.BOOLEAN)
        @JvmField
        val SPLASH_TIMER: MetadataKey<Int> = MetadataKey(14, MetadataSerializers.INT)
    }

    object Ageable {

        @JvmField
        val BABY: MetadataKey<Boolean> = MetadataKey(16, MetadataSerializers.BOOLEAN)
    }

    object Tamable {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE)
        @JvmField
        val OWNER: MetadataKey<UUID?> = MetadataKey(18, MetadataSerializers.OPTIONAL_UUID)
    }

    object Axolotl {

        @JvmField
        val VARIANT: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
        @JvmField
        val PLAYING_DEAD: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN)
        @JvmField
        val FROM_BUCKET: MetadataKey<Boolean> = MetadataKey(19, MetadataSerializers.BOOLEAN)
    }

    object Bee {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE)
        @JvmField
        val ANGER_TIME: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.INT)
    }

    object Cat {

        @JvmField
        val VARIANT: MetadataKey<Int> = MetadataKey(19, MetadataSerializers.INT)
        @JvmField
        val LYING: MetadataKey<Boolean> = MetadataKey(20, MetadataSerializers.BOOLEAN)
        @JvmField
        val RELAXED: MetadataKey<Boolean> = MetadataKey(21, MetadataSerializers.BOOLEAN)
        @JvmField
        val COLLAR_COLOR: MetadataKey<Int> = MetadataKey(22, MetadataSerializers.INT)
    }

    object Fox {

        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(18, MetadataSerializers.BYTE)
        @JvmField
        val FIRST_TRUSTED: MetadataKey<UUID?> = MetadataKey(19, MetadataSerializers.OPTIONAL_UUID)
        @JvmField
        val SECOND_TRUSTED: MetadataKey<UUID?> = MetadataKey(20, MetadataSerializers.OPTIONAL_UUID)
    }

    object Goat {

        @JvmField
        val SCREAMING: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
    }

    object Mooshroom {

        @JvmField
        val TYPE: MetadataKey<String> = MetadataKey(17, MetadataSerializers.STRING)
    }

    object Ocelot {

        @JvmField
        val TRUSTING: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
    }

    object Panda {

        @JvmField
        val UNHAPPY_TIMER: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
        @JvmField
        val SNEEZE_TIMER: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.INT)
        @JvmField
        val EATING_TIMER: MetadataKey<Int> = MetadataKey(19, MetadataSerializers.INT)
        @JvmField
        val MAIN_GENE: MetadataKey<Byte> = MetadataKey(20, MetadataSerializers.BYTE)
        @JvmField
        val HIDDEN_GENE: MetadataKey<Byte> = MetadataKey(21, MetadataSerializers.BYTE)
        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(22, MetadataSerializers.BYTE)
    }

    object Parrot {

        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(19, MetadataSerializers.INT)
    }

    object Pig {

        @JvmField
        val SADDLE: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
        @JvmField
        val BOOST_TIME: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.INT)
    }

    object PolarBear {

        @JvmField
        val STANDING: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
    }

    object Rabbit {

        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
    }

    object Sheep {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE)
    }

    object Turtle {

        @JvmField
        val HOME: MetadataKey<Vec3i> = MetadataKey(17, MetadataSerializers.BLOCK_POS)
        @JvmField
        val HAS_EGG: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN)
        @JvmField
        val LAYING_EGG: MetadataKey<Boolean> = MetadataKey(19, MetadataSerializers.BOOLEAN)
        @JvmField
        val DESTINATION: MetadataKey<Vec3i> = MetadataKey(20, MetadataSerializers.BLOCK_POS)
        @JvmField
        val GOING_HOME: MetadataKey<Boolean> = MetadataKey(21, MetadataSerializers.BOOLEAN)
        @JvmField
        val TRAVELLING: MetadataKey<Boolean> = MetadataKey(22, MetadataSerializers.BOOLEAN)
    }

    object Wolf {

        @JvmField
        val BEGGING: MetadataKey<Boolean> = MetadataKey(19, MetadataSerializers.BOOLEAN)
        @JvmField
        val COLLAR_COLOR: MetadataKey<Int> = MetadataKey(20, MetadataSerializers.INT)
        @JvmField
        val ANGER_TIME: MetadataKey<Int> = MetadataKey(21, MetadataSerializers.INT)
    }

    object Fish {

        @JvmField
        val FROM_BUCKET: MetadataKey<Boolean> = MetadataKey(16, MetadataSerializers.BOOLEAN)
    }

    object Dolphin {

        @JvmField
        val TREASURE_POSITION: MetadataKey<Vec3i> = MetadataKey(16, MetadataSerializers.BLOCK_POS)
        @JvmField
        val GOT_FISH: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
        @JvmField
        val MOISTURE: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.INT)
    }

    object Pufferfish {

        @JvmField
        val PUFF_STATE: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
    }

    object GlowSquid {

        @JvmField
        val REMAINING_DARK_TICKS: MetadataKey<Int> = MetadataKey(16, MetadataSerializers.INT)
    }

    object TropicalFish {

        @JvmField
        val VARIANT: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
    }
}
