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
package org.kryptonmc.krypton.world

/**
 * These are specific world events that will be sent by the server to easily
 * signal to clients when they occur. The seemingly arbitrary IDs are from
 * [here](https://wiki.vg/Protocol#Effect).
 */
object WorldEvents {

    const val DISPENSER_DISPENSES: Int = 1000
    const val DISPENSER_FAIL: Int = 1001
    const val DISPENSER_PROJECTILE_LAUNCH: Int = 1002
    const val ENDER_EYE_LAUNCH: Int = 1003
    const val FIREWORK_SHOOT: Int = 1004
    const val OPEN_IRON_DOOR: Int = 1005
    const val OPEN_WOODEN_DOOR: Int = 1006
    const val OPEN_WOODEN_TRAP_DOOR: Int = 1007
    const val OPEN_FENCE_GATE: Int = 1008
    const val EXTINGUISH_FIRE: Int = 1009
    const val PLAY_RECORDING: Int = 1010
    const val CLOSE_IRON_DOOR: Int = 1011
    const val CLOSE_WOODEN_DOOR: Int = 1012
    const val CLOSE_WOODEN_TRAP_DOOR: Int = 1013
    const val CLOSE_FENCE_GATE: Int = 1014
    const val GHAST_WARNING: Int = 1015
    const val GHAST_FIREBALL: Int = 1016
    const val DRAGON_FIREBALL: Int = 1017
    const val BLAZE_FIREBALL: Int = 1018
    const val ZOMBIE_WOODEN_DOOR: Int = 1019
    const val ZOMBIE_IRON_DOOR: Int = 1020
    const val ZOMBIE_DOOR_CRASH: Int = 1021
    const val WITHER_BLOCK_BREAK: Int = 1022
    const val WITHER_BOSS_SPAWN: Int = 1023
    const val WITHER_BOSS_SHOOT: Int = 1024
    const val BAT_LIFTOFF: Int = 1025
    const val ZOMBIE_INFECTED: Int = 1026
    const val ZOMBIE_CONVERTED: Int = 1027
    const val DRAGON_DEATH: Int = 1028
    const val ANVIL_BROKEN: Int = 1029
    const val ANVIL_USED: Int = 1030
    const val ANVIL_LAND: Int = 1031
    const val PORTAL_TRAVEL: Int = 1032
    const val CHORUS_GROW: Int = 1033
    const val CHORUS_DEATH: Int = 1034
    const val BREWING_STAND_BREW: Int = 1035
    const val CLOSE_IRON_TRAP_DOOR: Int = 1036
    const val OPEN_IRON_TRAP_DOOR: Int = 1037
    const val END_PORTAL_SPAWN: Int = 1038
    const val PHANTOM_BITE: Int = 1039
    const val ZOMBIE_TO_DROWNED: Int = 1040
    const val HUSK_TO_ZOMBIE: Int = 1041
    const val GRINDSTONE_USED: Int = 1042
    const val PAGE_TURN: Int = 1043
    const val SMITHING_TABLE_USED: Int = 1044
    const val POINTED_DRIPSTONE_LAND: Int = 1045
    const val DRIP_LAVA_INTO_CAULDRON: Int = 1046
    const val DRIP_WATER_INTO_CAULDRON: Int = 1047
    const val SKELETON_TO_STRAY: Int = 1048
    const val COMPOSTER_FILL: Int = 1500
    const val LAVA_FIZZ: Int = 1501
    const val REDSTONE_TORCH_BURNOUT: Int = 1502
    const val END_PORTAL_FRAME_FILL: Int = 1503
    const val DRIPSTONE_DRIP: Int = 1504
    const val PLANT_GROWTH_BOTH: Int = 1505
    const val SHOOT_SMOKE: Int = 2000
    const val DESTROY_BLOCK: Int = 2001
    const val POTION_SPLASH: Int = 2002
    const val EYE_OF_ENDER_DEATH: Int = 2003
    const val MOB_BLOCK_SPAWN: Int = 2004
    const val PLANT_GROWTH: Int = 2005
    const val DRAGON_FIREBALL_SPLASH: Int = 2006
    const val INSTANT_POTION_SPLASH: Int = 2007
    const val DRAGON_BLOCK_BREAK: Int = 2008
    const val WATER_EVAPORATING: Int = 2009
    const val END_GATEWAY_SPAWN: Int = 3000
    const val DRAGON_SUMMON_ROAR: Int = 3001
    const val ELECTRIC_SPARK: Int = 3002
    const val WAX_ON: Int = 3003
    const val WAX_OFF: Int = 3004
    const val SCRAPE: Int = 3005
}
