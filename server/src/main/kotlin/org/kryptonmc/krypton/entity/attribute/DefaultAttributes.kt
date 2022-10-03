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
package org.kryptonmc.krypton.entity.attribute

import com.google.common.collect.ImmutableMap
import org.kryptonmc.api.entity.EntityType
import org.kryptonmc.api.entity.LivingEntity
import org.kryptonmc.krypton.entity.KryptonEntityTypes
import org.kryptonmc.krypton.entity.KryptonLivingEntity
import org.kryptonmc.krypton.entity.ambient.KryptonBat
import org.kryptonmc.krypton.entity.animal.KryptonAxolotl
import org.kryptonmc.krypton.entity.animal.KryptonBee
import org.kryptonmc.krypton.entity.animal.KryptonCat
import org.kryptonmc.krypton.entity.animal.KryptonChicken
import org.kryptonmc.krypton.entity.animal.KryptonCow
import org.kryptonmc.krypton.entity.animal.KryptonFox
import org.kryptonmc.krypton.entity.animal.KryptonGoat
import org.kryptonmc.krypton.entity.animal.KryptonOcelot
import org.kryptonmc.krypton.entity.animal.KryptonPanda
import org.kryptonmc.krypton.entity.animal.KryptonParrot
import org.kryptonmc.krypton.entity.animal.KryptonPig
import org.kryptonmc.krypton.entity.animal.KryptonPolarBear
import org.kryptonmc.krypton.entity.animal.KryptonRabbit
import org.kryptonmc.krypton.entity.animal.KryptonSheep
import org.kryptonmc.krypton.entity.animal.KryptonWolf
import org.kryptonmc.krypton.entity.aquatic.KryptonDolphin
import org.kryptonmc.krypton.entity.aquatic.KryptonFish
import org.kryptonmc.krypton.entity.aquatic.KryptonSquid
import org.kryptonmc.krypton.entity.monster.KryptonCreeper
import org.kryptonmc.krypton.entity.monster.KryptonZombie
import org.kryptonmc.krypton.entity.player.KryptonPlayer

object DefaultAttributes {

    private val SUPPLIERS = Builder()
        .put(KryptonEntityTypes.ARMOR_STAND, KryptonLivingEntity::attributes)
        .put(KryptonEntityTypes.AXOLOTL, KryptonAxolotl::attributes)
        .put(KryptonEntityTypes.BAT, KryptonBat::attributes)
        .put(KryptonEntityTypes.BEE, KryptonBee::attributes)
        .put(KryptonEntityTypes.CAT, KryptonCat::attributes)
        .put(KryptonEntityTypes.CHICKEN, KryptonChicken::attributes)
        .put(KryptonEntityTypes.COD, KryptonFish::attributes)
        .put(KryptonEntityTypes.COW, KryptonCow::attributes)
        .put(KryptonEntityTypes.CREEPER, KryptonCreeper::attributes)
        .put(KryptonEntityTypes.DOLPHIN, KryptonDolphin::attributes)
        .put(KryptonEntityTypes.FOX, KryptonFox::attributes)
        .put(KryptonEntityTypes.GLOW_SQUID, KryptonSquid::attributes)
        .put(KryptonEntityTypes.GOAT, KryptonGoat::attributes)
        .put(KryptonEntityTypes.MOOSHROOM, KryptonCow::attributes)
        .put(KryptonEntityTypes.OCELOT, KryptonOcelot::attributes)
        .put(KryptonEntityTypes.PANDA, KryptonPanda::attributes)
        .put(KryptonEntityTypes.PARROT, KryptonParrot::attributes)
        .put(KryptonEntityTypes.PIG, KryptonPig::attributes)
        .put(KryptonEntityTypes.PLAYER, KryptonPlayer::attributes)
        .put(KryptonEntityTypes.POLAR_BEAR, KryptonPolarBear::attributes)
        .put(KryptonEntityTypes.PUFFERFISH, KryptonFish::attributes)
        .put(KryptonEntityTypes.RABBIT, KryptonRabbit::attributes)
        .put(KryptonEntityTypes.SALMON, KryptonFish::attributes)
        .put(KryptonEntityTypes.SHEEP, KryptonSheep::attributes)
        .put(KryptonEntityTypes.SQUID, KryptonSquid::attributes)
        .put(KryptonEntityTypes.TROPICAL_FISH, KryptonFish::attributes)
        .put(KryptonEntityTypes.WOLF, KryptonWolf::attributes)
        .put(KryptonEntityTypes.ZOMBIE, KryptonZombie::attributes)
        .build()

    @JvmStatic
    fun get(type: EntityType<out LivingEntity>): AttributeSupplier =
        checkNotNull(SUPPLIERS.get(type)) { "Could not find attributes for entity type $type!" }
}

private typealias Builder = ImmutableMap.Builder<EntityType<out LivingEntity>, AttributeSupplier>

private inline fun Builder.put(type: EntityType<out LivingEntity>, supplier: () -> AttributeSupplier.Builder): Builder = put(type, supplier().build())
