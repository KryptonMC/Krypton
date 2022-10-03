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
package org.kryptonmc.krypton.entity

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.registry.Registries
import org.kryptonmc.krypton.entity.ambient.KryptonBat
import org.kryptonmc.krypton.entity.animal.KryptonAxolotl
import org.kryptonmc.krypton.entity.animal.KryptonBee
import org.kryptonmc.krypton.entity.animal.KryptonChicken
import org.kryptonmc.krypton.entity.animal.KryptonFox
import org.kryptonmc.krypton.entity.animal.KryptonOcelot
import org.kryptonmc.krypton.entity.animal.KryptonPanda
import org.kryptonmc.krypton.entity.animal.KryptonPig
import org.kryptonmc.krypton.entity.animal.KryptonPolarBear
import org.kryptonmc.krypton.entity.animal.KryptonCat
import org.kryptonmc.krypton.entity.animal.KryptonCow
import org.kryptonmc.krypton.entity.animal.KryptonGoat
import org.kryptonmc.krypton.entity.animal.KryptonMooshroom
import org.kryptonmc.krypton.entity.animal.KryptonParrot
import org.kryptonmc.krypton.entity.animal.KryptonRabbit
import org.kryptonmc.krypton.entity.animal.KryptonSheep
import org.kryptonmc.krypton.entity.animal.KryptonTurtle
import org.kryptonmc.krypton.entity.animal.KryptonWolf
import org.kryptonmc.krypton.entity.aquatic.KryptonCod
import org.kryptonmc.krypton.entity.aquatic.KryptonDolphin
import org.kryptonmc.krypton.entity.aquatic.KryptonGlowSquid
import org.kryptonmc.krypton.entity.aquatic.KryptonPufferfish
import org.kryptonmc.krypton.entity.aquatic.KryptonSalmon
import org.kryptonmc.krypton.entity.aquatic.KryptonSquid
import org.kryptonmc.krypton.entity.aquatic.KryptonTropicalFish
import org.kryptonmc.krypton.entity.hanging.KryptonPainting
import org.kryptonmc.krypton.entity.monster.KryptonCreeper
import org.kryptonmc.krypton.entity.monster.KryptonZombie
import org.kryptonmc.krypton.entity.projectile.KryptonArrow
import org.kryptonmc.krypton.entity.projectile.KryptonDragonFireball
import org.kryptonmc.krypton.entity.projectile.KryptonEgg
import org.kryptonmc.krypton.entity.projectile.KryptonEnderPearl
import org.kryptonmc.krypton.entity.projectile.KryptonExperienceBottle
import org.kryptonmc.krypton.entity.projectile.KryptonFireworkRocket
import org.kryptonmc.krypton.entity.projectile.KryptonFishingHook
import org.kryptonmc.krypton.entity.projectile.KryptonLargeFireball
import org.kryptonmc.krypton.entity.projectile.KryptonLlamaSpit
import org.kryptonmc.krypton.entity.projectile.KryptonShulkerBullet
import org.kryptonmc.krypton.entity.projectile.KryptonSmallFireball
import org.kryptonmc.krypton.entity.projectile.KryptonSnowball
import org.kryptonmc.krypton.entity.projectile.KryptonSpectralArrow
import org.kryptonmc.krypton.entity.projectile.KryptonThrownPotion
import org.kryptonmc.krypton.entity.projectile.KryptonTrident
import org.kryptonmc.krypton.entity.projectile.KryptonWitherSkull
import org.kryptonmc.krypton.entity.serializer.AgeableSerializer
import org.kryptonmc.krypton.entity.serializer.AreaEffectCloudSerializer
import org.kryptonmc.krypton.entity.serializer.ArmorStandSerializer
import org.kryptonmc.krypton.entity.serializer.EntitySerializer
import org.kryptonmc.krypton.entity.serializer.ExperienceOrbSerializer
import org.kryptonmc.krypton.entity.serializer.MobSerializer
import org.kryptonmc.krypton.entity.serializer.ambient.BatSerializer
import org.kryptonmc.krypton.entity.serializer.animal.AxolotlSerializer
import org.kryptonmc.krypton.entity.serializer.animal.BeeSerializer
import org.kryptonmc.krypton.entity.serializer.animal.CatSerializer
import org.kryptonmc.krypton.entity.serializer.animal.ChickenSerializer
import org.kryptonmc.krypton.entity.serializer.animal.FoxSerializer
import org.kryptonmc.krypton.entity.serializer.animal.GoatSerializer
import org.kryptonmc.krypton.entity.serializer.animal.MooshroomSerializer
import org.kryptonmc.krypton.entity.serializer.animal.OcelotSerializer
import org.kryptonmc.krypton.entity.serializer.animal.PandaSerializer
import org.kryptonmc.krypton.entity.serializer.animal.ParrotSerializer
import org.kryptonmc.krypton.entity.serializer.animal.PigSerializer
import org.kryptonmc.krypton.entity.serializer.animal.PolarBearSerializer
import org.kryptonmc.krypton.entity.serializer.animal.RabbitSerializer
import org.kryptonmc.krypton.entity.serializer.animal.SheepSerializer
import org.kryptonmc.krypton.entity.serializer.animal.TurtleSerializer
import org.kryptonmc.krypton.entity.serializer.animal.WolfSerializer
import org.kryptonmc.krypton.entity.serializer.aquatic.DolphinSerializer
import org.kryptonmc.krypton.entity.serializer.aquatic.FishSerializer
import org.kryptonmc.krypton.entity.serializer.aquatic.GlowSquidSerializer
import org.kryptonmc.krypton.entity.serializer.aquatic.PufferfishSerializer
import org.kryptonmc.krypton.entity.serializer.aquatic.TropicalFishSerializer
import org.kryptonmc.krypton.entity.serializer.hanging.PaintingSerializer
import org.kryptonmc.krypton.entity.serializer.monster.CreeperSerializer
import org.kryptonmc.krypton.entity.serializer.monster.ZombieSerializer
import org.kryptonmc.krypton.entity.serializer.player.PlayerSerializer
import org.kryptonmc.krypton.entity.serializer.projectile.AcceleratingProjectileSerializer
import org.kryptonmc.krypton.entity.serializer.projectile.ArrowSerializer
import org.kryptonmc.krypton.entity.serializer.projectile.FireballSerializer
import org.kryptonmc.krypton.entity.serializer.projectile.LargeFireballSerializer
import org.kryptonmc.krypton.entity.serializer.projectile.ProjectileSerializer
import org.kryptonmc.krypton.entity.serializer.projectile.ShulkerBulletSerializer
import org.kryptonmc.krypton.entity.serializer.projectile.SpectralArrowSerializer
import org.kryptonmc.krypton.entity.serializer.projectile.ThrowableProjectileSerializer
import org.kryptonmc.krypton.entity.serializer.projectile.TridentSerializer
import org.kryptonmc.krypton.entity.serializer.vehicle.BoatSerializer
import org.kryptonmc.krypton.entity.serializer.vehicle.CommandBlockMinecartSerializer
import org.kryptonmc.krypton.entity.serializer.vehicle.FurnaceMinecartSerializer
import org.kryptonmc.krypton.entity.serializer.vehicle.MinecartLikeSerializer
import org.kryptonmc.krypton.entity.serializer.vehicle.TNTMinecartSerializer
import org.kryptonmc.krypton.entity.vehicle.KryptonBoat
import org.kryptonmc.krypton.entity.vehicle.KryptonCommandBlockMinecart
import org.kryptonmc.krypton.entity.vehicle.KryptonFurnaceMinecart
import org.kryptonmc.krypton.entity.vehicle.KryptonMinecart
import org.kryptonmc.krypton.entity.vehicle.KryptonTNTMinecart
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import java.util.function.Function

