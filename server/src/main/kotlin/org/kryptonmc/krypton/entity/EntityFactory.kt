/*
 * This file is part of the Krypton project, licensed under the Apache License v2.0
 *
 * Copyright (C) 2021-2023 KryptonMC and the contributors of the Krypton project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kryptonmc.krypton.entity

import net.kyori.adventure.key.Key
import org.apache.logging.log4j.LogManager
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.api.registry.RegistryReference
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
import org.kryptonmc.krypton.entity.monster.KryptonBlaze
import org.kryptonmc.krypton.entity.monster.KryptonCaveSpider
import org.kryptonmc.krypton.entity.monster.KryptonCreeper
import org.kryptonmc.krypton.entity.monster.KryptonDrowned
import org.kryptonmc.krypton.entity.monster.KryptonEndermite
import org.kryptonmc.krypton.entity.monster.KryptonGiant
import org.kryptonmc.krypton.entity.monster.KryptonGuardian
import org.kryptonmc.krypton.entity.monster.KryptonHusk
import org.kryptonmc.krypton.entity.monster.KryptonSilverfish
import org.kryptonmc.krypton.entity.monster.KryptonSpider
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
import org.kryptonmc.krypton.registry.KryptonRegistries
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag
import java.util.function.Function

/**
 * This exists primarily because not all entities are implemented yet, and so this logic,
 * which would normally be in KryptonEntityType using a factory, is here instead.
 *
 * This is used for instantiating new entities from type and NBT.
 */
object EntityFactory {

    private val LOGGER = LogManager.getLogger()
    private val TYPE_MAP = mapOf(
        entry(EntityTypes.AREA_EFFECT_CLOUD, ::KryptonAreaEffectCloud),
        entry(EntityTypes.ARMOR_STAND, ::KryptonArmorStand),
        entry(EntityTypes.ARROW, ::KryptonArrow),
        entry(EntityTypes.AXOLOTL, ::KryptonAxolotl),
        entry(EntityTypes.BAT, ::KryptonBat),
        entry(EntityTypes.BEE, ::KryptonBee),
        entry(EntityTypes.BLAZE, ::KryptonBlaze),
        entry(EntityTypes.BOAT, ::KryptonBoat),
        entry(EntityTypes.CAT, ::KryptonCat),
        entry(EntityTypes.CAVE_SPIDER, ::KryptonCaveSpider),
        entry(EntityTypes.CHICKEN, ::KryptonChicken),
        entry(EntityTypes.COD, ::KryptonCod),
        entry(EntityTypes.COMMAND_BLOCK_MINECART, ::KryptonCommandBlockMinecart),
        entry(EntityTypes.COW, ::KryptonCow),
        entry(EntityTypes.CREEPER, ::KryptonCreeper),
        entry(EntityTypes.DOLPHIN, ::KryptonDolphin),
        entry(EntityTypes.DRAGON_FIREBALL, ::KryptonDragonFireball),
        entry(EntityTypes.DROWNED, ::KryptonDrowned),
        entry(EntityTypes.EGG, ::KryptonEgg),
        entry(EntityTypes.ENDER_PEARL, ::KryptonEnderPearl),
        entry(EntityTypes.ENDERMITE, ::KryptonEndermite),
        entry(EntityTypes.EXPERIENCE_BOTTLE, ::KryptonExperienceBottle),
        entry(EntityTypes.EXPERIENCE_ORB, ::KryptonExperienceOrb),
        entry(EntityTypes.FIREWORK_ROCKET, ::KryptonFireworkRocket),
        entry(EntityTypes.FIREBALL, ::KryptonLargeFireball),
        entry(EntityTypes.FISHING_HOOK, ::KryptonFishingHook),
        entry(EntityTypes.FOX, ::KryptonFox),
        entry(EntityTypes.FURNACE_MINECART, ::KryptonFurnaceMinecart),
        entry(EntityTypes.GIANT, ::KryptonGiant),
        entry(EntityTypes.GLOW_SQUID, ::KryptonGlowSquid),
        entry(EntityTypes.GOAT, ::KryptonGoat),
        entry(EntityTypes.GUARDIAN, ::KryptonGuardian),
        entry(EntityTypes.HUSK, ::KryptonHusk),
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
        entry(EntityTypes.SILVERFISH, ::KryptonSilverfish),
        entry(EntityTypes.SMALL_FIREBALL, ::KryptonSmallFireball),
        entry(EntityTypes.SNOWBALL, ::KryptonSnowball),
        entry(EntityTypes.SPECTRAL_ARROW, ::KryptonSpectralArrow),
        entry(EntityTypes.SPIDER, ::KryptonSpider),
        entry(EntityTypes.SQUID, ::KryptonSquid),
        entry(EntityTypes.TNT_MINECART, ::KryptonTNTMinecart),
        entry(EntityTypes.TRIDENT, ::KryptonTrident),
        entry(EntityTypes.TROPICAL_FISH, ::KryptonTropicalFish),
        entry(EntityTypes.TURTLE, ::KryptonTurtle),
        entry(EntityTypes.WITHER_SKULL, ::KryptonWitherSkull),
        entry(EntityTypes.WOLF, ::KryptonWolf),
        entry(EntityTypes.ZOMBIE, ::KryptonZombie)
    )

    /**
     * This does nothing more than lookup the entity's factory in the map using the type
     * and instantiate the entity with the world. It will return an entity with all of its
     * data set to the default values on initialisation.
     */
    @JvmStatic
    fun create(type: EntityType<Entity>, world: KryptonWorld): KryptonEntity? = TYPE_MAP.get(type)?.apply(world)

    /**
     * This is used to create an entity from an NBT tag.
     *
     * It will try to resolve the entity's type from the given id, warning and returning null if
     * it can't resolve the type, and then will use that to create the entity, and further, load
     * its data from the nbt tag, if it is non-null.
     */
    @JvmStatic
    fun create(world: KryptonWorld, id: String, nbt: CompoundTag?): KryptonEntity? {
        return try {
            val type = KryptonRegistries.ENTITY_TYPE.get(Key.key(id))
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
    private inline fun entry(type: RegistryReference<out EntityType<Entity>>, crossinline constructor: (KryptonWorld) -> KryptonEntity): Entry =
        Entry(type.get(), Function { constructor(it) })
}

private typealias Entry = Pair<EntityType<Entity>, Function<KryptonWorld, KryptonEntity>?>
