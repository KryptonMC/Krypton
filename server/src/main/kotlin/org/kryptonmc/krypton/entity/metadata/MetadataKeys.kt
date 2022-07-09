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
import org.kryptonmc.api.util.Catalogue
import org.kryptonmc.krypton.effect.particle.ParticleOptions
import org.kryptonmc.krypton.entity.Pose
import org.kryptonmc.krypton.item.KryptonItemStack
import org.kryptonmc.nbt.CompoundTag
import org.spongepowered.math.vector.Vector3f
import org.spongepowered.math.vector.Vector3i
import java.util.OptionalInt
import java.util.UUID

@Catalogue(MetadataKey::class)
object MetadataKeys {

    @JvmField
    val AREA_EFFECT_CLOUD: AreaEffectCloudKeys = AreaEffectCloudKeys
    @JvmField
    val LIVING: LivingEntityKeys = LivingEntityKeys
    @JvmField
    val BAT: BatKeys = BatKeys
    @JvmField
    val BOAT: BoatKeys = BoatKeys
    @JvmField
    val ARMOR_STAND: ArmorStandKeys = ArmorStandKeys
    @JvmField
    val ARROW_LIKE: ArrowLikeKeys = ArrowLikeKeys
    @JvmField
    val ARROW: ArrowKeys = ArrowKeys
    @JvmField
    val TRIDENT: TridentKeys = TridentKeys
    @JvmField
    val FIREBALL: FireballKeys = FireballKeys
    @JvmField
    val FIREWORK_ROCKET: FireworkRocketKeys = FireworkRocketKeys
    @JvmField
    val FISHING_HOOK: FishingHookKeys = FishingHookKeys
    @JvmField
    val THROWABLE_PROJECTILE: ThrowableProjectileKeys = ThrowableProjectileKeys
    @JvmField
    val WITHER_SKULL: WitherSkullKeys = WitherSkullKeys
    @JvmField
    val MINECART_LIKE: MinecartLikeKeys = MinecartLikeKeys
    @JvmField
    val COMMAND_BLOCK_MINECART: CommandBlockMinecartKeys = CommandBlockMinecartKeys
    @JvmField
    val FURNACE_MINECART: FurnaceMinecartKeys = FurnaceMinecartKeys
    @JvmField
    val MOB: MobKeys = MobKeys
    @JvmField
    val CREEPER: CreeperKeys = CreeperKeys
    @JvmField
    val ZOMBIE: ZombieKeys = ZombieKeys
    @JvmField
    val PLAYER: PlayerKeys = PlayerKeys
    @JvmField
    val AGEABLE: AgeableKeys = AgeableKeys
    @JvmField
    val TAMABLE: TamableKeys = TamableKeys
    @JvmField
    val AXOLOTL: AxolotlKeys = AxolotlKeys
    @JvmField
    val BEE: BeeKeys = BeeKeys
    @JvmField
    val CAT: CatKeys = CatKeys
    @JvmField
    val FOX: FoxKeys = FoxKeys
    @JvmField
    val GOAT: GoatKeys = GoatKeys
    @JvmField
    val MOOSHROOM: MooshroomKeys = MooshroomKeys
    @JvmField
    val OCELOT: OcelotKeys = OcelotKeys
    @JvmField
    val PANDA: PandaKeys = PandaKeys
    @JvmField
    val PARROT: ParrotKeys = ParrotKeys
    @JvmField
    val PIG: PigKeys = PigKeys
    @JvmField
    val POLAR_BEAR: PolarBearKeys = PolarBearKeys
    @JvmField
    val RABBIT: RabbitKeys = RabbitKeys
    @JvmField
    val SHEEP: SheepKeys = SheepKeys
    @JvmField
    val TURTLE: TurtleKeys = TurtleKeys
    @JvmField
    val WOLF: WolfKeys = WolfKeys
    @JvmField
    val FISH: FishKeys = FishKeys
    @JvmField
    val DOLPHIN: DolphinKeys = DolphinKeys
    @JvmField
    val PUFFERFISH: PufferfishKeys = PufferfishKeys
    @JvmField
    val GLOW_SQUID: GlowSquidKeys = GlowSquidKeys
    @JvmField
    val TROPICAL_FISH: TropicalFishKeys = TropicalFishKeys

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

    object LivingEntityKeys {

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
        val BED_LOCATION: MetadataKey<Vector3i?> = MetadataKey(14, MetadataSerializers.OPTIONAL_POSITION)
    }