object EntityFactory {

    private val LOGGER = logger<EntityFactory>()
    private val TYPE_MAP = mapOf(
        EntityTypes.AREA_EFFECT_CLOUD to Entry(::KryptonAreaEffectCloud, AreaEffectCloudSerializer),
        EntityTypes.ARMOR_STAND to Entry(::KryptonArmorStand, ArmorStandSerializer),
        EntityTypes.ARROW to Entry(::KryptonArrow, ArrowSerializer),
        EntityTypes.AXOLOTL to Entry(::KryptonAxolotl, AxolotlSerializer),
        EntityTypes.BAT to Entry(::KryptonBat, BatSerializer),
        EntityTypes.BEE to Entry(::KryptonBee, BeeSerializer),
        EntityTypes.BOAT to Entry(::KryptonBoat, BoatSerializer),
        EntityTypes.CAT to Entry(::KryptonCat, CatSerializer),
        EntityTypes.CHICKEN to Entry(::KryptonChicken, ChickenSerializer),
        EntityTypes.COD to Entry(::KryptonCod, FishSerializer),
        EntityTypes.COMMAND_BLOCK_MINECART to Entry(::KryptonCommandBlockMinecart, CommandBlockMinecartSerializer),
        EntityTypes.COW to Entry(::KryptonCow, AgeableSerializer),
        EntityTypes.CREEPER to Entry(::KryptonCreeper, CreeperSerializer),
        EntityTypes.DOLPHIN to Entry(::KryptonDolphin, DolphinSerializer),
        EntityTypes.DRAGON_FIREBALL to Entry(::KryptonDragonFireball, AcceleratingProjectileSerializer),
        EntityTypes.EGG to Entry(::KryptonEgg, ThrowableProjectileSerializer),
        EntityTypes.ENDER_PEARL to Entry(::KryptonEnderPearl, ThrowableProjectileSerializer),
        EntityTypes.EXPERIENCE_BOTTLE to Entry(::KryptonExperienceBottle, ThrowableProjectileSerializer),
        EntityTypes.EXPERIENCE_ORB to Entry(::KryptonExperienceOrb, ExperienceOrbSerializer),
        EntityTypes.FIREWORK_ROCKET to Entry(::KryptonFireworkRocket, ProjectileSerializer),
        EntityTypes.FIREBALL to Entry(::KryptonLargeFireball, LargeFireballSerializer),
        EntityTypes.FISHING_HOOK to Entry(::KryptonFishingHook, ProjectileSerializer),
        EntityTypes.FOX to Entry(::KryptonFox, FoxSerializer),
        EntityTypes.FURNACE_MINECART to Entry(::KryptonFurnaceMinecart, FurnaceMinecartSerializer),
        EntityTypes.GLOW_SQUID to Entry(::KryptonGlowSquid, GlowSquidSerializer),
        EntityTypes.GOAT to Entry(::KryptonGoat, GoatSerializer),
        EntityTypes.LLAMA_SPIT to Entry(::KryptonLlamaSpit, ProjectileSerializer),
        EntityTypes.MINECART to Entry(::KryptonMinecart, MinecartLikeSerializer),
        EntityTypes.MOOSHROOM to Entry(::KryptonMooshroom, MooshroomSerializer),
        EntityTypes.OCELOT to Entry(::KryptonOcelot, OcelotSerializer),
        EntityTypes.PAINTING to Entry(::KryptonPainting, PaintingSerializer),
        EntityTypes.PANDA to Entry(::KryptonPanda, PandaSerializer),
        EntityTypes.PARROT to Entry(::KryptonParrot, ParrotSerializer),
        EntityTypes.PIG to Entry(::KryptonPig, PigSerializer),
        EntityTypes.PLAYER to Entry(null, PlayerSerializer),
        EntityTypes.POLAR_BEAR to Entry(::KryptonPolarBear, PolarBearSerializer),
        EntityTypes.POTION to Entry(::KryptonThrownPotion, ThrowableProjectileSerializer),
        EntityTypes.PUFFERFISH to Entry(::KryptonPufferfish, PufferfishSerializer),
        EntityTypes.RABBIT to Entry(::KryptonRabbit, RabbitSerializer),
        EntityTypes.SALMON to Entry(::KryptonSalmon, ThrowableProjectileSerializer),
        EntityTypes.SHEEP to Entry(::KryptonSheep, SheepSerializer),
        EntityTypes.SHULKER_BULLET to Entry(::KryptonShulkerBullet, ShulkerBulletSerializer),
        EntityTypes.SMALL_FIREBALL to Entry(::KryptonSmallFireball, FireballSerializer),
        EntityTypes.SNOWBALL to Entry(::KryptonSnowball, ThrowableProjectileSerializer),
        EntityTypes.SPECTRAL_ARROW to Entry(::KryptonSpectralArrow, SpectralArrowSerializer),
        EntityTypes.SQUID to Entry(::KryptonSquid, MobSerializer),
        EntityTypes.TNT_MINECART to Entry(::KryptonTNTMinecart, TNTMinecartSerializer),
        EntityTypes.TRIDENT to Entry(::KryptonTrident, TridentSerializer),
        EntityTypes.TROPICAL_FISH to Entry(::KryptonTropicalFish, TropicalFishSerializer),
        EntityTypes.TURTLE to Entry(::KryptonTurtle, TurtleSerializer),
        EntityTypes.WITHER_SKULL to Entry(::KryptonWitherSkull, AcceleratingProjectileSerializer),
        EntityTypes.WOLF to Entry(::KryptonWolf, WolfSerializer),
        EntityTypes.ZOMBIE to Entry(::KryptonZombie, ZombieSerializer)
    )

    @JvmStatic
    fun create(type: EntityType<out Entity>, world: KryptonWorld): KryptonEntity? = TYPE_MAP.get(type)?.first?.apply(world)

    @JvmStatic
    fun create(world: KryptonWorld, id: String, nbt: CompoundTag?): KryptonEntity? {
        return try {
            val type = Registries.ENTITY_TYPE.get(Key.key(id))
            val entity = create(type, world)
            if (entity == null) {
                LOGGER.warn("No entity found with ID $id!")
                return null
            }
            if (nbt != null) serializer(type).load(entity, nbt)
            return entity
        } catch (exception: RuntimeException) {
            LOGGER.warn("Exception loading entity", exception)
            null
        }
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun <E : Entity> serializer(type: EntityType<out E>): EntitySerializer<E> =
        requireNotNull(TYPE_MAP.get(type)?.second) { "Cannot find serializer for type ${type.key().asString()}!" } as EntitySerializer<E>
}

private typealias Entry<E> = Pair<Function<KryptonWorld, KryptonEntity>?, EntitySerializer<E>>
