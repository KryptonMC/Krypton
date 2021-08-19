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
package org.kryptonmc.krypton.entity

import net.kyori.adventure.key.Key
import org.kryptonmc.api.entity.Entity
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.krypton.entity.ambient.KryptonBat
import org.kryptonmc.krypton.entity.hanging.KryptonPainting
import org.kryptonmc.krypton.entity.monster.KryptonCreeper
import org.kryptonmc.krypton.entity.monster.KryptonZombie
import org.kryptonmc.krypton.entity.projectile.KryptonArrow
import org.kryptonmc.krypton.entity.projectile.KryptonDragonFireball
import org.kryptonmc.krypton.entity.projectile.KryptonEgg
import org.kryptonmc.krypton.entity.projectile.KryptonEnderPearl
import org.kryptonmc.krypton.entity.projectile.KryptonExperienceBottle
import org.kryptonmc.krypton.entity.projectile.KryptonFireworkRocket
import org.kryptonmc.krypton.entity.projectile.KryptonLargeFireball
import org.kryptonmc.krypton.entity.projectile.KryptonLlamaSpit
import org.kryptonmc.krypton.entity.projectile.KryptonShulkerBullet
import org.kryptonmc.krypton.entity.projectile.KryptonSmallFireball
import org.kryptonmc.krypton.entity.projectile.KryptonSnowball
import org.kryptonmc.krypton.entity.projectile.KryptonSpectralArrow
import org.kryptonmc.krypton.entity.projectile.KryptonThrownPotion
import org.kryptonmc.krypton.entity.projectile.KryptonTrident
import org.kryptonmc.krypton.entity.projectile.KryptonWitherSkull
import org.kryptonmc.krypton.registry.InternalRegistries
import org.kryptonmc.krypton.util.logger
import org.kryptonmc.krypton.world.KryptonWorld
import org.kryptonmc.nbt.CompoundTag

object EntityFactory {

    private val LOGGER = logger<EntityFactory>()
    private val TYPE_MAP = mapOf<EntityType<out Entity>, (KryptonWorld) -> KryptonEntity>(
        EntityTypes.AREA_EFFECT_CLOUD to ::KryptonAreaEffectCloud,
        EntityTypes.ARMOR_STAND to ::KryptonArmorStand,
        EntityTypes.ARROW to ::KryptonArrow,
        EntityTypes.BAT to ::KryptonBat,
        EntityTypes.CREEPER to ::KryptonCreeper,
        EntityTypes.DRAGON_FIREBALL to ::KryptonDragonFireball,
        EntityTypes.EXPERIENCE_ORB to ::KryptonExperienceOrb,
        EntityTypes.FIREWORK_ROCKET to ::KryptonFireworkRocket,
        EntityTypes.FIREBALL to ::KryptonLargeFireball,
        EntityTypes.LLAMA_SPIT to ::KryptonLlamaSpit,
        EntityTypes.PAINTING to ::KryptonPainting,
        EntityTypes.SHULKER_BULLET to ::KryptonShulkerBullet,
        EntityTypes.SMALL_FIREBALL to ::KryptonSmallFireball,
        EntityTypes.SNOWBALL to ::KryptonSnowball,
        EntityTypes.SPECTRAL_ARROW to ::KryptonSpectralArrow,
        EntityTypes.EGG to ::KryptonEgg,
        EntityTypes.ENDER_PEARL to ::KryptonEnderPearl,
        EntityTypes.EXPERIENCE_BOTTLE to ::KryptonExperienceBottle,
        EntityTypes.POTION to ::KryptonThrownPotion,
        EntityTypes.TRIDENT to ::KryptonTrident,
        EntityTypes.WITHER_SKULL to ::KryptonWitherSkull,
        EntityTypes.ZOMBIE to ::KryptonZombie
    )

    @Suppress("UNCHECKED_CAST")
    fun <T : Entity> create(type: EntityType<T>, world: KryptonWorld): T? = TYPE_MAP[type]?.invoke(world) as? T

    fun create(type: EntityType<out Entity>, world: KryptonWorld): KryptonEntity? = TYPE_MAP[type]?.invoke(world)

    fun create(
        world: KryptonWorld,
        id: String,
        nbt: CompoundTag?
    ): KryptonEntity? = try {
        create(InternalRegistries.ENTITY_TYPE[Key.key(id)], world)?.apply { if (nbt != null) load(nbt) } ?: run {
            LOGGER.warn("No entity found with ID $id")
            return null
        }
    } catch (exception: RuntimeException) {
        LOGGER.warn("Exception loading entity", exception)
        null
    }
}
