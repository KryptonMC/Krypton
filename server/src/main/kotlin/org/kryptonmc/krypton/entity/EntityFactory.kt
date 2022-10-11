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
        entry(EntityTypes.AREA_EFFECT_CLOUD, ::KryptonAreaEffectCloud),
        entry(EntityTypes.ARMOR_STAND, ::KryptonArmorStand),
        entry(EntityTypes.ARROW, ::KryptonArrow),
        entry(EntityTypes.AXOLOTL, ::KryptonAxolotl),
        entry(EntityTypes.BAT, ::KryptonBat),
        entry(EntityTypes.BEE, ::KryptonBee),
        entry(EntityTypes.BOAT, ::KryptonBoat),
        entry(EntityTypes.CAT, ::KryptonCat),
        entry(EntityTypes.CHICKEN, ::KryptonChicken),
        entry(EntityTypes.COD, ::KryptonCod),
        entry(EntityTypes.COMMAND_BLOCK_MINECART, ::KryptonCommandBlockMinecart),
        entry(EntityTypes.COW, ::KryptonCow),
        entry(EntityTypes.CREEPER, ::KryptonCreeper),
        entry(EntityTypes.DOLPHIN, ::KryptonDolphin),
        entry(EntityTypes.DRAGON_FIREBALL, ::KryptonDragonFireball),
        entry(EntityTypes.EGG, ::KryptonEgg),
        entry(EntityTypes.ENDER_PEARL, ::KryptonEnderPearl),
        entry(EntityTypes.EXPERIENCE_BOTTLE, ::KryptonExperienceBottle),
        entry(EntityTypes.EXPERIENCE_ORB, ::KryptonExperienceOrb),
        entry(EntityTypes.FIREWORK_ROCKET, ::KryptonFireworkRocket),
        entry(EntityTypes.FIREBALL, ::KryptonLargeFireball),
        entry(EntityTypes.FISHING_HOOK, ::KryptonFishingHook),
        entry(EntityTypes.FOX, ::KryptonFox),
        entry(EntityTypes.FURNACE_MINECART, ::KryptonFurnaceMinecart),
        entry(EntityTypes.GLOW_SQUID, ::KryptonGlowSquid),
        entry(EntityTypes.GOAT, ::KryptonGoat),
        entry(EntityTypes.LLAMA_SPIT, ::KryptonLlamaSpit),
        entry(EntityTypes.MINECART, ::KryptonMinecart),
        entry(EntityTypes.MOOSHROOM, ::KryptonMooshroom),
        entry(EntityTypes.OCELOT, ::KryptonOcelot),
        entry(EntityTypes.PAINTING, ::KryptonPainting),
        entry(EntityTypes.PANDA, ::KryptonPanda),
        entry(EntityTypes.PARROT, ::KryptonParrot),
        entry(EntityTypes.PIG, ::KryptonPig),
        EntityTypes.PLAYER to null,
        entry(EntityTypes.POLAR_BEAR, ::KryptonPolarBear),
        entry(EntityTypes.POTION, ::KryptonThrownPotion),
        entry(EntityTypes.PUFFERFISH, ::KryptonPufferfish),
        entry(EntityTypes.RABBIT, ::KryptonRabbit),
        entry(EntityTypes.SALMON, ::KryptonSalmon),
        entry(EntityTypes.SHEEP, ::KryptonSheep),
        entry(EntityTypes.SHULKER_BULLET, ::KryptonShulkerBullet),
        entry(EntityTypes.SMALL_FIREBALL, ::KryptonSmallFireball),
        entry(EntityTypes.SNOWBALL, ::KryptonSnowball),
        entry(EntityTypes.SPECTRAL_ARROW, ::KryptonSpectralArrow),
        entry(EntityTypes.SQUID, ::KryptonSquid),
        entry(EntityTypes.TNT_MINECART, ::KryptonTNTMinecart),
        entry(EntityTypes.TRIDENT, ::KryptonTrident),
        entry(EntityTypes.TROPICAL_FISH, ::KryptonTropicalFish),
        entry(EntityTypes.TURTLE, ::KryptonTurtle),
        entry(EntityTypes.WITHER_SKULL, ::KryptonWitherSkull),
        entry(EntityTypes.WOLF, ::KryptonWolf),
        entry(EntityTypes.ZOMBIE, ::KryptonZombie)
    )

    @JvmStatic
    fun create(type: EntityType<Entity>, world: KryptonWorld): KryptonEntity? = TYPE_MAP.get(type)?.apply(world)

    @JvmStatic
    fun create(world: KryptonWorld, id: String, nbt: CompoundTag?): KryptonEntity? {
        return try {
            val type = Registries.ENTITY_TYPE.get(Key.key(id))
            val entity = create(type, world)
            if (entity == null) {
                LOGGER.warn("No entity found with ID $id!")
                return null
            }
            if (nbt != null) entity.load(nbt)
            return entity
        } catch (exception: RuntimeException) {
            LOGGER.warn("Exception loading entity", exception)
            null
        }
    }

    @JvmStatic
    private fun entry(type: EntityType<Entity>, function: Function<KryptonWorld, KryptonEntity>): Entry = Entry(type, function)
}

private typealias Entry = Pair<EntityType<Entity>, Function<KryptonWorld, KryptonEntity>?>
