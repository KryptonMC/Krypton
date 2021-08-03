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
package org.kryptonmc.krypton.world.biome

import org.kryptonmc.api.entity.EntityTypes
import org.kryptonmc.krypton.entity.MobCategory
import org.kryptonmc.krypton.world.biome.MobSpawnSettings.SpawnerData

fun MobSpawnSettings.Builder.common() = apply {
    caves()
    monsters(95, 5, 100)
}

fun MobSpawnSettings.Builder.default() = apply {
    farmAnimals()
    common()
}

fun MobSpawnSettings.Builder.farmAnimals() = apply {
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.SHEEP, 12, 4, 4))
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.PIG, 10, 4, 4))
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.CHICKEN, 10, 4, 4))
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.COW, 8, 4, 4))
}

fun MobSpawnSettings.Builder.monsters(zombieWeight: Int, zombieVillagerWeight: Int, skeletonWeight: Int) = apply {
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.SPIDER, 100, 4, 4))
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.ZOMBIE, zombieWeight, 4, 4))
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.ZOMBIE_VILLAGER, zombieVillagerWeight, 4, 4))
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.SKELETON, skeletonWeight, 4, 4))
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.CREEPER, 100, 4, 4))
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.SLIME, 100, 4, 4))
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.ENDERMAN, 10, 1, 4))
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.WITCH, 5, 1, 1))
}

fun MobSpawnSettings.Builder.caveWater() = apply {
    spawn(MobCategory.UNDERGROUND_WATER_CREATURE, SpawnerData(EntityTypes.GLOW_SQUID, 10, 4, 6))
    spawn(MobCategory.UNDERGROUND_WATER_CREATURE, SpawnerData(EntityTypes.AXOLOTL, 10, 4, 6))
}

fun MobSpawnSettings.Builder.caves() = apply {
    spawn(MobCategory.AMBIENT, SpawnerData(EntityTypes.BAT, 10, 8, 8))
    caveWater()
}

fun MobSpawnSettings.Builder.ocean(squidWeight: Int, maxSquids: Int, codWeight: Int) = apply {
    spawn(MobCategory.WATER_CREATURE, SpawnerData(EntityTypes.SQUID, squidWeight, 1, maxSquids))
    spawn(MobCategory.WATER_CREATURE, SpawnerData(EntityTypes.COD, codWeight, 3, 6))
    common()
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.DROWNED, 5, 1, 1))
}

fun MobSpawnSettings.Builder.warmOcean(squidWeight: Int, minSquids: Int) = apply {
    spawn(MobCategory.WATER_CREATURE, SpawnerData(EntityTypes.SQUID, squidWeight, minSquids, 4))
    spawn(MobCategory.WATER_AMBIENT, SpawnerData(EntityTypes.TROPICAL_FISH, 25, 8, 8))
    spawn(MobCategory.WATER_CREATURE, SpawnerData(EntityTypes.DOLPHIN, 2, 1, 2))
    common()
}

fun MobSpawnSettings.Builder.plains() = apply {
    farmAnimals()
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.HORSE, 5, 2, 6))
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.DONKEY, 1, 1, 3))
    common()
}

fun MobSpawnSettings.Builder.snowy() = apply {
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.RABBIT, 10, 2, 3))
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.POLAR_BEAR, 1, 1, 2))
    caves()
    monsters(95, 5, 20)
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.STRAY, 80, 4, 4))
}

fun MobSpawnSettings.Builder.desert() = apply {
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.RABBIT, 4, 2, 3))
    caves()
    monsters(19, 1, 100)
    spawn(MobCategory.MONSTER, SpawnerData(EntityTypes.HUSK, 80, 4, 4))
}

fun MobSpawnSettings.Builder.mooshroom() = apply {
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.MOOSHROOM, 8, 4, 8))
    caves()
}

fun MobSpawnSettings.Builder.jungle() = apply {
    farmAnimals()
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.CHICKEN, 10, 4, 4))
    common()
}

fun MobSpawnSettings.Builder.savanna() = apply {
    farmAnimals()
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.HORSE, 1, 2, 6))
    spawn(MobCategory.CREATURE, SpawnerData(EntityTypes.DONKEY, 1, 1, 1))
    common()
}