    object ArmorStandKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(15, MetadataSerializers.BYTE)
        @JvmField
        val HEAD_ROTATION: MetadataKey<Vector3f> = MetadataKey(16, MetadataSerializers.ROTATION)
        @JvmField
        val BODY_ROTATION: MetadataKey<Vector3f> = MetadataKey(17, MetadataSerializers.ROTATION)
        @JvmField
        val LEFT_ARM_ROTATION: MetadataKey<Vector3f> = MetadataKey(18, MetadataSerializers.ROTATION)
        @JvmField
        val RIGHT_ARM_ROTATION: MetadataKey<Vector3f> = MetadataKey(19, MetadataSerializers.ROTATION)
        @JvmField
        val LEFT_LEG_ROTATION: MetadataKey<Vector3f> = MetadataKey(20, MetadataSerializers.ROTATION)
        @JvmField
        val RIGHT_LEG_ROTATION: MetadataKey<Vector3f> = MetadataKey(21, MetadataSerializers.ROTATION)
    }

    object ArrowLikeKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(8, MetadataSerializers.BYTE)
        @JvmField
        val PIERCING_LEVEL: MetadataKey<Byte> = MetadataKey(9, MetadataSerializers.BYTE)
    }

    object ArrowKeys {

        @JvmField
        val COLOR: MetadataKey<Int> = MetadataKey(10, MetadataSerializers.INT)
    }

    object TridentKeys {

        @JvmField
        val LOYALTY_LEVEL: MetadataKey<Int> = MetadataKey(10, MetadataSerializers.INT)
        @JvmField
        val ENCHANTED: MetadataKey<Boolean> = MetadataKey(11, MetadataSerializers.BOOLEAN)
    }

    object FireballKeys {

        @JvmField
        val ITEM: MetadataKey<KryptonItemStack> = MetadataKey(8, MetadataSerializers.ITEM_STACK)
    }

    object FireworkRocketKeys {

        @JvmField
        val ITEM: MetadataKey<KryptonItemStack> = MetadataKey(8, MetadataSerializers.ITEM_STACK)
        @JvmField
        val ATTACHED: MetadataKey<OptionalInt> = MetadataKey(9, MetadataSerializers.OPTIONAL_INT)
        @JvmField
        val SHOT_AT_ANGLE: MetadataKey<Boolean> = MetadataKey(10, MetadataSerializers.BOOLEAN)
    }

    object FishingHookKeys {

        @JvmField
        val HOOKED: MetadataKey<Int> = MetadataKey(8, MetadataSerializers.INT)
        @JvmField
        val BITING: MetadataKey<Boolean> = MetadataKey(9, MetadataSerializers.BOOLEAN)
    }

    object ThrowableProjectileKeys {

        @JvmField
        val ITEM: MetadataKey<KryptonItemStack> = MetadataKey(8, MetadataSerializers.ITEM_STACK)
    }

    object WitherSkullKeys {

        @JvmField
        val DANGEROUS: MetadataKey<Boolean> = MetadataKey(8, MetadataSerializers.BOOLEAN)
    }

    object MinecartLikeKeys {

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

    object CommandBlockMinecartKeys {

        @JvmField
        val COMMAND: MetadataKey<String> = MetadataKey(14, MetadataSerializers.STRING)
        @JvmField
        val LAST_OUTPUT: MetadataKey<Component> = MetadataKey(15, MetadataSerializers.COMPONENT)
    }

    object FurnaceMinecartKeys {

        @JvmField
        val HAS_FUEL: MetadataKey<Boolean> = MetadataKey(14, MetadataSerializers.BOOLEAN)
    }

    object MobKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(15, MetadataSerializers.BYTE)
    }

    object CreeperKeys {

        @JvmField
        val STATE: MetadataKey<Int> = MetadataKey(16, MetadataSerializers.INT)
        @JvmField
        val CHARGED: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
        @JvmField
        val IGNITED: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN)
    }

    object ZombieKeys {

        @JvmField
        val BABY: MetadataKey<Boolean> = MetadataKey(16, MetadataSerializers.BOOLEAN)
        @JvmField
        val CONVERTING: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN)
    }

    object PlayerKeys {

        @JvmField
        val ADDITIONAL_HEARTS: MetadataKey<Float> = MetadataKey(15, MetadataSerializers.FLOAT)
        @JvmField
        val SCORE: MetadataKey<Int> = MetadataKey(16, MetadataSerializers.INT)
        @JvmField
        val SKIN_FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE)
        @JvmField
        val MAIN_HAND: MetadataKey<Byte> = MetadataKey(18, MetadataSerializers.BYTE)
        @JvmField
        val LEFT_SHOULDER: MetadataKey<CompoundTag> = MetadataKey(19, MetadataSerializers.NBT)
        @JvmField
        val RIGHT_SHOULDER: MetadataKey<CompoundTag> = MetadataKey(20, MetadataSerializers.NBT)
    }

    object AreaEffectCloudKeys {

        @JvmField
        val RADIUS: MetadataKey<Float> = MetadataKey(8, MetadataSerializers.FLOAT)
        @JvmField
        val COLOR: MetadataKey<Int> = MetadataKey(9, MetadataSerializers.INT)
        @JvmField
        val IGNORE_RADIUS: MetadataKey<Boolean> = MetadataKey(10, MetadataSerializers.BOOLEAN)
        @JvmField
        val PARTICLE: MetadataKey<ParticleOptions> = MetadataKey(11, MetadataSerializers.PARTICLE)
    }

    object BatKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(16, MetadataSerializers.BYTE)
    }

    object BoatKeys {

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

    object AgeableKeys {

        @JvmField
        val BABY: MetadataKey<Boolean> = MetadataKey(16, MetadataSerializers.BOOLEAN)
    }

    object TamableKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE)
        @JvmField
        val OWNER: MetadataKey<UUID?> = MetadataKey(18, MetadataSerializers.OPTIONAL_UUID)
    }

    object AxolotlKeys {

        @JvmField
        val VARIANT: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
        @JvmField
        val PLAYING_DEAD: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN)
        @JvmField
        val FROM_BUCKET: MetadataKey<Boolean> = MetadataKey(19, MetadataSerializers.BOOLEAN)
    }

    object BeeKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE)
        @JvmField
        val ANGER_TIME: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.INT)
    }

    object CatKeys {

        @JvmField
        val VARIANT: MetadataKey<Int> = MetadataKey(19, MetadataSerializers.INT)
        @JvmField
        val LYING: MetadataKey<Boolean> = MetadataKey(20, MetadataSerializers.BOOLEAN)
        @JvmField
        val RELAXED: MetadataKey<Boolean> = MetadataKey(21, MetadataSerializers.BOOLEAN)
        @JvmField
        val COLLAR_COLOR: MetadataKey<Int> = MetadataKey(22, MetadataSerializers.INT)
    }

    object FoxKeys {

        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(18, MetadataSerializers.BYTE)
        @JvmField
        val FIRST_TRUSTED: MetadataKey<UUID?> = MetadataKey(19, MetadataSerializers.OPTIONAL_UUID)
        @JvmField
        val SECOND_TRUSTED: MetadataKey<UUID?> = MetadataKey(20, MetadataSerializers.OPTIONAL_UUID)
    }

    object GoatKeys {

        @JvmField
        val SCREAMING: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
    }

    object MooshroomKeys {

        @JvmField
        val TYPE: MetadataKey<String> = MetadataKey(17, MetadataSerializers.STRING)
    }

    object OcelotKeys {

        @JvmField
        val TRUSTING: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
    }

    object PandaKeys {

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

    object ParrotKeys {

        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(19, MetadataSerializers.INT)
    }

    object PigKeys {

        @JvmField
        val SADDLE: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
        @JvmField
        val BOOST_TIME: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.INT)
    }

    object PolarBearKeys {

        @JvmField
        val STANDING: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
    }

    object RabbitKeys {

        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
    }

    object SheepKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE)
    }

    object TurtleKeys {

        @JvmField
        val HOME: MetadataKey<Vector3i> = MetadataKey(17, MetadataSerializers.POSITION)
        @JvmField
        val HAS_EGG: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN)
        @JvmField
        val LAYING_EGG: MetadataKey<Boolean> = MetadataKey(19, MetadataSerializers.BOOLEAN)
        @JvmField
        val DESTINATION: MetadataKey<Vector3i> = MetadataKey(20, MetadataSerializers.POSITION)
        @JvmField
        val GOING_HOME: MetadataKey<Boolean> = MetadataKey(21, MetadataSerializers.BOOLEAN)
        @JvmField
        val TRAVELLING: MetadataKey<Boolean> = MetadataKey(22, MetadataSerializers.BOOLEAN)
    }

    object WolfKeys {

        @JvmField
        val BEGGING: MetadataKey<Boolean> = MetadataKey(19, MetadataSerializers.BOOLEAN)
        @JvmField
        val COLLAR_COLOR: MetadataKey<Int> = MetadataKey(20, MetadataSerializers.INT)
        @JvmField
        val ANGER_TIME: MetadataKey<Int> = MetadataKey(21, MetadataSerializers.INT)
    }

    object FishKeys {

        @JvmField
        val FROM_BUCKET: MetadataKey<Boolean> = MetadataKey(16, MetadataSerializers.BOOLEAN)
    }

    object DolphinKeys {

        @JvmField
        val TREASURE_POSITION: MetadataKey<Vector3i> = MetadataKey(16, MetadataSerializers.POSITION)
        @JvmField
        val GOT_FISH: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN)
        @JvmField
        val MOISTURE: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.INT)
    }

    object PufferfishKeys {

        @JvmField
        val PUFF_STATE: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
    }

    object GlowSquidKeys {

        @JvmField
        val REMAINING_DARK_TICKS: MetadataKey<Int> = MetadataKey(16, MetadataSerializers.INT)
    }

    object TropicalFishKeys {

        @JvmField
        val VARIANT: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.INT)
    }
}
