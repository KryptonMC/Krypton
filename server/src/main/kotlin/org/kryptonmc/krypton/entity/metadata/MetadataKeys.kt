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
import org.kryptonmc.api.effect.particle.ParticleEffect
import org.kryptonmc.api.effect.particle.ParticleTypes
import org.kryptonmc.api.effect.particle.particleEffect
import org.kryptonmc.api.entity.animal.type.AxolotlVariant
import org.kryptonmc.api.entity.animal.type.CatType
import org.kryptonmc.api.entity.animal.type.FoxType
import org.kryptonmc.api.entity.animal.type.MooshroomType
import org.kryptonmc.api.entity.animal.type.ParrotType
import org.kryptonmc.api.entity.aquatic.TropicalFishVariant
import org.kryptonmc.api.item.data.DyeColors
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.api.util.Catalogue
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
    val FLAGS: MetadataKey<Byte> = MetadataKey(0, MetadataSerializers.BYTE, 0)
    @JvmField
    val AIR_TICKS: MetadataKey<Int> = MetadataKey(1, MetadataSerializers.VAR_INT, 300)
    @JvmField
    val CUSTOM_NAME: MetadataKey<Component?> = MetadataKey(2, MetadataSerializers.OPTIONAL_COMPONENT, null)
    @JvmField
    val CUSTOM_NAME_VISIBILITY: MetadataKey<Boolean> = MetadataKey(3, MetadataSerializers.BOOLEAN, false)
    @JvmField
    val SILENT: MetadataKey<Boolean> = MetadataKey(4, MetadataSerializers.BOOLEAN, false)
    @JvmField
    val NO_GRAVITY: MetadataKey<Boolean> = MetadataKey(5, MetadataSerializers.BOOLEAN, false)
    @JvmField
    val POSE: MetadataKey<Pose> = MetadataKey(6, MetadataSerializers.POSE, Pose.STANDING)
    @JvmField
    val FROZEN_TICKS: MetadataKey<Int> = MetadataKey(7, MetadataSerializers.VAR_INT, 0)

    object LivingEntityKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(8, MetadataSerializers.BYTE, 0)
        @JvmField
        val HEALTH: MetadataKey<Float> = MetadataKey(9, MetadataSerializers.FLOAT, 1F)
        @JvmField
        val POTION_EFFECT_COLOR: MetadataKey<Int> = MetadataKey(10, MetadataSerializers.VAR_INT, 0)
        @JvmField
        val POTION_EFFECT_AMBIENCE: MetadataKey<Boolean> = MetadataKey(11, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val ARROWS: MetadataKey<Int> = MetadataKey(12, MetadataSerializers.VAR_INT, 0)
        @JvmField
        val STINGERS: MetadataKey<Int> = MetadataKey(13, MetadataSerializers.VAR_INT, 0)
        @JvmField
        val BED_LOCATION: MetadataKey<Vector3i?> = MetadataKey(14, MetadataSerializers.OPTIONAL_POSITION, null)
    }

    object ArmorStandKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(15, MetadataSerializers.BYTE, 0)
        @JvmField
        val HEAD_ROTATION: MetadataKey<Vector3f> = MetadataKey(16, MetadataSerializers.ROTATION, Vector3f.ZERO)
        @JvmField
        val BODY_ROTATION: MetadataKey<Vector3f> = MetadataKey(17, MetadataSerializers.ROTATION, Vector3f.ZERO)
        @JvmField
        val LEFT_ARM_ROTATION: MetadataKey<Vector3f> = MetadataKey(18, MetadataSerializers.ROTATION, Vector3f(-10F, 0F, -10F))
        @JvmField
        val RIGHT_ARM_ROTATION: MetadataKey<Vector3f> = MetadataKey(19, MetadataSerializers.ROTATION, Vector3f(-15F, 0F, 10F))
        @JvmField
        val LEFT_LEG_ROTATION: MetadataKey<Vector3f> = MetadataKey(20, MetadataSerializers.ROTATION, Vector3f(-1F, 0F, -1F))
        @JvmField
        val RIGHT_LEG_ROTATION: MetadataKey<Vector3f> = MetadataKey(21, MetadataSerializers.ROTATION, Vector3f(1F, 0F, 1F))
    }

    object ArrowLikeKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(8, MetadataSerializers.BYTE, 0)
        @JvmField
        val PIERCING_LEVEL: MetadataKey<Byte> = MetadataKey(9, MetadataSerializers.BYTE, 0)
    }

    object ArrowKeys {

        @JvmField
        val COLOR: MetadataKey<Int> = MetadataKey(10, MetadataSerializers.VAR_INT, -1)
    }

    object TridentKeys {

        @JvmField
        val LOYALTY_LEVEL: MetadataKey<Int> = MetadataKey(10, MetadataSerializers.VAR_INT, 0)
        @JvmField
        val ENCHANTED: MetadataKey<Boolean> = MetadataKey(11, MetadataSerializers.BOOLEAN, false)
    }

    object FireballKeys {

        @JvmField
        val ITEM: MetadataKey<KryptonItemStack> = MetadataKey(8, MetadataSerializers.SLOT, KryptonItemStack.EMPTY)
    }

    object FireworkRocketKeys {

        @JvmField
        val ITEM: MetadataKey<KryptonItemStack> = MetadataKey(8, MetadataSerializers.SLOT, KryptonItemStack.EMPTY)
        @JvmField
        val ATTACHED: MetadataKey<OptionalInt> = MetadataKey(9, MetadataSerializers.OPTIONAL_VAR_INT, OptionalInt.empty())
        @JvmField
        val SHOT_AT_ANGLE: MetadataKey<Boolean> = MetadataKey(10, MetadataSerializers.BOOLEAN, false)
    }

    object FishingHookKeys {

        @JvmField
        val HOOKED: MetadataKey<Int> = MetadataKey(8, MetadataSerializers.VAR_INT, 0)
        @JvmField
        val BITING: MetadataKey<Boolean> = MetadataKey(9, MetadataSerializers.BOOLEAN, false)
    }

    object ThrowableProjectileKeys {

        @JvmField
        val ITEM: MetadataKey<KryptonItemStack> = MetadataKey(8, MetadataSerializers.SLOT, KryptonItemStack.EMPTY)
    }

    object WitherSkullKeys {

        @JvmField
        val DANGEROUS: MetadataKey<Boolean> = MetadataKey(8, MetadataSerializers.BOOLEAN, false)
    }

    object MobKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(15, MetadataSerializers.BYTE, 0)
    }

    object CreeperKeys {

        @JvmField
        val STATE: MetadataKey<Int> = MetadataKey(16, MetadataSerializers.VAR_INT, -1)
        @JvmField
        val CHARGED: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val IGNITED: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN, false)
    }

    object ZombieKeys {

        @JvmField
        val BABY: MetadataKey<Boolean> = MetadataKey(16, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val CONVERTING: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN, false)
    }

    object PlayerKeys {

        @JvmField
        val ADDITIONAL_HEARTS: MetadataKey<Float> = MetadataKey(15, MetadataSerializers.FLOAT, 0F)
        @JvmField
        val SCORE: MetadataKey<Int> = MetadataKey(16, MetadataSerializers.VAR_INT, 0)
        @JvmField
        val SKIN_FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE, 0)
        @JvmField
        val MAIN_HAND: MetadataKey<Byte> = MetadataKey(18, MetadataSerializers.BYTE, 1)
        @JvmField
        val LEFT_SHOULDER: MetadataKey<CompoundTag> = MetadataKey(19, MetadataSerializers.NBT, CompoundTag.empty())
        @JvmField
        val RIGHT_SHOULDER: MetadataKey<CompoundTag> = MetadataKey(20, MetadataSerializers.NBT, CompoundTag.empty())
    }

    object AreaEffectCloudKeys {

        @JvmField
        val RADIUS: MetadataKey<Float> = MetadataKey(8, MetadataSerializers.FLOAT, 0.5F)
        @JvmField
        val COLOR: MetadataKey<Int> = MetadataKey(9, MetadataSerializers.VAR_INT, 0)
        @JvmField
        val IGNORE_RADIUS: MetadataKey<Boolean> = MetadataKey(10, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val PARTICLE: MetadataKey<ParticleEffect> = MetadataKey(11, MetadataSerializers.PARTICLE, particleEffect(ParticleTypes.EFFECT))
    }

    object BatKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(16, MetadataSerializers.BYTE, 0)
    }

    object AgeableKeys {

        @JvmField
        val BABY: MetadataKey<Boolean> = MetadataKey(16, MetadataSerializers.BOOLEAN, false)
    }

    object TamableKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE, 0)
        @JvmField
        val OWNER: MetadataKey<UUID?> = MetadataKey(18, MetadataSerializers.OPTIONAL_UUID, null)
    }

    object AxolotlKeys {

        @JvmField
        val VARIANT: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.VAR_INT, AxolotlVariant.LUCY.ordinal)
        @JvmField
        val PLAYING_DEAD: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val FROM_BUCKET: MetadataKey<Boolean> = MetadataKey(19, MetadataSerializers.BOOLEAN, false)
    }

    object BeeKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE, 0)
        @JvmField
        val ANGER_TIME: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.VAR_INT, 0)
    }

    object CatKeys {

        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(19, MetadataSerializers.VAR_INT, CatType.BLACK.ordinal)
        @JvmField
        val LYING: MetadataKey<Boolean> = MetadataKey(20, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val RELAXED: MetadataKey<Boolean> = MetadataKey(21, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val COLLAR_COLOR: MetadataKey<Int> = MetadataKey(22, MetadataSerializers.VAR_INT, Registries.DYE_COLORS.idOf(DyeColors.RED))
    }

    object FoxKeys {

        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.VAR_INT, FoxType.RED.ordinal)
        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(18, MetadataSerializers.BYTE, 0)
        @JvmField
        val FIRST_TRUSTED: MetadataKey<UUID?> = MetadataKey(19, MetadataSerializers.OPTIONAL_UUID, null)
        @JvmField
        val SECOND_TRUSTED: MetadataKey<UUID?> = MetadataKey(20, MetadataSerializers.OPTIONAL_UUID, null)
    }

    object GoatKeys {

        @JvmField
        val SCREAMING: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN, false)
    }

    object MooshroomKeys {

        @JvmField
        val TYPE: MetadataKey<String> = MetadataKey(17, MetadataSerializers.STRING, MooshroomType.RED.name.lowercase())
    }

    object OcelotKeys {

        @JvmField
        val TRUSTING: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN, false)
    }

    object PandaKeys {

        @JvmField
        val UNHAPPY_TIMER: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.VAR_INT, 0)
        @JvmField
        val SNEEZE_TIMER: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.VAR_INT, 0)
        @JvmField
        val EATING_TIMER: MetadataKey<Int> = MetadataKey(19, MetadataSerializers.VAR_INT, 0)
        @JvmField
        val MAIN_GENE: MetadataKey<Byte> = MetadataKey(20, MetadataSerializers.BYTE, 0)
        @JvmField
        val HIDDEN_GENE: MetadataKey<Byte> = MetadataKey(21, MetadataSerializers.BYTE, 0)
        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(22, MetadataSerializers.BYTE, 0)
    }

    object ParrotKeys {

        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(19, MetadataSerializers.VAR_INT, ParrotType.RED_AND_BLUE.ordinal)
    }

    object PigKeys {

        @JvmField
        val SADDLE: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val BOOST_TIME: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.VAR_INT, 0)
    }

    object PolarBearKeys {

        @JvmField
        val STANDING: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN, false)
    }

    object RabbitKeys {

        @JvmField
        val TYPE: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.VAR_INT, 0)
    }

    object SheepKeys {

        @JvmField
        val FLAGS: MetadataKey<Byte> = MetadataKey(17, MetadataSerializers.BYTE, 0)
    }

    object TurtleKeys {

        @JvmField
        val HOME: MetadataKey<Vector3i> = MetadataKey(17, MetadataSerializers.POSITION, Vector3i.ZERO)
        @JvmField
        val HAS_EGG: MetadataKey<Boolean> = MetadataKey(18, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val LAYING_EGG: MetadataKey<Boolean> = MetadataKey(19, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val DESTINATION: MetadataKey<Vector3i> = MetadataKey(20, MetadataSerializers.POSITION, Vector3i.ZERO)
        @JvmField
        val GOING_HOME: MetadataKey<Boolean> = MetadataKey(21, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val TRAVELLING: MetadataKey<Boolean> = MetadataKey(22, MetadataSerializers.BOOLEAN, false)
    }

    object WolfKeys {

        @JvmField
        val BEGGING: MetadataKey<Boolean> = MetadataKey(19, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val COLLAR_COLOR: MetadataKey<Int> = MetadataKey(20, MetadataSerializers.VAR_INT, Registries.DYE_COLORS.idOf(DyeColors.RED))
        @JvmField
        val ANGER_TIME: MetadataKey<Int> = MetadataKey(21, MetadataSerializers.VAR_INT, 0)
    }

    object FishKeys {

        @JvmField
        val FROM_BUCKET: MetadataKey<Boolean> = MetadataKey(16, MetadataSerializers.BOOLEAN, false)
    }

    object DolphinKeys {

        @JvmField
        val TREASURE_POSITION: MetadataKey<Vector3i> = MetadataKey(16, MetadataSerializers.POSITION, Vector3i.ZERO)
        @JvmField
        val GOT_FISH: MetadataKey<Boolean> = MetadataKey(17, MetadataSerializers.BOOLEAN, false)
        @JvmField
        val MOISTURE: MetadataKey<Int> = MetadataKey(18, MetadataSerializers.VAR_INT, 2400)
    }

    object PufferfishKeys {

        @JvmField
        val PUFF_STATE: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.VAR_INT, 0)
    }

    object GlowSquidKeys {

        @JvmField
        val REMAINING_DARK_TICKS: MetadataKey<Int> = MetadataKey(16, MetadataSerializers.VAR_INT, 0)
    }

    object TropicalFishKeys {

        @JvmField
        val VARIANT: MetadataKey<Int> = MetadataKey(17, MetadataSerializers.VAR_INT, TropicalFishVariant.KOB.ordinal)
    }
}
