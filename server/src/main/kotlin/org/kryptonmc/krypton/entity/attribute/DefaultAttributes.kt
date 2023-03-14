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
package org.kryptonmc.krypton.entity.attribute

import com.google.common.collect.ImmutableMap
import org.kryptonmc.krypton.entity.KryptonEntityType
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
import org.kryptonmc.krypton.entity.monster.KryptonBlaze
import org.kryptonmc.krypton.entity.monster.KryptonCaveSpider
import org.kryptonmc.krypton.entity.monster.KryptonCreeper
import org.kryptonmc.krypton.entity.monster.KryptonEndermite
import org.kryptonmc.krypton.entity.monster.KryptonGiant
import org.kryptonmc.krypton.entity.monster.KryptonGuardian
import org.kryptonmc.krypton.entity.monster.KryptonSilverfish
import org.kryptonmc.krypton.entity.monster.KryptonSpider
import org.kryptonmc.krypton.entity.monster.KryptonZombie
import org.kryptonmc.krypton.entity.player.KryptonPlayer

object DefaultAttributes {

    private val SUPPLIERS = Builder()
        .put(KryptonEntityTypes.ARMOR_STAND, KryptonLivingEntity::attributes)
        .put(KryptonEntityTypes.AXOLOTL, KryptonAxolotl::attributes)
        .put(KryptonEntityTypes.BAT, KryptonBat::attributes)
        .put(KryptonEntityTypes.BEE, KryptonBee::attributes)
        .put(KryptonEntityTypes.BLAZE, KryptonBlaze::attributes)
        .put(KryptonEntityTypes.CAT, KryptonCat::attributes)
        .put(KryptonEntityTypes.CAVE_SPIDER, KryptonCaveSpider::attributes)
        .put(KryptonEntityTypes.CHICKEN, KryptonChicken::attributes)
        .put(KryptonEntityTypes.COD, KryptonFish::attributes)
        .put(KryptonEntityTypes.COW, KryptonCow::attributes)
        .put(KryptonEntityTypes.CREEPER, KryptonCreeper::attributes)
        .put(KryptonEntityTypes.DOLPHIN, KryptonDolphin::attributes)
        .put(KryptonEntityTypes.DROWNED, KryptonZombie::attributes)
        .put(KryptonEntityTypes.ENDERMITE, KryptonEndermite::attributes)
        .put(KryptonEntityTypes.FOX, KryptonFox::attributes)
        .put(KryptonEntityTypes.GIANT, KryptonGiant::attributes)
        .put(KryptonEntityTypes.GLOW_SQUID, KryptonSquid::attributes)
        .put(KryptonEntityTypes.GOAT, KryptonGoat::attributes)
        .put(KryptonEntityTypes.GUARDIAN, KryptonGuardian::attributes)
        .put(KryptonEntityTypes.HUSK, KryptonZombie::attributes)
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
        .put(KryptonEntityTypes.SILVERFISH, KryptonSilverfish::attributes)
        .put(KryptonEntityTypes.SPIDER, KryptonSpider::attributes)
        .put(KryptonEntityTypes.SQUID, KryptonSquid::attributes)
        .put(KryptonEntityTypes.TROPICAL_FISH, KryptonFish::attributes)
        .put(KryptonEntityTypes.WOLF, KryptonWolf::attributes)
        .put(KryptonEntityTypes.ZOMBIE, KryptonZombie::attributes)
        .build()

    @JvmStatic
    fun get(type: Key): AttributeSupplier = checkNotNull(SUPPLIERS.get(type)) { "Could not find attributes for entity type $type!" }
}

private typealias Key = KryptonEntityType<KryptonLivingEntity>
private typealias Builder = ImmutableMap.Builder<Key, AttributeSupplier>

private inline fun Builder.put(type: Key, supplier: () -> AttributeSupplier.Builder): Builder = put(type, supplier().build())
