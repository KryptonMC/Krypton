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
import org.kryptonmc.api.entity.aquatic.TropicalFishShape
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

    @JvmField val AREA_EFFECT_CLOUD: AreaEffectCloudKeys = AreaEffectCloudKeys
    @JvmField val LIVING: LivingEntityKeys = LivingEntityKeys
    @JvmField val BAT: BatKeys = BatKeys
    @JvmField val ARMOR_STAND: ArmorStandKeys = ArmorStandKeys
    @JvmField val ARROW_LIKE: ArrowLikeKeys = ArrowLikeKeys
    @JvmField val ARROW: ArrowKeys = ArrowKeys
    @JvmField val TRIDENT: TridentKeys = TridentKeys
    @JvmField val FIREBALL: FireballKeys = FireballKeys
    @JvmField val FIREWORK_ROCKET: FireworkRocketKeys = FireworkRocketKeys
    @JvmField val FISHING_HOOK: FishingHookKeys = FishingHookKeys
    @JvmField val THROWABLE_PROJECTILE: ThrowableProjectileKeys = ThrowableProjectileKeys
    @JvmField val WITHER_SKULL: WitherSkullKeys = WitherSkullKeys
    @JvmField val MOB: MobKeys = MobKeys
    @JvmField val CREEPER: CreeperKeys = CreeperKeys
    @JvmField val ZOMBIE: ZombieKeys = ZombieKeys
    @JvmField val PLAYER: PlayerKeys = PlayerKeys
    @JvmField val AGEABLE: AgeableKeys = AgeableKeys
    @JvmField val TAMABLE: TamableKeys = TamableKeys
    @JvmField val AXOLOTL: AxolotlKeys = AxolotlKeys
    @JvmField val BEE: BeeKeys = BeeKeys
    @JvmField val CAT: CatKeys = CatKeys
    @JvmField val FOX: FoxKeys = FoxKeys
    @JvmField val GOAT: GoatKeys = GoatKeys
    @JvmField val MOOSHROOM: MooshroomKeys = MooshroomKeys
    @JvmField val OCELOT: OcelotKeys = OcelotKeys
    @JvmField val PANDA: PandaKeys = PandaKeys
    @JvmField val PARROT: ParrotKeys = ParrotKeys
    @JvmField val PIG: PigKeys = PigKeys
    @JvmField val POLAR_BEAR: PolarBearKeys = PolarBearKeys
    @JvmField val RABBIT: RabbitKeys = RabbitKeys
    @JvmField val SHEEP: SheepKeys = SheepKeys
    @JvmField val TURTLE: TurtleKeys = TurtleKeys
    @JvmField val WOLF: WolfKeys = WolfKeys
    @JvmField val FISH: FishKeys = FishKeys
    @JvmField val DOLPHIN: DolphinKeys = DolphinKeys
    @JvmField val PUFFERFISH: PufferfishKeys = PufferfishKeys
    @JvmField val GLOW_SQUID: GlowSquidKeys = GlowSquidKeys
    @JvmField val TROPICAL_FISH: TropicalFishKeys = TropicalFishKeys

    @JvmField val FLAGS: MetadataKey<Byte> = create(0, MetadataSerializers.BYTE, 0)
    @JvmField val AIR_TICKS: MetadataKey<Int> = create(1, MetadataSerializers.VAR_INT, 300)
    @JvmField val CUSTOM_NAME: MetadataKey<Component?> = create(2, MetadataSerializers.OPTIONAL_COMPONENT, null)
    @JvmField val CUSTOM_NAME_VISIBILITY: MetadataKey<Boolean> = create(3, MetadataSerializers.BOOLEAN, false)
    @JvmField val SILENT: MetadataKey<Boolean> = create(4, MetadataSerializers.BOOLEAN, false)
    @JvmField val NO_GRAVITY: MetadataKey<Boolean> = create(5, MetadataSerializers.BOOLEAN, false)
    @JvmField val POSE: MetadataKey<Pose> = create(6, MetadataSerializers.POSE, Pose.STANDING)
    @JvmField val FROZEN_TICKS: MetadataKey<Int> = create(7, MetadataSerializers.VAR_INT, 0)

    object LivingEntityKeys {

        @JvmField val FLAGS: MetadataKey<Byte> = create(8, MetadataSerializers.BYTE, 0)
        @JvmField val HEALTH: MetadataKey<Float> = create(9, MetadataSerializers.FLOAT, 1F)
        @JvmField val POTION_EFFECT_COLOR: MetadataKey<Int> = create(10, MetadataSerializers.VAR_INT, 0)
        @JvmField val POTION_EFFECT_AMBIENCE: MetadataKey<Boolean> = create(11, MetadataSerializers.BOOLEAN, false)
        @JvmField val ARROWS: MetadataKey<Int> = create(12, MetadataSerializers.VAR_INT, 0)
        @JvmField val STINGERS: MetadataKey<Int> = create(13, MetadataSerializers.VAR_INT, 0)
        @JvmField val BED_LOCATION: MetadataKey<Vector3i?> = create(14, MetadataSerializers.OPTIONAL_POSITION, null)
    }

    object ArmorStandKeys {

        @JvmField val FLAGS: MetadataKey<Byte> = create(15, MetadataSerializers.BYTE, 0)
        @JvmField val HEAD_ROTATION: MetadataKey<Vector3f> = create(16, MetadataSerializers.ROTATION, Vector3f.ZERO)
        @JvmField val BODY_ROTATION: MetadataKey<Vector3f> = create(17, MetadataSerializers.ROTATION, Vector3f.ZERO)
        @JvmField val LEFT_ARM_ROTATION: MetadataKey<Vector3f> = create(18, MetadataSerializers.ROTATION, Vector3f(-10F, 0F, -10F))
        @JvmField val RIGHT_ARM_ROTATION: MetadataKey<Vector3f> = create(19, MetadataSerializers.ROTATION, Vector3f(-15F, 0F, 10F))
        @JvmField val LEFT_LEG_ROTATION: MetadataKey<Vector3f> = create(20, MetadataSerializers.ROTATION, Vector3f(-1F, 0F, -1F))
        @JvmField val RIGHT_LEG_ROTATION: MetadataKey<Vector3f> = create(21, MetadataSerializers.ROTATION, Vector3f(1F, 0F, 1F))
    }

    object ArrowLikeKeys {

        @JvmField val FLAGS: MetadataKey<Byte> = create(8, MetadataSerializers.BYTE, 0)
        @JvmField val PIERCING_LEVEL: MetadataKey<Byte> = create(9, MetadataSerializers.BYTE, 0)
    }

    object ArrowKeys {

        @JvmField val COLOR: MetadataKey<Int> = create(10, MetadataSerializers.VAR_INT, -1)
    }

    object TridentKeys {

        @JvmField val LOYALTY_LEVEL: MetadataKey<Int> = create(10, MetadataSerializers.VAR_INT, 0)
        @JvmField val ENCHANTED: MetadataKey<Boolean> = create(11, MetadataSerializers.BOOLEAN, false)
    }

    object FireballKeys {

        @JvmField val ITEM: MetadataKey<KryptonItemStack> = create(8, MetadataSerializers.SLOT, KryptonItemStack.EMPTY)
    }

    object FireworkRocketKeys {

        @JvmField val ITEM: MetadataKey<KryptonItemStack> = create(8, MetadataSerializers.SLOT, KryptonItemStack.EMPTY)
        @JvmField val ATTACHED: MetadataKey<OptionalInt> = create(9, MetadataSerializers.OPTIONAL_VAR_INT, OptionalInt.empty())
        @JvmField val SHOT_AT_ANGLE: MetadataKey<Boolean> = create(10, MetadataSerializers.BOOLEAN, false)
    }

    object FishingHookKeys {

        @JvmField val HOOKED: MetadataKey<Int> = create(8, MetadataSerializers.VAR_INT, 0)
        @JvmField val BITING: MetadataKey<Boolean> = create(9, MetadataSerializers.BOOLEAN, false)
    }

    object ThrowableProjectileKeys {

        @JvmField val ITEM: MetadataKey<KryptonItemStack> = create(8, MetadataSerializers.SLOT, KryptonItemStack.EMPTY)
    }

    object WitherSkullKeys {

        @JvmField val DANGEROUS: MetadataKey<Boolean> = create(8, MetadataSerializers.BOOLEAN, false)
    }

    object MobKeys {

        @JvmField val FLAGS: MetadataKey<Byte> = create(15, MetadataSerializers.BYTE, 0)
    }

    object CreeperKeys {

        @JvmField val STATE: MetadataKey<Int> = create(16, MetadataSerializers.VAR_INT, -1)
        @JvmField val CHARGED: MetadataKey<Boolean> = create(17, MetadataSerializers.BOOLEAN, false)
        @JvmField val IGNITED: MetadataKey<Boolean> = create(18, MetadataSerializers.BOOLEAN, false)
    }

    object ZombieKeys {

        @JvmField val BABY: MetadataKey<Boolean> = create(16, MetadataSerializers.BOOLEAN, false)
        @JvmField val CONVERTING: MetadataKey<Boolean> = create(18, MetadataSerializers.BOOLEAN, false)
    }

    object PlayerKeys {

        @JvmField val ADDITIONAL_HEARTS: MetadataKey<Float> = create(15, MetadataSerializers.FLOAT, 0F)
        @JvmField val SCORE: MetadataKey<Int> = create(16, MetadataSerializers.VAR_INT, 0)
        @JvmField val SKIN_FLAGS: MetadataKey<Byte> = create(17, MetadataSerializers.BYTE, 0)
        @JvmField val MAIN_HAND: MetadataKey<Byte> = create(18, MetadataSerializers.BYTE, 1)
        @JvmField val LEFT_SHOULDER: MetadataKey<CompoundTag> = create(19, MetadataSerializers.NBT, CompoundTag.empty())
        @JvmField val RIGHT_SHOULDER: MetadataKey<CompoundTag> = create(20, MetadataSerializers.NBT, CompoundTag.empty())
    }

    object AreaEffectCloudKeys {

        @JvmField val RADIUS: MetadataKey<Float> = create(8, MetadataSerializers.FLOAT, 0.5F)
        @JvmField val COLOR: MetadataKey<Int> = create(9, MetadataSerializers.VAR_INT, 0)
        @JvmField val IGNORE_RADIUS: MetadataKey<Boolean> = create(10, MetadataSerializers.BOOLEAN, false)
        @JvmField val PARTICLE: MetadataKey<ParticleEffect> = create(11, MetadataSerializers.PARTICLE, particleEffect(ParticleTypes.EFFECT))
    }

    object BatKeys {

        @JvmField val FLAGS: MetadataKey<Byte> = create(16, MetadataSerializers.BYTE, 0)
    }

    object AgeableKeys {

        @JvmField val BABY: MetadataKey<Boolean> = create(16, MetadataSerializers.BOOLEAN, false)
    }

    object TamableKeys {

        @JvmField val FLAGS: MetadataKey<Byte> = create(17, MetadataSerializers.BYTE, 0)
        @JvmField val OWNER: MetadataKey<UUID?> = create(18, MetadataSerializers.OPTIONAL_UUID, null)
    }

    object AxolotlKeys {

        @JvmField val VARIANT: MetadataKey<Int> = create(17, MetadataSerializers.VAR_INT, AxolotlVariant.LUCY.ordinal)
        @JvmField val PLAYING_DEAD: MetadataKey<Boolean> = create(18, MetadataSerializers.BOOLEAN, false)
        @JvmField val FROM_BUCKET: MetadataKey<Boolean> = create(19, MetadataSerializers.BOOLEAN, false)
    }

    object BeeKeys {

        @JvmField val FLAGS: MetadataKey<Byte> = create(17, MetadataSerializers.BYTE, 0)
        @JvmField val ANGER_TIME: MetadataKey<Int> = create(18, MetadataSerializers.VAR_INT, 0)
    }

    object CatKeys {

        @JvmField val TYPE: MetadataKey<Int> = create(19, MetadataSerializers.VAR_INT, CatType.BLACK.ordinal)
        @JvmField val LYING: MetadataKey<Boolean> = create(20, MetadataSerializers.BOOLEAN, false)
        @JvmField val RELAXED: MetadataKey<Boolean> = create(21, MetadataSerializers.BOOLEAN, false)
        @JvmField val COLLAR_COLOR: MetadataKey<Int> = create(22, MetadataSerializers.VAR_INT, Registries.DYE_COLORS.idOf(DyeColors.RED))
    }

    object FoxKeys {

        @JvmField val TYPE: MetadataKey<Int> = create(17, MetadataSerializers.VAR_INT, FoxType.RED.ordinal)
        @JvmField val FLAGS: MetadataKey<Byte> = create(18, MetadataSerializers.BYTE, 0)
        @JvmField val FIRST_TRUSTED: MetadataKey<UUID?> = create(19, MetadataSerializers.OPTIONAL_UUID, null)
        @JvmField val SECOND_TRUSTED: MetadataKey<UUID?> = create(20, MetadataSerializers.OPTIONAL_UUID, null)
    }

    object GoatKeys {

        @JvmField val SCREAMING: MetadataKey<Boolean> = create(17, MetadataSerializers.BOOLEAN, false)
    }

    object MooshroomKeys {

        @JvmField val TYPE: MetadataKey<String> = create(17, MetadataSerializers.STRING, MooshroomType.RED.serialized)
    }

    object OcelotKeys {

        @JvmField val TRUSTING: MetadataKey<Boolean> = create(17, MetadataSerializers.BOOLEAN, false)
    }

    object PandaKeys {

        @JvmField val UNHAPPY_TIMER: MetadataKey<Int> = create(17, MetadataSerializers.VAR_INT, 0)
        @JvmField val SNEEZE_TIMER: MetadataKey<Int> = create(18, MetadataSerializers.VAR_INT, 0)
        @JvmField val EATING_TIMER: MetadataKey<Int> = create(19, MetadataSerializers.VAR_INT, 0)
        @JvmField val MAIN_GENE: MetadataKey<Byte> = create(20, MetadataSerializers.BYTE, 0)
        @JvmField val HIDDEN_GENE: MetadataKey<Byte> = create(21, MetadataSerializers.BYTE, 0)
        @JvmField val FLAGS: MetadataKey<Byte> = create(22, MetadataSerializers.BYTE, 0)
    }

    object ParrotKeys {

        @JvmField val TYPE: MetadataKey<Int> = create(19, MetadataSerializers.VAR_INT, ParrotType.RED_AND_BLUE.ordinal)
    }

    object PigKeys {

        @JvmField val SADDLE: MetadataKey<Boolean> = create(17, MetadataSerializers.BOOLEAN, false)
        @JvmField val BOOST_TIME: MetadataKey<Int> = create(18, MetadataSerializers.VAR_INT, 0)
    }

    object PolarBearKeys {

        @JvmField val STANDING: MetadataKey<Boolean> = create(17, MetadataSerializers.BOOLEAN, false)
    }

    object RabbitKeys {

        @JvmField val TYPE: MetadataKey<Int> = create(17, MetadataSerializers.VAR_INT, 0)
    }

    object SheepKeys {

        @JvmField val FLAGS: MetadataKey<Byte> = create(17, MetadataSerializers.BYTE, 0)
    }

    object TurtleKeys {

        @JvmField val HOME: MetadataKey<Vector3i> = create(17, MetadataSerializers.POSITION, Vector3i.ZERO)
        @JvmField val HAS_EGG: MetadataKey<Boolean> = create(18, MetadataSerializers.BOOLEAN, false)
        @JvmField val LAYING_EGG: MetadataKey<Boolean> = create(19, MetadataSerializers.BOOLEAN, false)
        @JvmField val DESTINATION: MetadataKey<Vector3i> = create(20, MetadataSerializers.POSITION, Vector3i.ZERO)
        @JvmField val GOING_HOME: MetadataKey<Boolean> = create(21, MetadataSerializers.BOOLEAN, false)
        @JvmField val TRAVELLING: MetadataKey<Boolean> = create(22, MetadataSerializers.BOOLEAN, false)
    }

    object WolfKeys {

        @JvmField val BEGGING: MetadataKey<Boolean> = create(19, MetadataSerializers.BOOLEAN, false)
        @JvmField val COLLAR_COLOR: MetadataKey<Int> = create(20, MetadataSerializers.VAR_INT, Registries.DYE_COLORS.idOf(DyeColors.RED))
        @JvmField val ANGER_TIME: MetadataKey<Int> = create(21, MetadataSerializers.VAR_INT, 0)
    }

    object FishKeys {

        @JvmField val FROM_BUCKET: MetadataKey<Boolean> = create(16, MetadataSerializers.BOOLEAN, false)
    }

    object DolphinKeys {

        @JvmField val TREASURE_POSITION: MetadataKey<Vector3i> = create(16, MetadataSerializers.POSITION, Vector3i.ZERO)
        @JvmField val GOT_FISH: MetadataKey<Boolean> = create(17, MetadataSerializers.BOOLEAN, false)
        @JvmField val MOISTURE: MetadataKey<Int> = create(18, MetadataSerializers.VAR_INT, 2400)
    }

    object PufferfishKeys {

        @JvmField val PUFF_STATE: MetadataKey<Int> = create(17, MetadataSerializers.VAR_INT, 0)
    }

    object GlowSquidKeys {

        @JvmField val REMAINING_DARK_TICKS: MetadataKey<Int> = create(16, MetadataSerializers.VAR_INT, 0)
    }

    object TropicalFishKeys {

        @JvmField val VARIANT: MetadataKey<Int> = create(17, MetadataSerializers.VAR_INT, TropicalFishShape.KOB.ordinal)
    }

    @JvmStatic
    private fun <T> create(
        id: Int,
        serializer: MetadataSerializer<T>,
        default: T
    ): MetadataKey<T> = MetadataKey(id, serializer, default)
}
