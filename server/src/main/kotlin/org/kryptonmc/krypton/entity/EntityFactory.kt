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

object EntityFactory {

    private val LOGGER = logger<EntityFactory>()
    private val TYPE_MAP = mapOf<EntityType<out Entity>, (KryptonWorld) -> KryptonEntity>(
        EntityTypes.AREA_EFFECT_CLOUD to ::KryptonAreaEffectCloud,
        EntityTypes.ARMOR_STAND to ::KryptonArmorStand,
        EntityTypes.ARROW to ::KryptonArrow,
        EntityTypes.AXOLOTL to ::KryptonAxolotl,
        EntityTypes.BAT to ::KryptonBat,
        EntityTypes.BEE to ::KryptonBee,
        EntityTypes.BOAT to ::KryptonBoat,
        EntityTypes.CAT to ::KryptonCat,
        EntityTypes.CHICKEN to ::KryptonChicken,
        EntityTypes.COD to ::KryptonCod,
        EntityTypes.COW to ::KryptonCow,
        EntityTypes.CREEPER to ::KryptonCreeper,
        EntityTypes.DOLPHIN to ::KryptonDolphin,
        EntityTypes.DRAGON_FIREBALL to ::KryptonDragonFireball,
        EntityTypes.EXPERIENCE_ORB to ::KryptonExperienceOrb,
        EntityTypes.FIREWORK_ROCKET to ::KryptonFireworkRocket,
        EntityTypes.FIREBALL to ::KryptonLargeFireball,
        EntityTypes.FOX to ::KryptonFox,
        EntityTypes.GLOW_SQUID to ::KryptonGlowSquid,
        EntityTypes.GOAT to ::KryptonGoat,
        EntityTypes.LLAMA_SPIT to ::KryptonLlamaSpit,
        EntityTypes.MINECART to ::KryptonMinecart,
        EntityTypes.COMMAND_BLOCK_MINECART to ::KryptonCommandBlockMinecart,
        EntityTypes.FURNACE_MINECART to ::KryptonFurnaceMinecart,
        EntityTypes.TNT_MINECART to ::KryptonTNTMinecart,
        EntityTypes.MOOSHROOM to ::KryptonMooshroom,
        EntityTypes.OCELOT to ::KryptonOcelot,
        EntityTypes.PAINTING to ::KryptonPainting,
        EntityTypes.PANDA to ::KryptonPanda,
        EntityTypes.PARROT to ::KryptonParrot,
        EntityTypes.PIG to ::KryptonPig,
        EntityTypes.POLAR_BEAR to ::KryptonPolarBear,
        EntityTypes.PUFFERFISH to ::KryptonPufferfish,
        EntityTypes.RABBIT to ::KryptonRabbit,
        EntityTypes.SALMON to ::KryptonSalmon,
        EntityTypes.SHEEP to ::KryptonSheep,
        EntityTypes.SHULKER_BULLET to ::KryptonShulkerBullet,
        EntityTypes.SMALL_FIREBALL to ::KryptonSmallFireball,
        EntityTypes.SNOWBALL to ::KryptonSnowball,
        EntityTypes.SPECTRAL_ARROW to ::KryptonSpectralArrow,
        EntityTypes.SQUID to ::KryptonSquid,
        EntityTypes.EGG to ::KryptonEgg,
        EntityTypes.ENDER_PEARL to ::KryptonEnderPearl,
        EntityTypes.EXPERIENCE_BOTTLE to ::KryptonExperienceBottle,
        EntityTypes.POTION to ::KryptonThrownPotion,
        EntityTypes.TRIDENT to ::KryptonTrident,
        EntityTypes.TROPICAL_FISH to ::KryptonTropicalFish,
        EntityTypes.TURTLE to ::KryptonTurtle,
        EntityTypes.WITHER_SKULL to ::KryptonWitherSkull,
        EntityTypes.WOLF to ::KryptonWolf,
        EntityTypes.ZOMBIE to ::KryptonZombie,
        EntityTypes.FISHING_HOOK to ::KryptonFishingHook
    )
    private val SERIALIZERS = mapOf(
        EntityTypes.AREA_EFFECT_CLOUD to AreaEffectCloudSerializer,
        EntityTypes.ARMOR_STAND to ArmorStandSerializer,
        EntityTypes.ARROW to ArrowSerializer,
        EntityTypes.AXOLOTL to AxolotlSerializer,
        EntityTypes.BAT to BatSerializer,
        EntityTypes.BEE to BeeSerializer,
        EntityTypes.BOAT to BoatSerializer,
        EntityTypes.CAT to CatSerializer,
        EntityTypes.CHICKEN to ChickenSerializer,
        EntityTypes.COD to FishSerializer,
        EntityTypes.COMMAND_BLOCK_MINECART to CommandBlockMinecartSerializer,
        EntityTypes.COW to AgeableSerializer,
        EntityTypes.CREEPER to CreeperSerializer,
        EntityTypes.DOLPHIN to DolphinSerializer,
        EntityTypes.DRAGON_FIREBALL to AcceleratingProjectileSerializer,
        EntityTypes.EGG to ThrowableProjectileSerializer,
        EntityTypes.ENDER_PEARL to ThrowableProjectileSerializer,
        EntityTypes.EXPERIENCE_BOTTLE to ThrowableProjectileSerializer,
        EntityTypes.EXPERIENCE_ORB to ExperienceOrbSerializer,
        EntityTypes.FIREWORK_ROCKET to ProjectileSerializer,
        EntityTypes.FIREBALL to LargeFireballSerializer,
        EntityTypes.FISHING_HOOK to ProjectileSerializer,
        EntityTypes.FOX to FoxSerializer,
        EntityTypes.FURNACE_MINECART to FurnaceMinecartSerializer,
        EntityTypes.GLOW_SQUID to GlowSquidSerializer,
        EntityTypes.GOAT to GoatSerializer,
        EntityTypes.LLAMA_SPIT to ProjectileSerializer,
        EntityTypes.MINECART to MinecartLikeSerializer,
        EntityTypes.MOOSHROOM to MooshroomSerializer,
        EntityTypes.OCELOT to OcelotSerializer,
        EntityTypes.PAINTING to PaintingSerializer,
        EntityTypes.PANDA to PandaSerializer,
        EntityTypes.PARROT to ParrotSerializer,
        EntityTypes.PIG to PigSerializer,
        EntityTypes.PLAYER to PlayerSerializer,
        EntityTypes.POLAR_BEAR to PolarBearSerializer,
        EntityTypes.POTION to ThrowableProjectileSerializer,
        EntityTypes.PUFFERFISH to PufferfishSerializer,
        EntityTypes.RABBIT to RabbitSerializer,
        EntityTypes.SALMON to FishSerializer,
        EntityTypes.SHEEP to SheepSerializer,
        EntityTypes.SHULKER_BULLET to ShulkerBulletSerializer,
        EntityTypes.SMALL_FIREBALL to FireballSerializer,
        EntityTypes.SNOWBALL to ThrowableProjectileSerializer,
        EntityTypes.SPECTRAL_ARROW to SpectralArrowSerializer,
        EntityTypes.SQUID to MobSerializer,
        EntityTypes.TNT_MINECART to TNTMinecartSerializer,
        EntityTypes.TRIDENT to TridentSerializer,
        EntityTypes.TROPICAL_FISH to TropicalFishSerializer,
        EntityTypes.TURTLE to TurtleSerializer,
        EntityTypes.WITHER_SKULL to AcceleratingProjectileSerializer,
        EntityTypes.WOLF to WolfSerializer,
        EntityTypes.ZOMBIE to ZombieSerializer
    )

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : Entity> create(type: EntityType<T>, world: KryptonWorld): T? = TYPE_MAP[type]?.invoke(world) as? T

    @JvmStatic
    fun create(type: EntityType<out Entity>, world: KryptonWorld): KryptonEntity? = TYPE_MAP[type]?.invoke(world)

    @JvmStatic
    fun create(world: KryptonWorld, id: String, nbt: CompoundTag?): KryptonEntity? {
        return try {
            val type = Registries.ENTITY_TYPE[Key.key(id)]
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

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <E : Entity> serializer(type: EntityType<out E>): EntitySerializer<E> {
        val serializer = requireNotNull(SERIALIZERS[type]) { "Cannot find serializer for type ${type.key().asString()}" }
        return serializer as EntitySerializer<E>
    }
}
